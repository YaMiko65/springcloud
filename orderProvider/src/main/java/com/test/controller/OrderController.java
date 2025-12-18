package com.test.controller;

import com.test.pojo.Order;
import com.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 获取所有订单
    @GetMapping("/list")
    public List<Order> getAllOrders() {
        return orderService.list();
    }

    // 根据ID获取订单
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getById(id);
    }

    // 创建订单 (简单示例)
    @PostMapping("/create")
    public boolean createOrder(@RequestBody Order order) {
        order.setCreateTime(new Date());
        return orderService.save(order);
    }
}