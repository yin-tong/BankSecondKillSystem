package com.bsks.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bsks.api.annotation.AccessLimiter;
import com.bsks.api.annotation.JWTHasRole;
import com.bsks.api.conponent.SnowFlakeId;
import com.bsks.api.entity.Account;
import com.bsks.api.entity.BsksOrder;
import com.bsks.api.entity.BsksProduct;
import com.bsks.api.result.Result;
import com.bsks.api.result.ResultCode;
import com.bsks.service.*;
import com.bsks.utils.SM2Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(value = "/kill",tags = "秒杀下单功能接口")
@RestController
public class KillController {

    @Autowired
    private SnowFlakeId snowFlakeId;

    @Autowired
    private ProductService productService;

    @Autowired
    private FirstFilterService firstFilterService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RocketMqServer rocketMqServer;

    /**
     * 获取秒杀凭证
     * @param accountId 账户id
     * @param productId 产品id
     * @param number 抢购数量
     * @return
     */
    @ApiOperation("获取秒杀凭证(先获取秒杀凭证再进行秒杀)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId",value = "账户id", paramType = "query"),
            @ApiImplicitParam(name = "productId",value = "产品id", paramType = "query"),
            @ApiImplicitParam(name = "number",value = "抢购数量", paramType = "query"),
    })
    @GetMapping("/getKillVoucher")
    @AccessLimiter(limitCount = 1, limitTime = 60 , oauthService = OauthService.class,getValue = "getValue")
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class, getValue = "getValue")
    public Result getKillVoucher(long accountId, long productId, int number) throws IOException {

        //1. 校验产品id是否有效
        Result productResult = productService.findProductById(productId);
        if (productResult.getCode() != 20000){
            return new Result(productResult.getCode(),productResult.getMessage());
        }
        if (productResult.getData() == null){
            return new Result(ResultCode.ProductNotExist);
        }
        BsksProduct product = JSONObject.parseObject(JSON.toJSONString(productResult.getData()),BsksProduct.class);
        // 秒杀时间未到，返回错误信息
        if (product.getKillTime().after(new Date())){
            return new Result(ResultCode.KillNotTime);
        }
        // 商品状态为1，表示不可见，返回无此产品的信息
        if (product.getStatus() == 1){
            return new Result(ResultCode.ProductNotExist);
        }

        // 抢购数量超过限购数量，返回错误信息
        if (number>product.getLimitedQuantity()){
            return new Result(ResultCode.OutLimitedQuantity);
        }
        // 商品数量为0,返回错误信息
        if (product.getQuantity() == 0){
            return new Result(ResultCode.SoldOut);
        }
        //2. 校验账户id是否有效
        Result accountResult = accountService.findById(accountId);
        if (accountResult.getCode() != 20000){
            return new Result(accountResult.getCode(),accountResult.getMessage());
        }
        if (accountResult.getData() == null){
            return new Result(ResultCode.AccountNotExist);
        }
        Account account = JSONObject.parseObject(JSON.toJSONString(accountResult.getData()),Account.class);
        System.out.println(account);
        //3. 验证账户是否通过初筛
        Result rejectedResult = firstFilterService.isRejected(account.getIdentityId(), account.getAge());
        if (rejectedResult.getCode() != 20000){
            return new Result(rejectedResult.getCode(),rejectedResult.getMessage());
        }
        boolean isRejected = (boolean) rejectedResult.getData();
        if (!isRejected){
            return new Result(ResultCode.isRejected);
        }
        //4. 用户购买成功数量+抢购数量超过限购数量,返回错误信息
        Result hasNumbersResult = orderService.findNumbers(account.getPhone(), product.getName());
        if (hasNumbersResult.getCode() != 20000){
            return new Result(hasNumbersResult.getCode(),hasNumbersResult.getMessage());
        }
        int hasNumbers = (int) hasNumbersResult.getData();
        if (hasNumbers + number > product.getLimitedQuantity()){
            return new Result(ResultCode.OverLimitedQuantity);
        }

        BigDecimal price = product.getPrice();
        BigDecimal payMoney = price.multiply(BigDecimal.valueOf(number));

        // 生成订单信息
        BsksOrder order = new BsksOrder();
        order.setId(snowFlakeId.nextId());
        order.setAccountId(accountId);
        order.setProductId(productId);
        order.setUserName(account.getName());
        order.setPhone(account.getPhone());
        order.setProductName(product.getName());
        order.setLimitedQuantity(product.getLimitedQuantity());
        order.setNumber(number);
        order.setPayMoney(payMoney);
        order.setOrderTime(new Date());
        order.setVersion(1L);
        String voucher = SM2Utils.encrypt(JSON.toJSONString(order));
        return new Result ( "获取凭证成功!",voucher);
    }

    /**
     * 秒杀
     * @return
     */
    @ApiOperation("秒杀")
    @ApiImplicitParam(name = "voucher",value = "抢购凭证", paramType = "query")
    @GetMapping("/kill/{voucher}")
    @AccessLimiter(limitCount = 1, limitTime = 60 , oauthService = OauthService.class,getValue = "getValue")
    @JWTHasRole( roleName = "USER", oauthService = OauthService.class,getValue = "getValue")
    public Result kill(@PathVariable String voucher) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String mapJsonString;
        try {
            mapJsonString = SM2Utils.decrypt(voucher);
        }catch (Exception e){
            e.printStackTrace();
            return new Result ( ResultCode.PathError);
        }
        BsksOrder order = JSONObject.parseObject(mapJsonString, BsksOrder.class);
        Map<String,Object> orderMap = new HashMap<>();
        orderMap.put("order",order);
        //4. 存入消息队列
        rocketMqServer.sendOrderMessage(order.getId(),orderMap);
        return new Result ( "正在排队中!");
    }
}
