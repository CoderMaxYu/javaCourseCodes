package com.yuwq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuwq.mapper.OrderMapper;
import com.yuwq.po.Order;
import com.yuwq.service.OrderService;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
