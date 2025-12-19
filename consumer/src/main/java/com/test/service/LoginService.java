package com.test.service;

import com.test.pojo.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

// [修改处] 增加 url 属性，强制指向本地 8003 端口
@FeignClient(name = "loginProvider", url = "http://localhost:8003")
public interface LoginService {

    @RequestMapping("/getUserByUsername/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username);

    @RequestMapping("/findUserByUserId/{userId}")
    public List<String> findUserByUserId(@PathVariable("userId") Integer userId);
}