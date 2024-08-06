package com.smril.domain.activity.model.entity;

import lombok.Data;

/**
 * @author smril
 * @create 2024/8/6 11:58
 * @description
 */

@Data
public class SkuRechargeEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 商品SKU - activity + activity count
     */
    private Long sku;
    /**
     * 幂等业务单号，外部谁充值谁透传，保证幂等（多次调用结果唯一，不会多次重置）
     */
    private String outBusinessNo;
}
