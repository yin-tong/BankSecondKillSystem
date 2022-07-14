package com.bsks.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.entity.BsksOrder;
import com.bsks.api.result.Result;
import com.bsks.mapper.BsksOrderMapper;
import com.bsks.service.BsksOrderService;
import com.bsks.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/admin",tags = "管理员功能接口")
@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    private BsksOrderService bsksOrderService;


    /**
     * 查看所有订单信息
     * @return Result
     */
    @ApiOperation("查看所有订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/getOrder")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result getOrder(@RequestParam( value = "currentPage", required = false, defaultValue = "1") long currentPage,
                           @RequestParam( value = "pageSize",required = false, defaultValue = "10") long pageSize){
        List<BsksOrder> orders = bsksOrderService.getOrder(currentPage,pageSize);
        int orderCount = bsksOrderService.getOrderCount();
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",orderCount);
        map.put("orders",orders);
        return new Result("获取订单成功",map);
    }

    /**
     * 根据客户名字进行查询
     * @param userName 客户名字
     * @return Result
     */
    @ApiOperation("根据客户名字进行查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "客户姓名",paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/findByUserName")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result findByUserName(@RequestParam("userName") String userName,
                                 @RequestParam( value = "currentPage", required = false, defaultValue = "1") long currentPage,
                                 @RequestParam( value = "pageSize",required = false, defaultValue = "10") long pageSize){
        List<BsksOrder> orders = bsksOrderService.findByUserName(userName,currentPage,pageSize);
        int byUserNameCount = bsksOrderService.findByUserNameCount(userName);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",byUserNameCount);
        map.put("orders",orders);
        return new Result("查询成功",map);
    }

    /**
     * 根据手机号码查询
     * @param phone 手机号码
     * @return list
     */
    @ApiOperation("根据手机号码查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "手机号码",paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/findByPhone")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result findByPhone(@RequestParam("phone")String phone,
                              @RequestParam( value = "currentPage", required = false, defaultValue = "1") long currentPage,
                              @RequestParam( value = "pageSize",required = false, defaultValue = "10") long pageSize) {
        List<BsksOrder> orders = bsksOrderService.findByPhone(phone,currentPage,pageSize);
        int byPhoneCount = bsksOrderService.findByPhoneCount(phone);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",byPhoneCount);
        map.put("orders",orders);
        return new Result("查询成功",map);
    }

    /**
     * 根据产品名称进行查询
     * @param productName 客户名字
     * @return Result
     */
    @ApiOperation("根据产品名称进行查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productName",value = "产品名称",paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/findByProductName")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result findByProductName(@RequestParam("productName") String productName,
                                    @RequestParam( value = "currentPage", required = false, defaultValue = "1") long currentPage,
                                    @RequestParam( value = "pageSize",required = false, defaultValue = "10") long pageSize){
        List<BsksOrder> orders = bsksOrderService.findByProductName(productName,currentPage,pageSize);
        int byProductNameCount = bsksOrderService.findByProductNameCount(productName);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",byProductNameCount);
        map.put("orders",orders);
        return new Result("查询成功",map);
    }

    /**
     * 通过订单id删除订单
     * @param orderIds 多个订单id
     * @return Result
     */
    @ApiOperation("通过订单id删除订单")
    @ApiImplicitParam(name = "orderIds",value = "订单id",paramType = "query")
    @GetMapping("/delete")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result delete(@RequestParam("orderIds") String orderIds) {
        String[] idStrs = orderIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String idStr : idStrs) {
            ids.add(Long.parseLong(idStr));
        }
        bsksOrderService.delete(ids);
        return new Result("删除成功");
    }
}
