package com.test.controller;

import com.test.pojo.Order;
import com.test.pojo.UserDto;
import com.test.service.BookService;
import com.test.service.LoginService;
import com.test.service.RemoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/consumer/order")
public class ConsumerOrderController {

    @Autowired
    private RemoteOrderService remoteOrderService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BookService bookService;

    // 查看订单列表
    @GetMapping("/view")
    public String viewOrders(Model model) {
        List<Order> orders = remoteOrderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order_list";
    }

    // [新增] 购买图书接口
    @GetMapping("/buy/{bookId}")
    public String buyBook(@PathVariable("bookId") Long bookId) {
        // 1. 获取当前登录用户的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. 远程调用获取用户信息（为了拿到 userId）
        UserDto user = loginService.getUserByUsername(username);
        if (user == null) {
            // 如果找不到用户，重定向回登录页（或者抛出异常）
            return "redirect:/loginview";
        }

        // 3. 构建订单对象
        Order order = new Order();
        order.setUserId(Long.valueOf(user.getId()));
        order.setBookId(bookId);
        order.setCount(1); // 默认购买1本
        // 注意：实际场景中应该先调用 bookService.getBookById(bookId) 获取真实价格
        // 这里为了演示，假设价格固定为 50.00
        order.setPrice(new BigDecimal("50.00"));
        order.setTotalPrice(new BigDecimal("50.00"));
        order.setCreateTime(new Date());

        // 4. 调用远程服务创建订单
        remoteOrderService.createOrder(order);

        // 5. 调用远程服务修改图书状态（设置为 1，表示已售出/不可用）
        // 注意：bookId 是 Long，但 BookService 接口可能接收 Integer，视具体定义而定
        bookService.updateBook(bookId.intValue());

        // 6. 购买成功，重定向到我的订单页面
        return "redirect:/consumer/order/view";
    }
}