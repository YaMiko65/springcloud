package com.test.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("inventory") // 假设数据库表名为 inventory
public class Inventory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer bookId; // 关联图书ID
    private Integer stock;  // 库存数量
}