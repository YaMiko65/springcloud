package com.test.controller;

import com.test.pojo.EBook;
import com.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/list")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        // 移除硬编码的 session 设置，改用 Spring Security 获取用户信息
        return "book_list";
    }

    @RequestMapping("/search")
    public String searchBooks(EBook book, Model model) {
        List<EBook> books = bookService.searchBook(book);
        model.addAttribute("books", books);
        return "book_list";
    }

    @RequestMapping("/find/{id}")
    public String updateBook(@PathVariable("id") Integer id) {
        bookService.updateBook(id);
        // 操作完成后重定向到列表页，实现数据刷新
        return "redirect:/book/list";
    }
}