package com.tj.common.kafka.consumer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 所有topic的consumer持有者
 * @author silongz
 *
 */
public class ConsumerHolder {

	private static List<ConsumerThread> consumers = new CopyOnWriteArrayList<ConsumerThread>();
	
	private static List<ConsumerBlockingThread> blockingConsumers = new CopyOnWriteArrayList<ConsumerBlockingThread>();
	
	/**
	 * 缓存consumer
	 * @param groupAndTopic
	 * @param executor
	 */
	public static void addConsumer(ConsumerThread consumer) {
		consumers.add(consumer) ;
	}
	
	/**
	 * 缓存consumer
	 * @param groupAndTopic
	 * @param executor
	 */
	public static void addBlockingConsumer(ConsumerBlockingThread consumer) {
		blockingConsumers.add(consumer) ;
	}

	/**
	 * 获取所有Consumer
	 * @return
	 */
    public static List<ConsumerThread> getAllConsumers(){
    	return consumers ;
    }
    
    /**
     * 获取所有Consumer
     * @return
     */
    public static List<ConsumerBlockingThread> getAllBlockingConsumers(){
    	return blockingConsumers ;
    }
}
