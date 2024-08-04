package com.smril.domain.activity.service;

import com.smril.domain.activity.model.entity.ActivityOrderEntity;
import com.smril.domain.activity.model.entity.ActivityShopCartEntity;

/**
 * @author smril
 * @create 2024/8/4 20:14
 * @description 抽奖活动订单接口
 */
public interface IRaffleOrder {
    ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity);
}
