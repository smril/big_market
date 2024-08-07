package com.smril.infrastructure.persistent.dao;

import com.smril.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author smril
 * @create 2024/8/4 19:46
 * @description 商品sku Dao
 */

@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(Long sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
