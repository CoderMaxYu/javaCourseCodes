package com.yuwq;

import com.yuwq.feign.Bank2Client;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@RestController
@MapperScan("com.yuwq.mapper")
public class Bank1Application {
    @Autowired
    private Bank2Client bank2Client;
    public static void main(String[] args) {
        SpringApplication.run(Bank1Application.class, args);
    }
    @GetMapping("/hi")
    public String hi(){
        System.out.println("我是bank1！");
        String result = bank2Client.hello();
        return result;
    }
}
