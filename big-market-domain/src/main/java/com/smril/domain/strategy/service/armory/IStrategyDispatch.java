package com.smril.domain.strategy.service.armory;

public interface IStrategyDispatch {
    /* 随机抽概率表中的奖品 */
    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);
}
