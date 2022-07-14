package com.bsks.controller;

import com.bsks.api.result.Result;
import com.bsks.service.AccountPayService;
import com.bsks.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


/**
 * 微服务内部调用接口
 */
@Api(value = "/service",tags = "微服务内部调用接口")
@RestController
public class ServiceController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountPayService accountPayService;


    @ApiOperation(value = "根据账户id查询账户")
    @ApiImplicitParam(name = "accountId",value = "账户id", paramType = "query")
    @GetMapping("/service/findById")
    public Result findById(@RequestParam("accountId") Long accountId){
        return new Result("查询成功",accountService.findById(accountId));
    }


    @ApiOperation(value = "扣款接口(账户id，转账金额)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId",value = "账户id", paramType = "query"),
            @ApiImplicitParam(name = "payMoney",value = "支付金额", paramType = "query")
    })
    @GetMapping("/service/accountPay")
    public Result accountPay(@RequestParam("accountId")long accountId,@RequestParam("payMoney")BigDecimal payMoney) {
        return accountPayService.pay(accountId,payMoney);
    }

}
