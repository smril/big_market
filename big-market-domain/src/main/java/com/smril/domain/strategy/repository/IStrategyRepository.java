package com.smril.domain.strategy.repository;

import com.smril.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTables(Long strategyId, Integer rateRange, HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTables);

    int getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rateKey);
}
