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
    public List<EBook> findAllBooks();

    @RequestMapping("/search")
    public List<EBook> searchBook(@RequestBody EBook books);

    @RequestMapping("/find/{id}")
    public Boolean updateBook(@PathVariable("id") Integer id);

    @RequestMapping("/del/{id}")
    public void delBookById(@PathVariable("id") Integer id);

    @RequestMapping("/addbook")
    public void addBook(@RequestBody EBook book);
}
