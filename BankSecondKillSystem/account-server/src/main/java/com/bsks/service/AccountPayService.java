package com.bsks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsks.api.entity.Account;
import com.bsks.api.result.Result;
import com.bsks.entity.AccountPay;

import java.math.BigDecimal;
import java.util.List;

public interface AccountPayService extends IService<AccountPay> {

    AccountPay findByAccountId(Long accountId);

    Result updateAccountPay(AccountPay accountPay);

    List<AccountPay> queryAll();

    Result pay(Long accountId, BigDecimal payMoney);

}
