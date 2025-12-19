package com.test.service;

import com.test.pojo.EBook;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "bookProvider")
public interface BookService {

    @RequestMapping("/list")
    List<EBook> findAllBooks();

    @RequestMapping("/search")
    List<EBook> searchBook(@RequestBody EBook books);

    // 获取图书详情
    @RequestMapping("/get/{id}")
    EBook getBookById(@PathVariable("id") Integer id);

    // 更新图书状态 (1:借阅, 3:购买)
    @RequestMapping("/status/{id}/{status}")
    Boolean updateStatus(@PathVariable("id") Integer id, @PathVariable("status") String status);

    @RequestMapping("/find/{id}")
    Boolean updateBook(@PathVariable("id") Integer id); // 旧接口

    @RequestMapping("/del/{id}")
    void delBookById(@PathVariable("id") Integer id);

    @RequestMapping("/addbook")
    void addBook(@RequestBody EBook book);
}