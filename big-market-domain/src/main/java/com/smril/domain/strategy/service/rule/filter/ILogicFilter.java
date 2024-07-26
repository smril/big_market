package com.smril.domain.strategy.service.rule.filter;

import com.smril.domain.strategy.model.entity.RuleActionEntity;
import com.smril.domain.strategy.model.entity.RuleMatterEntity;

/* 抽奖规则过滤接口 */
public interface ILogicFilter<T extends RuleActionEntity.RaffleEntity> {
    RuleActionEntity<T> filter(RuleMatterEntity ruleMatterEntity);
}
