package com.yuwq.spring.config;


import com.yuwq.spring.common.Klass;
import com.yuwq.spring.common.School;
import com.yuwq.spring.common.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class StuKlassSchoolAutoConfigure {

    List<Student> students;

    @Bean
    public Student getStudent(){
        return new Student("test",22);
    }

    @Bean
    public Klass getKlass(){
        Klass klass = new Klass();
        students = new ArrayList<>();
        students.add(getStudent());
        klass.setStudents(students);
        return klass;
    }

}
