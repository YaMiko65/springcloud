package com.test.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal; // [新增导包]
import java.util.Date;

@Data
@TableName("t_order") // 对应数据库表名 t_order
public class Order implements Serializable {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private Long userId;

    private Long bookId;

    // [新增] 数量
    private Integer count;

    // [新增] 单价
    private BigDecimal price;

    // [新增] 总价 (对应数据库字段通常为 total_price，MyBatisPlus会自动映射驼峰)
    private BigDecimal totalPrice;

    // 格式化时间，方便前端显示
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}