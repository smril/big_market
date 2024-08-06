package com.smril.domain.activity.service.rule;

/**
 * @author smril
 * @create 2024/8/6 14:25
 * @description
 */
public interface IActionChainArmory {

    IActionChain next();

    IActionChain appendNext(IActionChain next);
}
