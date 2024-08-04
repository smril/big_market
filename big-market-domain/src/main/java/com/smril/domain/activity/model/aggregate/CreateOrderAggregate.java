package com.smril.domain.activity.model.aggregate;

import com.smril.domain.activity.model.entity.ActivityAccountEntity;
import com.smril.domain.activity.model.entity.ActivityOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smril
 * @create 2024/8/4 21:13
 * @description 下单聚合对象
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderAggregate {

    /**
     * 活动账户实体
     */
    private ActivityAccountEntity activityAccountEntity;

    /**
     * 活动订单实体
     */
    private ActivityOrderEntity activityOrderEntity;
}
