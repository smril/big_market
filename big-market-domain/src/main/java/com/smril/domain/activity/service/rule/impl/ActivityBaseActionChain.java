package com.smril.domain.activity.service.rule.impl;

import com.smril.domain.activity.model.entity.ActivityCountEntity;
import com.smril.domain.activity.model.entity.ActivityEntity;
import com.smril.domain.activity.model.entity.ActivitySkuEntity;
import com.smril.domain.activity.model.valobj.ActivityStateVO;
import com.smril.domain.activity.service.rule.AbstractActionChain;
import com.smril.types.enums.ResponseCode;
import com.smril.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author smril
 * @create 2024/8/6 14:45
 * @description 活动规则过滤【日期、状态】
 */

@Slf4j
@Component("activity_base_action")
public class ActivityBaseActionChain extends AbstractActionChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {

        log.info("活动责任链-基础信息【有效期、状态】校验开始 sku: {} activity: {}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        /* 校验活动状态 */
        if(!ActivityStateVO.open.equals(activityEntity.getState())) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }
        /* 校验活动日期 */
        Date currentDate = new Date();
        if(activityEntity.getBeginDateTime().after(currentDate) || activityEntity.getEndDateTime().before(currentDate)) {
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(), ResponseCode.ACTIVITY_DATE_ERROR.getInfo());
        }
        /* 校验活动sku库存 */
        if(activitySkuEntity.getStockCountSurplus() <= 0) {
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(), ResponseCode.ACTIVITY_STATE_ERROR.getInfo());
        }

        return next().action(activitySkuEntity, activityEntity, activityCountEntity);

    }
}
