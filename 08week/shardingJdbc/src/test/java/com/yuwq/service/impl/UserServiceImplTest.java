package com.yuwq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yuwq.po.User;
import com.yuwq.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void saveUser() {
        User user = new User(null, "xsj", 1, new Date(),  new Date());
        boolean success = userService.save(user);
        System.out.println(success);
    }

    @Test
    public void deleteUser() {
        boolean success = userService.remove(new QueryWrapper<User>());
        System.out.println(success);
    }

    @Test
    public void updateUser(){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("user_id",1459751565705256961L);

        User user = new User();
        user.setUserName("yuwq");
        boolean success = userService.update(user,updateWrapper);
        System.out.println(success);
    }

    @Test
    public void saveBatchUser() {
        List<User> users = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            User user = new User(null, "yuwq", 1, new Date(), new Date());
            users.add(user);
        }
        boolean success = userService.saveBatch(users);
        System.out.println(success);
    }
}
