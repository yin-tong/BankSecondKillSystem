package com.bsks;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bsks.algorithm.SM2Algorithm;
import com.bsks.service.JWTService;
import com.bsks.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JWTTest {

    //使用HMAC256算法创建token
    @Test
    public void contextLoads() {
        // 1. 创建
        // 指定token过期时间为10秒
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);

        String token = JWT.create()
                .withHeader(new HashMap<>())  // Header
                .withClaim("user_name", 21)  // Payload
                .withClaim("userName", "baobao")
                .withExpiresAt(calendar.getTime())  // 过期时间
                .sign(Algorithm.HMAC256("!34ADAS"));  // 签名用的secret

        System.out.println(token);

        //2.验签

        // 3. 解析
        // 创建解析对象，使用的算法和secret要与创建token时保持一致
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!34ADAS")).build();
        // 解析指定的token
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        // 获取解析后的token中的payload信息
        Claim userName = decodedJWT.getClaim("userName");
        System.out.println(userName.asString());
    }

    //使用自定义SM2算法创建,解析token
    @Test
    public void contextLoads2() {

        String token = JWT.create()
                .withHeader(new HashMap<>())  // Header
                .withClaim("age", 21)  // Payload
                .withClaim("userName", "baobao2")
                .sign(new SM2Algorithm("SM2","sm2"));  // 签名用的secret

        System.out.println(token);
        // 创建解析对象，使用的算法和secret要与创建token时保持一致
        JWTVerifier jwtVerifier = JWT.require(new SM2Algorithm("SM2","sm2")).build();
        // 解析指定的token
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        // 获取解析后的token中的payload信息
        Claim userName = decodedJWT.getClaim("userName");
        System.out.println(userName.asString());
    }

    //获取token
    @Test
    public void contextLoads3() {
        Map map = new HashMap<String,String>();
        map.put("useName","12");
        String token = JWTUtils.createTokenSM2(map);
        System.out.println(token);
    }

    /**
     * 测试解析token
     */
    @Test
    public void contextLoads4() {

        String errorToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJTTTIifQ.eyJhY2NvdW50SWQiOiIxIiwicm9sZU5hbWUiOiJBRE1JTiIsInVzZXJOYW1lIjoiYWRtaW4ifQ.MEUCIHznzGmfV5EZ-7IB6AcV8YJ3UWE-cZfSwWbUCiFjFMgIAiEA9Gq88h3PbTaquj0IixGBzEO8yPrYNz8BeKi_Y2V0Mn";

        // 创建解析对象，使用的算法和secret要与创建token时保持一致
        JWTVerifier jwtVerifier = JWT.require(new SM2Algorithm("SM2","sm2")).build();
        // 解析指定的token
        DecodedJWT decodedJWT = jwtVerifier.verify(errorToken);
        // 获取解析后的token中的payload信息
        Claim userName = decodedJWT.getClaim("userName");
        System.out.println(userName.asString());
    }

    @Autowired
    private JWTService jwtService;

    @Test
    public void test() throws IOException {
        Map<String,String> map = new HashMap<>();
        map.put("1","1");
        String tokenSM2 = jwtService.createToken(map);
        System.out.println(tokenSM2);

        System.out.println(jwtService.getValue(tokenSM2, "1"));

        System.out.println(jwtService.getValue(tokenSM2, "8"));

    }
}
