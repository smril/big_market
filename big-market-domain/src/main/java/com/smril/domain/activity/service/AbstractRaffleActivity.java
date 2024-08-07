package com.smril.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.smril.domain.activity.model.aggregate.CreateOrderAggregate;
import com.smril.domain.activity.model.entity.*;
import com.smril.domain.activity.repository.IActivityRepository;
import com.smril.domain.activity.service.rule.IActionChain;
import com.smril.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import com.smril.types.enums.ResponseCode;
import com.smril.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author smril
 * @create 2024/8/4 21:25
 * @description
 */

@Slf4j
public abstract class AbstractRaffleActivity extends RaffleActivitySupport implements IRaffleOrder{

    public AbstractRaffleActivity(DefaultActivityChainFactory defaultActivityChainFactory, IActivityRepository activityRepository) {
        super(defaultActivityChainFactory, activityRepository);
    }

    @Override
    public ActivityOrderEntity createRaffleActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        //通过Sku查询活动信息，读取raffle_activity_sku表
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        //查询活动信息，读取raffle_activity表
        ActivityEntity activityEntity = activityRepository.queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //查询次数信息，读取raffle_activity_count表
        ActivityCountEntity activityCountEntity = activityRepository.queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果: {} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }

    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        /* 参数校验 */
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if(null == sku || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        /* 查询基础信息 */
        //通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        //查询活动信息
        ActivityEntity activityEntity = queryRaffleActivityByActivityId(activitySkuEntity.getActivityId());
        //查询次数信息（用户在活动上可以参与的次数）
        ActivityCountEntity activityCountEntity =queryRaffleActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        /* 活动动作规则校验，责任链 */
        IActionChain actionChain = defaultActivityChainFactory.openActionChain();
        boolean success = actionChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        /* 构建订单聚合对象 */
        CreateOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);
        /* 保存订单 */
        doSaveOrder(createOrderAggregate);

        /* 返回单号 */
        return createOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);

    protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}
