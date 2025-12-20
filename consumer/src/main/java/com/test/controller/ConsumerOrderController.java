package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.Order;
import com.test.pojo.UserDto;
import com.test.service.BookService;
import com.test.service.LoginService;
import com.test.service.RemoteInventoryService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal; // 导入 BigDecimal
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/consumer/order")
public class ConsumerOrderController {

    @Autowired
    private RemoteOrderService remoteOrderService;

    @Autowired
    private RemoteInventoryService remoteInventoryService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private BookService bookService;

    // 查看订单列表
    @GetMapping("/view")
    public String viewOrders(Model model) {
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

        List<Order> orders;
        if (isAdmin) {
            orders = remoteOrderService.getAllOrders();
        } else {
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

    // [修改] 购买图书
    @GetMapping("/buy/{bookId}")
    public String buyBook(@PathVariable("bookId") Long bookId,
                          @RequestParam(value = "count", defaultValue = "1") Integer count) {
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

        if (book == null) {
            return "redirect:/book/list";
        }

        // 1. 尝试扣减库存
        boolean stockDecreased = remoteInventoryService.decreaseStock(bookId.intValue(), count);

        if (stockDecreased) {
            // 2. 库存扣减成功，创建订单
            Order order = new Order();
            order.setUserId(Long.valueOf(user.getId()));
            order.setBookId(bookId);
            order.setCount(count);
            order.setPrice(book.getPrice());

            // [修复] 使用 BigDecimal 的 multiply 方法计算总价
            // book.getPrice() * count -> book.getPrice().multiply(new BigDecimal(count))
            BigDecimal total = book.getPrice().multiply(new BigDecimal(count));
            order.setTotalPrice(total);

            order.setCreateTime(new Date());

            remoteOrderService.createOrder(order);
        } else {
            System.out.println("购买失败，库存不足");
            return "redirect:/book/list";
        }

        return "redirect:/consumer/order/view";
    }
}