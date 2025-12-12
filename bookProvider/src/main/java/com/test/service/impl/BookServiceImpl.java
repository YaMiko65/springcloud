package com.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.dao.BookMapper;
import com.test.pojo.EBook;
import com.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, EBook> implements BookService {

    @Autowired
    private BookMapper bookMapper;

    public List<EBook> findAll() {
        List<EBook> books = bookMapper.selectList(null);
        return books;
    }

    @Override
    public List<EBook> searchBooks(EBook books) {
        //查询所有图书的list集合
        List<EBook> book = this.findAll();
        //条件查询到的list集合
        List<EBook> bs = new ArrayList<>();
        //获取条件
        String bname = books.getName();
        String bauthor = books.getAuthor();
        if (bname.isEmpty() && bauthor.isEmpty()) {
            bs = book;
        } else {
            for (EBook b : book) {
                if ((!bname.isEmpty() && b.getName().contains(bname))
                        || (!bauthor.isEmpty() && b.getAuthor().contains(bauthor))) {
                    bs.add(b);
                }
            }
        }
        return bs;
    }

}
