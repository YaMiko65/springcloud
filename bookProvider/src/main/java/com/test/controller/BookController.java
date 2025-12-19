package com.test.controller;

import com.test.pojo.EBook;
import com.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/list")
    public List<EBook> findAllBooks() {
        return bookService.findAll();
    }

    @RequestMapping("/search")
    public List<EBook> searchBook(@RequestBody EBook books) {
        return bookService.searchBooks(books);
    }

    // [新增] 获取单本图书详情，用于计算价格
    @RequestMapping("/get/{id}")
    public EBook getBookById(@PathVariable("id") Integer id) {
        return bookService.getById(id);
    }

    // [重构] 通用状态修改接口 (借阅传1，购买传3)
    @RequestMapping("/status/{id}/{status}")
    public Boolean updateBookStatus(@PathVariable("id") Integer id, @PathVariable("status") String status){
        EBook eBook = bookService.getById(id);
        if(eBook != null){
            eBook.setStatus(status);
            return bookService.updateById(eBook);
        }
        return false;
    }

    // [保留/兼容] 旧的借阅接口，现在重定向到新的状态接口逻辑
    @RequestMapping("/find/{id}")
    public Boolean updateBook(@PathVariable("id") Integer id){
        return updateBookStatus(id, "1");
    }

    @RequestMapping("/del/{id}")
    public void delBookById(@PathVariable("id") Integer id){
        bookService.removeById(id);
    }

    @RequestMapping("/addbook")
    public void addBook(@RequestBody EBook book){
        bookService.save(book);
    }
}