package com.test.service;

import com.test.pojo.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventoryProvider", url = "http://localhost:9527/inventory")
public interface RemoteInventoryService {
    @GetMapping("/{bookId}")
    Inventory getStockByBookId(@PathVariable("bookId") Integer bookId);

    @PostMapping("/save")
    boolean saveStock(@RequestBody Inventory inventory);
}