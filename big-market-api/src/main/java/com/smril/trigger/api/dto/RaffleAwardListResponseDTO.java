package com.smril.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smril
 * @create 2024/7/29 19:34
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardListResponseDTO {
    private Integer awardId;
    private String awardTitle;
    private String awardSubtitle;
    private Integer sort;
}
