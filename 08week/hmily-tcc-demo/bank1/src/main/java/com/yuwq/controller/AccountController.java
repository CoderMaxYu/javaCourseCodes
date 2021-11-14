package com.yuwq.controller;

import com.yuwq.po.AccountInfo;
import com.yuwq.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank1")
public class AccountController {
    @Autowired
    private AccountService accountService;


    @RequestMapping("/transfer")
    public Boolean transferNest(@RequestParam("name")String name, @RequestParam("amout")Double amout) {
        return accountService.decreaseBalance(name, amout);
    }

    @GetMapping("/get")
    public AccountInfo get(@RequestParam("name") String name){
        return accountService.selectByName(name);
    }


}
