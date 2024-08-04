package com.smril.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smril
 * @create 2024/8/4 20:44
 * @description
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityShopCartEntity {
    /** 用户ID */
    private String userId;
    /** 商品SKU - activity + activity count */
    private Long sku;

}
