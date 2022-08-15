package com.os.operatelog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpLog {

    /**tiêu đề*/
    String describe() default "";
    /**mô tả chi tiết*/
    String detail() default "";
    /**khóa chính*/
    String bizId();
    /**tên bảng*/
    String bizTable();
    /**loại query*/
    String opType();
}
