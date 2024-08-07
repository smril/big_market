package com.smril.domain.activity.service.armory;

import java.util.Date;

/**
 * @author smril
 * @create 2024/8/7 19:06
 * @description
 */
public interface IActivityDispatch {

    /**
     *
     * 根据策略ID和奖品ID，扣减奖品缓存库存
     *
     * @param sku
     * @param endDateTime 活动结束时间，根据结束时间设置加锁的key为结束时间
     * @return 扣减结果
     */
    boolean subtractionActivitySkuStock(Long sku, Date endDateTime);

}
