package com.tj.common.kafka.consumer;

import java.util.Arrays;
import java.util.ResourceBundle;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;


/**
 * 指定offset消费的线程
 * @author silongz
 *
 */
public class OffsetConsumerThread implements Runnable{

	private ResourceBundle rs ;
	
	private int partitionNum ;
	
	private long offset ;
	
	private String topic ;
	
	private KafkaReceiveMsgCallBack callBack ;
	
	public OffsetConsumerThread(String topic , int partitionNum , long offset, ResourceBundle rs, KafkaReceiveMsgCallBack callBack){
		this.topic = topic ;
		this.partitionNum = partitionNum ;
		this.offset = offset ;
		this.callBack = callBack ;
		this.rs = rs ;
	}
	
	@Override
	public void run() {
		KafkaConsumer<String, String> consumer = ConsumerBuilder.buildNew(rs,true);
		TopicPartition partition = new TopicPartition(topic, partitionNum);
		consumer.assign(Arrays.asList(partition));
		consumer.seek(partition, offset);
		ConsumerRecords<String, String> records = consumer.poll(100);
		for (ConsumerRecord<String, String> record : records) {
			callBack.call(record.key(), record.value());
		}
		consumer.close();
	}
	
}
