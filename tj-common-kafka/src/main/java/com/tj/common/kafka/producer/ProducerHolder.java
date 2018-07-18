package com.tj.common.kafka.producer;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.kafka.clients.producer.Producer;

/**
 * producer持有
 * @author silongz
 *
 */
public class ProducerHolder {

	private static Map<String, Producer<String, String>> allProducers = new ConcurrentHashMap<String,Producer<String, String>>() ;
	
	public static void put(String propertiesFilePrefix,Producer<String, String> producer){
		allProducers.put(propertiesFilePrefix, producer) ;
	}
	
	public static Producer<String, String> get(String propertiesFilePrefix) {
		return allProducers.get(propertiesFilePrefix) ;
	}
	
	public static List<Producer<String, String>> getAllProducers(){
		List<Producer<String, String>> list = new CopyOnWriteArrayList<Producer<String,String>>() ;
		for (Entry<String, Producer<String, String>> entry : allProducers.entrySet()) {
			list.add(entry.getValue()) ;
		}
		return list ;
	}
}
