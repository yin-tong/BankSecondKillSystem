package com.bsks.service;

import com.bsks.result.Result;
import com.bsks.service.impl.OauthFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "oauth-server",fallback = OauthFallbackService.class)
public interface OauthService {

    /**
     * 判断 token 是否有效
     * @param token
     * @return
     */
    @PostMapping("/verifyToken")
    Result verifyToken(@RequestParam("token")String token);
}
