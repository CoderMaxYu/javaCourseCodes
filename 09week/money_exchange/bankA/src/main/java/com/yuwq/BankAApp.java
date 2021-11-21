package com.yuwq;

import com.yuwq.service.DemoService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ImportResource({"classpath:spring-dubbo.xml"})
@MapperScan({"com.yuwq.mapper"})
@SpringBootApplication
@RestController
public class BankAApp {
    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(BankAApp.class, args);
    }

    @Autowired
    private DemoService demoService;

    @RequestMapping("/hi")
    public void hi() {
        System.out.println(demoService.say("hello,client"));
    }
}