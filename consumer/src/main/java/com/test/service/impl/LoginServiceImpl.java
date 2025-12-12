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

        UserDto userDto = loginService.getUserByUsername(username);
        if (userDto == null) {
            return null;
        }

        List<String> privilde = loginService.findUserByUserId(userDto.getId());
        String[] privildeArrays = new String[privilde.size()];
        privilde.toArray(privildeArrays);

        UserDetails userDetails = User.withUsername(userDto.getUsername())
                .password(userDto.getPassword())
                .authorities(privildeArrays).build();

        return userDetails;
    }
}
