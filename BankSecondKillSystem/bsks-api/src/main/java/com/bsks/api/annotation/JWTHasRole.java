package com.bsks.api.annotation;


import java.lang.annotation.*;

/**
 * 接口鉴权：获取token中的角色名判断是否等于roleName
 */
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JWTHasRole {

    // 认证服务的类
    Class<?> oauthService() default void.class;

    // 认证服务获取value的方法名
    String getValue();

    // 角色名
    String roleName();
}
