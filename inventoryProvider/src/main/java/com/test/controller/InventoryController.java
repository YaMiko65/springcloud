package com.test.controller;

import com.test.pojo.Inventory;
import com.test.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // 查询库存
    @GetMapping("/{bookId}")
    public Inventory getStock(@PathVariable Integer bookId) {
        return inventoryService.query().eq("book_id", bookId).one();
    }

    // 更多接口：添加库存、扣减库存...
}