package com.test.pojo;

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
    private Date createTime;
}