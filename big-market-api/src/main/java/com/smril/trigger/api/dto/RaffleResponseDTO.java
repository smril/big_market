package com.smril.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smril
 * @create 2024/7/29 19:40
 * @description 抽奖应答结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleResponseDTO {
    private Integer awardId;
    private Integer awardIndex;
}
