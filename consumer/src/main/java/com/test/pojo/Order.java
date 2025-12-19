package com.test.pojo;

import com.fasterxml.jackson.annotation.JsonFormat; // [新增导包]
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
}