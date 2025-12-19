package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.Order;
import com.test.service.BookService;
import com.test.service.RemoteOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RemoteOrderService remoteOrderService;

    @RequestMapping("/list")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "book_list";
    }

    @RequestMapping("/search")
    public String searchBooks(EBook book, Model model) {
        List<EBook> books = bookService.searchBook(book);
        model.addAttribute("books", books);
        return "book_list";
    }

    // 借阅图书
    @RequestMapping("/find/{id}")
    public String updateBook(@PathVariable("id") Integer id) {
        // 调用 Feign 更新状态为 1 (借阅中)
        bookService.updateStatus(id, "1");
        return "redirect:/book/list";
    }

    // [新增] 购买图书
    @RequestMapping("/buy/{id}")
    public String buyBook(@PathVariable("id") Integer id) {
        // 1. 获取图书信息
        EBook book = bookService.getBookById(id);

        if (book != null && "0".equals(book.getStatus())) {
            // 2. 创建订单对象
            Order order = new Order();
            order.setBookId(Long.valueOf(book.getId()));
            // TODO: 这里应该从 SecurityContextHolder 获取当前登录用户的ID
            order.setUserId(1L); // 暂时硬编码为用户ID 1
            order.setCount(1);
            order.setPrice(book.getPrice());
            order.setTotalPrice(book.getPrice());
            order.setCreateTime(new Date());

            // 3. 调用远程订单服务下单
            remoteOrderService.createOrder(order);

            // 4. 更新图书状态为 3 (已售出)
            bookService.updateStatus(id, "3");
        }

        return "redirect:/consumer/order/view"; // 购买成功后跳转到我的订单页面
    }
}