package com.yuwq.feign;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class Bank2ClientFallback implements Bank2Client {

    @Override
    public Boolean transfer(@RequestParam("amount") Double amount) {

        return false;
    }

    @Override
    public String hello() {
        return "bank2 宕机了！";
    }
}