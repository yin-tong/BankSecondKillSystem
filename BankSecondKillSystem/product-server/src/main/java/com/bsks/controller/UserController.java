package com.bsks.controller;

import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.entity.BsksProduct;
import com.bsks.api.result.Result;
import com.bsks.service.OauthService;
import com.bsks.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 申请抢购记录 前端控制器
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
@Api(value = "/user",tags = "普通用户的功能接口")
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private ProductService productService;

    /**
     * 查询产品状态为0的商品信息
     * @return
     */
    @ApiOperation("查询产品状态为0的产品信息")
    @GetMapping("/getProducts")
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class, getValue = "getValue")
    public Result findProductByStatusZero(){
        List<BsksProduct> products = productService.findProductByStatusZero();
        return new Result("获取商品信息成功",products);
    }
}

