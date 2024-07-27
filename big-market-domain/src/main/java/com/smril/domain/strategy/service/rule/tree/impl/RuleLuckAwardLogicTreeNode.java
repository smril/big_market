package com.smril.domain.strategy.service.rule.tree.impl;

import com.smril.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.smril.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                        .awardId(101)
                        .awardRuleValue("1,100")
                        .build()
                )
                .build();
    }
}
