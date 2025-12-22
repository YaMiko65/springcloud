package com.test.service;

import com.test.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-provider") // 确保这里的名字与 orderProvider 的 application.name 一致
public interface RemoteOrderService {
    @GetMapping("/order/{id}")
    Order getOrder(@PathVariable("id") Long id);

    @PostMapping("/order/updateStatus")
    boolean updateStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status);
}