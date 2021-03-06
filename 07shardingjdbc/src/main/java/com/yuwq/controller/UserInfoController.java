package com.yuwq.controller;

import com.yuwq.model.UserInfo;
import com.yuwq.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户信息控制器
 */
@RestController
public class UserInfoController {
	@Autowired
	UserInfoRepository userInfoRepository;
	
	/**
	 * 获取所有用户信息
	 * @return
	 */
	@GetMapping("/userinfo")
	public List<UserInfo> getUserInfos(){
		return userInfoRepository.findAll();
	}
	
	/**
	 * 增加新用户
	 * @param name
	 * @return
	 */
	@GetMapping("/userinfo/{name}")
	public UserInfo addUserInfo(@PathVariable String name){
		UserInfo userInfo = new UserInfo();
		userInfo.setName(name);
		return userInfoRepository.save(userInfo);
	}
	
	/**
	 * 增加新用户后再立即查找该用户信息
	 * @param name
	 * @return
	 */
	@GetMapping("/userinfo/wr/{name}")
	public UserInfo writeAndRead(@PathVariable String name) {
		UserInfo userInfo = new UserInfo();
		userInfo.setName(name);
		userInfoRepository.saveAndFlush(userInfo);
		return userInfoRepository.findById(userInfo.getId()).orElse(null);
	}
}
