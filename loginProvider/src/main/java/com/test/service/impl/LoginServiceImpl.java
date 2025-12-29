package com.test.service.impl;

import com.test.dao.LoginDao;
import com.test.pojo.UserDto;
import com.test.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;

    @Override
    public UserDto getUserByUsername(String username) {
        return loginDao.getUserByUsername(username);
    }

    // [新增]
    @Override
    public UserDto getUserById(Integer id) {
        return loginDao.getUserById(id);
    }

    @Override
    public List<String> findUserByUserId(Integer userId) {
        return loginDao.findUserByUserId(userId);
    }
}