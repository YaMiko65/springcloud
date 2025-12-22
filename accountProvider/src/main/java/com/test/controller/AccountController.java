package com.test.controller;

import com.test.dao.AccountDao;
import com.test.pojo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @PostMapping("/updatePwd")
    public boolean updatePassword(@RequestParam("username") String username,
                                  @RequestParam("password") String password) {
        int rows = accountDao.updatePassword(username, password);
        return rows > 0;
    }

    @PostMapping("/updateStatus")
    public boolean updateStatus(@RequestParam("username") String username,
                                @RequestParam("valid") String valid) {
        int rows = accountDao.updateStatus(username, valid);
        return rows > 0;
    }

    @GetMapping("/list")
    public List<UserDto> getAllUsers() {
        return accountDao.findAllUsers();
    }

    // [新增] 添加用户接口
    @PostMapping("/add")
    public boolean addUser(@RequestBody UserDto user) {
        int rows = accountDao.addUser(user);
        return rows > 0;
    }

    // [新增] 删除用户接口
    @PostMapping("/delete")
    public boolean deleteUser(@RequestParam("id") Integer id) {
        int rows = accountDao.deleteUser(id);
        return rows > 0;
    }
}