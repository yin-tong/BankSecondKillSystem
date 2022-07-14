package com.bsks.service.impl;


import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Service
public class AccountFallBackService implements AccountService {
    @Override
    public Result pay(String outAccountPhone, String inAccountPhone, BigDecimal payMoney) {
        return new Result(ResultCode.AccountServerError);
    }

    @Override
    public Result accountPay(long accountId, BigDecimal payMoney) {
        return new Result ( ResultCode.AccountServerError );
    }


}
