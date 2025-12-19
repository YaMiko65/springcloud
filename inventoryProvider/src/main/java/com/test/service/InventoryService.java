package com.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.Inventory;

public interface InventoryService extends IService<Inventory> {
    // 可以定义扣减库存等业务方法
    boolean decreaseStock(Integer bookId, Integer count);
}