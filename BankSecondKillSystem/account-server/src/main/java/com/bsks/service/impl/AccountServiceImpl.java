package com.bsks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsks.api.entity.Account;
import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.mapper.AccountMapper;
import com.bsks.service.AccountService;
import com.bsks.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 账户信息表 服务实现类
 * </p>
 *
 * @author Li
 * @since 2022-02-10
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 根据手机号码查询账户
     * @param phone 手机号码
     * @return 账户
     */
    @Override
    public Account findByPhone(String phone) {
        Account accountByPhone = redisService.findAccountByPhone(phone);
        if (accountByPhone == null){
            QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone",phone);
            accountByPhone = accountMapper.selectOne(queryWrapper);
            if (accountByPhone != null){
                redisService.saveAccount(accountByPhone);
            }
        }
        return accountByPhone;
    }


    /**
     * 注册账户(普通用户)
     * @param account 账户
     */
    @Override
    public Result register(Account account){
        System.out.println("-----"+account.getPhone());
        //根据手机号查询账户
        Account byPhone = findByPhone(account.getPhone());
        //数据库中有账户绑定了该手机号，返回异常信息
        if(byPhone != null){
            return new Result(ResultCode.PhoneRegistered);
        }
        //插入数据库
        accountMapper.insert(account);
        return new Result("注册成功");
    }

    /**
     * 修改账户信息
     * @param account
     */
    @Transactional
    @Override
    public Result updateAccount(Account account){
        int flag = accountMapper.updateById(account);
        if (flag == 0){
            return new Result(ResultCode.AccountUpdateError);
        }
        if (redisService.contains(account)){
            redisService.deleteAccount(account);
        }
        return new Result("更新账户成功");
    }

    /**
     * 根据姓名模糊查询账户数量
     * @param name 姓名
     */
    @Override
    public int fuzzyQueryByNameCount(String name) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        return accountMapper.selectCount(queryWrapper);
    }

    /**
     * 根据姓名模糊查询账户
     * @param name 姓名
     * @param page 页号
     * @param size 一页的数量
     */
    @Override
    public List<Account> fuzzyQueryByName(String name, long page, long size){
        Page<Account> accountPage = new Page(page,size);
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        accountMapper.selectPage(accountPage,queryWrapper);
        return accountPage.getRecords();
    }

    /**
     * 根据身份证号模糊查询账户数量
     * @return
     */
    @Override
    public int fuzzyQueryByIdentityIdCount(String identityId) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("identity_id",identityId);
        return accountMapper.selectCount(queryWrapper);
    }

    /**
     * 根据身份证号模糊查询账户
     * @param identityId 身份证号码
     */
    @Override
    public List<Account> fuzzyQueryByIdentityId(String identityId,long page, long size){
        Page<Account> accountPage = new Page<>(page,size);
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("identity_id",identityId);
        accountMapper.selectPage(accountPage,queryWrapper);
        return accountPage.getRecords();
    }

    /**
     * 根据id查询账户
     * @param id
     * @return
     */
    @Override
    public Account findById(Long id){
        Account accountById = redisService.findAccountById(id);
        if (accountById == null){
            accountById = accountMapper.selectById(id);
            if (accountById != null){
                redisService.saveAccount(accountById);
            }
        }
        return accountById;
    }

    /**
     * 获取所有账户数量
     * @return
     */
    @Override
    public int getAccountCount() {
        return accountMapper.selectCount(null);
    }

    /**
     * 获取所有账户
     * @return
     */
    @Override
    public List<Account> getAccount(long page,long pageSize) {
        Page<Account> accountPage = new Page<>(page,pageSize);
        accountMapper.selectPage(accountPage,null);
        return accountPage.getRecords();
    }

    /**
     * 根据身份证号模糊查询账户数量
     * @param phone
     * @return
     */
    @Override
    public int fuzzyQueryByPhoneCount(String phone) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("phone",phone);
        return accountMapper.selectCount(queryWrapper);
    }

    /**
     * 根据身份证号模糊查询账户
     * @param phone
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public List<Account> fuzzyQueryByPhone(String phone, long currentPage, long pageSize) {
        Page<Account> page = new Page<>(currentPage,pageSize);
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("phone",phone);
        accountMapper.selectPage(page,queryWrapper);
        return page.getRecords();
    }
}
