package com.test.service;

import com.test.pojo.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

// 确保 name 和 url 配置正确
@FeignClient(name = "loginProvider", url = "http://localhost:8003")
public interface LoginService {

    @RequestMapping("/getUserByUsername/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username);

    // [新增] 对应 Provider 的接口
    @RequestMapping("/getUserById/{id}")
    public UserDto getUserById(@PathVariable("id") Integer id);

    @RequestMapping("/findUserByUserId/{userId}")
    public List<String> findUserByUserId(@PathVariable("userId") Integer userId);
}