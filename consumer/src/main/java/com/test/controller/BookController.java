package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.User;
import com.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/list")
    public String findAllBooks(Model model, HttpSession session) {
        List<EBook> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        session.setAttribute("user",new User("zhangsan","ADMIN"));
        return "book_list";
    }

    @RequestMapping("/search")
    public String searchBooks(EBook book,Model model,HttpSession session) {
        List<EBook> books = bookService.searchBook(book);
        model.addAttribute("books",books);
        session.setAttribute("user",new User("zhangsan","ADMIN"));
        return "book_list";
    }

    @RequestMapping("/find/{id}")
    public String updateBook(@PathVariable("id") Integer id, Model model, HttpSession session) {
        bookService.updateBook(id);
        session.setAttribute("user",new User("zhangsan","ADMIN"));
        return "book_list";
    }


}
