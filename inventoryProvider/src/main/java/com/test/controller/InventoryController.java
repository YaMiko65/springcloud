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

    @GetMapping("/{bookId}")
    public Inventory getStock(@PathVariable Integer bookId) {
        return inventoryService.query().eq("book_id", bookId).one();
    }

    // [新增] 保存或更新库存
    @PostMapping("/save")
    public boolean saveStock(@RequestBody Inventory inventory) {
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
}