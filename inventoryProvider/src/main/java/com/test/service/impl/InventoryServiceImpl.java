package com.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dao.InventoryMapper;
import com.test.pojo.Inventory;
import com.test.service.InventoryService;
import org.springframework.stereotype.Service;

/**
 * 库存服务实现类
 * 必须添加 @Service 注解，Spring 才能扫描到它并注册为 Bean
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Override
    public boolean decreaseStock(Integer bookId, Integer count) {
        // 简单的扣减库存逻辑实现
        // 相当于 SQL: UPDATE inventory SET stock = stock - count WHERE book_id = bookId AND stock >= count
        return update()
                .setSql("stock = stock - " + count)
                .eq("book_id", bookId)
                .ge("stock", count) // 确保库存足够，防止负库存
                .update();
    }
}