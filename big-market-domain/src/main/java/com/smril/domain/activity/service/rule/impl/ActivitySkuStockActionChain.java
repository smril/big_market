package com.smril.domain.activity.service.rule.impl;

import com.smril.domain.activity.model.entity.ActivityCountEntity;
import com.smril.domain.activity.model.entity.ActivityEntity;
import com.smril.domain.activity.model.entity.ActivitySkuEntity;
import com.smril.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.smril.domain.activity.repository.IActivityRepository;
import com.smril.domain.activity.service.armory.IActivityDispatch;
import com.smril.domain.activity.service.rule.AbstractActionChain;
import com.smril.types.enums.ResponseCode;
import com.smril.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author smril
 * @create 2024/8/6 14:46
 * @description 商品库存规则节点
 */

@Slf4j
@Component("activity_sku_stock_action")
public class ActivitySkuStockActionChain extends AbstractActionChain {

    @Resource
    private IActivityDispatch activityDispatch;

    @Resource
    private IActivityRepository activityRepository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-商品库存处理【校验&扣减】开始 sku: {}, activityId: {}", activitySkuEntity.getSku(), activitySkuEntity.getActivityId());

        boolean status = activityDispatch.subtractionActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());

        if(status) { //true: 库存扣减成功
            log.info("活动责任链-商品库存处理【校验&扣减】成功 sku: {}, activityId: {}", activitySkuEntity.getSku(), activitySkuEntity.getActivityId());

            //写入延迟队列，延迟消费更新库存记录
            activityRepository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                    .sku(activitySkuEntity.getSku())
                    .activityId(activityEntity.getActivityId())
                    .build());

            return true;
        }

        throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getInfo());
    }
}
