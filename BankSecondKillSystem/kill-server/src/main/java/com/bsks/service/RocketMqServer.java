package com.bsks.service;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Map;

public interface RocketMqServer {

    void sendOrderMessage(long key, Map<String,Object> orderMessage) throws MQBrokerException, RemotingException, InterruptedException, MQClientException;
}
