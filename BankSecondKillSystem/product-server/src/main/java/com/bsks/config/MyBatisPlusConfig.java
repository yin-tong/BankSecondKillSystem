package com.bsks.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("com.bsks.mapper")
@EnableTransactionManagement
@Configuration
public class MyBatisPlusConfig {

    //乐观锁
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor ();
    }

    //分页
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return  new PaginationInterceptor ();
    }


}
