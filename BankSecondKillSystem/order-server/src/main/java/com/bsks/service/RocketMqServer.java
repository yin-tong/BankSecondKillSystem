package com.bsks.service;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Map;

public interface RocketMqServer {

    void sendProductMessage(Map<String,Object> messageBody) throws MQBrokerException, RemotingException, InterruptedException, MQClientException;
}
