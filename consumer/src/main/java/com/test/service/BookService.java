package com.test.service;

import com.test.pojo.EBook;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// [修改] name保留(用于负载均衡标识)，增加 url 指向网关
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

    @RequestMapping("/find/{id}")
    Boolean updateBook(@PathVariable("id") Integer id);

    @RequestMapping("/del/{id}")
    void delBookById(@PathVariable("id") Integer id);

    @RequestMapping("/addbook")
    void addBook(@RequestBody EBook book);
}