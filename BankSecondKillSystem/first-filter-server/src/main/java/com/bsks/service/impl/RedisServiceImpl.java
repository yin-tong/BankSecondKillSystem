package com.bsks.service.impl;

import com.bsks.entity.FirstFilterRule;
import com.bsks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 判断redis中是否有初筛规则
     * @return
     */
    @Override
    public boolean hasFirstFilter(){
        return redisTemplate.hasKey("firstFilterRule");
    }

    /**
     * 从redis中获取初筛规则
     * @return
     */
    @Override
    public FirstFilterRule getFirstFilterRule(){
        FirstFilterRule firstFilterRule = (FirstFilterRule) redisTemplate.opsForValue().get("firstFilterRule");
        System.out.println("----redis获取初筛规则"+firstFilterRule);
        return firstFilterRule;
    }

    /**
     * redis中存储初筛规则
     * @return
     */
    @Override
    public void saveUpdateFirstFilterRule(FirstFilterRule firstFilterRule){
        System.out.println("----redis更新或创建初筛规则"+firstFilterRule);
        redisTemplate.opsForValue().set("firstFilterRule",firstFilterRule);
    }
}
