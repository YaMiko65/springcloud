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

    // 对应 Provider 的 updateBookInfo 方法
    @PostMapping("/updateInfo")
    Boolean updateBookInfo(@RequestBody EBook book);

    @RequestMapping("/del/{id}")
    void delBookById(@PathVariable("id") Integer id);

    @PostMapping("/addbook")
    EBook addBook(@RequestBody EBook book);
}