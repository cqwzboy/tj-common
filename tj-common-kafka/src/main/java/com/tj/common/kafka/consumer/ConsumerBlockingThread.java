package com.tj.common.kafka.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tj.common.thread.TJThreadFactory;

/**
 * 每个topic对应一个consumer线程
 * @author silongz
 *
 */
public class ConsumerBlockingThread implements Runnable{

	private Logger logger = LoggerFactory.getLogger(ConsumerBlockingThread.class) ;
	
	//全局停止接收kafka消息的开关
	private final AtomicBoolean closed = new AtomicBoolean(false);
	
	//阻塞队列是否已经取空的标识
	private final AtomicBoolean isEmpty = new AtomicBoolean(false);
	
	//当前consumer是否已暂停
	private final AtomicBoolean isPaused = new AtomicBoolean(false);
	
	private KafkaConsumer<String, String> consumer ;
	
	private ResourceBundle rs ;
	
	private String topic ;
	
	//默认阻塞队列大小
	private int blockingSize = 500 ;
	
	private KafkaReceiveMsgCallBack callBack ;
	
	private BlockingQueue<ConsumerRecord<String, String>> blockRecords ;
	
	private ScheduledExecutorService executorService ;
	
	public ConsumerBlockingThread(String topic, int blockingSize, ResourceBundle rs, KafkaReceiveMsgCallBack callBack){
		this.topic = topic ;
		this.callBack = callBack ;
		if(blockingSize > blockingSize){
			this.blockingSize = blockingSize ;
		}
		this.rs = rs ;
	}
	
	@Override
	public void run() {
		consumer = ConsumerBuilder.buildNew(rs,false);
		consumer.subscribe(Arrays.asList(topic));
		ConsumerHolder.addBlockingConsumer(this);
		
		sendBlockMsg();
		
		while (!closed.get()) {
			try {
				pollMsgFromKafka();
			} catch (Exception e) {
				logger.error(String.format("阻塞消息topic【%s】,消费异常", topic),e);
			}
		}
	}

	/**
	 * 从kafka中拉取消息
	 * @param blockFlag
	 */
	private void pollMsgFromKafka() {
		ConsumerRecords<String, String> records = consumer.poll(100);
		
		if(blockRecords == null){
			blockRecords = new LinkedBlockingQueue<ConsumerRecord<String, String>>(blockingSize) ;
		}
		if(records.count() > 0){
			for (ConsumerRecord<String, String> record : records) {
				boolean isNotFull = blockRecords.offer(record);
				if(!isNotFull && !isPaused.get()){
					consumer.pause(getTopicPatitions());
					isPaused.set(true);
					logger.debug("阻塞队列已满，暂停从kafka接收消息") ;
				}
				if(!isNotFull){
					try {
						blockRecords.put(record);
					} catch (InterruptedException e) {
						logger.debug("阻塞队列已满，重新put等待中断，直接消费");
						callBack.call(record.key(), record.value());
					}
				}
			}
			//一个批次处理完成后才提交offset
			consumer.commitSync();
		}
		//如果阻塞队列已经取空，则新恢复从kafka消费数据
		if(isEmpty.get() && isPaused.get()){
			consumer.resume(getTopicPatitions());
			isEmpty.set(false);
			isPaused.set(false);
			logger.debug("阻塞队列已空，恢复从kafka接收消息") ;
		}
	}

	/**
	 * 发送阻塞队列中的消息
	 */
	private void sendBlockMsg() {
		executorService = Executors.newScheduledThreadPool(1, new TJThreadFactory("send-blocking-records-" + topic,
				true));
		executorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (blockRecords != null) {
					ConsumerRecord<String, String> record = blockRecords.poll();
					if (record != null) {
						callBack.call(record.key(), record.value());
					} else if (record == null && !isEmpty.get()) {
						isEmpty.set(true);
						logger.debug("阻塞队列中的消息已经取完，重置为空");
					}
				}
			}
		}, 1000, 1, TimeUnit.MILLISECONDS);
	}

	/**
	 * 关闭consumer
	 */
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
        executorService.shutdown();
        ConsumerRecord<String, String> record = null ;
        while((record = blockRecords.poll()) != null){
        	callBack.call(record.key(), record.value());
        }
    }
	
    /**
     * 获取当前topic的partitions
     * @return
     */
    private List<TopicPartition> getTopicPatitions() {
		List<PartitionInfo> partitions = consumer.partitionsFor(topic) ;
		List<TopicPartition> topicPartitions = new ArrayList<TopicPartition>() ;
		for (PartitionInfo partitionInfo : partitions) {
			TopicPartition tp = new TopicPartition(topic, partitionInfo.partition()) ;
			topicPartitions.add(tp) ;
		}
		return topicPartitions;
	}
}
