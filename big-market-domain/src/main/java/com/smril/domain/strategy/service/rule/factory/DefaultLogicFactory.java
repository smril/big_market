package com.smril.domain.strategy.service.rule.factory;

import com.alibaba.fastjson2.util.AnnotationUtils;
import com.smril.domain.strategy.model.entity.RuleActionEntity;
import com.smril.domain.strategy.service.annotation.LogicStrategy;
import com.smril.domain.strategy.service.rule.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultLogicFactory {
    /*  */
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    /*  */
    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logicFilter -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logicFilter.getClass(), LogicStrategy.class);  //获取注解
            if(strategy != null) {
                logicFilterMap.put(strategy.logicMode().getCode(), logicFilter);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        RULE_WIGHT("rule_weight", "before", "根据抽奖权重返回可抽奖范围key"),
        RULE_BLACKLIST("rule_blacklist", "before", "黑名单规则过滤，黑名单直接返回"),
        RULE_LOCK("rule_lock", "center","抽奖n次后可解锁的奖品"),
        RULE_LUCK_AWARD("rule_luck_award", "after","幸运奖兜底"),
            ;

        private final String code;
        private final String type;
        private final String info;

        public static boolean isCenter(String code) {
            return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }
    }
}
