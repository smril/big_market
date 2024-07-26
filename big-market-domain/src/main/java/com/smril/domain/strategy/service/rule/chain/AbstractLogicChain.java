package com.smril.domain.strategy.service.rule.chain;

public abstract class AbstractLogicChain implements ILogicChain {

    ILogicChain next;

    @Override
    public ILogicChain next() {  //获取责任链的下一个
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {  //添加责任链
        this.next = next;
        return next;
    }

    abstract protected String ruleModel();
}
