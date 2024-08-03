package com.smril.domain.strategy.service.armory;

import com.smril.domain.strategy.model.entity.StrategyAwardEntity;
import com.smril.domain.strategy.model.entity.StrategyEntity;
import com.smril.domain.strategy.model.entity.StrategyRuleEntity;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.types.common.Constants;
import com.smril.types.enums.ResponseCode;
import com.smril.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmoryDispatch implements IStrategyArmory, IStrategyDispatch {

    /* 仓储层 */
    @Resource
    private IStrategyRepository repository;

    /*  */
    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        /* 根据策略ID从仓储层拿到奖品概率,与strategy_award表交互 */
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        //判空
        if(strategyAwardEntities == null || strategyAwardEntities.isEmpty()) {
            throw new AppException("strategy award list is empty!");
        }

        /* 装配时读取奖品库存，并缓存到redis中 */
        for(StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Integer awardId = strategyAwardEntity.getAwardId();
            Integer awardCount = strategyAwardEntity.getAwardCount();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }

        /* 调用同名函数，将生成的概率表map结构存入redis */
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        /* 权重策略配置 */
        //用仓储层根据策略ID获取到策略
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        if(strategyEntity == null) {
            throw new AppException("strategy entity is null!");
        }

        /* 找到看有无rule_weight策略 */
        String ruleWeight = strategyEntity.getRuleWeight();
        if (ruleWeight == null) {
            return true;
        }

        /* 查询rule_weight规则的值 */
        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if(strategyRuleEntity == null) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getInfo());
        }

        Map<String, List<Integer>> ruleWeightValueMap = strategyRuleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValueMap.keySet();
        for (String key : keys) {
            List<Integer> ruleWeightValues = ruleWeightValueMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesCLone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesCLone.removeIf(entity->!ruleWeightValues.contains(entity.getAwardId()));
            assembleLotteryStrategy(String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(key), strategyAwardEntitiesCLone);
        }
        return true;

    }

    private void cacheStrategyAwardCount(Long strategyId, Integer awardId, Integer awardCount) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        repository.queryCacheStrategyAwardCount(cacheKey, awardCount);
    }

    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        /* 获取最小概率值 */
        BigDecimal minAwardRate = strategyAwardEntities
                .stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        /* 获取概率综合 */
        BigDecimal totalAwardRate = strategyAwardEntities
                .stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        /* 获取概率范围，即需要分多少个格子 */
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        /* 概率查找表 */
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());

        /* 构造概率查找表中的奖品 */
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();

            for(int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        /* 打乱顺序 */
        Collections.shuffle(strategyAwardSearchRateTables);

        HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTables = new HashMap<>();

        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffledStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        /* 存入redis */
        repository.storeStrategyAwardSearchRateTables(key, shuffledStrategyAwardSearchRateTables.size(), shuffledStrategyAwardSearchRateTables);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(strategyId);  //获取概率总和
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat("_").concat(ruleWeightValue);
        int rateRange = repository.getRateRange(key);  //获取概率总和
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Boolean subtractionAwardStock(Long strategyId, Integer awardId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.subtractionAwardStock(cacheKey);
    }
}
