package com.test.service;

import com.test.pojo.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventoryProvider", url = "http://localhost:9527/inventory")
public interface RemoteInventoryService {
    @GetMapping("/{bookId}")
    Inventory getStockByBookId(@PathVariable("bookId") Integer bookId);

    @PostMapping("/save")
    boolean saveStock(@RequestBody Inventory inventory);

    // [新增] 远程调用扣减库存
    @PostMapping("/decrease")
    boolean decreaseStock(@RequestParam("bookId") Integer bookId, @RequestParam("count") Integer count);
}