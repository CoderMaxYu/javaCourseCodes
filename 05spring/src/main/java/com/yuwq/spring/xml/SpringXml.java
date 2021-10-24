package com.yuwq.spring.xml;

import com.yuwq.spring.common.Student;
import com.yuwq.spring.common.TestA;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringXml {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        Student student = (Student) context.getBean("test");
        student.setName("world");
        student.setAge(99);
        student.print();


        TestA testA = (TestA) context.getBean("testA");
        testA.print();

        Student stuDefi = (Student) context.getBean("stuDefi");
        if (stuDefi != null) {
            System.out.println(stuDefi);
        }
    }
}
