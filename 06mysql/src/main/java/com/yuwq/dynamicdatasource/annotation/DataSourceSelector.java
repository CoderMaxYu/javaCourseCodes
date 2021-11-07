package com.yuwq.dynamicdatasource.annotation;

import com.yuwq.dynamicdatasource.enums.DbConnectionTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSourceSelector {

    /**
     * 数据源类型
     * @return MASTER or SLAVE
     */
    DbConnectionTypeEnum value() default DbConnectionTypeEnum.MASTER;
}