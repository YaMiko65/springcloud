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

    @RequestMapping("/get/{id}")
    public EBook getBookById(@PathVariable("id") Integer id) {
        return bookService.getById(id);
    }

    // 修改状态
    @RequestMapping("/status/{id}/{status}")
    public Boolean updateBookStatus(@PathVariable("id") Integer id, @PathVariable("status") String status){
        EBook eBook = bookService.getById(id);
        if(eBook != null){
            eBook.setStatus(status);
            return bookService.updateById(eBook);
        }
        return false;
    }

    // [新增] 全量更新图书信息 (用于编辑)
    @PostMapping("/updateInfo")
    public Boolean updateBookInfo(@RequestBody EBook book) {
        return bookService.updateById(book);
    }

    @RequestMapping("/del/{id}")
    public void delBookById(@PathVariable("id") Integer id){
        bookService.removeById(id);
    }

    // [修改] 返回 EBook 以便获取生成的 ID
    @PostMapping("/addbook")
    public EBook addBook(@RequestBody EBook book){
        bookService.save(book);
        return book;
    }
}