package com.smril.infrastructure.persistent.repository;

import com.smril.domain.strategy.model.entity.StrategyAwardEntity;
import com.smril.domain.strategy.model.entity.StrategyEntity;
import com.smril.domain.strategy.model.entity.StrategyRuleEntity;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.infrastructure.persistent.dao.IStrategyAwardDao;
import com.smril.infrastructure.persistent.dao.IStrategyDao;
import com.smril.infrastructure.persistent.dao.IStrategyRuleDao;
import com.smril.infrastructure.persistent.po.Strategy;
import com.smril.infrastructure.persistent.po.StrategyRule;
import com.smril.infrastructure.persistent.redis.IRedisService;
import com.smril.infrastructure.persistent.po.StrategyAward;
import com.smril.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Repository
public class StrategyRepository implements IStrategyRepository {

    @Resource
    IStrategyRuleDao strategyRuleDao;
    @Resource
    IStrategyDao strategyDao;
    @Resource
    IStrategyAwardDao strategyAwardDao;
    @Resource
    IRedisService redisService;
    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if(strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }
        //从数据库读
        List<StrategyAward> strategyAwards = strategyAwardDao.queryStrategyAwardListByStrategyId(strategyId);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity  = StrategyAwardEntity.builder()
                        .strategyId(strategyAward.getStrategyId())
                        .awardId(strategyAward.getAwardId())
                        .awardCount(strategyAward.getAwardCount())
                        .awardCountSurplus(strategyAward.getAwardCountSurplus())
                        .awardRate(strategyAward.getAwardRate())
                        .build();
            strategyAwardEntities.add(strategyAwardEntity);
        }
        //加入redis
        redisService.setValue(cacheKey, strategyAwardEntities);

        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardSearchRateTables(String key, Integer rateRange, HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTables) {
        // 1. 存储抽奖策略范围值，如10000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
        // 2. 存储概率查找表
        Map<Integer, Integer> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(shuffledStrategyAwardSearchRateTables);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return getRateRange(String.valueOf(strategyId));
    }

    @Override
    public int getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }

    @Override
    public Integer getStrategyAwardAssemble(String key, int rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateKey);
    }

    /*  */
    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if(strategyEntity != null) return strategyEntity;

        Strategy strategy = strategyDao.queryStrategyByStrategyId(strategyId);
        strategyEntity = StrategyEntity.builder()
                .strategyId(strategy.getStrategyId())
                .strategyDesc(strategy.getStrategyDesc())
                .ruleModels(strategy.getRuleModels())
                .build();

        redisService.setValue(cacheKey, strategyEntity);
        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setStrategyId(strategyId);
        strategyRuleReq.setRuleModel(ruleModel);
        StrategyRule strategyRuleRes = strategyRuleDao.queryStrategyRule(strategyRuleReq);
               return StrategyRuleEntity.builder()
                              .strategyId(strategyRuleRes.getStrategyId())
                              .awardId(strategyRuleRes.getAwardId())
                              .awardType(strategyRuleRes.getAwardType())
                              .ruleModel(strategyRuleRes.getRuleModel())
                              .ruleValue(strategyRuleRes.getRuleValue())
                              .ruleDesc(strategyRuleRes.getRuleDesc())
                              .build();
    }
}
