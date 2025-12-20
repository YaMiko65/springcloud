package com.test.controller;

import com.test.pojo.EBook;
import com.test.pojo.Inventory;
import com.test.service.BookService;
import com.test.service.RemoteInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RemoteInventoryService inventoryService; // 注入库存服务

    // --- 辅助方法：填充库存信息 ---
    private void populateStock(List<EBook> books) {
        if (books == null || books.isEmpty()) return;

        for (EBook book : books) {
            try {
                // 调用远程服务查询库存
                Inventory inv = inventoryService.getStockByBookId(book.getId());
                if (inv != null && inv.getStock() != null) {
                    book.setStock(inv.getStock());
                } else {
                    book.setStock(0); // 查不到默认0
                }
            } catch (Exception e) {
                // 捕获异常，防止库存服务挂掉导致整个页面无法访问
                System.err.println("查询库存失败 bookId=" + book.getId() + ": " + e.getMessage());
                book.setStock(0);
            }
        }
    }

    // 列表页
    @RequestMapping("/list")
    public String findAllBooks(Model model) {
        List<EBook> books = bookService.findAllBooks();
        populateStock(books); // 填充库存
        model.addAttribute("books", books);
        return "book_list";
    }

    // 搜索
    @RequestMapping("/search")
    public String searchBooks(EBook book, Model model) {
        List<EBook> books = bookService.searchBook(book);
        populateStock(books); // 填充库存
        model.addAttribute("books", books);
        return "book_list";
    }

    // [修改] 借阅图书，增加数量参数
    @RequestMapping("/find/{id}")
    public String updateBook(@PathVariable("id") Integer id,
                             @RequestParam(value = "count", defaultValue = "1") Integer count) {
        // 调用库存服务扣减库存
        boolean success = inventoryService.decreaseStock(id, count);

        if (success) {
            // 扣减成功，代表借阅成功。
            // 注意：这里不再将图书状态改为"1"(借阅中)，只要还有库存，其他人就能继续借阅。
            // 如果业务需求是借阅一次库存-1，此处逻辑已满足。
            System.out.println("借阅成功，图书ID: " + id + ", 数量: " + count);
        } else {
            // 扣减失败（库存不足），可以添加错误处理逻辑
            System.out.println("借阅失败，库存不足，图书ID: " + id);
        }

        return "redirect:/book/list";
    }
}