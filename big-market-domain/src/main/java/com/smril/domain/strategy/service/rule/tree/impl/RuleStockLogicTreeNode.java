package com.smril.domain.strategy.service.rule.tree.impl;

import com.smril.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.smril.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.armory.IStrategyDispatch;
import com.smril.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_stock")
public class RuleStockLogicTreeNode implements ILogicTreeNode {

    @Resource
    private IStrategyDispatch strategyDispatch;

    @Resource
    private IStrategyRepository repository;

    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId, String ruleValue) {
        log.info("规则过滤-库存扣减: userId:{}, strategyId:{}, awardId: {}", userId, strategyId, awardId);

        //扣减奖品
        Boolean status = strategyDispatch.subtractionAwardStock(strategyId, awardId);

        //如果扣减成功
        if(status) {
            repository.awardStockConsumeSendQueue(StrategyAwardStockKeyVO.builder()
                    .strategyId(strategyId)
                    .awardId(awardId)
                    .build()
            );

            return DefaultTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .strategyAwardVO(DefaultTreeFactory.StrategyAwardVO.builder()
                            .awardId(awardId)
                            .awardRuleValue("")
                            .build()
                    )
                    .build();
        }

        log.warn("规则过滤-库存扣减-告警，库存不足。userId: {}, strategyId:{}, awardId: {}", userId, strategyId, awardId);
        return DefaultTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
