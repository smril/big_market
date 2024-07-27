package com.smril.domain.strategy.service.rule.tree.factory.engine;

import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

public interface IDecisionTreeEngine {
    DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId);
}
