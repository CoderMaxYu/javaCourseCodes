package com.yuwq.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountDO implements Serializable {

    private static final long serialVersionUID = -81849676368907419L;

    private Integer id;

    private String accountName;

    private String accountBalance;

    private String frozenBalance;
}