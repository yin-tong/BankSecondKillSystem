package com.bsks.service.impl;

import com.alibaba.fastjson.JSON;
import com.bsks.service.RocketMqServer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *RocketMq 服务类
 */
@Service
public class RocketMqServerImpl implements RocketMqServer {

    @Value("${mq.order.topic}")
    private String topic;

    @Value ("${mq.order.tag}")
    private String tag;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    /**
     * 将订单信息发送到mq
     * @param key
     * @param orderMessage
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQClientException
     */
    @Override
    public void sendOrderMessage(long key,Map<String,Object> orderMessage) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(topic,tag, String.valueOf(key), JSON.toJSONString(orderMessage).getBytes());
        rocketMQTemplate.getProducer().send(message);
    }
}
