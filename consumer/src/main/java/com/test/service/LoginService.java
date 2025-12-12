package com.test.service;

import com.test.pojo.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "loginProvider")
public interface LoginService {

    @RequestMapping("/getUserByUsername/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username);

    @RequestMapping("/findUserByUserId/{userId}")
    public List<String> findUserByUserId(@PathVariable("userId") Integer userId);

}
