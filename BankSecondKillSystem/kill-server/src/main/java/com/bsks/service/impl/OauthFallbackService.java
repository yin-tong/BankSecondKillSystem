package com.bsks.service.impl;

import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.OauthService;
import org.springframework.stereotype.Service;

@Service
public class OauthFallbackService implements OauthService {

    @Override
    public Result createToken(String map) {
        return new Result(ResultCode.OauthServerError);
    }

    @Override
    public Result getValue(String token, String key) {
        return new Result(ResultCode.OauthServerError);
    }


    @Override
    public Result deleteToken(String token) {
        return new Result(ResultCode.OauthServerError);
    }
}
