package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.Order;
import com.test.pojo.UserDto;
import com.test.service.BookService;
import com.test.service.LoginService;
import com.test.service.RemoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
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

    // 查看订单列表 (根据权限区分)
    @GetMapping("/view")
    public String viewOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/loginview";
        }

        // 判断是否为管理员
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                isAdmin = true;
                break;
            }
        }

        List<Order> orders;
        if (isAdmin) {
            // 1. 管理员查看所有订单
            orders = remoteOrderService.getAllOrders();
        } else {
            // 2. 普通用户只看自己的订单
            String username = authentication.getName();
            UserDto user = loginService.getUserByUsername(username);
            if (user != null) {
                orders = remoteOrderService.getOrdersByUserId(Long.valueOf(user.getId()));
            } else {
                return "redirect:/loginview";
            }
        }

        model.addAttribute("orders", orders);
        return "order_list";
    }

    @GetMapping("/buy/{bookId}")
    public String buyBook(@PathVariable("bookId") Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/loginview";
        }
        String username = authentication.getName();
        UserDto user = loginService.getUserByUsername(username);

        if (user == null) {
            return "redirect:/loginview";
        }

        EBook book = bookService.getBookById(bookId.intValue());

        if (book == null || !"0".equals(book.getStatus())) {
            return "redirect:/book/list";
        }

        Order order = new Order();
        order.setUserId(Long.valueOf(user.getId()));
        order.setBookId(bookId);
        order.setCount(1);
        order.setPrice(book.getPrice());
        order.setTotalPrice(book.getPrice());
        order.setCreateTime(new Date());

        boolean orderCreated = remoteOrderService.createOrder(order);

        if (orderCreated) {
            bookService.updateStatus(bookId.intValue(), "3");
        }

        return "redirect:/consumer/order/view";
    }
}