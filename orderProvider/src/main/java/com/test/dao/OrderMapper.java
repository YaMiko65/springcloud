package com.test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}