package com.bsks.service.impl;


import com.bsks.result.Result;
import com.bsks.result.ResultCode;
import com.bsks.service.OauthService;
import org.springframework.stereotype.Service;

@Service
public class OauthFallbackService implements OauthService {
    @Override
    public Result verifyToken(String token) {
        return new Result(ResultCode.OauthServerError);
    }
}
