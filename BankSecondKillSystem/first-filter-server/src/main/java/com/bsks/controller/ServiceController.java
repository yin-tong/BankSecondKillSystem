package com.bsks.controller;

import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.result.Result;
import com.bsks.service.FirstFilterRecordService;
import com.bsks.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    private FirstFilterRecordService firstFilterRecordService;

    @ApiOperation(value = "通过身份证号,年龄判断是否通过初筛")
    @ApiImplicitParam(name = "identityId",value = "身份证号码", paramType = "query")
    @PostMapping("/service/isRejected")
    public Result isRejected(@RequestParam("identityId") String identityId,@RequestParam("age") int age){
        return new Result("判断成功",firstFilterRecordService.isRejected(identityId,age));
    }

}
