package com.test.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer count;
    private BigDecimal price;
    private BigDecimal totalPrice;

    // [新增] 添加与 Provider 一致的格式化注解
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // --- 以下为新增展示字段 (不存储在数据库，仅用于前端显示) ---

    private String userName;  // 用户姓名
    private String bookName;  // 书籍名称
    private String orderType; // 订单类型 (购买/借阅)
}