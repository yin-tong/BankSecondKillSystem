package com.bsks.controller;

import com.alibaba.fastjson.JSON;
import com.bsks.result.Result;
import com.bsks.service.JWTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(value = "/",tags = "jwt功能接口")
@RestController
public class JWTController {

    @Autowired
    private JWTService jwtService;

    /**
     * 生成token
     * @param map
     * @return
     * @throws Exception
     */
    @ApiOperation("创建token")
    @PostMapping("/createToken")
    public Result createToken(@RequestParam("map") String map) throws Exception {
        Map<String,String> payloadMap = (Map<String, String>) JSON.parse(map);
        String token = jwtService.createToken(payloadMap);
        return new Result("创建token成功",token);
    }

    /**
     * 判断 token 是否正确
     * @param token
     * @return
     */
    @ApiOperation("判断 token 是否正确")
    @PostMapping("/verifyToken")
    public Result verifyToken(@RequestParam("token")String token) throws IOException {
        System.out.println("验证的token:"+ token);
        boolean b = jwtService.verifyToken(token);
        return new Result("判断成功",b);
    }

    /**
     * 从token主体中根据key获取value
     * @param token
     * @return
     */
    @ApiOperation("从token主体中根据key获取value")
    @ApiImplicitParam(name = "token",value = "令牌", paramType = "body")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token",value = "令牌", paramType = "body"),
            @ApiImplicitParam(name = "key",value = "key", paramType = "query")
    })
    @PostMapping("/getValue")
    public Result getValue(@RequestParam("token")String token,@RequestParam("key")String key) throws IOException {
        System.out.println("根据key获取value的token:"+token+"key: "+key);
        return jwtService.getValue(token,key);
    }

    /**
     * 客户端退出，删除token
     * @param token
     * @return
     */
    @ApiOperation("客户端退出，删除token")
    @PostMapping("/deleteToken")
    public Result deleteToken(@RequestParam("token")String token) throws IOException {
        System.out.println("退出，准备删除token："+token);
        return jwtService.deleteToken(token);
    }
}
