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
 *RocketMq 服务类: 向mq中生产回退产品数量的信息，供产品服务回退库存
 */
@Service
public class RocketMqServerImpl implements RocketMqServer {

    @Value("${mq.product.topic}")
    private String productTopic;

    @Value ("${mq.product.tag}")
    private String productTag;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    /**
     * 将回退产品数量的信息发送到mq
     * @param messageBody
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     * @throws MQClientException
     */
    @Override
    public void sendProductMessage(Map<String,Object> messageBody) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        Message message = new Message(productTopic,productTag, null, JSON.toJSONString(messageBody).getBytes());
        rocketMQTemplate.getProducer().send(message);
    }
}
