package com.yuwq.spring.controller;

import com.yuwq.spring.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private PersonService personService;

    @RequestMapping("test")
    public String  test(){
        personService.addPerson();
        return  "success";
    }
}
