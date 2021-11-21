package com.yuwq.service.impl;

import com.yuwq.entity.AccountDO;
import com.yuwq.entity.FrozenDO;
import com.yuwq.mapper.AccountInfoMapper;
import com.yuwq.service.BankAAccountService;
import com.yuwq.service.BankBAccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bankAAccountService")
@Slf4j
public class BankAAccountServiceImpl implements BankAAccountService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private BankBAccountService bankBAccountService;

    @Override
    @Transactional
    @HmilyTCC(confirmMethod = "confirmMethod", cancelMethod = "cancelMethod")
    public Boolean decreaseBalance(String from, String to, double amount) {

        //扣减金额
        AccountDO outaccountDO = new AccountDO();
        outaccountDO.setAccountNo("ZS_MY");
        outaccountDO.setBalance(amount);
        outaccountDO.setUserId(from);

        AccountDO inAccountDO = new AccountDO();
        inAccountDO.setAccountNo("ZS_RMB");
        inAccountDO.setBalance(amount * 7);
        inAccountDO.setUserId(from);
        //将钱从美元账户转出
        int result1 = accountInfoMapper.decreaseAccount(outaccountDO);
        if (result1==0){
            throw new RuntimeException("账户金额不足，少于："+amount);
        }
        //将钱转入人民币账户
        accountInfoMapper.increaseAccount(inAccountDO);
        // 冻结金额
        FrozenDO frozenDO = new FrozenDO();
        frozenDO.setBalance(amount);//美元转人民币
        frozenDO.setOutAccountNo("ZS_MY");
        frozenDO.setUserId(from);
        int i = accountInfoMapper.insertFrozen(frozenDO);

        FrozenDO frozenDO2 = new FrozenDO();
        frozenDO2.setBalance(amount * 7);
        frozenDO2.setInAccountNo("ZS_RMB");
        frozenDO2.setUserId(from);
        int j = accountInfoMapper.insertFrozen(frozenDO2);

        //远程调用bank2，汇款
        if (!bankBAccountService.increaseAccountBalance(to, "LS_MY", amount)) {
            throw new HmilyRuntimeException("bank2Client exception");
        }

        log.info("******** BankA Service  end try...  ");

        return Boolean.TRUE;
    }

    public boolean confirmMethod(String from, String to, double amount) {
        accountInfoMapper.deleteOutFrozen(from, "ZS_MY", amount);
        accountInfoMapper.deleteInFrozen(from, "ZS_RMB", amount * 7);

        AccountDO inAccountDO = new AccountDO();
        inAccountDO.setAccountNo("ZS_RMB");
        inAccountDO.setBalance(amount * 7);
        inAccountDO.setUserId(from);
        //将钱从人民币账户扣除
        accountInfoMapper.decreaseAccount(inAccountDO);

        log.info("******** BankA Service begin commit...");
        return Boolean.TRUE;
    }

    @Transactional
    public boolean cancelMethod(String from, String to, double amount) {
        //恢复金额
        AccountDO outaccountDO = new AccountDO();
        outaccountDO.setAccountNo("ZS_MY");
        outaccountDO.setBalance(amount);
        outaccountDO.setUserId(from);

        AccountDO inAccountDO = new AccountDO();
        inAccountDO.setAccountNo("ZS_RMB");
        inAccountDO.setBalance(amount * 7);
        inAccountDO.setUserId(from);
        //恢复美元账户金额
        accountInfoMapper.increaseAccount(outaccountDO);
        //恢复人民币账户金额
        accountInfoMapper.decreaseAccount(inAccountDO);

        //删除冻结金额
        accountInfoMapper.deleteOutFrozen(from, "ZS_MY", amount);
        accountInfoMapper.deleteInFrozen(from, "ZS_RMB", amount * 7);
        log.info("******** BankA Service end rollback...  ");
        return Boolean.TRUE;
    }

}
