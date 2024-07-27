package com.smril.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RuleTree {
    private Long id;
    private String treeId;
    private String treeName;
    private String treeDesc;
    private String treeRootRuleKey;
    private Date createTime;
    private Date updateTime;
}
