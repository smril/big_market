package com.smril.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class RuleTreeNodeLine {
    private Long id;
    private String treeId;
    private String ruleNodeFrom;
    private String ruleNodeTo;
    private String ruleLimitType;
    private String ruleLimitValue;
    private Date createTime;
    private Date updateTime;
}
