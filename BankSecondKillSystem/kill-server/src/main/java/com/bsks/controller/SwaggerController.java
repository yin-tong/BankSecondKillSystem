package com.bsks.controller;

import com.bsks.api.entity.Account;
import com.bsks.api.entity.BsksOrder;
import com.bsks.api.entity.BsksProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/swagger",tags = "返回实体类")
@RestController
@RequestMapping("/swagger")
public class SwaggerController {

    @ApiOperation("账户")
    @GetMapping(value = "/account")
    public Account account() {
        return new Account();
    }

    @ApiOperation("产品")
    @GetMapping(value = "/bsksProduct")
    public BsksProduct bsksProduct() {
        return new BsksProduct();
    }

    @ApiOperation("订单")
    @GetMapping(value = "/bsksOrder")
    public BsksOrder bsksOrder() {
        return new BsksOrder();
    }
}
