package com.tj.common.kafka;

import java.util.List;

import org.apache.kafka.clients.producer.Producer;

import com.tj.common.kafka.consumer.ConsumerBlockingThread;
import com.tj.common.kafka.consumer.ConsumerHolder;
import com.tj.common.kafka.consumer.ConsumerThread;
import com.tj.common.kafka.producer.ProducerHolder;

/**
 * jvm退出时，需要调用的关闭工具类
 * @author silongz
 *
 */
public class CloseUtils {

	/**
	 * 关闭producer
	 */
	public static void closeProducer(){
		List<Producer<String, String>> allProducers = ProducerHolder.getAllProducers() ;
		if(allProducers != null && allProducers.size() > 0){
			for (Producer<String, String> producer : allProducers) {
				producer.flush();
				producer.close(); 
			}
		}
	}

	/**
	 * 关闭所有consumer
	 */
	public static void closeAllConsumers(){
		List<ConsumerThread> allConsumers = ConsumerHolder.getAllConsumers() ;
		if(allConsumers != null && allConsumers.size() > 0){
			for (ConsumerThread kafkaConsumer : allConsumers) {
				if(kafkaConsumer != null){
					kafkaConsumer.shutdown(); 
				}
			}
		}
		
		List<ConsumerBlockingThread> allBlockingConsumers = ConsumerHolder.getAllBlockingConsumers() ;
		if(allBlockingConsumers != null && allBlockingConsumers.size() > 0){
			for (ConsumerBlockingThread kafkaBlockingConsumer : allBlockingConsumers) {
				if(kafkaBlockingConsumer != null){
					kafkaBlockingConsumer.shutdown(); 
				}
			}
		}
	}
}
