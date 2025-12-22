package com.test.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer count;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer status; // 0-未归还, 1-已归还

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}