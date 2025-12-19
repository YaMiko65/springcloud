package com.test.service;

import com.test.pojo.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 通过网关 (9527) 访问 inventory 服务
@FeignClient(name = "inventoryProvider", url = "http://localhost:9527/inventory")
public interface RemoteInventoryService {

    // 根据图书ID查询库存
    @GetMapping("/{bookId}")
    Inventory getStockByBookId(@PathVariable("bookId") Integer bookId);
}