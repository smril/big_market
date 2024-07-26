package com.smril.domain.strategy.service.rule.chain.impl;

import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.armory.IStrategyDispatch;
import com.smril.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.smril.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component("rule_weight")
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Override
    protected String ruleModel() {
        return "rule_weight";
    }

    @Resource
    IStrategyRepository repository;

    @Resource
    IStrategyDispatch strategyDispatch;

    public Long userScore = 0L;

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始: userId: {}, strategyId: {}, ruleModel: {}", userId, strategyId, ruleModel());

        /* 开始过滤 */
        //拿到权重
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if(analyticalValueGroup == null || analyticalValueGroup.isEmpty()) {
            return next().logic(userId, strategyId);  //说明没有权重,直接放行到下一个责任链
        }

        //转换Key值
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        //找出最小值
        Long nextValue = analyticalSortedKeys.stream()
                .sorted(Comparator.reverseOrder())
                .filter(analyticalSortedKeyValue -> userScore >= analyticalSortedKeyValue)
                .findFirst()
                .orElse(null);

        if(nextValue != null) {
            Integer awardId = strategyDispatch.getRandomAwardId(strategyId, analyticalValueGroup.get(nextValue));
            log.info("抽奖责任链-权重接管: userId: {}, strategyId: {}, ruleModel: {}, awardId: {}", userId, strategyId, ruleModel(), awardId);
            return awardId;
        }

        log.info("抽奖责任链-权重放行: userId: {}, strategyId: {}, ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            if(ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            String[] parts = ruleValueKey.split(Constants.COLON);
            if(parts.length != 2) {
                throw new IllegalArgumentException("rule_weight invalid format" + ruleValueKey);
            }

            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }
}
