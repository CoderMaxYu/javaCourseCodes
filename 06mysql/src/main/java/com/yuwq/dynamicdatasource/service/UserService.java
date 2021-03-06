package com.yuwq.dynamicdatasource.service;

import com.yuwq.dynamicdatasource.annotation.DataSourceSelector;
import com.yuwq.dynamicdatasource.enums.DbConnectionTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DataSourceSelector(value = DbConnectionTypeEnum.MASTER)
    public List<Map<String, Object>> queryForMaster(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from c_user");
        return maps;
    }

    @DataSourceSelector(value = DbConnectionTypeEnum.SLAVE)
    public List<Map<String, Object>> queryForSlave(){
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from c_user");
        return maps;
    }
}