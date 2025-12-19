package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.Inventory;
import com.test.service.BookService;
import com.test.service.RemoteInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("book/admin")
public class BookControllerAdmin {

    @Autowired
    private BookService bookService;

    @Autowired
    private RemoteInventoryService inventoryService;

    // 辅助方法：批量填充库存信息
    private void populateStock(List<EBook> books) {
        if (books == null) return;
        for (EBook book : books) {
            try {
                // 远程调用库存服务获取库存
                Inventory inv = inventoryService.getStockByBookId(book.getId());
                book.setStock(inv != null && inv.getStock() != null ? inv.getStock() : 0);
            } catch (Exception e) {
                // 容错处理，如果库存服务不可用，显示0
                book.setStock(0);
                e.printStackTrace();
            }
        }
    }

    // 图书管理列表页
    @RequestMapping("manag")
    @Secured("ROLE_ADMIN")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        populateStock(books); // 填充库存，确保管理员看到最新数据
        model.addAttribute("books", books);
        return "book_manag";
    }

    // 搜索功能
    @RequestMapping("search")
    public String searchBook(Model model, EBook book) {
        List<EBook> books = bookService.searchBook(book);
        populateStock(books);
        model.addAttribute("books", books);
        return "book_manag";
    }

    // 跳转到添加页面
    @RequestMapping("jumpaddbook")
    public String jumpBookAdd(Model model) {
        model.addAttribute("book", new EBook()); // 传空对象用于新增表单绑定
        return "addpagebook";
    }

    // [核心功能] 跳转到编辑页面 (数据回显)
    @RequestMapping("jumpedit/{id}")
    public String jumpBookEdit(@PathVariable("id") Integer id, Model model) {
        // 1. 远程调用获取图书基本信息
        EBook book = bookService.getBookById(id);

        // 2. 远程调用获取库存信息
        Inventory inv = inventoryService.getStockByBookId(id);
        if (inv != null && inv.getStock() != null) {
            book.setStock(inv.getStock());
        } else {
            book.setStock(0);
        }

        model.addAttribute("book", book);
        return "addpagebook"; // 复用添加页面
    }

    // [核心功能] 统一保存接口 (支持新增和修改)
    @PostMapping("savebook")
    public String saveBook(EBook book, @RequestParam(value = "stock", defaultValue = "0") Integer stock) {
        if (book.getId() == null) {
            // --- 新增逻辑 ---
            EBook savedBook = bookService.addBook(book);
            // 只有当图书保存成功且有ID返回时，才保存库存
            if (savedBook != null && savedBook.getId() != null) {
                saveInventory(savedBook.getId(), stock);
            }
        } else {
            // --- 修改逻辑 ---
            // 1. 更新图书基本信息 (调用 Provider 的 updateInfo 接口)
            bookService.updateBookInfo(book);
            // 2. 更新库存信息 (调用 Inventory Provider 的接口)
            saveInventory(book.getId(), stock);
        }
        return "redirect:/book/admin/manag";
    }

    // 辅助方法：保存库存
    private void saveInventory(Integer bookId, Integer stock) {
        Inventory inv = new Inventory();
        inv.setBookId(bookId);
        inv.setStock(stock);
        inventoryService.saveStock(inv);
    }

    // 删除图书
    @RequestMapping("del/{id}")
    public String deleteBook(@PathVariable("id") Integer id) {
        bookService.delBookById(id);
        return "redirect:/book/admin/manag";
    }
}