package com.test.service;

import com.test.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

// [修改] 指向网关
@FeignClient(value = "orderProvider", url = "http://localhost:9527")
public interface RemoteOrderService {

    @GetMapping("/order/list")
    List<Order> getAllOrders();

    @GetMapping("/order/{id}")
    Order getOrderById(@PathVariable("id") Long id);

    @PostMapping("/order/create")
    boolean createOrder(@RequestBody Order order);
}