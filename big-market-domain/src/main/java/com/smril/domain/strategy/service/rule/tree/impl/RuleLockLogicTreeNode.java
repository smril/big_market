package com.smril.domain.strategy.service.rule.tree.impl;

import com.smril.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    @Resource
    IStrategyRepository repository;

    //临时用户抽奖次数
    public Long userCount = 1L;

    /* 抽奖次数过滤 */
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        //查询到奖品限制数量
        String awardCountString = repository.queryStrategyRuleValue(strategyId, awardId, ruleModel());

        //未找到，直接放行
        if(awardCountString == null) {
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                    .build();
        }
        Integer awardCount = Integer.parseInt(awardCountString);

        //比对是否有抽奖资格
        if(userCount < awardCount) {
            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .build();
        }
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }

    protected String ruleModel() {
        return DefaultTreeFactory.LogicModel.RULE_LOCK.getCode();
    }
}
