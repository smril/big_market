package com.smril.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/* 要发的奖 */
public class RaffleAwardEntity {
    private Integer awardId;
    private String awardConfig;
    private Integer sort;
}
