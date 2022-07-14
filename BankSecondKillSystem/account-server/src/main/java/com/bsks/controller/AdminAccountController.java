package com.bsks.controller;

import com.alibaba.fastjson.JSON;
import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.entity.Account;
import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.api.result.ResultException;
import com.bsks.entity.AccountPay;
import com.bsks.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员可访问的控制层
 */
@Api(value = "/admin",tags = "管理员功能接口")
@RequestMapping("/admin")
@RestController
public class AdminAccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OauthService oauthService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AccountPayService accountPayService;

    @ApiOperation(value = "通过手机号码，密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "手机号码", paramType = "body"),
            @ApiImplicitParam(name = "password",value = "密码", paramType = "body")
    })
    @PostMapping("/passwordLogin")
    public Result aminPasswordLogin(String phone,String password){
        System.out.println(phone);
        // 根据手机号码找到账户信息
        Account account = accountService.findByPhone(phone);
        // 账户为空,密码错误,角色不是管理员，返回错误信息
        if (account == null || !account.getPassword().equals(password) || !account.getRoleName().equals("ADMIN")){
            return new Result(ResultCode.PasswordLoginError);
        }
        // 构建token payload部分的map
        Map<String,String> payloadMap = new HashMap();
        payloadMap.put("accountId",String.valueOf(account.getId()));
        payloadMap.put("roleName",account.getRoleName());
        // 创建token
        String map = JSON.toJSONString(payloadMap);
        Result oauthResult = oauthService.createToken(map);
        if (oauthResult.getCode() != 20000){
            return new Result(oauthResult.getCode(),oauthResult.getMessage());
        }
        // 构造返回信息
        Map<String,Object> resultMap = new HashMap();
        resultMap.put("accountId",String.valueOf(account.getId()));
        resultMap.put("token",oauthResult.getData());
        return new Result("登录成功",resultMap);
    }


    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class, getValue= "getValue")
    public Result adminLogout(String token){
        Result oauthResult = oauthService.deleteToken(token);
        if (oauthResult.getCode() != 20000){
            return new Result(oauthResult.getCode(),oauthResult.getMessage());
        }
        return new Result("退出成功");
    }


    @ApiOperation(value = "通过账户id获取个人账户信息")
    @ApiImplicitParam(name = "accountId",value = "账户id", paramType = "query")
    @GetMapping("/getSelfAccount")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue= "getValue")
    public Result adminFindAccount(@RequestParam("accountId") long accountId){
        Account account = accountService.findById(accountId);
        if (account == null || !account.getRoleName().equals("ADMIN")){
            throw new ResultException(ResultCode.AccountNullError);
        }
        List<AccountPay> accountPays = accountPayService.queryAll();
        BigDecimal sum = BigDecimal.valueOf(0);
        for (AccountPay accountPay : accountPays) {
            sum=sum.add(accountPay.getPayMoney ());
            sum=sum.subtract(accountPay.getSynchronousMoney());
            accountPay.setSynchronousMoney(accountPay.getPayMoney());
            accountPayService.updateAccountPay(accountPay);
        }
        account.setMoney(account.getMoney().add(sum));
        accountService.updateAccount(account);
        Account account2 = accountService.findById(accountId);
        return new Result("获取账户信息成功",account2);
    }

    /**
     * 修改账户信息
     * @param account 账户
     */
    @ApiOperation(value = "修改个人账户信息")
    @PostMapping("/updateSelfAccount")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue= "getValue")
    public Result updateAdminAccount(Account account){
        return accountService.updateAccount(account);
    }



    @ApiOperation(value = "获取账户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/getAccount")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue= "getValue")
    public Result getAdminAccount(@RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int count = accountService.getAccountCount();
        List<Account> accounts = accountService.getAccount(currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",count);
        map.put("accounts",accounts);
        return new Result("获取成功",map);
    }

    @ApiOperation(value = "根据姓名模糊查询账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId",value = "账户id", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/fuzzyQueryByName")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue= "getValue")
    public Result fuzzyQueryByName(@RequestParam("accountName") String name,
                                   @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int count = accountService.fuzzyQueryByNameCount(name);
        List<Account> accounts = accountService.fuzzyQueryByName(name,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",count);
        map.put("accounts",accounts);
        return new Result("查询成功",map);
    }

    @ApiOperation(value = "根据手机号码查询账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "手机号码", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/fuzzyQueryByPhone")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue= "getValue")
    public Result fuzzyQueryByPhone(@RequestParam("phone") String phone,
                                   @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int count = accountService.fuzzyQueryByPhoneCount(phone);
        List<Account> accounts = accountService.fuzzyQueryByPhone(phone,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",count);
        map.put("accounts",accounts);
        return new Result("查询成功",map);
    }

    @ApiOperation(value = "根据身份证号查询账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identityId",value = "身份证号码", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/fuzzyQueryByIdentityId")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue= "getValue")
    public Result fuzzyQueryByIdentityId(@RequestParam("identityId")String identityId,
                                         @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int count = accountService.fuzzyQueryByIdentityIdCount(identityId);
        List<Account> accounts = accountService.fuzzyQueryByIdentityId(identityId,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",count);
        map.put("accounts",accounts);
        return new Result("查询成功",map);
    }
}
