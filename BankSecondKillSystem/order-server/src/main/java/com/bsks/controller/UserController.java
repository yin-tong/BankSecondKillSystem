package com.bsks.controller;


import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.entity.BsksOrder;
import com.bsks.api.result.Result;
import com.bsks.service.BsksOrderService;
import com.bsks.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/user",tags = "普通用户功能接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private BsksOrderService bsksOrderService;

    /**
     * 查询个人订单(状态为0)
     * @param accountId 账户id
     * @return
     */
    @ApiOperation("根据账户id查看个人订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId",value = "账户id",paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/getSelfOrder")
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class,getValue = "getValue")
    public Result selfOrder(@RequestParam("accountId")long accountId,
                            @RequestParam( value = "currentPage", required = false, defaultValue = "1") int currentPage,
                            @RequestParam( value = "pageSize",required = false, defaultValue = "10") int pageSize){
        List<BsksOrder> bsksOrders = bsksOrderService.selfOrder(accountId, currentPage, pageSize);
        int cnt = bsksOrderService.selfOrderCount(accountId);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",cnt);
        map.put("orders",bsksOrders);
        return new Result("查询成功",map);
    }


    /**
     * 删除个人订单(把状态变成0)
     * @param orderIds 账户id
     * @return Result
     */
    @ApiOperation("根据订单id删除个人订单(把状态变成0)")
    @ApiImplicitParam(name = "orderIds",value = "订单id",paramType = "query")
    @GetMapping("/deleteSelfOrder")
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class,getValue = "getValue")
    public Result deleteSelf(@RequestParam("orderIds") String orderIds){
        String[] idStrs = orderIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String idStr : idStrs) {
            ids.add(Long.parseLong(idStr));
        }
        bsksOrderService.deleteSelfOrder(ids);
        return new Result("删除成功");
    }
}
