package com.smril.domain.activity.service.rule;

/**
 * @author smril
 * @create 2024/8/6 14:32
 * @description
 */
public abstract class AbstractActionChain implements IActionChain {

    private IActionChain next;

    public IActionChain next() {
        return next;
    }

    public IActionChain appendNext(IActionChain next) {
        this.next = next;
        return next;
    }
}
