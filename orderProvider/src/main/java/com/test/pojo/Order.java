package com.test.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data; // 确保 pom.xml 中有 lombok 依赖，如果没有，请手动生成 getter/setter

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_order") // 对应数据库表名 t_order
public class Order implements Serializable {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private Long userId;

    private Long bookId;

    // 格式化时间，方便前端显示
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}