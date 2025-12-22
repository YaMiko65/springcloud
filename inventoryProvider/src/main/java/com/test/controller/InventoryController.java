package com.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.pojo.Inventory;
import com.test.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // ... 原有 getStock, saveStock 保持不变 ...

    @GetMapping("/{bookId}")
    public Inventory getStock(@PathVariable Integer bookId) {
        return inventoryService.query().eq("book_id", bookId).one();
    }

    @PostMapping("/save")
    public boolean saveStock(@RequestBody Inventory inventory) {
        // ... 原有代码 ...
        QueryWrapper<Inventory> qw = new QueryWrapper<>();
        qw.eq("book_id", inventory.getBookId());
        Inventory exist = inventoryService.getOne(qw);
        if (exist != null) {
            exist.setStock(inventory.getStock());
            return inventoryService.updateById(exist);
        } else {
            return inventoryService.save(inventory);
        }
    }

    // 扣减库存
    @PostMapping("/decrease")
    public boolean decreaseStock(@RequestParam("bookId") Integer bookId, @RequestParam("count") Integer count) {
        return inventoryService.decreaseStock(bookId, count);
    }

    // [新增] 增加库存（归还）
    @PostMapping("/increase")
    public boolean increaseStock(@RequestParam("bookId") Integer bookId, @RequestParam("count") Integer count) {
        // 这里复用 InventoryService，需要去 ServiceImpl 实现具体逻辑，或者直接在这里操作
        // 为简化，直接在这里写逻辑，建议在 Service 中实现原子操作
        QueryWrapper<Inventory> qw = new QueryWrapper<>();
        qw.eq("book_id", bookId);
        Inventory inventory = inventoryService.getOne(qw);
        if (inventory != null) {
            inventory.setStock(inventory.getStock() + count);
            return inventoryService.updateById(inventory);
        }
        return false;
    }
}