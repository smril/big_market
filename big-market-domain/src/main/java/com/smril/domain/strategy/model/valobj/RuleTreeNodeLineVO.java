package com.smril.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleTreeNodeLineVO {
    /* 规则树ID */
    private Integer treeId;
    /* 规则Key节点 From */
    private String ruleNodeFrom;
    /* 规则Key节点 To */
    private String ruleNodeTo;
    /* 限定类型  */
    private RuleLimitTypeVO ruleLimitType;
    /* 限定值（到下个节点） */
    private RuleLogicCheckTypeVO ruleLimitValue;
}
