package com.smril.domain.strategy.service.raffle;

import com.smril.domain.strategy.model.entity.StrategyAwardEntity;
import com.smril.domain.strategy.model.valobj.RuleTreeVO;
import com.smril.domain.strategy.model.valobj.StrategyAwardRuleModelVO;
import com.smril.domain.strategy.model.valobj.StrategyAwardStockKeyVO;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.domain.strategy.service.AbstractRaffleStrategy;
import com.smril.domain.strategy.service.IRaffleAward;
import com.smril.domain.strategy.service.IRaffleStock;
import com.smril.domain.strategy.service.armory.IStrategyDispatch;
import com.smril.domain.strategy.service.rule.chain.ILogicChain;
import com.smril.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.smril.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy implements IRaffleAward, IRaffleStock {

    @Autowired
    private DefaultChainFactory defaultChainFactory;
    @Autowired
    private DefaultTreeFactory defaultTreeFactory;

    public DefaultRaffleStrategy(IStrategyRepository repository, IStrategyDispatch strategyDispatch, DefaultChainFactory chainFactory, DefaultTreeFactory treeFactory, DefaultChainFactory defaultChainFactory, DefaultTreeFactory defaultTreeFactory) {
        super(repository, strategyDispatch, chainFactory, treeFactory);
        this.defaultChainFactory = defaultChainFactory;
        this.defaultTreeFactory = defaultTreeFactory;
    }

    @Override
    public DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        /* 构造责任链 */
        ILogicChain logicChain = defaultChainFactory.openLogicChain(strategyId);
        /* 责任链过滤 */
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        /* 查询rule_models */
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        /* 没有规则限制则直接返回 */
        if(null == strategyAwardRuleModelVO) {
            return DefaultTreeFactory.StrategyAwardVO.builder()
                    .awardId(awardId)
                    .build();
        }
        /* 数据库中提取规则树 */
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if(null == ruleTreeVO) {
            throw new RuntimeException("存在抽奖策略配置的规则模型Key，但未在库表的规则树中找到");
        }
        /* 规则树上面已经构造好了，这里返回一个执行引擎 */
        IDecisionTreeEngine treeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
        /* 规则树过滤 */
        return treeEngine.process(userId, strategyId, awardId);
    }

    @Override
    public StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Integer awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }
}
