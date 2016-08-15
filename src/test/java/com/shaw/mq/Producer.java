package com.shaw.mq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class Producer {
	public static void main(String[] args)
			throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		DefaultMQProducer producer = new DefaultMQProducer("testProductGroup");
		producer.setNamesrvAddr("10.1.50.124:9876");
		producer.setInstanceName("Producer1");
		producer.start();
		try {
			for (int i = 0; i < 20; i++) {
				// 构造消息 需要 topic tag body
				Message msg = null;
				if (i % 2 == 0) {
					msg = new Message("TopicTest", "TagA", "orderId" + i, ("hello mq" + i).getBytes());
				} else {
					msg = new Message("TopicTest", "TagB", ("hello mq" + i).getBytes());
				}
				SendResult result = producer.send(msg);
				System.out.println(result);
			}
		} finally {
			producer.shutdown();
		}

	}
}
