package com.bsks.controller;

import com.alibaba.fastjson.JSON;
import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.entity.Account;
import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.AccountService;
import com.bsks.service.MessageService;
import com.bsks.service.OauthService;
import com.bsks.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(value = "/user",tags = "普通用户功能接口")
@RestController
@RequestMapping("/user")
public class UserAccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OauthService oauthService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisService redisService;


    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result register(Account account){
        account.setRoleName("USER");
        return accountService.register(account);
    }


    @ApiOperation(value = "通过手机号码，密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "手机号码", paramType = "body"),
            @ApiImplicitParam(name = "password",value = "密码", paramType = "body")
    })
    @PostMapping("/passwordLogin")
    public Result userPasswordLogin(String phone, String password){
        // 根据手机号码找到账户信息
        Account account = accountService.findByPhone(phone);
        // 账户为空,密码错误,角色不是管理员，返回错误信息
        if (account == null || !account.getPassword().equals(password) || !account.getRoleName().equals("USER")){
            return new Result(ResultCode.PasswordLoginError);
        }

        // 获取token
        Map<String,String> payloadMap = new HashMap();
        payloadMap.put("accountId",String.valueOf(account.getId()));
        payloadMap.put("roleName",account.getRoleName());
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
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class,getValue= "getValue")
    public Result userLogout(String token){
        Result oauthResult = oauthService.deleteToken(token);
        if (oauthResult.getCode() != 20000){
            return new Result(oauthResult.getCode(),oauthResult.getMessage());
        }
        return new Result("退出成功");
    }


    @ApiOperation(value = "根据id获取个人账户信息")
    @ApiImplicitParam(name = "accountId",value = "账户id", paramType = "query")
    @GetMapping("/getSelfAccount")
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class,getValue= "getValue")
    public Result findAccountById(@RequestParam("accountId") long accountId){
        Account account = accountService.findById(accountId);
        if (account == null || !account.getRoleName().equals("USER")){
            return new Result(ResultCode.AccountNullError);
        }
        return new Result("获取账户信息成功",account);
    }

    /**
     * 修改账户信息
     * @param account 账户
     */
    @ApiOperation(value = "修改个人账户信息")
    @PostMapping("/updateSelfAccount")
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class,getValue= "getValue")
    public Result updateUserAccount(Account account){
        System.out.println(account);
        return accountService.updateAccount(account);
    }

}
