package com.smril.infrastructure.persistent.dao;

import com.smril.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IRuleTreeDao {

    RuleTree queryRuleTreeByTreeId(String treeId);

}
