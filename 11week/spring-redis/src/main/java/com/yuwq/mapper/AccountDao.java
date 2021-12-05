package com.yuwq.mapper;

import com.yuwq.entity.AccountDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AccountDao {
    @Select("select * from account_info")
    List<AccountDO> findAll();

    @Select("select * from account_info where id = #{id}")
    AccountDO findById(@Param("id") Long id);

    @Update("update account_info set frozen_balance = 10 where id = #{id}")
    int update(@Param("id") Long id);

    @Delete("delete from account_info where id = #{id}")
    int delete(@Param("id") Long id);
}
