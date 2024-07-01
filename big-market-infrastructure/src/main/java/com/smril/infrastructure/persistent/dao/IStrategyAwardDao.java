package com.smril.infrastructure.persistent.dao;

import com.smril.infrastructure.persistent.po.Award;
import com.smril.infrastructure.persistent.po.StrategyAward;
import com.smril.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {
    List<StrategyRule> queryStrategyAwardList();

    List<StrategyAward> queryStrategyAwardListByStrategyId(Long strategyId);
}
