package com.yuwq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
@MapperScan("com.yuwq.mapper")
public class Bank2Application {

    public static void main(String[] args) {
        SpringApplication.run(Bank2Application.class, args);
    }
    @GetMapping("/hi")
    public String hi(){
        return "i'm bank2!";
    }
}
