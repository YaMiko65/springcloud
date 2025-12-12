package com.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.EBook;

import java.util.List;

public interface BookService extends IService<EBook> {

    public List<EBook> findAll();

    public List<EBook> searchBooks(EBook book);

}
