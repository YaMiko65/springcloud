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

    // 列表页
    @RequestMapping("/list")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "book_list";
    }

    // 搜索
    @RequestMapping("/search")
    public String searchBooks(EBook book, Model model) {
        List<EBook> books = bookService.searchBook(book);
        model.addAttribute("books", books);
        return "book_list";
    }

    // 借阅图书 (保留)
    @RequestMapping("/find/{id}")
    public String updateBook(@PathVariable("id") Integer id) {
        // 调用 Feign 更新状态为 1 (借阅中)
        bookService.updateStatus(id, "1");
        return "redirect:/book/list";
    }

}