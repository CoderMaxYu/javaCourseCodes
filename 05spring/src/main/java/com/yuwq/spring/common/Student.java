package com.yuwq.spring.common;


import lombok.Data;

@Data
public class Student {

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Student() {

    }

    private String name;

    private Integer age;

    public  void print(){
        System.out.println("该学生姓名为:"+this.name+",年龄为:"+this.age);
    }
}
