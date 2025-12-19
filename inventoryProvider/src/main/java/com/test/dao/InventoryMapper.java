package com.test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.pojo.Inventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
}