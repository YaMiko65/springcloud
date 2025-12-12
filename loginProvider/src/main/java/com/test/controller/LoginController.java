package com.test.controller;

import com.test.pojo.UserDto;
import com.test.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/getUserByUsername/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username){
        return loginService.getUserByUsername(username);
    };

    @RequestMapping("/findUserByUserId/{userId}")
    public List<String> findUserByUserId(@PathVariable("userId") Integer userId){
        return loginService.findUserByUserId(userId);
    };


}
