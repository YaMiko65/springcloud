package com.test.service;

import com.test.pojo.UserDto;

import java.util.List;

public interface LoginService {

    public UserDto getUserByUsername(String username);

    public List<String> findUserByUserId(Integer userId);

}
