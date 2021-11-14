package com.yuwq.service;


import com.yuwq.po.AccountInfo;

public interface AccountService {

    Boolean decreaseBalance(String name,Double amount);

    AccountInfo selectByName(String accountName);
}
