package com.yuwq.service.impl;

import com.yuwq.feign.Bank2Client;
import com.yuwq.mapper.AccountInfoMapper;
import com.yuwq.po.AccountInfo;
import com.yuwq.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private Bank2Client bank2Client;

    @Override
    @Transactional
    @HmilyTCC(confirmMethod = "confirmMethod", cancelMethod = "cancelMethod")
    public Boolean decreaseBalance(String name, Double amount) {

        //从账户扣减
        if (accountInfoMapper.decreaseBalance(name, amount) <= 0) {
            //扣减失败
            throw new HmilyRuntimeException("bank1 exception，扣减失败");
        }
        //远程调用bank2
        if (!bank2Client.transfer(amount)) {
            throw new HmilyRuntimeException("bank2Client exception");
        }
        if (amount == 10) {//异常一定要抛在Hmily里面
            throw new RuntimeException("bank1 make exception  10");
        }
        log.info("******** Bank1 Service  end try...  ");

        return Boolean.TRUE;
    }

    @Override
    public AccountInfo selectByName(String accountName) {
        return accountInfoMapper.selectByName(accountName);
    }


    public boolean confirmMethod(String name, Double amount) {
        int result = accountInfoMapper.confirm();
        log.info("******** Bank1 Service begin commit...");
        return result > 0;
    }

    public boolean cancelMethod(String name, Double amount) {
        int result = accountInfoMapper.cancel();
        log.info("******** Bank1 Service end rollback...  ");
        return result > 0;
    }

}
