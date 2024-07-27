package com.smril.domain.strategy.service.rule.chain.impl;

import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.smril.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.smril.types.common.Constants;
import com.smril.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_blacklist")
public class BlackListLogicChain extends AbstractLogicChain {
    @Override
    protected String ruleModel() {
        return DefaultChainFactory.LogicModel.RULE_BLACKLIST.getCode();
    }

    @Resource
    IStrategyRepository repository;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始: userId: {}, strategyId: {}, ruleModel: {}", userId, strategyId, ruleModel());

        /* 开始过滤 */
        //1. 拿到黑名单ID
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        if(ruleValue == null) {
            log.info("抽奖责任链-没有黑名单信息: userId: {}, strategyId: {}, ruleModel: {}", userId, strategyId, ruleModel());
            return next().logic(userId, strategyId);
        }
        //2. 分割
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Integer awardId = Integer.parseInt(splitRuleValue[0]);

        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);
        //3. 遍历查找
        for(String blackId : userBlackIds) {
            if(blackId.equals(userId)) {  //接管，责任链结束
                log.info("抽奖责任链-黑名单接管: userId: {}, strategyId: {}, ruleModel: {}, awardId: {}", userId, strategyId, ruleModel(), awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .build();
            }
        }

        //继续下一个责任链
        log.info("抽奖责任链-黑名单放行: userId: {}, strategyId: {}, ruleModel: {}", userId, strategyId, ruleModel());
        return next().logic(userId, strategyId);
    }
}
