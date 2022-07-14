package com.bsks.service.impl;

import com.bsks.service.MessageService;
import com.bsks.utils.SendMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 短信服务类
 */
@Service
public class MessageServiceImpl implements MessageService {

    private String signName = "有枫来了个人公众号";

    private String loginTemplateId = "1347809";

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    // 验证码长度
    private int codeLength = 6;

    // 有效时长(分钟)
    private int codeTimeLength = 2;

    /**
     * 给特定手机号码发送登录验证码
     * @param phoneNumber 手机号码
     */
    @Override
    public boolean sendLoginCode(String phoneNumber){
        //获取验证码
        String code = SendMessageUtil.createCode(codeLength);
        //获取手机号码
        String[] phoneNumberSet = {phoneNumber};
        String[] templateParams ={ code, String.valueOf(codeTimeLength)};
        boolean isSend = SendMessageUtil.sendMessage(phoneNumberSet,loginTemplateId,templateParams,signName);
        if (isSend){
            // 存入redis，设置有效时长
            redisTemplate.opsForValue().set("loginCode:"+phoneNumber,code,codeLength, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }
}
