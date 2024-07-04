package com.smril.infrastructure.persistent.dao;

import com.smril.infrastructure.persistent.po.Award;
import com.smril.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyDao {

    List<Strategy> queryStrategyList();

    Strategy queryStrategyByStrategyId(Long strategyId);
}
