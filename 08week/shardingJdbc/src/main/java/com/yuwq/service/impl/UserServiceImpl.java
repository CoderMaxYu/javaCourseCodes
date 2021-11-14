package com.yuwq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuwq.mapper.UserMapper;
import com.yuwq.po.User;
import com.yuwq.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
