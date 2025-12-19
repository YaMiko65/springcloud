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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("book/admin")
public class BookControllerAdmin {

    @Autowired
    private BookService bookService;

    @Autowired
    private RemoteInventoryService inventoryService; // 注入库存服务

    // --- 辅助方法：填充库存信息 ---
    private void populateStock(List<EBook> books) {
        if (books == null || books.isEmpty()) return;

        for (EBook book : books) {
            try {
                Inventory inv = inventoryService.getStockByBookId(book.getId());
                if (inv != null && inv.getStock() != null) {
                    book.setStock(inv.getStock());
                } else {
                    book.setStock(0);
                }
            } catch (Exception e) {
                System.err.println("查询库存失败 bookId=" + book.getId() + ": " + e.getMessage());
                book.setStock(0);
            }
        }
    }

    @RequestMapping("manag")
    @Secured("ROLE_ADMIN")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        populateStock(books); // 填充库存
        model.addAttribute("books", books);
        return "book_manag";
    }

    @RequestMapping("search")
    public String searchBook(Model model, EBook book) {
        List<EBook> books = bookService.searchBook(book);
        populateStock(books); // 填充库存
        model.addAttribute("books", books);
        return "book_manag";
    }

    @RequestMapping("find/{id}")
    public String updateBook(@PathVariable("id") Integer id) {
        bookService.updateBook(id);
        return "redirect:/book/admin/manag";
    }

    @RequestMapping("del/{id}")
    public String deleteBook(@PathVariable("id") Integer id) {
        bookService.delBookById(id);
        return "redirect:/book/admin/manag";
    }

    @RequestMapping("jumpaddbook")
    public String jumpBookAdd() {
        return "addpagebook";
    }

    @RequestMapping("addbook")
    public String jumpBooksAdd(EBook book) {
        bookService.addBook(book);
        return "redirect:/book/admin/manag";
    }
}