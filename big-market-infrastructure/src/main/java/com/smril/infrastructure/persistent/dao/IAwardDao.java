package com.smril.infrastructure.persistent.dao;

import com.smril.infrastructure.persistent.po.Award;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAwardDao {
    List<Award> queryAwardList();
}
