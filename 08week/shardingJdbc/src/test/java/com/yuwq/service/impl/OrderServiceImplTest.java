package com.yuwq.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yuwq.po.Order;
import com.yuwq.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void saveOrder(){
        Order order = new Order(null,1459751565705256961L,100L,1,new Date(), new Date());
        boolean success = orderService.save(order);
        System.out.println(success);
    }

    @Test
    public void updateOrder(){
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper();
        updateWrapper.eq("order_id",1459755096059355138L);

        Order order = new Order();
        order.setStatus(0);
        boolean success = orderService.update(order,updateWrapper);
        System.out.println(success);
    }

    @Test
    public void get(){
        //查询条件只有order的主键，走两个库，然后根据分片策略查order_2表
        Order order = orderService.getById(1459755096059355138L);
        System.out.println(order);

        //查询条件有user_id,和id，先根据user_id选择db1，再根据id选择order_2表
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",1459755096059355138L);
        queryWrapper.eq("user_id",1459751565705256961L);
        Order order2 = orderService.getOne(queryWrapper);
        System.out.println(order2);
    }

    @Test
    public void deleteOrder(){
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        boolean success = orderService.remove(queryWrapper);
        System.out.println(success);
    }

}
