package com.test.service;

import com.test.pojo.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "accountProvider", url = "http://localhost:8006")
public interface RemoteAccountService {

    @PostMapping("/account/updatePwd")
    boolean updatePassword(@RequestParam("username") String username, @RequestParam("password") String password);

    // [新增] 远程调用状态更新
    @PostMapping("/account/updateStatus")
    boolean updateStatus(@RequestParam("username") String username, @RequestParam("valid") String valid);

    @GetMapping("/account/list")
    List<UserDto> getAllUsers();
}