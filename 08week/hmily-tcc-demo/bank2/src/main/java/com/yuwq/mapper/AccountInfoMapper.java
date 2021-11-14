package com.yuwq.mapper;

import com.yuwq.po.AccountInfo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AccountInfoMapper {
    @Update("update account_info set  frozen_balance = #{frozenBalance} " +
            "where  account_name = #{accountName}")
    int increaseAccountBalance(String accountName,Double frozenBalance);

    @Update("update account_info set account_balance = account_balance + frozen_balance , frozen_balance = 0 " +
            "where frozen_balance > 0")
    int confirmAccountBalance();

    @Update("update account_info set  frozen_balance = 0 " +
            "where  frozen_balance >0 and account_name = #{accountName}")
    int cancelAccountBalance(String accountName);

    @Select("select id as 'id',account_name as 'accountName',account_balance as 'accountBalance', frozen_balance as 'frozenBalance'" +
            " from account_info where account_name = #{accountName}")
    AccountInfo selectByName(String accountName);
}
