package com.smril.domain.strategy.service.rule.chain;

import com.smril.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

public interface ILogicChain extends ILogicChainArmory{
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId); //审批接口
}
