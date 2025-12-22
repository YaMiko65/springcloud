package com.test.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_order")
public class Order implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long bookId;

    private Integer count;

    private BigDecimal price;

    private BigDecimal totalPrice;

    // [新增] 订单状态：0-未归还/进行中, 1-已归还/已完成
    // 注意：请在数据库 t_order 表中添加字段 status int default 0
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // [辅助字段] 用于前端显示书籍名称和用户名，数据库中无此字段
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String bookName;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String userName;
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private String orderType;
}