package com.smril.domain.strategy.service.armory;

import com.smril.domain.strategy.model.entity.StrategyAwardEntity;
import com.smril.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmory implements IStrategyArmory {

    @Resource
    private IStrategyRepository repository;

    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        //获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities
                .stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        //获取概率综合
        BigDecimal totalAwardRate = strategyAwardEntities
                .stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //获取概率范围，即需要分多少个格子
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        //概率查找表
        List<Integer> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());

        //构造概率查找表
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();

            for(int i = 0; i < rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        Collections.shuffle(strategyAwardSearchRateTables);

        HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTables = new HashMap<>();

        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffledStrategyAwardSearchRateTables.put(i, strategyAwardSearchRateTables.get(i));
        }

        //redis
        repository.storeStrategyAwardSearchRateTables(strategyId, shuffledStrategyAwardSearchRateTables.size(), shuffledStrategyAwardSearchRateTables);

    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        int rateRange = repository.getRateRange(strategyId);  //获取概率总和
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }
}
