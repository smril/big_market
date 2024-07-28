package com.smril.domain.strategy.service;

import com.smril.domain.strategy.model.valobj.StrategyAwardStockKeyVO;

/**
 * @author smril
 * @create 2024/7/28 12:31
 * @description 抽奖库存相关服务，获取库存消耗队列
 */
public interface IRaffleStock {

    /* 获取奖品库存消耗队列 */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /* 更新奖品库存消耗记录 */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);
}
