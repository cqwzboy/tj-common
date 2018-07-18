package com.tj.common.kafka;

import com.tj.common.kafka.producer.TjKafkaProducer;

public class TestProducer {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 50 ; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1; i++) {
						TjKafkaProducer.sendMsg("TOPIC_ZSL_TEST_MSG_11111", "[{\"channel\":1,\"content\":\"你好\",\"createTime\":1523525465209,\"customerId\":1234567890,\"isCustomer\":0,\"msgId\":\"1111111\",\"msgType\":0,\"reciever\":\"1234567890\",\"sender\":\"xxx\",\"shopId\":3221,\"uid\":\"1111111\",\"updateTime\":1523525465297}]");
//						TjKafkaProducer.sendMsg("spark_test_zsl", " If you desire an interview, I shallbe most happy to call in person, on any day and at any time you may appoint");
					}
				}
			}) ;
			t.start();
		}
		System.out.println("发送完毕");
		CloseUtils.closeProducer();
		System.out.println("关闭producer");
	}
}
