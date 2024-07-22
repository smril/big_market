package com.smril.domain.strategy.repository;

import com.smril.domain.strategy.model.entity.StrategyAwardEntity;
import com.smril.domain.strategy.model.entity.StrategyEntity;
import com.smril.domain.strategy.model.entity.StrategyRuleEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/* 提供仓储服务，负责从数据库读取概率信息以及存取概率信息到redis */
public interface IStrategyRepository {
    /* 从redis或数据库中取出符合strategyId的数据，并且只提取需要用到的几个字段，如果是从数据库中提取，缓存入redis */
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);
    /* 与redis交互，将概率总和以及概率查找表存储到redis */
    void storeStrategyAwardSearchRateTables(String key, Integer rateRange, HashMap<Integer, Integer> shuffledStrategyAwardSearchRateTables);
    /* 与redis交互，获取概率总和，即应该开多大的空间去存储抽奖链表 */
    int getRateRange(Long strategyId);

    int getRateRange(String key);
    /* 与redis交互 */
    Integer getStrategyAwardAssemble(String key, int rateKey);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);
}
