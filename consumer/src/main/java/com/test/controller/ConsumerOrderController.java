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

import java.math.BigDecimal;
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

        // [新增] 遍历订单列表，填充详细信息（用户名、书名、订单类型）
        if (orders != null && !orders.isEmpty()) {
            for (Order order : orders) {
                // 1. 填充书籍名称
                if (order.getBookId() != null) {
                    EBook book = bookService.getBookById(order.getBookId().intValue());
                    if (book != null) {
                        order.setBookName(book.getName());
                    } else {
                        order.setBookName("未知书籍");
                    }
                }

                // 2. 填充用户姓名
                if (order.getUserId() != null) {
                    try {
                        List<String> userInfos = loginService.findUserByUserId(order.getUserId().intValue());
                        if (userInfos != null && !userInfos.isEmpty()) {
                            // 假设 list 第一个元素是用户名
                            order.setUserName(userInfos.get(0));
                        } else {
                            order.setUserName("未知用户");
                        }
                    } catch (Exception e) {
                        order.setUserName("用户查询失败");
                    }
                }

                // 3. 填充订单类型 (根据单价判断，大于0为购买，否则为借阅)
                if (order.getPrice() != null && order.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    order.setOrderType("购买");
                } else {
                    order.setOrderType("借阅");
                }
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
            order.setPrice(book.getPrice()); // 购买时设置价格

            // 使用 BigDecimal 的 multiply 方法计算总价
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