package com.smril.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smril
 * @create 2024/8/7 19:35
 * @description
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySkuStockKeyVO {
    private Long sku;

    private Long activityId;
}
