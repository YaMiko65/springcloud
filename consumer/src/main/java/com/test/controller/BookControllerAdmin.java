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

    // 辅助方法：填充库存
    private void populateStock(List<EBook> books) {
        if (books == null) return;
        for (EBook book : books) {
            try {
                Inventory inv = inventoryService.getStockByBookId(book.getId());
                book.setStock(inv != null && inv.getStock() != null ? inv.getStock() : 0);
            } catch (Exception e) {
                book.setStock(0);
            }
        }
    }

    @RequestMapping("manag")
    @Secured("ROLE_ADMIN")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        populateStock(books);
        model.addAttribute("books", books);
        return "book_manag";
    }

    @RequestMapping("search")
    public String searchBook(Model model, EBook book) {
        List<EBook> books = bookService.searchBook(book);
        populateStock(books);
        model.addAttribute("books", books);
        return "book_manag";
    }

    // 跳转添加页面
    @RequestMapping("jumpaddbook")
    public String jumpBookAdd(Model model) {
        model.addAttribute("book", new EBook()); // 传空对象
        return "addpagebook";
    }

    // [新增] 跳转编辑页面
    @RequestMapping("jumpedit/{id}")
    public String jumpBookEdit(@PathVariable("id") Integer id, Model model) {
        EBook book = bookService.getBookById(id);
        // 获取库存并设置到 book 对象中，以便在页面显示
        Inventory inv = inventoryService.getStockByBookId(id);
        if (inv != null) {
            book.setStock(inv.getStock());
        }
        model.addAttribute("book", book);
        return "addpagebook"; // 复用添加页面
    }

    // [修改] 统一保存接口（处理添加和修改）
    @PostMapping("savebook")
    public String saveBook(EBook book, @RequestParam(value = "stock", defaultValue = "0") Integer stock) {
        if (book.getId() == null) {
            // 新增
            EBook savedBook = bookService.addBook(book);
            if (savedBook != null && savedBook.getId() != null) {
                // 保存库存
                Inventory inv = new Inventory();
                inv.setBookId(savedBook.getId());
                inv.setStock(stock);
                inventoryService.saveStock(inv);
            }
        } else {
            // 修改
            bookService.updateBookInfo(book);
            // 更新库存
            Inventory inv = new Inventory();
            inv.setBookId(book.getId());
            inv.setStock(stock);
            inventoryService.saveStock(inv);
        }
        return "redirect:/book/admin/manag";
    }

    @RequestMapping("del/{id}")
    public String deleteBook(@PathVariable("id") Integer id) {
        bookService.delBookById(id);
        return "redirect:/book/admin/manag";
    }
}