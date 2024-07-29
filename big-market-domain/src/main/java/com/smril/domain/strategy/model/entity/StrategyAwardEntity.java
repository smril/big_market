package com.smril.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardEntity {

    private Long strategyId;

    private Integer awardId;

    private String awardTitle;

    private String awardSubtitle;

    private Integer awardCount;

    private Integer awardCountSurplus;

    private BigDecimal awardRate;

    private Integer sort;

}
