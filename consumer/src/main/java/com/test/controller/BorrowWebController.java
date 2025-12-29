package com.test.controller;

// ... imports 保持不变 ...
import com.test.pojo.EBook;
import com.test.pojo.Order;
import com.test.pojo.UserDto;
import com.test.service.BookService;
import com.test.service.LoginService;
import com.test.service.RemoteOrderService;
import com.test.service.RemoteReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/borrow")
public class BorrowWebController {

    @Autowired
    private RemoteOrderService remoteOrderService;
    @Autowired
    private RemoteReturnService remoteReturnService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private BookService bookService;

    // 借阅列表页面
    @GetMapping("/list")
    public String borrowList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/loginview";
        }

        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                isAdmin = true;
                break;
            }
        }

        List<Order> allOrders;
        if (isAdmin) {
            allOrders = remoteOrderService.getAllOrders();
        } else {
            String username = authentication.getName();
            UserDto user = loginService.getUserByUsername(username);
            if (user != null) {
                allOrders = remoteOrderService.getOrdersByUserId(Long.valueOf(user.getId()));
            } else {
                return "redirect:/loginview";
            }
        }

        List<Order> borrowOrders = new ArrayList<>();
        if (allOrders != null) {
            for (Order order : allOrders) {
                // 判定是否为借阅订单
                boolean isBorrow = (order.getPrice() == null || order.getPrice().compareTo(BigDecimal.ZERO) <= 0);

                if (isBorrow) {
                    // 填充书籍名称
                    if (order.getBookId() != null) {
                        EBook book = bookService.getBookById(order.getBookId().intValue());
                        if (book != null) order.setBookName(book.getName());
                    }

                    // [修改核心]：填充用户名 (改为调用 getUserById 获取 UserDto)
                    if (order.getUserId() != null) {
                        try {
                            UserDto u = loginService.getUserById(order.getUserId().intValue());
                            if (u != null) {
                                order.setUserName(u.getUsername()); // 获取真正的用户名
                            } else {
                                order.setUserName("未知用户");
                            }
                        } catch (Exception e) {
                            order.setUserName("用户查询失败");
                        }
                    }
                    borrowOrders.add(order);
                }
            }
        }

        model.addAttribute("orders", borrowOrders);
        model.addAttribute("isAdmin", isAdmin);
        return "borrow_list";
    }

    // 处理归还动作
    @GetMapping("/returnBook/{orderId}")
    public String returnBook(@PathVariable("orderId") Long orderId) {
        remoteReturnService.returnBook(orderId);
        return "redirect:/borrow/list";
    }
}