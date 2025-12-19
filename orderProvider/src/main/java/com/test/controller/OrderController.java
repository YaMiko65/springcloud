package com.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    // [保留] 管理员可能需要查看所有订单
    @GetMapping("/list")
    public List<Order> getAllOrders() {
        return orderService.list();
    }

    // [新增] 根据用户ID获取订单列表
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable("userId") Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time"); // 按时间倒序
        return orderService.list(wrapper);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping("/create")
    public boolean createOrder(@RequestBody Order order) {
        order.setCreateTime(new Date());
        return orderService.save(order);
    }
}