package com.test.dao;

import com.test.pojo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 修改密码
    public int updatePassword(String username, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE username = ?";
        return jdbcTemplate.update(sql, newPassword, username);
    }

    // [新增] 修改账号状态
    public int updateStatus(String username, String valid) {
        String sql = "UPDATE user SET valid = ? WHERE username = ?";
        return jdbcTemplate.update(sql, valid, username);
    }

    // 获取所有用户（供管理员使用）
    public List<UserDto> findAllUsers() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserDto.class));
    }
}