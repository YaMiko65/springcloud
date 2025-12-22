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

    // ... list, user list, getOrder ...

    @GetMapping("/list")
    public List<Order> getAllOrders() {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return orderService.list(wrapper);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable("userId") Long userId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        return orderService.list(wrapper);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping("/create")
    public boolean createOrder(@RequestBody Order order) {
        order.setCreateTime(new Date());
        // 默认状态为 0 (未归还)
        if (order.getStatus() == null) {
            order.setStatus(0);
        }
        return orderService.save(order);
    }

    // [新增] 更新订单状态
    @PostMapping("/updateStatus")
    public boolean updateStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        Order order = orderService.getById(id);
        if (order != null) {
            order.setStatus(status);
            return orderService.updateById(order);
        }
        return false;
    }
}