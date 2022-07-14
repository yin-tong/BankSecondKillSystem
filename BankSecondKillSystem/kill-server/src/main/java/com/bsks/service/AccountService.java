package com.bsks.service;


import com.bsks.api.result.Result;
import com.bsks.service.impl.AccountFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "account-server",fallback = AccountFallbackService.class)
public interface AccountService {

    /**
     * 根据账户id查询账户
     * @param accountId 账户id
     * @return 账户
     */
    @GetMapping("/service/findById")
    Result findById(@RequestParam("accountId") Long accountId);

}
