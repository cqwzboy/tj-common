package com.tj.common.kafka.consumer;

import com.tj.common.kafka.producer.CustomKafkaProducer;

/**
 * 定制化客户端工厂类
 * 
 * @author silongz
 *
 */
public class TjKafkaFactory {

	/**
	 * 获取定制化的消费客户端
	 * @param propertiesFilePrefix -- 配置文件名称前缀
	 * @return
	 */
	public static CustomKafkaConsumer getCustomKafkaConsumer(String propertiesFilePrefix) {
		return new CustomKafkaConsumer(propertiesFilePrefix) ;
	}
	
	/**
	 * 获取定制化的生产客户端
	 * @param propertiesFilePrefix -- 配置文件名称前缀
	 * @return
	 */
	public static CustomKafkaProducer getCustomKafkaProducer(String propertiesFilePrefix) {
		return new CustomKafkaProducer(propertiesFilePrefix) ;
	}
}
