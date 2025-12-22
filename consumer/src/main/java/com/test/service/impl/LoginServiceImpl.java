package com.test.service.impl;

import com.test.pojo.UserDto;
import com.test.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginServiceImpl implements UserDetailsService {

    @Autowired
    private LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 获取用户信息
        UserDto userDto = loginService.getUserByUsername(username);
        if (userDto == null) {
            // 如果找不到用户，可以返回 null 由 Spring Security 处理
            return null;
        }

        // 2. 获取用户权限
        List<String> privilde = loginService.findUserByUserId(userDto.getId());

        // [核心修改]：处理权限列表，确保新用户拥有默认权限
        // 创建一个新的 ArrayList 以确保集合是可变的（避免 RPC 返回不可变列表导致报错）
        List<String> authorities = new ArrayList<>();
        if (privilde != null) {
            authorities.addAll(privilde);
        }

        // 如果权限列表为空（说明是新用户或未分配角色的用户），默认添加 ROLE_COMMON 权限
        // 这样该用户登录后就能看到 main.html 中要求 ROLE_COMMON 的菜单项
        if (authorities.isEmpty()) {
            authorities.add("ROLE_COMMON");
        }

        // 将 List 转换为数组
        String[] privildeArrays = authorities.toArray(new String[0]);

        // 3. 判断账号状态
        // 假设 userDto.getValid() 为 "1" 时表示账号有效
        boolean isEnabled = "1".equals(userDto.getValid());

        // 4. 构建 UserDetails 对象
        UserDetails userDetails = User.withUsername(userDto.getUsername())
                .password(userDto.getPassword())
                .disabled(!isEnabled)
                .authorities(privildeArrays).build();

        return userDetails;
    }
}