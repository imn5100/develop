package com.shaw.mq;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Consumer {
	public static void main(String[] args) throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumeGroupName");
		consumer.setNamesrvAddr("127.0.0.1:9876");
		consumer.setInstanceName("Consumer1");

		// 订阅消息topic
		consumer.subscribe("TopicTest", "");
		// 可订阅多个topic
		consumer.subscribe("TopicTest2", "*");

		consumer.registerMessageListener(new MessageListenerConcurrently() {
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.println(Thread.currentThread().getName() + "Receive New Messages1: " + msgs);
				System.out.println(Thread.currentThread().getName() + "Receive New Messages-size: " + msgs.size());
				for (MessageExt msg : msgs) {
					if (msg.getTopic().equals("TopicTest")) {
						// 执行TopicTest1的消费逻辑
						if (msg.getTags() != null && msg.getTags().equals("TagA")) {
							// 执行TagA的消费
							System.out.println("topic1-" + new String(msg.getBody()) + " key:" + msg.getKeys());
						} else if (msg.getTags() != null && msg.getTags().equals("TagC")) {
							// 执行TagC的消费
							System.out.println("topic1-" + new String(msg.getBody()));
						} else if (msg.getTags() != null && msg.getTags().equals("TagD")) {
							// 执行TagD的消费
							System.out.println("topic1-" + new String(msg.getBody()));
						}
					} else if (msg.getTopic().equals("TopicTest2")) {
						System.out.println("topic2-" + new String(msg.getBody()));
					}
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		consumer.start();

		System.out.println("Consumer Started.");
	}
}
