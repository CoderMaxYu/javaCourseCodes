package com.yuwq.service.impl;

import com.yuwq.service.DemoService;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoServiceImpl implements DemoService {

    public String say(String msg) {
        return "服务提供："+msg;
    }
}
