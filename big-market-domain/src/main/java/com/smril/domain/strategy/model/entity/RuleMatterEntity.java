package com.smril.domain.strategy.model.entity;

import lombok.Data;

@Data
public class RuleMatterEntity {
    private String userId;
    private Long strategyId;
    private Integer awardId;
    private String ruleModel;
}
