package com.yuwq.controller;

import com.yuwq.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank2")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @GetMapping("/hi")
    public String hello(){
        return "hi,this is bank2!";
    }

    @RequestMapping("/transfer")
    public Boolean test2(@RequestParam("amount") Double amount) {
        this.accountService.increaseAccountBalance("ls", amount);
        return true;
    }
}
