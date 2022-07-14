package com.bsks.controller;

import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.result.Result;
import com.bsks.entity.FirstFilterRecord;
import com.bsks.service.FirstFilterRecordService;
import com.bsks.service.OauthService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/firstFilterRecord",tags = "初筛记录功能接口")
@RequestMapping("/firstFilterRecord")
@RestController
public class FirstFilterRecordController {

    @Autowired
    private FirstFilterRecordService firstFilterRecordService;

    @ApiOperation(value = "获取初筛记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/findAll")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result findAllRecord(@RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int totalNumber = firstFilterRecordService.findAllRecordCount();
        List<FirstFilterRecord> records = firstFilterRecordService.findAllRecord(currentPage, pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",totalNumber);
        map.put("records",records);
        return new Result("获取数据成功",map);
    }


    @ApiOperation(value = "根据身份证号码查询初筛记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identityId",value = "身份证号码", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数", defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10", paramType = "query")
    })
    @GetMapping("/findByIdentityId")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result findRecordByIdentityId(String identityId,
                                         @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int totalNumber = firstFilterRecordService.findRecordCountByIdentityId(identityId);
        List<FirstFilterRecord> records = firstFilterRecordService.findRecordByIdentityId(identityId,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",totalNumber);
        map.put("records",records);
        return new Result("获取数据成功",map);
    }


    @ApiOperation(value = "根据日期查询初筛记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start",value = "开始时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "end",value = "结束时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数", defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10", paramType = "query")
    })
    @GetMapping("/findByDate")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result findRecordByDate(Date start,
                                   Date end,
                                   @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int totalNumber = firstFilterRecordService.findRecordCountByDate(start,end);
        List<FirstFilterRecord> records = firstFilterRecordService.findRecordByDate(start,end,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",totalNumber);
        map.put("records",records);
        return new Result("获取数据成功",map);
    }

    @ApiOperation(value = "根据身份证号码，时间段查询初筛记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identityId",value = "身份证号码", paramType = "query"),
            @ApiImplicitParam(name = "start",value = "开始时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "end",value = "结束时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数", defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10", paramType = "query")
    })
    @GetMapping("/findByIdentityIdDate")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result findRecordByIdentityIdDate(String identityId,
                                             Date start,
                                             Date end,
                                             @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int totalNumber = firstFilterRecordService.findRecordCountByIdentityIdDate(identityId,start,end);
        List<FirstFilterRecord> records = firstFilterRecordService.findRecordByIdentityIdDate(identityId,start,end,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",totalNumber);
        map.put("records",records);
        return new Result("获取数据成功",map);
    }




}
