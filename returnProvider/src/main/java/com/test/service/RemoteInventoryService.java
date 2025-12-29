package com.test.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// [修改] name 必须与 inventoryProvider 的 spring.application.name (inventoryProvider) 一致
@FeignClient(name = "inventoryProvider")
public interface RemoteInventoryService {
    @PostMapping("/inventory/increase")
    boolean increaseStock(@RequestParam("bookId") Integer bookId, @RequestParam("count") Integer count);
}