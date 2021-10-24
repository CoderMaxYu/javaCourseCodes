package com.yuwq.spring.common;


import org.springframework.stereotype.Component;

/**
 * component方式注册Bean
 */

@Component
public class TestA {

        public void print(){
            System.out.println("this is a  component test");
        }
}
