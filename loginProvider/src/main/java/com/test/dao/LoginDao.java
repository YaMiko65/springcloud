package com.test.dao;

import com.test.pojo.Privileda;
import com.test.pojo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LoginDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public UserDto getUserByUsername(String username) {
        String sql = "select * from user where username = ?";
        List<UserDto> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserDto.class), username);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public List<String> findUserByUserId(Integer userId) {
        String sql = "select * from user u,priv p,user_priv up where up.user_id=u.id and up.priv_id=p.id and u.id=?";
        List<Privileda> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Privileda.class), userId);

        List<String> privilegeArray = new ArrayList<>();
        list.forEach(p -> privilegeArray.add(p.getAuthority()));
        return privilegeArray;
    }

}
