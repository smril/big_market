package com.smril.domain.strategy.service;

import com.smril.domain.strategy.model.entity.RaffleAwardEntity;
import com.smril.domain.strategy.model.entity.RaffleFactorEntity;
import com.smril.domain.strategy.model.entity.RuleActionEntity;
import com.smril.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.smril.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.armory.IStrategyDispatch;
import com.smril.domain.strategy.service.rule.chain.ILogicChain;
import com.smril.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.smril.types.enums.ResponseCode;
import com.smril.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/* 负责抽奖流程以及更新库表库存 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy, IRaffleStock {

    protected IStrategyRepository repository;
    protected IStrategyDispatch strategyDispatch;
    protected DefaultChainFactory chainFactory;
    protected DefaultTreeFactory treeFactory;

    public AbstractRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory) {
        this.repository = repository;
        this.strategyDispatch = strategyDispatch;
        this.chainFactory = chainFactory;
        this.treeFactory = treeFactory;
    }

    /* 用户抽奖，然后返回奖品信息 */
    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactorEntity) {

        String userId = raffleFactorEntity.getUserId();
        Long strategyId = raffleFactorEntity.getStrategyId();
        if(null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        /* 责任链抽奖过滤 */
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainStrategyAwardVO.getAwardId(), chainStrategyAwardVO.getLogicModel());
        //如果责任链过滤后得到的结果不是默认抽奖
        if(!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())) {
            log.info("跳过规则树直接返回");
            return RaffleAwardEntity.builder()
                    .awardId(chainStrategyAwardVO.getAwardId())
                    .build();
        }

        /* 规则树抽奖过滤 */
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId, chainStrategyAwardVO.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        return RaffleAwardEntity.builder()
                .awardId(treeStrategyAwardVO.getAwardId())
                .awardConfig(treeStrategyAwardVO.getAwardRuleValue())
                .build();
    }

    public abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    public abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId);

}
