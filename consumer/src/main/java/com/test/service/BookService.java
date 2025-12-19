package com.test.service;

import com.test.pojo.EBook;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "bookProvider")
public interface BookService {

    // ================= 基础查询 =================
    @RequestMapping("/list")
    List<EBook> findAllBooks();

    @RequestMapping("/search")
    List<EBook> searchBook(@RequestBody EBook book);

    // ================= 购买/详情业务 (新增) =================
    // 获取图书详情（用于获取真实价格）
    @RequestMapping("/get/{id}")
    EBook getBookById(@PathVariable("id") Integer id);

    // 更新图书状态 (1:借阅, 3:购买)
    @RequestMapping("/status/{id}/{status}")
    Boolean updateStatus(@PathVariable("id") Integer id, @PathVariable("status") String status);

    // ================= 管理员/旧业务 (恢复缺失的方法) =================

    // 旧的借阅接口（兼容旧代码）
    @RequestMapping("/find/{id}")
    Boolean updateBook(@PathVariable("id") Integer id);

    // [恢复] 删除图书
    @RequestMapping("/del/{id}")
    void delBookById(@PathVariable("id") Integer id);

    // [恢复] 新增图书
    @RequestMapping("/addbook")
    void addBook(@RequestBody EBook book);
}