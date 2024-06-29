package com.smril.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

@Data
public class StrategyRule {
    private Long id;
    private Long strategyId;
    private Integer awardId;
    private Integer awardType;
    private String ruleModel;
    private String ruleValue;
    private String ruleDesc;
    private Date createTime;
    private Date updateTime;
}
