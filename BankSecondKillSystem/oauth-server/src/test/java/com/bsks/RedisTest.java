package com.bsks;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置hash过时
     * @throws InterruptedException
     */
    @Test
    public void contextLoads2() throws InterruptedException {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("1x","1","1");
        hashOperations.put("1x","2","2");
        Thread.sleep(5000);
        hashOperations.delete("1x","1");
        System.out.println(hashOperations.get("1x", "1"));
    }
}
