package com.yuwq.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FrozenDO implements Serializable {

    private static final long serialVersionUID = -81849676368907419L;

    private Integer id;

    private String userId;

    private String inAccountNo;

    private String outAccountNo;

    private Double balance;

    private Date createTime;

    private Date updateTime;
}