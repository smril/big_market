package com.smril.domain.strategy.service.rule.chain.factory;

import com.smril.domain.strategy.model.entity.StrategyEntity;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.rule.chain.ILogicChain;
import com.smril.types.exception.AppException;
import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultChainFactory {

    private final Map<String, ILogicChain> logicChainGroup;

    protected IStrategyRepository repository;

    public DefaultChainFactory(Map<String, ILogicChain> logicChainGroup, IStrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    /* 创建责任链 */
    public ILogicChain openLogicChain(Long strategyId) {

        if(logicChainGroup.isEmpty()) {
            throw new AppException("logicChainGroup is empty!");
        }

        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();
        if(null == ruleModels || ruleModels.length == 0) {
            return logicChainGroup.get("default");
        }
        ILogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain chain = logicChainGroup.get(ruleModels[i]);
            current = current.appendNext(chain);
        }

        current.appendNext(logicChainGroup.get("default"));

        return logicChain;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StrategyAwardVO {
        private Integer awardId;
        private String logicModel;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_DEFAULT("rule_default", "默认抽奖"),
        RULE_BLACKLIST("rule_blacklist", "黑名单抽奖"),
        RULE_WEIGHT("rule_weight", "权重规则"),
        ;

        private final String code;
        private final String info;
    }
}
