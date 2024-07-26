package com.smril.domain.strategy.service.rule.chain;

public interface ILogicChain extends ILogicChainArmory{
    Integer logic(String userId, Long strategyId); //审批接口
}
