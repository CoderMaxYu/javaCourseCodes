package com.yuwq.po;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order")
public class Order {
    @TableId
    private Long orderId;
    private Long userId;
    private Long totalPrice;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
