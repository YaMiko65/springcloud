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

    // 修改账号状态
    public int updateStatus(String username, String valid) {
        String sql = "UPDATE user SET valid = ? WHERE username = ?";
        return jdbcTemplate.update(sql, valid, username);
    }

    // 获取所有用户（供管理员使用）
    public List<UserDto> findAllUsers() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserDto.class));
    }

    // [修改] 添加新用户 (增加重复性校验)
    public int addUser(UserDto user) {
        // 1. 检查用户名是否存在
        String checkSql = "SELECT count(*) FROM user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, user.getUsername());

        if (count != null && count > 0) {
            return 0; // 用户名已存在，返回 0 表示没有任何行被插入
        }

        // 2. 执行插入，默认状态为1（正常）
        String sql = "INSERT INTO user (username, password, valid) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), "1");
    }

    // 删除用户
    public int deleteUser(Integer id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}