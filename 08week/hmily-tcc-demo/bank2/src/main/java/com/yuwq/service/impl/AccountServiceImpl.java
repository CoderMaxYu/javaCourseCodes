package com.yuwq.service.impl;

import com.yuwq.mapper.AccountInfoMapper;
import com.yuwq.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Override
    @Transactional
    @HmilyTCC(confirmMethod = "confirmMethod", cancelMethod = "cancelMethod")
    public void increaseAccountBalance(String accountName, Double amount) {
        accountInfoMapper.increaseAccountBalance(accountName, amount);
        log.info("******** Bank2 Service Begin try ...");

    }
    //TODO hmily报错，找不到confirmMethod方法
    public void confirmMethod(String accountName, Double amount) {
        accountInfoMapper.confirmAccountBalance();
        log.info("******** Bank2 Service commit...  ");
    }

    public void cancelMethod(String accountName, Double amount) {
        accountInfoMapper.cancelAccountBalance(accountName);
        log.info("******** Bank2 Service begin cancel...  ");

    }
}
