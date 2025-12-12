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
        UserDto userDto = loginDao.getUserByUsername(username);
        return userDto;
    }

    @Override
    public List<String> findUserByUserId(Integer userId) {
        List<String> userByUserId = loginDao.findUserByUserId(userId);
        return userByUserId;
    }
}
