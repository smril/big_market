package com.smril.domain.strategy.service.rule.chain;

public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);
}
