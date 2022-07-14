package com.bsks.controller;

import com.bsks.api.result.Result;
import com.bsks.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微服务内部调用接口
 */
@Api(value = "/service",tags = "微服务内部调用接口")
@RestController
public class ServiceController {

    @Autowired
    private ProductService productService;

    /**
     * 根据产品id查询产品信息
     * @param productId 产品id
     * @return 产品
     */
    @ApiOperation(value = "根据产品id查询产品信息")
    @ApiImplicitParam(name = "productId",value = "产品id",paramType = "query")
    @GetMapping("/service/findProductById")
    public Result findProductById(@RequestParam("productId") long productId){
        return new Result("查询成功",productService.findProductById(productId));
    }

    /**
     * 从redis中扣减库存
     * @param productId 产品id
     * @param number 扣减数量
     * @return
     */
    @ApiOperation(value = "秒杀时从redis中扣减库存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId",value = "产品id",paramType = "query"),
            @ApiImplicitParam(name = "number",value = "产品库存扣减数量",paramType = "query")
    })
    @GetMapping("/service/reduceProductQuantity")
    public Result reduceProductQuantity(@RequestParam("productId")long productId,@RequestParam("number") int number){
        return productService.reduceProductQuantity(productId,number);
    }

}
