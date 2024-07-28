package com.smril.domain.strategy.service.rule.tree.impl;

import com.smril.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.smril.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.smril.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_luck_award")
public class RuleLuckAwardLogicTreeNode implements ILogicTreeNode {

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-兜底奖品开始 userId: {}, strategyId: {}, awardId: {}, ruleValue: {}", userId, strategyId, awardId, ruleValue);
        String[] split = ruleValue.split(Constants.COLON);

        if(split.length == 0) {
            log.error("规则过滤-兜底奖品，兜底奖品未配置告警 userId: {}, strategyId: {}, awardId: {}", userId, strategyId, awardId);
            throw new RuntimeException("兜底奖品未配置 + ruleValue");
        }

        //兜底奖品配置
        Integer luckAwardId = Integer.parseInt(split[0]);
        String awardRuleValue = split.length > 1 ? split[1] : "";
        log.info("规则过滤-兜底奖品配置 userId: {}, strategyId: {}, awardId: {}, awardRuleValue: {}", userId, strategyId, awardId, awardRuleValue);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .awardRuleValue(awardRuleValue)
                        .build()
                )
                .build();


    }
}
