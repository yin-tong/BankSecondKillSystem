package com.bsks.controller;

import com.bsks.entity.FirstFilterRecord;
import com.bsks.entity.FirstFilterRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/swagger",tags = "返回实体类")
@RestController
@RequestMapping("/swagger")
public class SwaggerController {

    @ApiOperation("初筛规则")
    @GetMapping(value = "/firstFilterRule")
    public FirstFilterRule firstFilterRule() {
        return new FirstFilterRule();
    }

    @ApiOperation("初筛记录")
    @GetMapping(value = "/firstFilterRecord")
    public FirstFilterRecord firstFilterRecord() {
        return new FirstFilterRecord();
    }
}
