package com.test.service;

import com.test.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// value = "orderProvider" 对应 orderProvider 服务 application.yml 中的 spring.application.name
@FeignClient(value = "orderProvider")
public interface RemoteOrderService {

    // 对应 orderProvider 中 OrderController 的 /order/list 接口
    @GetMapping("/order/list")
    List<Order> getAllOrders();

    // 对应 orderProvider 中 OrderController 的 /order/{id} 接口
    @GetMapping("/order/{id}")
    Order getOrderById(@PathVariable("id") Long id);

    // 对应 orderProvider 中 OrderController 的 /order/create 接口
    @PostMapping("/order/create")
    boolean createOrder(@RequestBody Order order);
}