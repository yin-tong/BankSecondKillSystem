package com.bsks.controller;

import com.alibaba.fastjson.JSON;
import com.bsks.api.conponent.SnowFlakeId;
import com.bsks.api.entity.BsksOrder;
import com.bsks.api.result.Result;
import com.bsks.service.AccountService;
import com.bsks.service.BsksOrderService;
import com.bsks.service.ProductService;
import com.bsks.service.RocketMqServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 消费mq中的订单信息，创建订单
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",consumerGroup = "${mq.order.group-name}",messageModel = MessageModel.CLUSTERING)
public class MqCreateOrderController implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    @Autowired
    private SnowFlakeId snowFlakeId;

    @Autowired
    private ProductService productService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BsksOrderService bsksOrderService;

    @Autowired
    private RocketMqServer rocketMqServer;

    @SneakyThrows
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println(Thread.currentThread().getName()+",messageId: "+messageExt.getMsgId());
        //1. 从mq中将订单拿出来
        String body = new String( messageExt.getBody(),"UTF-8" );
        Map<String,Object> map = JSON.parseObject(body, Map.class);
        // 获取消息中的订单信息
        BsksOrder bsksOrder = JSON.parseObject(JSON.toJSONString(map.get("order")), BsksOrder.class);
        // 防止消息被重复消费
        BsksOrder bsksOrder1 = bsksOrderService.getById(bsksOrder.getId());
        if (bsksOrder1 != null){
            return;
        }
        //3. 用户购买成功数量+抢购数量超过限购数量
        int numbers = bsksOrderService.findNumbers(bsksOrder.getPhone(), bsksOrder.getProductName());
        if (numbers + bsksOrder.getNumber() > bsksOrder.getLimitedQuantity()){
            System.out.println("超过限购数量: "+bsksOrder);
            bsksOrder.setType("抢购失败");
            bsksOrderService.save(bsksOrder);
            return;
        }
        //4. 扣减库存
        Result resultProduct = productService.reduceProductQuantity(bsksOrder.getProductId(),bsksOrder.getNumber());
        if (resultProduct.getCode() != 20000){
            System.out.println ("扣减库存失败: "+bsksOrder);
            bsksOrder.setType("抢购失败");
            bsksOrderService.save(bsksOrder);
            return;
        }
        //5. 生成成功订单
        bsksOrder.setType("抢购成功");
        try {
            bsksOrderService.save(bsksOrder);
        }
        catch (Exception e){
            // 生成订单失败，回退库存
            System.out.println ("生成订单失败，回退库存"+bsksOrder);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("orderId",bsksOrder.getId());
            map2.put("productId",bsksOrder.getProductId());
            map2.put("number",bsksOrder.getNumber());
            rocketMqServer.sendProductMessage(map2);
            return;
        }
        //6. 付款
        Result payResult = accountService.accountPay (bsksOrder.getAccountId(),bsksOrder.getPayMoney());
        if(payResult.getCode() != 20000){
            // 付款失败，回退库存，删除订单
            System.out.println ("付款失败，回退库存，删除订单"+bsksOrder);
            Map<String,Object> map2 = new HashMap<>();
            map2.put("orderId",bsksOrder.getId());
            map2.put("productId",bsksOrder.getProductId());
            map2.put("number",bsksOrder.getNumber());
            rocketMqServer.sendProductMessage(map2);
            // 保存失败订单
            bsksOrder.setType("抢购失败");
            bsksOrderService.updateById(bsksOrder);
            return;
        }
        bsksOrder.setType("抢购成功");
        System.out.println("抢购成功："+bsksOrder);
        bsksOrderService.save(bsksOrder);
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 每次拉取的间隔，单位为毫秒,默认为0
        defaultMQPushConsumer.setPullInterval(0);
        // 设置每次从队列中拉取的消息数，默认为32
        defaultMQPushConsumer.setPullBatchSize(32);
        defaultMQPushConsumer.setConsumeThreadMax(20);
        defaultMQPushConsumer.setConsumeThreadMin(20);
    }
}
