package com.smril.infrastructure.persistent.dao;

import com.smril.infrastructure.persistent.po.RaffleActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author smril
 * @create 2024/8/3 19:37
 * @description 抽奖活动表Dao
 */

@Mapper
public interface IRaffleActivityDao {
    RaffleActivity queryRaffleActivityByActivityId(Long activityId);
}
