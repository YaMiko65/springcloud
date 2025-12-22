package com.test.controller;

import com.test.pojo.Order;
import com.test.service.RemoteInventoryService;
import com.test.service.RemoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/return")
public class ReturnController {

    @Autowired
    private RemoteOrderService remoteOrderService;

    @Autowired
    private RemoteInventoryService remoteInventoryService;

    // 执行归还操作
    @PostMapping("/do/{orderId}")
    public boolean returnBook(@PathVariable("orderId") Long orderId) {
        // 1. 获取订单信息
        Order order = remoteOrderService.getOrder(orderId);
        if (order == null) {
            return false;
        }

        // 2. 检查状态，只有未归还(0)的才能归还
        if (order.getStatus() != null && order.getStatus() == 1) {
            return false; // 已经归还过了
        }

        // 3. 调用库存服务，增加库存
        boolean stockResult = remoteInventoryService.increaseStock(order.getBookId().intValue(), order.getCount());

        if (stockResult) {
            // 4. 调用订单服务，更新状态为已归还(1)
            return remoteOrderService.updateStatus(orderId, 1);
        }

        return false;
    }
}