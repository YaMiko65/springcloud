package com.test.controller;

import com.test.dao.BookMapper;
import com.test.pojo.EBook;
import com.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/list")
    public List<EBook> findAllBooks() {
        List<EBook> books = bookService.findAll();
        return books;
    }

    /**
     * @RequestBody 请求
     * @ResponseBody 响应
     */
    @RequestMapping("/search")
    public List<EBook> searchBook(@RequestBody EBook books) {
        List<EBook> bs = bookService.searchBooks(books);
        return bs;
    }

    @RequestMapping("/find/{id}")
    public Boolean updateBook(@PathVariable("id") Integer id){

        //从数据库根据id获取数据
        EBook eBook = bookService.getById(id);
        //修改数据--图书状态改成1
        eBook.setStatus("1");
        //根据id存储数据
        boolean update = bookService.updateById(eBook);
        return update;
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
