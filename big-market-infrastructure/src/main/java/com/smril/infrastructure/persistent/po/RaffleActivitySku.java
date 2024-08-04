package com.smril.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * @author smril
 * @create 2024/8/4 19:39
 * @description 抽奖活动sku持久化对象
 */

@Data
public class RaffleActivitySku {

    /**
     * 自增ID
     */
    private Long id;

    private Long sku;

    private Long activityId;

    private Long activityCountId;

    private Integer stockCount;

    private Integer stockCountSurplus;

    private Date createTime;

    private Date updateTime;
}
