package com.test.service;

import com.test.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// [修改] name 必须与 orderProvider 的 spring.application.name (orderProvider) 一致
@FeignClient(name = "orderProvider")
public interface RemoteOrderService {
    @GetMapping("/order/{id}")
    Order getOrder(@PathVariable("id") Long id);

    @PostMapping("/order/updateStatus")
    boolean updateStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status);
}