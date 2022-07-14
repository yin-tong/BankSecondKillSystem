package com.bsks.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bsks.api.entity.BsksProduct;
import com.bsks.entity.ReturnProductRecord;
import com.bsks.mapper.ReturnProductRecordMapper;
import com.bsks.service.ProductService;
import com.mysql.cj.protocol.Message;
import com.mysql.cj.protocol.MessageListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 消费mq中的商品信息，回退库存
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.product.topic}",consumerGroup = "${mq.product.group-name}",messageModel = MessageModel.BROADCASTING)
public class MqReturnProductController implements MessageListener, RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

    @Autowired
    private ProductService productService;

    @Autowired
    private ReturnProductRecordMapper returnProductRecordMapper;

    @SneakyThrows
    @Override
    public void onMessage(MessageExt messageExt) {
        //1. 从mq中将回退库存的信息拿出来
        String body = new String( messageExt.getBody(),"UTF-8" );
        Map<String,Object> map = JSON.parseObject(body, Map.class);
        // 获取消息中的订单id
        long orderId = JSON.parseObject(JSON.toJSONString(map.get("orderId")), long.class);
        QueryWrapper<ReturnProductRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        ReturnProductRecord returnProductRecord = returnProductRecordMapper.selectOne(queryWrapper);
        // 重复消费不做操作
        if (returnProductRecord !=null){
            return;
        }
        // 获取消息中的回退产品的id
        long productId = JSON.parseObject(JSON.toJSONString(map.get("productId")), long.class);
        // 获取消息中的回退产品的数量
        int number = JSON.parseObject(JSON.toJSONString(map.get("number")), int.class);
        //2. 回退库存
        try {
            productService.increaseProductQuantity(productId,number);
            returnProductRecord = new ReturnProductRecord();
            returnProductRecord.setOrderId(orderId);
            returnProductRecord.setProductId(productId);
            returnProductRecord.setNumber(number);
            returnProductRecordMapper.insert(returnProductRecord);
            System.out.println("回退库存成功："+productId+"库存+"+number);
        }catch (Exception e){
            throw new Exception("回退库存失败："+productId+"库存+"+number);
        }
    }


    @Override
    public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
        // 每次拉取的间隔，单位为毫秒,默认为0
        defaultMQPushConsumer.setPullInterval(0);
        // 设置每次从队列中拉取的消息数，默认为32
        defaultMQPushConsumer.setPullBatchSize(32);
        //消费端拉去到消息以后分配线索去消费
        defaultMQPushConsumer.setConsumeThreadMin(20);
        //最大消费线程，一般情况下，默认队列没有塞满，是不会启用新的线程的
        defaultMQPushConsumer.setConsumeThreadMax(20);
    }

    @Override
    public boolean processMessage(Message message) {
        return false;
    }

    @Override
    public void error(Throwable throwable) {

    }
}
