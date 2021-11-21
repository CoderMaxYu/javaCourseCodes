package com.yuwq.controller;

import com.yuwq.service.BankAAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank1")
public class AccountController {
    @Autowired
    private BankAAccountService accountService;


    @RequestMapping("/transfer")
    public Boolean transferNest() {
        return accountService.decreaseBalance("zs","ls", 100);
    }


}
