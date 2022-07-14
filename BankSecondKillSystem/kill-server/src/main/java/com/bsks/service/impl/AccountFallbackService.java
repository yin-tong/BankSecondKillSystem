package com.bsks.service.impl;


import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountFallbackService implements AccountService {

    /**
     * 根据账户id查询账户兜底方法
     * @param accountId 账户id
     * @return 账户
     */
    @Override
    public Result findById(Long accountId) {
        return new Result(ResultCode.AccountServerError);
    }
}
