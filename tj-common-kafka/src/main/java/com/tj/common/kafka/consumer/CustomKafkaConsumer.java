package com.tj.common.kafka.consumer;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定制化的消息消费端
 * 
 * @author silongz
 *
 */
public class CustomKafkaConsumer {

	private ResourceBundle rs;
	
	private static Map<String,ResourceBundle> rsMap = new ConcurrentHashMap<String,ResourceBundle>() ;

	public CustomKafkaConsumer(String propertiesFilePrefix) {
		rs = rsMap.get(propertiesFilePrefix) ;
		if(rs == null){
			rs = ResourceBundle.getBundle(propertiesFilePrefix);
			rsMap.put(propertiesFilePrefix, rs) ;
		}
	}

	/**
	 * 订阅topic消息
	 * 
	 * @param topic
	 * @param callBack
	 */
	public void receiveMsg(String topic, KafkaReceiveMsgCallBack callBack) {
		Thread t = new Thread(new ConsumerThread(topic, rs, callBack));
		t.setName("tj-kafka-receiveMsg-thread-topic" + topic);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * 订阅topic消息,并指定阻塞消息的长度，当阻塞消息数达到blockingSize时，会暂停从kafka消费消息直到被阻塞消息消费完成
	 * 
	 * @param topic
	 * @param blockingSize
	 *            (blockingSize >= 500时才生效，小于500时，默认阻塞500条)
	 * @param callBack
	 */
	public void receiveBlockingMsg(String topic, int blockingSize, KafkaReceiveMsgCallBack callBack) {
		Thread t = new Thread(new ConsumerBlockingThread(topic, blockingSize, rs, callBack));
		t.setName("tj-kafka-receiveBlockingMsg-thread-topic" + topic);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * 指定topic,partition,offset消费消息(该方法不会持续监听，消费完当前所有消息后，监听结束) 注意：相同consumer
	 * group_id已消费过的消息，调用该方法不会重复消费。
	 * 所以调用该方法时，请修改kafka.properties中的group_id，auto_offset_reset必须配置成earliest，
	 * 另外需要说的是，比如1000条消息，第一次从指定offset是500，那么本次消费501到1000条消息，第二次指定offset是0
	 * 会重新把1到1000的消息再消费一次，第三次再指定任何offset都不会再重复消费，因为所有消息都已做了消费确认。
	 * 
	 * @param topic
	 * @param partitionNum
	 * @param offset
	 * @param callBack
	 */
	public void receiveMsgByOffset(String topic, int partitionNum, long offset, KafkaReceiveMsgCallBack callBack) {
		Thread t = new Thread(new OffsetConsumerThread(topic, partitionNum, offset, rs, callBack));
		t.setName("tj-kafka-receiveMsg-by-offset-thread-topic" + topic);
		t.setDaemon(true);
		t.start();
	}

}
