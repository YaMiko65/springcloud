package com.test.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-provider")
public interface RemoteInventoryService {
    @PostMapping("/inventory/increase")
    boolean increaseStock(@RequestParam("bookId") Integer bookId, @RequestParam("count") Integer count);
}