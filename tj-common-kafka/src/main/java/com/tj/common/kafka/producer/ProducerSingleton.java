package com.tj.common.kafka.producer;

import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.tj.common.kafka.PropertiesKey;

/**
 * producer单例(一个配置文件对应一个单列)，KafkaProducer是线程安全模型，所以使用单例模式
 * 
 * @author silongz
 *
 */
public class ProducerSingleton {

	/**
	 * 获取消息发送客户端的单实例
	 * @return
	 */
	public static Producer<String, String> getInstance(String propertiesFilePrefix) {
		Producer<String, String> producer = ProducerHolder.get(propertiesFilePrefix) ;
		if(producer == null){
			ResourceBundle rs = ResourceBundle.getBundle(propertiesFilePrefix) ;
			// 完整的配置项，请参考：http://kafka.apache.org/documentation.html#producerconfigs
			Properties props = new Properties();
			props.put("bootstrap.servers", rs.getString(PropertiesKey.BOOTSTRAP_BROKERS));
			props.put("key.serializer", StringSerializer.class.getName());
			props.put("value.serializer", StringSerializer.class.getName());
			props.put("compression.type", "gzip");
	        props.put("linger.ms", "5");
	        props.put("acks", "all");
	        props.put("retries ", 3);
	        //当消息因为网络波动发生丢失时，可以开启重连和重试时间配置，（需要注意的是，这2个配置项开启后，每次发送时，发送延迟也会比较大）
//	        props.put("reconnect.backoff.ms", 20000);
//	        props.put("retry.backoff.ms", 20000);
			producer = new KafkaProducer<>(props); 
			ProducerHolder.put(propertiesFilePrefix, producer);
		}
		return producer ;
	}
	
}
