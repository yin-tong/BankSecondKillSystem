package com.bsks.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bsks.algorithm.SM2Algorithm;
import com.bsks.result.ResultCode;
import com.bsks.result.ResultException;

import java.io.*;
import java.util.Map;

public class JWTUtils {

    /**
     * 生成token
     * @param payload token携带的信息
     * @return token字符串
     */
    public static String createTokenSM2(Map<String,String> payload){
        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payload.forEach((k,v) -> builder.withClaim(k,v));
        // 签名时传入私钥
        String token = builder.sign(new SM2Algorithm("SM2","sm2"));
        return token;
    }

    /**
     * 解码
     * @param token
     * @return
     * @throws IOException
     */
    public static DecodedJWT decodeSM2(String token) throws IOException {
        // 创建解析对象，使用的算法和secret要与创建token时保持一致
        JWTVerifier jwtVerifier = JWT.require(new SM2Algorithm("SM2","sm2")).build();
        // 解析指定的token
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }

    /**
     * 验证toke是否合法
     * @param token
     * @return
     * @throws IOException
     */
    public static boolean verifyTokenSM2(String token) throws IOException {
        //1. token为空返回false
        if (token == null || "".equals(token)){
            return false;
        }
        //2. token不合法，返回false
        try {
            decodeSM2(token);
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
        return true;
    }

}
