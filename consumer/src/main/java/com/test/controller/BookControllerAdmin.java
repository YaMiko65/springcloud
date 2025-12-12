package com.test.controller;

import com.test.pojo.EBook;
import com.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("book/admin")
public class BookControllerAdmin {

    @Autowired
    private BookService bookService;

    @RequestMapping("manag")
    @Secured("ROLE_ADMIN")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        model.addAttribute("books", books);
        return "book_manag";
    }

    @RequestMapping("search")
    public String searchBook(Model model,EBook book) {
        List<EBook> books = bookService.searchBook(book);
        model.addAttribute("books", books);
        return "book_manag";
    }

    @RequestMapping("find/{id}")
    public String updateBook(Model model,@PathVariable("id") Integer id){
        bookService.updateBook(id);
        return "book_manag";
    }

    @RequestMapping("del/{id}")
    public String deleteBook(Model model,@PathVariable("id") Integer id){
        bookService.delBookById(id);
        return "book_manag";
    }

    @RequestMapping("jumpaddbook")
    public String jumpBookAdd(){
        return "addpagebook";
    }

    @RequestMapping("addbook")
    public String jumpBooksAdd(EBook book){
        bookService.addBook(book);
        return "redirect:/book/admin/manag";
    }

}
