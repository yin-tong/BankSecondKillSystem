package com.bsks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsks.api.entity.Account;
import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.entity.AccountPay;
import com.bsks.mapper.AccountMapper;
import com.bsks.mapper.AccountPayMapper;
import com.bsks.service.AccountPayService;
import com.bsks.service.AccountService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountPayServiceImpl extends ServiceImpl<AccountPayMapper, AccountPay> implements AccountPayService {

    @Autowired
    private AccountPayMapper accountPayMapper;

    @Autowired
    private AccountService accountService;
    /**
     * 根据id查询支付账户
     * @param accountId
     * @return
     */
    @Override
    public AccountPay findByAccountId(Long accountId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id",accountId);
        AccountPay accountPay = accountPayMapper.selectOne (queryWrapper);
        return accountPay;
    }

    @Transactional
    @Override
    public Result updateAccountPay(AccountPay accountPay){
        int flag = accountPayMapper.updateById(accountPay);
        if (flag == 0){
            return new Result(ResultCode.AccountUpdateError);
        }
        return new Result("更新账户成功");
    }

    @Override
    public List<AccountPay> queryAll(){
        QueryWrapper<AccountPay> wrapper = new QueryWrapper<AccountPay> (  );
        wrapper.isNotNull ("pay_money");
        List<AccountPay> accountPays = accountPayMapper.selectList (wrapper);
        return accountPays;
    };

    /**
     * 根据账户id扣除钱，并且更新扣款记录
     * @param accountId
     * @param payMoney
     * @return
     */
    @Transactional
    @Override
    public Result pay(Long accountId, BigDecimal payMoney){
        //1.判断转钱账户非空
        Account outAccount = accountService.findById(accountId);
        if (outAccount == null){
            return new Result(ResultCode.PayAccountNull);
        }
        //3. 余额<支付金额，返回错误信息
        if (outAccount.getMoney().compareTo(payMoney) == -1){
            return new Result(ResultCode.MoneyNotEnough);
        }
        //4. 扣钱
        outAccount.setMoney(outAccount.getMoney().subtract(payMoney));
        accountService.updateAccount(outAccount);
        //5. 更新扣款记录
        AccountPay accountPay = findByAccountId(accountId);
        if (accountPay == null){
            accountPay = new AccountPay();
            accountPay.setAccountId(accountId);
            accountPay.setName(outAccount.getName());
            accountPay.setPayMoney(payMoney);
            accountPay.setSynchronousMoney(BigDecimal.valueOf(0));
            accountPayMapper.insert(accountPay);
            return new Result("扣款成功");
        }
        accountPay.setPayMoney(accountPay.getPayMoney().add(payMoney));
        updateAccountPay(accountPay);
        return new Result("扣款成功");
    }

}
