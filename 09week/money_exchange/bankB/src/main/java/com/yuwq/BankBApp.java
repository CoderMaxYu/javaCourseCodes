package com.yuwq;

import com.yuwq.service.DemoService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@ImportResource({"classpath:spring-dubbo.xml"})
@SpringBootApplication
@MapperScan({"com.yuwq.mapper"})
@RestController
public class BankBApp {

    @Autowired
    private DemoService demoService;

    public static void main(String[] args) {

        SpringApplication.run(BankBApp.class, args);
    }

    @RequestMapping("/hi")
    public void hi() {
        System.out.println(demoService.say("hello"));
    }
}