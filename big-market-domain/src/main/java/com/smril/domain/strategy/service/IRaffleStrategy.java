package com.smril.domain.strategy.service;

import com.smril.domain.strategy.model.entity.RaffleAwardEntity;
import com.smril.domain.strategy.model.entity.RaffleFactorEntity;

/* 抽奖策略接口 */
public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity);
}
