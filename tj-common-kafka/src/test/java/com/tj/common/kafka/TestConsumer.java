package com.tj.common.kafka;

import java.util.concurrent.atomic.AtomicLong;

import com.tj.common.kafka.consumer.KafkaReceiveMsgCallBack;
import com.tj.common.kafka.consumer.TjKafkaConsumer;

public class TestConsumer {

	private static AtomicLong count = new AtomicLong(0) ;
	
	public static void main(String[] args) throws InterruptedException {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("jvm退出，关闭所有consumer");
				CloseUtils.closeAllConsumers(); 
			}
		}));
//		TjKafkaConsumer.receiveBlockingMsg("TOPIC_ZSL_TEST_MSG_11111", 2000, new KafkaReceiveMsgCallBack() {
//			@Override
//			public void call(String key, String msg) {
//				System.out.println(msg);
//			}
//		});
		TjKafkaConsumer.receiveBlockingMsg("TJK_VOICE_EXCERCISE_ILLUSEXAM", 2000, new KafkaReceiveMsgCallBack() {
			@Override
			public void call(String key, String msg) {
				System.out.println(msg);
			}
		});

		while(true){
		}
	}
}
