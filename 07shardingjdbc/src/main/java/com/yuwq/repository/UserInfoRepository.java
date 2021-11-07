package com.yuwq.repository;

import com.yuwq.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 用户表操作接口
 */
public interface UserInfoRepository extends JpaRepository<UserInfo,Integer>{

}
