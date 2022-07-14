package com.bsks.service;

import com.bsks.api.result.Result;
import com.bsks.service.impl.AccountFallBackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Service
@FeignClient(value = "account-server",fallback = AccountFallBackService.class)
public interface AccountService {

    /**
     * 支付
     * @param outAccountPhone 付钱账户的手机号码
     * @param inAccountPhone 收钱账户的手机号码
     * @param payMoney 支付金额
     */
    @GetMapping("/service/pay")
    Result pay(@RequestParam("outAccountPhone")String outAccountPhone, @RequestParam("inAccountPhone") String inAccountPhone, @RequestParam("payMoney") BigDecimal payMoney);

    @GetMapping("/service/accountPay")
    Result accountPay(@RequestParam("accountId")long accountId,@RequestParam("payMoney")BigDecimal payMoney);
}
