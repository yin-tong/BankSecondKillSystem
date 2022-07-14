package com.bsks.service;

import com.bsks.api.result.Result;
import com.bsks.service.impl.OauthFallbackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Service
@FeignClient(value = "oauth-server",fallback = OauthFallbackService.class)
public interface OauthService {

    /**
     * 生成token
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("/createToken")
    Result createToken(@RequestParam("map") String map);

    /**
     * 从token主体中根据key获取value
     * @param token
     * @return
     */
    @PostMapping("/getValue")
    Result getValue(@RequestParam("token")String token,@RequestParam("key")String key);

    /**
     * 客户端退出，删除token
     * @param token
     * @return
     */
    @PostMapping("/deleteToken")
    Result deleteToken(@RequestParam("token")String token);
}
