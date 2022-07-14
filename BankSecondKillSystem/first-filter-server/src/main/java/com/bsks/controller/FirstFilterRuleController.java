package com.bsks.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.result.Result;
import com.bsks.entity.FirstFilterRule;
import com.bsks.service.FirstFilterRecordService;
import com.bsks.service.FirstFilterRuleService;
import com.bsks.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 初筛的拒绝规则 前端控制器
 * </p>
 *
 * @author Li
 * @since 2022-02-12
 */
@Api(value = "/firstFilterRule",tags = "初筛规则功能接口")
@RequestMapping("/firstFilterRule")
@RestController
public class FirstFilterRuleController {

    @Autowired
    private FirstFilterRuleService firstFilterRuleService;

    @ApiOperation(value = "获取初筛规则")
    @GetMapping("/get")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result get(){
        return new Result("获取成功",firstFilterRuleService.getFirstFilterRule());
    }

    @ApiOperation(value = "更新初筛规则")
    @PostMapping("/update")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result update(FirstFilterRule firstFilterRule){
        return firstFilterRuleService.updateFirstFilterRule(firstFilterRule);
    }
}

