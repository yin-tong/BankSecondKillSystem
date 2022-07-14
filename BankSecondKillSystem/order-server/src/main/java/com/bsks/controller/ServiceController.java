package com.bsks.controller;

import com.bsks.api.result.Result;
import com.bsks.service.BsksOrderService;
import com.bsks.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微服务内部调用接口
 */
@Api(value = "/service",tags = "微服务内部调用接口")
@RestController
public class ServiceController {

    @Autowired
    private BsksOrderService bsksOrderService;

    /**
     * 根据手机号码，产品名称查询用户已经购买的数量
     * @param phone 手机号码
     * @param productName 产品名称
     * @return 产品
     */
    @ApiOperation(value = "根据产品id查询产品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone",value = "手机号码",paramType = "query"),
            @ApiImplicitParam(name = "productName",value = "产品名称",paramType = "query")
    })
    @GetMapping("/service/findNumbers")
    public Result findNumbers(@RequestParam("phone") String phone,@RequestParam("productName") String productName){
        return new Result("查询成功",bsksOrderService.findNumbers(phone, productName));
    }


}
