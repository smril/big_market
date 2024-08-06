package com.smril.infrastructure.persistent.dao;

import com.smril.infrastructure.persistent.po.RaffleActivityAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author smril
 * @create 2024/8/3 19:37
 * @description 抽奖活动账户表
 */

@Mapper
public interface IRaffleActivityAccountDao {

    int updateAccountQuota(RaffleActivityAccount raffleActivityAccount);

    void insert(RaffleActivityAccount raffleActivityAccount);

}
