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
    public List<EBook> findAllBooks() { return bookService.findAll(); }

    @RequestMapping("/search")
    public List<EBook> searchBook(@RequestBody EBook books) { return bookService.searchBooks(books); }

    @RequestMapping("/get/{id}")
    public EBook getBookById(@PathVariable("id") Integer id) { return bookService.getById(id); }

    // 更新状态
    @RequestMapping("/status/{id}/{status}")
    public Boolean updateBookStatus(@PathVariable("id") Integer id, @PathVariable("status") String status){
        EBook eBook = bookService.getById(id);
        if(eBook != null){
            eBook.setStatus(status);
            return bookService.updateById(eBook);
        }
        return false;
    }

    // [核心功能] 处理图书信息的全量更新
    @PostMapping("/updateInfo")
    public Boolean updateBookInfo(@RequestBody EBook book) {
        // 使用 MyBatis Plus 的 updateById 方法更新数据库
        return bookService.updateById(book);
    }

    @RequestMapping("/del/{id}")
    public void delBookById(@PathVariable("id") Integer id){ bookService.removeById(id); }

    // [核心功能] 新增图书，并返回带 ID 的对象
    @PostMapping("/addbook")
    public EBook addBook(@RequestBody EBook book){
        bookService.save(book);
        return book; // 返回的对象包含自动生成的 ID
    }
}