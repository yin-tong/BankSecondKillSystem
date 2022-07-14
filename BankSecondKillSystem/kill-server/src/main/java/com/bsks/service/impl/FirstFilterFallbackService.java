package com.bsks.service.impl;

import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.FirstFilterService;
import org.springframework.stereotype.Service;

@Service
public class FirstFilterFallbackService implements FirstFilterService {
    @Override
    public Result isRejected(String identityId, int age) {
        return new Result(ResultCode.FirstFilterServerError);
    }
}
