package com.test.service;

import com.test.pojo.EBook;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "bookProvider", url = "http://localhost:9527")
public interface BookService {

    @RequestMapping("/list")
    List<EBook> findAllBooks();

    @RequestMapping("/search")
    List<EBook> searchBook(@RequestBody EBook book);

    @RequestMapping("/get/{id}")
    EBook getBookById(@PathVariable("id") Integer id);

    @RequestMapping("/status/{id}/{status}")
    Boolean updateStatus(@PathVariable("id") Integer id, @PathVariable("status") String status);

    // [新增]
    @PostMapping("/updateInfo")
    Boolean updateBookInfo(@RequestBody EBook book);

    @RequestMapping("/del/{id}")
    void delBookById(@PathVariable("id") Integer id);

    // [修改] 返回 EBook
    @PostMapping("/addbook")
    EBook addBook(@RequestBody EBook book);

    // 兼容旧接口，虽然现在我们主要用 updateInfo
    @RequestMapping("/find/{id}")
    Boolean updateBook(@PathVariable("id") Integer id);
}