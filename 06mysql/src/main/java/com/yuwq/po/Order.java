package com.yuwq.po;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order {

    private Long orderId;

    private Long userId;

    private BigDecimal totalPrice;

    private Date createTime;

    private Date updateTime;

    private Integer status;
}
