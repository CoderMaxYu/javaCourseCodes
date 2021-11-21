package com.yuwq.mapper;

import com.yuwq.entity.AccountDO;
import com.yuwq.entity.FrozenDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface AccountInfoMapper {

    @Insert("insert into frozen (user_id,in_account_no,out_account_no,balance,create_time,update_time)\n" +
            "values (#{userId},#{inAccountNo},#{outAccountNo},#{balance},now(),now())")
    int insertFrozen(FrozenDO frozenDO);

    @Delete("delete from frozen where user_id = #{userId} and in_account_no = #{inAccountNo} and balance = #{balance}")
    int deleteInFrozen(@Param("userId") String userId, @Param("inAccountNo") String inAccountNo, @Param("balance") double balance);

    @Delete("delete from frozen where user_id = #{userId} and out_account_no = #{outAccountNo} and balance = #{balance}")
    int deleteOutFrozen(@Param("userId") String userId, @Param("outAccountNo") String outAccountNo, @Param("balance") double balance);


    @Update("update account set balance = balance + #{balance} " +
            "where account_no = #{accountNo} and user_id = #{userId}")
    int updateAccount(AccountDO accountDO);

    @Update("update account set balance = balance - #{balance} " +
            "where account_no = #{accountNo} and user_id = #{userId} and balance > #{balance}")
    int decreaseAccount(AccountDO accountDO);

    @Update("update account set balance = balance + #{balance} " +
            "where account_no = #{accountNo} and user_id = #{userId}")
    int increaseAccount(AccountDO accountDO);


}
