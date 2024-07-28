package com.smril.domain.strategy.service.rule.tree.factory.engine.impl;

import com.smril.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.smril.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.smril.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.smril.domain.strategy.model.valobj.RuleTreeVO;
import com.smril.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.smril.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.smril.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    /* 规则Map */
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }

    /* 决策 */
    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId) {
        /* 构造返回值 */
        DefaultTreeFactory.StrategyAwardVO strategyAwardVO = null;

        String nextNode = ruleTreeVO.getTreeRootRuleNode();  //获取根节点
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();  //获取规则节点

        RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(nextNode);  //根节点的规则节点，第一个要执行的规则
        while (nextNode != null) {
            //ruleTreeNode.getRulKey()代表获取到代表规则的Key，然后通过Map拿到规则接口
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(ruleTreeNode.getRuleKey());
            String ruleValue = ruleTreeNode.getRuleValue();
            
            //通过拿到的规则过滤
            DefaultTreeFactory.TreeActionEntity logicEntity = logicTreeNode.logic(userId, strategyId, awardId, ruleValue);
            //拿到本次过滤的结果
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckType();
            strategyAwardVO = logicEntity.getStrategyAwardVO();
            log.info("决策树引擎【{}】 treeId: {}, node: {}, code: {}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), nextNode, ruleLogicCheckTypeVO.getCode());

            //获取下个节点
            nextNode = nextNode(ruleLogicCheckTypeVO.getCode(), ruleTreeNode.getTreeNodeLineVOList());
            ruleTreeNode = treeNodeMap.get(nextNode);
        }

        return strategyAwardVO;
    }

    /* 负责决定下一个节点 */
    private String nextNode(String matterValue, List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList) {
        if(null == ruleTreeNodeLineVOList || ruleTreeNodeLineVOList.isEmpty()) {
            return null;
        }
        for(RuleTreeNodeLineVO nodeLine : ruleTreeNodeLineVOList) {  //遍历规则连线里的规则
            if(decisionLogic(matterValue, nodeLine)) {
                return nodeLine.getRuleNodeTo();
            }
        }
        // throw new RuntimeException("决策树引擎， nextNode计算失败，未找到可执行节点！");
        return null;
    }

    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            //暂不实现
            case GT:
            case LT:
            case GE:
            case LE:
            default:
                return false;
        }
    }
}
