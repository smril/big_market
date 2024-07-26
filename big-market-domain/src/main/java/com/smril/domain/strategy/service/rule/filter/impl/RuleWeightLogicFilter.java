package com.smril.domain.strategy.service.rule.filter.impl;

import com.smril.domain.strategy.model.entity.RuleActionEntity;
import com.smril.domain.strategy.model.entity.RuleMatterEntity;
import com.smril.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.annotation.LogicStrategy;
import com.smril.domain.strategy.service.rule.filter.ILogicFilter;
import com.smril.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.smril.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {
    @Resource
    private IStrategyRepository repository;

    //临时测试
    public Long userScore = 4500L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatterEntity) {

        log.info("规则过滤-权重范围 userId:{} strategyId:{} ruleModel:{}", ruleMatterEntity.getUserId(), ruleMatterEntity.getStrategyId(), ruleMatterEntity.getRuleModel());
        String userId = ruleMatterEntity.getUserId();
        Long strategyId = ruleMatterEntity.getStrategyId();
        /* rule_value字段 */
        String ruleValue = repository.queryStrategyRuleValue(ruleMatterEntity.getStrategyId(), ruleMatterEntity.getAwardId(), ruleMatterEntity.getRuleModel());

        /* 查询到rule_value字段并进行分割放入Map */
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if(null == analyticalValueGroup) {  //没有规则就直接放行
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        /* 对档位进行排序 */
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        /* 找到第一个比用户积分小的档，就对应了用户的档位 */
        Long nextValue = analyticalSortedKeys.stream()
                .filter(key -> userScore >= key)
                .findFirst()
                .orElse(null);


        if (null != nextValue) {
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .data(RuleActionEntity.RaffleBeforeEntity.builder()
                            .strategyId(strategyId)
                            .ruleWeightValueKey(analyticalValueGroup.get(nextValue))  //拿到档位中的奖品信息
                            .build())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                    .build();
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for(String ruleValueKey : ruleValueGroups) { //4000:102,103,104
            if(ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            String[] parts = ruleValueKey.split(Constants.COLON);
            if(parts.length != 2) {
                throw new IllegalArgumentException("rule_weight rule_value format error" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }
}
