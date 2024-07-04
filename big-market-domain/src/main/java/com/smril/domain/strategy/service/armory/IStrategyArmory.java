package com.smril.domain.strategy.service.armory;

/*  */
public interface IStrategyArmory {
    /* 构造概率表 */
    void assembleLotteryStrategy(Long strategyId);
    /* 随机抽概率表中的奖品 */
    Integer getRandomAwardId(Long strategyId);
}
