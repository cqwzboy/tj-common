package com.tj.common.kafka.consumer;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 每个topic对应一个consumer线程
 * @author silongz
 *
 */
public class ConsumerThread implements Runnable{

	private Logger logger = LoggerFactory.getLogger(ConsumerThread.class) ;
			
	private final AtomicBoolean closed = new AtomicBoolean(false);
	
	private KafkaConsumer<String, String> consumer ;
	
	private ResourceBundle rs ;
	
	private String topic ;
	
	private KafkaReceiveMsgCallBack callBack ;
	
	public ConsumerThread(String topic, ResourceBundle rs, KafkaReceiveMsgCallBack callBack){
		this.topic = topic ;
		this.callBack = callBack ;
		this.rs = rs ;
	}
	
	@Override
	public void run() {
		consumer = ConsumerBuilder.buildNew(rs,true);
		consumer.subscribe(Arrays.asList(topic));
		ConsumerHolder.addConsumer(this);
		while (!closed.get()) {
			try {
				ConsumerRecords<String, String> records = consumer.poll(100);
				for (ConsumerRecord<String, String> record : records) {
					callBack.call(record.key(), record.value());
				}
			} catch (Exception e) {
				logger.error(String.format("非阻塞消息topic【%s】,消费异常", topic),e);
			}
		}
	}

	/**
	 * 关闭consumer
	 */
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
	
}
