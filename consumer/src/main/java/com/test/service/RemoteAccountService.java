package com.test.service;

import com.test.pojo.UserDto; // 确保consumer也有这个POJO，或者使用Map
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "accountProvider", url = "http://localhost:8006")
public interface RemoteAccountService {

    @PostMapping("/account/updatePwd")
    boolean updatePassword(@RequestParam("username") String username, @RequestParam("password") String password);

    @GetMapping("/account/list")
    List<UserDto> getAllUsers();
}