package com.tj.common.kafka.consumer;

import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.tj.common.kafka.PropertiesKey;

/**
 * consumer构造器,KafkaConsumer是非线程安全模型，所以每一个topic的每一次订阅都对应一个consumer
 * @author silongz
 *
 */
public class ConsumerBuilder {

	public final static int DEFAULT_MAX_POLL_SIZE = 1000 ;
	
	public final static int DEFAULT_SESSION_TIMEOUT = 30 * 1000 ;
	
	private ConsumerBuilder(){
		
	}
	
	public static KafkaConsumer<String, String> buildNew(ResourceBundle rs , boolean autoCommit) {
		 Properties props = new Properties();
		 props.put("bootstrap.servers", rs.getString(PropertiesKey.BOOTSTRAP_BROKERS));
		 props.put("group.id", rs.getString(PropertiesKey.GROUP_ID));
	     props.put("enable.auto.commit", String.valueOf(autoCommit));
	     props.put("auto.offset.reset", rs.getString(PropertiesKey.AUTO_OFFSET_RESET));
	     if(rs.containsKey(PropertiesKey.MAX_POLL_RECORDS)){
	    	 props.put("max.poll.records", rs.getString(PropertiesKey.MAX_POLL_RECORDS));
	     }else{
	    	 props.put("max.poll.records", DEFAULT_MAX_POLL_SIZE);
	     }
	     int sessionTimeOut = DEFAULT_SESSION_TIMEOUT ;
	     if(rs.containsKey(PropertiesKey.SESSION_TIMEOUT_MS)  && StringUtils.isNumeric(rs.getString(PropertiesKey.SESSION_TIMEOUT_MS))){
	    	 sessionTimeOut = Integer.parseInt(rs.getString(PropertiesKey.SESSION_TIMEOUT_MS)) ;
	     }
	     props.put("session.timeout.ms", sessionTimeOut);
	     props.put("request.timeout.ms", sessionTimeOut + 1);
	     props.put("key.deserializer", StringDeserializer.class.getName());
	     props.put("value.deserializer", StringDeserializer.class.getName());
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     return consumer ;
	}
}
