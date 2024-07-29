package com.smril.domain.strategy.service;

import com.smril.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

/**
 * @author smril
 * @create 2024/7/29 20:23
 * @description 策略奖品接口
 */
public interface IRaffleAward {

    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);

}
