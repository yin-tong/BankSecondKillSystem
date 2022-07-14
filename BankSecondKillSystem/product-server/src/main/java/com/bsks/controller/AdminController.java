package com.bsks.controller;

import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.entity.BsksProduct;
import com.bsks.api.result.Result;
import com.bsks.service.OauthService;
import com.bsks.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * 抢购存储产品 前端控制器
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
@Api(value = "/admin",tags = "管理员功能接口")
@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    private ProductService productService;

    /**
     * 获取产品信息
     * @param currentPage 页数
     * @param pageSize 一页的数据数量
     * @return
     */
    @ApiOperation("获取产品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage",value = "页码数",defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10",paramType = "query")
    })
    @GetMapping("/getProduct")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result getProduct(@RequestParam( value = "currentPage", required = false, defaultValue = "1") long currentPage,
                             @RequestParam( value = "pageSize",required = false, defaultValue = "10") long pageSize){
        List<BsksProduct> products = productService.getProduct(currentPage,pageSize);
        int cnt = productService.getProductCount();
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",cnt);
        map.put("products",products);
        return new Result("获取产品信息成功",map);
    }

    /**
     * 添加产品信息
     * @param product 产品
     */
    @ApiOperation("添加产品信息")
    @ApiImplicitParam(name = "product",value = "产品",paramType = "body")
    @PostMapping("/addProduct")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result addProduct(BsksProduct product){
        productService.addProduct(product);
        return new Result("增加产品成功");
    }

    /**
     * 删除产品信息
     * @param productIds 产品id
     */
    @ApiOperation("根据产品id删除产品信息")
    @ApiImplicitParam(name = "productIds",value = "产品id",paramType = "body")
    @GetMapping("/deleteProduct")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result deleteProduct(@RequestParam("productIds") String productIds){
        System.out.println(productIds);
        String[] idStrs = productIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String idStr : idStrs) {
            ids.add(Long.parseLong(idStr));
        }
        productService.deleteProduct(ids);
        return new Result("删除产品成功");
    }

    /**
     * 更新产品信息
     * @param product 产品
     */
    @ApiOperation("更新产品信息")
    @PostMapping("/updateProduct")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result updateProduct(BsksProduct product ){
        productService.updateProduct(product);
        return new Result("更新产品成功");
    }

    /**
     * 修改产品状态(0或1)
     * @param id
     */
    @ApiOperation("修改产品状态(0或1)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "产品id",paramType = "query"),
            @ApiImplicitParam(name = "status",value = "产品对于用户是否可见  0 可见 1 不可见",paramType = "query")
    })
    @GetMapping("/updateProductStatus")
    @JWTHasRole( roleName = "ADMIN", oauthService = OauthService.class,getValue = "getValue")
    public Result updateProductStatus(@RequestParam("id") long id,@RequestParam("status")int status){
        productService.updateProduct(id,status);
        return new Result("更新产品状态成功");
    }

    @ApiOperation(value = "根据时间段可以秒杀的产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start",value = "开始时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "end",value = "结束时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数", defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10", paramType = "query")
    })
    @GetMapping("/findByDate")
    public Result findByDate(Date start,
                             Date end,
                             @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int totalNumber = productService.findByDate(start,end);
        List<BsksProduct> records = productService.findByDate(start,end,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",totalNumber);
        map.put("products",records);
        return new Result("获取产品成功",map);
    }

    @ApiOperation(value = "根据产品名称查询产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productName",value = "产品名称", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数", defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10", paramType = "query")
    })
    @GetMapping("/findByName")
    public Result findByName(String productName,
                             @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                             @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int totalNumber = productService.findByName(productName);
        List<BsksProduct> records = productService.findByName(productName,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",totalNumber);
        map.put("products",records);
        return new Result("获取产品成功",map);
    }

    @ApiOperation(value = "根据产品名称，时间段查询的产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start",value = "开始时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "end",value = "结束时间(yyyy-MM-dd HH:mm:ss)", paramType = "query"),
            @ApiImplicitParam(name = "currentPage",value = "页码数", defaultValue = "1",paramType = "query"),
            @ApiImplicitParam(name = "pageSize",value = "每页展示的数据数量",defaultValue = "10", paramType = "query")
    })
    @GetMapping("/findByDateAndName")
    public Result findByDateAndName(String productName,
                                    Date start,
                                    Date end,
                                    @RequestParam(value = "currentPage",required = false,defaultValue = "1") long currentPage,
                                    @RequestParam(value = "pageSize",required = false,defaultValue = "10") long pageSize){
        int totalNumber = productService.findByDateAndName(productName,start,end);
        List<BsksProduct> records = productService.findByDateAndName(productName,start,end,currentPage,pageSize);
        Map<String,Object> map = new HashMap<>();
        map.put("totalNumber",totalNumber);
        map.put("products",records);
        return new Result("获取产品成功",map);
    }

}

