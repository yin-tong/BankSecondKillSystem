package com.bsks.task;

import com.bsks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务同步库存
 */
@Component
@EnableScheduling
public class SynchroQuantityTask {

    @Autowired
    private RedisService redisService;

    /**
     * 上一次开始执行时间点之后30秒再执行
     */
    @Scheduled(fixedRate = 30000)
    public void run(){
        try {
            redisService.synchroQuantity();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("redis同步缓存失败");
        }
    }
}
