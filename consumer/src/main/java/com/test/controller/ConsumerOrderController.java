package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.Order;
import com.test.pojo.UserDto;
import com.test.service.BookService;
import com.test.service.LoginService;
import com.test.service.RemoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    // [修正] 统一后的购买接口
    @GetMapping("/buy/{bookId}")
    public String buyBook(@PathVariable("bookId") Long bookId) {
        // 1. 获取当前登录用户 (不再硬编码 ID 1)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/loginview";
        }
        String username = authentication.getName();
        UserDto user = loginService.getUserByUsername(username);

        if (user == null) {
            return "redirect:/loginview";
        }

        // 2. 获取真实图书信息 (不再硬编码价格 50.00)
        // 注意：BookService 接收 Integer 类型 ID
        EBook book = bookService.getBookById(bookId.intValue());

        // 校验图书是否存在以及状态是否可售 (0: 可售)
        if (book == null || !"0".equals(book.getStatus())) {
            // 如果书不存在或已被借阅/售出，跳转回列表并提示（这里简单跳回）
            return "redirect:/book/list";
        }

        // 3. 构建订单对象
        Order order = new Order();
        order.setUserId(Long.valueOf(user.getId()));
        order.setBookId(bookId);
        order.setCount(1);

        // 使用数据库中的真实价格
        order.setPrice(book.getPrice());
        order.setTotalPrice(book.getPrice()); // 数量为1，总价等于单价
        order.setCreateTime(new Date());

        // 4. 调用远程服务创建订单
        boolean orderCreated = remoteOrderService.createOrder(order);

        // 5. 如果订单创建成功，更新图书状态为 "3" (已售出)
        //
        if (orderCreated) {
            bookService.updateStatus(bookId.intValue(), "3");
        }

        return "redirect:/consumer/order/view";
    }
}