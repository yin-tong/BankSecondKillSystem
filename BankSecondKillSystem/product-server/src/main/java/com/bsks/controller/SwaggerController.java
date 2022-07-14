package com.bsks.controller;


import com.bsks.api.entity.BsksProduct;
import com.bsks.entity.ReturnProductRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/swagger",tags = "返回实体类")
@RestController
@RequestMapping("/swagger")
public class SwaggerController {

    @ApiOperation("产品")
    @GetMapping(value = "/bsksProduct")
    public BsksProduct bsksProduct() {
        return new BsksProduct();
    }

    @ApiOperation("回退库存记录")
    @GetMapping(value = "/returnProductRecord")
    public ReturnProductRecord returnProductRecord() {
        return new ReturnProductRecord();
    }
}
