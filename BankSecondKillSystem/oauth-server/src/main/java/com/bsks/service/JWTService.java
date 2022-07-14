package com.bsks.service;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bsks.component.SnowFlakeId;
import com.bsks.result.Result;
import com.bsks.result.ResultCode;
import com.bsks.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JWTService {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlakeId snowFlakeId;

    /**
     * 创建token
     * @return Result
     */
    public String createToken(Map<String,String> map) {
        // 雪花算法生成jwt id
        String jti = String.valueOf(snowFlakeId.getNextId());
        map.put("jti",jti);
        String token = JWTUtils.createTokenSM2(map);
        // 将token存入redis,有效时长20分钟
        redisTemplate.opsForValue().set("token:"+jti,token,20,TimeUnit.MINUTES);
        System.out.println("----redis存储token:"+token);
        return token;
    }

    /**
     * 判断 token 是否有效
     * @param token
     * @return
     */
    public boolean verifyToken(String token) throws IOException {
        //1. 验证为非法token，返回false
        if (!JWTUtils.verifyTokenSM2(token)){
            System.out.println("------非法token");
            return false;
        }
        //2. 解码,获取解析后的token中的payload信息
        DecodedJWT decodedJWT =  JWTUtils.decodeSM2(token);
        //3. 获取jti
        String jti = decodedJWT.getClaim("jti").asString();
        //2. redis中不存在，token过时，返回false
        if (!redisTemplate.hasKey("token:"+jti)){
            System.out.println("------过期token");
            return false;
        }
        return true;
    }

    /**
     * 从token主体中根据key获取value
     * @param token
     * @return
     */
    public Result getValue(String token, String key) throws IOException {
        //1. token无效，返回false
        if (!verifyToken(token)){
            return new Result(ResultCode.WrongToken);
        }
        //2. 解码,获取解析后的token中的payload信息
        DecodedJWT decodedJWT =  JWTUtils.decodeSM2(token);
        //3. 获取value
        Claim value = decodedJWT.getClaim(key);
        if (value.asString() == null){
            return new Result(ResultCode.KeyError);
        }
        // 4. 将redis中token有效时长增加二十分钟
        Claim jti = decodedJWT.getClaim("jti");
        redisTemplate.expire("token:"+jti.asString(),20,TimeUnit.MINUTES);
        return new Result("获取value成功",value.asString());
    }

    /**
     * 删除token
     * @param token
     * @return
     */
    public Result deleteToken(String token) throws IOException {
        //1. token无效，不做操作
        if (!verifyToken(token)){
            return new Result(ResultCode.SessionObsolete);
        }
        //2. 解码,获取解析后的token中的payload信息
        DecodedJWT decodedJWT =  JWTUtils.decodeSM2(token);
        //3. 获取token id: jti
        String jti = decodedJWT.getClaim("jti").asString();
        // 5. 从redis中删除token
        if (redisTemplate.hasKey("token:"+jti)){
            redisTemplate.delete("token:"+jti);
            return new Result("删除成功");
        } else {
            return new Result(ResultCode.TokenOverdue);
        }
    }
}
