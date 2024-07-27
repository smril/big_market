package com.smril.domain.strategy.service.rule.chain.impl;

import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.armory.IStrategyDispatch;
import com.smril.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.smril.domain.strategy.service.rule.chain.ILogicChain;
import com.smril.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("default")
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    IStrategyDispatch strategyDispatch;

    /* 默认逻辑 */
    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);
        log.info("抽奖责任链，默认处理 userId: {}, strategyId: {}, ruleModel: {}, awardId: {}", userId, strategyId, ruleModel(), awardId);
        return DefaultChainFactory.StrategyAwardVO.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();
    }

    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode();
    }
}
