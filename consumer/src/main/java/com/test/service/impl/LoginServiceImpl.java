package com.test.service.impl;

import com.test.pojo.UserDto;
import com.test.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            // 如果找不到用户，可以返回 null 由 Spring Security 处理，或抛出 UsernameNotFoundException
            return null;
        }

        // 2. 获取用户权限
        List<String> privilde = loginService.findUserByUserId(userDto.getId());
        String[] privildeArrays = new String[privilde.size()];
        privilde.toArray(privildeArrays);

        // 3. 判断账号状态
        // 假设 userDto.getValid() 为 "1" 时表示账号有效（启用），其他值表示无效（禁用）
        // 请根据您数据库实际存储的值（如 "1"/"0" 或 "true"/"false"）调整此处的判断逻辑
        boolean isEnabled = "1".equals(userDto.getValid());

        // 4. 构建 UserDetails 对象，并设置禁用状态
        UserDetails userDetails = User.withUsername(userDto.getUsername())
                .password(userDto.getPassword())
                .disabled(!isEnabled) // 关键修改：如果 isEnabled 为 false，则将账户设为 disabled
                .authorities(privildeArrays).build();

        return userDetails;
    }
}