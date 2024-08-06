package com.smril.domain.activity.service.rule;

import com.smril.domain.activity.model.entity.ActivityCountEntity;
import com.smril.domain.activity.model.entity.ActivityEntity;
import com.smril.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author smril
 * @create 2024/8/6 14:18
 * @description
 */
public interface IActionChain extends IActionChainArmory {

    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);


}
