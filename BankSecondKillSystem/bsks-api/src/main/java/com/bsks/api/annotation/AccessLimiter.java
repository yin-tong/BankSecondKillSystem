package com.bsks.api.annotation;

import java.lang.annotation.*;

/**
 * 接口防刷: 一个用户key在limitTime时间内，最多访问limit次
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimiter {

    // 认证服务的类
    Class<?> oauthService() default void.class;

    // 认证服务获取value的方法名
    String getValue();

    // 限制的次数
    int limitCount() default  1;

    // 限制时间，单位 s
    int limitTime() default  1;
}
