package com.bsks.service.impl;

import com.bsks.api.entity.Account;
import com.bsks.api.result.ResultException;
import com.bsks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis中account对象的服务类
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 判断redis是否有该对象(确保redis中)
     * @param account 账户
     * @return true/false
     */
    @Override
    public boolean contains(Account account) {
        Account accountById = findAccountById(account.getId());
        if (accountById == null){
            return false;
        }
        return true;
    }

    /**
     * 往redis中存储账户
     * @param account 账户
     */
    @Override
    public void saveAccount(Account account) {
        // 在redis中默认存储20分钟
        redisTemplate.opsForValue().set("account:"+account.getId()+":"+account.getPhone(),account,20, TimeUnit.MINUTES);
        System.out.println("----redis存储账户:"+account);
    }

    /**
     * 根据账户id从redis中获取账户
     * @param id 账户id
     * @return 账户
     */
    @Override
    public Account findAccountById(long id) {
        Set<String> keys = redisTemplate.keys("account:" + id + ":*");
        if (keys == null || keys.size() == 0){
            return null;
        }
        List<Account> accountList = redisTemplate.opsForValue().multiGet(keys);
        System.out.println("----redis根据账户id获取账户:"+accountList.get(0));
        return accountList.get(0);
    }

    /**
     * 根据手机号码从redis中获取账户
     * @param phone 手机号码
     * @return 账户
     */
    @Override
    public Account findAccountByPhone(String phone) {
        if (phone == null || "".equals(phone)){
            throw new ResultException("手机号码不能为空");
        }
        Set<String> keys = redisTemplate.keys("account:*:" + phone);
        if (keys == null || keys.size() == 0){
            return null;
        }
        List<Account> accountList = redisTemplate.opsForValue().multiGet(keys);
        System.out.println("----redis根据手机号码获取账户:"+accountList.get(0));
        return accountList.get(0);
    }

    /**
     * 从redis中将账户删除
     * @param account
     */
    @Override
    public void deleteAccount(Account account) {
        redisTemplate.delete("account:"+account.getId()+":"+account.getPhone());
        System.out.println("----redis删除账户:"+account);
    }

    /**
     * 在redis中存储登录验证码
     * @param phoneNumber 手机号
     * @param code 验证码
     * @param codeTimeLength 有效时长（分钟）
     */
    @Override
    public void saveCode(String phoneNumber,String code,int codeTimeLength){
        redisTemplate.opsForValue().set("loginCode:"+phoneNumber,code,codeTimeLength, TimeUnit.MINUTES);
    }

    /**
     * 在redis中存储登录验证码
     * @param phoneNumber 手机号
     */
    @Override
    public boolean hasCode(String phoneNumber){
        return redisTemplate.hasKey("loginCode:"+phoneNumber);
    }

    /**
     * 在redis中存储登录验证码
     * @param phoneNumber 手机号
     */
    @Override
    public String getCode(String phoneNumber){
        return (String) redisTemplate.opsForValue().get("loginCode:"+phoneNumber);
    }

}
