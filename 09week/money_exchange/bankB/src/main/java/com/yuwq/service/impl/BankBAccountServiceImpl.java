package com.yuwq.service.impl;

import com.yuwq.entity.AccountDO;
import com.yuwq.mapper.AccountInfoMapper;
import com.yuwq.service.BankBAccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bankBAccountService")
@Slf4j
public class BankBAccountServiceImpl implements BankBAccountService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Override
    @Transactional
    @HmilyTCC(confirmMethod = "confirmMethod", cancelMethod = "cancelMethod")
    public boolean increaseAccountBalance(String user_id, String inAccountNo, double amount) {
        //钱汇入人民币账户
        AccountDO inAccountDO = new AccountDO();
        inAccountDO.setAccountNo("LS_RMB");
        inAccountDO.setBalance(amount);
        inAccountDO.setUserId(user_id);
        accountInfoMapper.increaseAccount(inAccountDO);
        log.info("******** BankB Service Begin try ...");
        return true;

    }

    @Transactional
    public void confirmMethod(String user_id, String inAccountNo, double amount) {

        //钱汇出人民币账户
        AccountDO outaccountDO = new AccountDO();
        outaccountDO.setAccountNo("LS_RMB");
        outaccountDO.setBalance(amount);
        outaccountDO.setUserId(user_id);
        accountInfoMapper.decreaseAccount(outaccountDO);
        //钱汇入美元账户
        AccountDO inAccountDO = new AccountDO();
        inAccountDO.setAccountNo("LS_MY");
        inAccountDO.setBalance(amount / 7);
        inAccountDO.setUserId(user_id);
        accountInfoMapper.increaseAccount(inAccountDO);

        log.info("******** BankB Service commit...  ");
    }

    public void cancelMethod(String user_id, String inAccountNo, double amount) {
        //钱汇出人民币账户
        AccountDO outaccountDO = new AccountDO();
        outaccountDO.setAccountNo("LS_RMB");
        outaccountDO.setBalance(amount);
        outaccountDO.setUserId(user_id);
        accountInfoMapper.decreaseAccount(outaccountDO);
        log.info("******** BankB Service begin cancel...  ");

    }
}
