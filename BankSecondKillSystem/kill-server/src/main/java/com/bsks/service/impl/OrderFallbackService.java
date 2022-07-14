package com.bsks.service.impl;

import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.OrderService;
import org.springframework.stereotype.Service;
@Service
public class OrderFallbackService implements OrderService {
    @Override
    public Result findNumbers(String phone, String productName) {
        return new Result(ResultCode. OrderServerError);
    }
}
