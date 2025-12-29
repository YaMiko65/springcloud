package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.Inventory;
import com.test.pojo.Order;
import com.test.pojo.UserDto;
import com.test.service.BookService;
import com.test.service.LoginService;
import com.test.service.RemoteInventoryService;
import com.test.service.RemoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RemoteInventoryService inventoryService;

    // 【新增】注入订单服务和登录服务
    @Autowired
    private RemoteOrderService remoteOrderService;

    @Autowired
    private LoginService loginService;

    // --- 辅助方法：填充库存信息 ---
    private void populateStock(List<EBook> books) {
        if (books == null || books.isEmpty()) return;

        for (EBook book : books) {
            try {
                Inventory inv = inventoryService.getStockByBookId(book.getId());
                if (inv != null && inv.getStock() != null) {
                    book.setStock(inv.getStock());
                } else {
                    book.setStock(0);
                }
            } catch (Exception e) {
                System.err.println("查询库存失败 bookId=" + book.getId() + ": " + e.getMessage());
                book.setStock(0);
            }
        }
    }

    // 列表页
    @RequestMapping("/list")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        populateStock(books);
        model.addAttribute("books", books);
        return "book_list";
    }

    // 搜索
    @RequestMapping("/search")
    public String searchBooks(EBook book, Model model) {
        List<EBook> books = bookService.searchBook(book);
        populateStock(books);
        model.addAttribute("books", books);
        return "book_list";
    }

    // [修改] 借阅图书
    @RequestMapping("/find/{id}")
    public String updateBook(@PathVariable("id") Integer id,
                             @RequestParam(value = "count", defaultValue = "1") Integer count) {

        // 1. 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/loginview";
        }
        String username = authentication.getName();
        UserDto user = loginService.getUserByUsername(username);
        if (user == null) {
            return "redirect:/loginview";
        }

        // 2. 调用库存服务扣减库存
        boolean success = inventoryService.decreaseStock(id, count);

        if (success) {
            // 3. 【新增逻辑】库存扣减成功，创建借阅订单
            try {
                Order order = new Order();
                order.setUserId(Long.valueOf(user.getId())); // 设置用户ID
                order.setBookId(Long.valueOf(id));           // 设置图书ID
                order.setCount(count);

                // 关键：借阅订单价格设为0，状态设为0（借阅中）
                order.setPrice(BigDecimal.ZERO);
                order.setTotalPrice(BigDecimal.ZERO);
                order.setStatus(0);
                order.setCreateTime(new Date());

                // 调用远程订单服务保存
                remoteOrderService.createOrder(order);

                System.out.println("借阅成功，订单已创建。图书ID: " + id);
            } catch (Exception e) {
                System.err.println("借阅订单创建失败: " + e.getMessage());
                // 实际生产中可能需要回滚库存，此处暂略
            }
        } else {
            System.out.println("借阅失败，库存不足，图书ID: " + id);
        }

        return "redirect:/book/list";
    }
}