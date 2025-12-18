package com.test.controller;

import com.test.pojo.Order;
import com.test.service.RemoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; // 注意：这里通常用 Controller 而不是 RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ⚠️ 注意：如果你之前用的是 @RestController，请改为 @Controller
// 因为我们需要返回页面跳转 (String "order_list")，而不是纯 JSON 数据
@Controller
@RequestMapping("/consumer/order")
public class ConsumerOrderController {

    @Autowired
    private RemoteOrderService remoteOrderService;

    // 页面跳转接口
    @GetMapping("/view")
    public String viewOrders(Model model) {
        // 1. 调用远程服务获取所有订单
        List<Order> orders = remoteOrderService.getAllOrders();
        // 2. 将订单数据存入 Model，供 HTML 页面读取
        model.addAttribute("orders", orders);
        // 3. 返回模板名称 (对应 templates/order_list.html)
        return "order_list";
    }

    // --- 保留之前的 JSON 接口 (可选) ---
    @ResponseBody // 如果类是 @Controller，返回 JSON 数据的方法需要加 @ResponseBody
    @PostMapping("/create")
    public String create(@RequestBody Order order) {
        boolean success = remoteOrderService.createOrder(order);
        return success ? "下单成功" : "下单失败";
    }
}