package com.yuwq.spring;

import com.yuwq.spring.common.Klass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext =  SpringApplication.run(Application.class, args);
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        System.out.println("datasource is :" + dataSource);
        //检查数据库是否是hikar数据库连接池
//        if (!(dataSource instanceof HikariDataSource)) {
//            System.err.println(" Wrong datasource type :"
//                    + dataSource.getClass().getCanonicalName());
//            System.exit(-1);
//        }
        Klass klass = applicationContext.getBean(Klass.class);
        System.out.println("klass is :" + klass);
    }

}
