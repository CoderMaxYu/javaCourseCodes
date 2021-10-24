package com.yuwq.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addPerson() {
        String sql ="insert into Person(name,birthday) values(?,?)";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {// 匿名内部类
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, "test1");
                preparedStatement.setDate(2, new Date(new java.util.Date().getTime()));
            }
        });
    }
}
