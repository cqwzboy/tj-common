package com.tj.common.activemq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageFormatException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.broker.scheduler.CronParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * activeMq客户端
 * 
 * @author silongz
 *
 */
public class ActiveMqClient {

	private static final Logger logger = LoggerFactory.getLogger(ActiveMqClient.class);
	
    private static Map<String, ReceiveMsgCacheInfo> receiveMsgCache = new ConcurrentHashMap<String, ReceiveMsgCacheInfo>() ;
    
	/**
	 * 发送消息
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 */
	public static void sendMsg(String msgKey, String msg, DeliveryType deliveryType) {
		sendTextMsg(msgKey, msg, deliveryType, 0, null);
	}
	
	/**
	 * 延迟发送消息(必须要先在broker配置xml文件中设置schedulerSupport=true才会生效)
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 * @param delay 
	 * @see http://activemq.apache.org/delay-and-schedule-message-delivery.html
	 */
	public static void sendDelayMsg(String msgKey, String msg, DeliveryType deliveryType , long delay) {
		sendTextMsg(msgKey, msg, deliveryType, delay, null);
	}
	
	/**
	 * 指定cronTab表达式发送text消息
	 * 注意：该接口调用几次，服务器就会调度几次（比如：第一次调用时，
	 * 设置一分钟调度一次，后面又调用一次，设置5分钟调度一次，那么2次调用就会开启2次调度，
	 * 如果想修改调度时间或者停止调度，请直接去mq控制台删除该queue或者删除原有的调度设置）
	 * (* * * * *)
	 *  分 时 日 月 周　
     * 第1列表示分钟1～59 
     * 第2列表示小时1～23（0表示0点）
     * 第3列表示日期1～31
     * 第4列表示月份1～12
     * 第5列标识号星期0～6（0表示星期天）
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 * @param delay 
	 */
	public static void sendCronTabMsg(String msgKey, String msg, DeliveryType deliveryType , String cronTab) {
		validateCronTab(cronTab);
		sendTextMsg(msgKey, msg, deliveryType, 0, cronTab);
	}

	/**
	 * 发送Map消息
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 */
	public static void sendMsg(String msgKey, Map<String, Object> msg, DeliveryType deliveryType) {
		sendMapMsg(msgKey, msg, deliveryType, 0, null);
	}
	
	/**
	 * 发送Map消息
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 */
	public static void sendDelayMsg(String msgKey, Map<String, Object> msg, DeliveryType deliveryType, long delay) {
		sendMapMsg(msgKey, msg, deliveryType, delay, null);
	}
	
	/**
	 * 指定cronTab表达式发送map消息
	 * 注意：该接口调用几次，服务器就会调度几次（比如：第一次调用时，
	 * 设置一分钟调度一次，后面又调用一次，设置5分钟调度一次，那么2次调用就会开启2次调度，
	 * 如果想修改调度时间或者停止调度，请直接去mq控制台删除该queue或者删除原有的调度设置）
	 * (* * * * *)
	 *  分 时 日 月 周　
     * 第1列表示分钟1～59 
     * 第2列表示小时1～23（0表示0点）
     * 第3列表示日期1～31
     * 第4列表示月份1～12
     * 第5列标识号星期0～6（0表示星期天）
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 * @param delay 
	 */
	public static void sendCronTabMsg(String msgKey, Map<String, Object> msg, DeliveryType deliveryType, String cronTab) {
		validateCronTab(cronTab);
		sendMapMsg(msgKey, msg, deliveryType, 0, cronTab);
	}

	/**
	 * 接收消息
	 * 
	 * @param msgKey
	 * @param receiveCallBack
	 */
	public static String receiveMsg(final String msgKey, final DeliveryType deliveryType, final ReceiveCallBack receiveCallBack) {
		if (StringUtils.isBlank(msgKey)) {
			throw new IllegalArgumentException("消息路由key不能为空");
		}
		return ActiveMqTemplate.excute(MQRole.CONSUMER, new ActiveMqCallBack() {
			@Override
			public String call(Connection connection , Session session) {
				try {
					if (DeliveryType.QUEUE.equals(deliveryType)) {
						Destination destination = session.createQueue(msgKey);
						MessageConsumer consumer = session.createConsumer(destination);
						consumer.setMessageListener(new TextMessageListener(receiveCallBack));
					} else if (DeliveryType.TOPIC.equals(deliveryType)) {
						Topic topic = session.createTopic(msgKey);
						MessageConsumer consumer = session.createConsumer(topic);
						consumer.setMessageListener(new TextMessageListener(receiveCallBack));
					}
					ReceiveMsgCacheInfo receiveMsgCacheInfo = new ReceiveMsgCacheInfo() ;
					receiveMsgCacheInfo.setConnection(connection);
					receiveMsgCacheInfo.setSession(session);
					receiveMsgCacheInfo.setMsgKey(msgKey);
					receiveMsgCacheInfo.setDeliveryType(deliveryType);
					receiveMsgCacheInfo.setReceiveCallBack(receiveCallBack);
					receiveMsgCache.put(msgKey, receiveMsgCacheInfo) ;
				} catch (Exception e) {
					logger.error("activeMq接收消息：{}失败：{}", msgKey, e.getMessage());
					throw new RuntimeException("接收test消息失败", e) ;
				}
				return null;
			}
		});
	}
	
	/**
	 * 接收map消息
	 * @param msgKey
	 * @param deliveryType
	 * @param receiveMapMsgCallBack
	 * @return
	 */
	public static String receiveMapMsg(final String msgKey, final DeliveryType deliveryType, final ReceiveMapMsgCallBack receiveMapMsgCallBack) {
		if (StringUtils.isBlank(msgKey)) {
			throw new IllegalArgumentException("消息路由key不能为空");
		}
		return ActiveMqTemplate.excute(MQRole.CONSUMER, new ActiveMqCallBack() {
			@Override
			public String call(Connection connection , Session session) {
				try {
					if (DeliveryType.QUEUE.equals(deliveryType)) {
						Destination destination = session.createQueue(msgKey);
						MessageConsumer consumer = session.createConsumer(destination);
						consumer.setMessageListener(new MapMessageListener(receiveMapMsgCallBack));
					} else if (DeliveryType.TOPIC.equals(deliveryType)) {
						Topic topic = session.createTopic(msgKey);
						MessageConsumer consumer = session.createConsumer(topic);
						consumer.setMessageListener(new MapMessageListener(receiveMapMsgCallBack));
					}
					ReceiveMsgCacheInfo receiveMsgCacheInfo = new ReceiveMsgCacheInfo() ;
					receiveMsgCacheInfo.setConnection(connection);
					receiveMsgCacheInfo.setSession(session);
					receiveMsgCacheInfo.setMsgKey(msgKey);
					receiveMsgCacheInfo.setDeliveryType(deliveryType);
					receiveMsgCacheInfo.setReceiveMapMsgCallBack(receiveMapMsgCallBack);
					receiveMsgCache.put(msgKey, receiveMsgCacheInfo) ;
				} catch (Exception e) {
					logger.error("activeMq接收消息：{}失败：{}", msgKey, e.getMessage());
					throw new RuntimeException("接收map消息失败", e) ;
				}
				return null;
			}
		});
	}

	/**
	 * 暂停接收消息
	 * @param msgKey 指定的消息路由key
	 * @param pauseTime 暂停时长（如果值为负数，则永久停止）
	 * @param unit 时间单位
	 */
    public static void pauseReceiveMsg(String msgKey , long pauseTime , TimeUnit unit) {
    	ReceiveMsgCacheInfo receiveMsgCacheInfo = receiveMsgCache.get(msgKey) ;
    	if(receiveMsgCacheInfo == null){
    		return ;
    	}
    	if(receiveMsgCacheInfo.getSession() != null){
    		ActiveMqTemplate.closeSession(receiveMsgCacheInfo.getSession());
    	}
    	if(receiveMsgCacheInfo.getConnection() != null){
    		ActiveMqTemplate.closeConnection(receiveMsgCacheInfo.getConnection());
    	}
    	if(pauseTime > 0){
    		PauseSchedulePool.put(receiveMsgCacheInfo, pauseTime, unit);
    	}
    }
    
    /**
     * 发送文本消息
     * @param msgKey
     * @param msg
     * @param deliveryType
     * @param delay
     * @param cronTab
     */
	private static void sendTextMsg(final String msgKey, final String msg, final DeliveryType deliveryType, final long delay, final String cronTab) {
		if (StringUtils.isBlank(msgKey)) {
			throw new IllegalArgumentException("消息路由key不能为空");
		}
		if (deliveryType == null) {
			throw new IllegalArgumentException("消息投递类型不能为空");
		}
		ActiveMqTemplate.excute(MQRole.PRODUCER, new ActiveMqCallBack() {
			@Override
			public String call(Connection connection , Session session) {
				try {
					Destination destination = null;
					if (DeliveryType.QUEUE.equals(deliveryType)) {
						destination = session.createQueue(msgKey);
					} else if (DeliveryType.TOPIC.equals(deliveryType)) {
						destination = session.createTopic(msgKey);
					}
					MessageProducer producer = session.createProducer(destination);
					producer.setDeliveryMode(DeliveryMode.PERSISTENT);
					TextMessage message = session.createTextMessage();
					if(delay > 0){
						message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
					}
					if(StringUtils.isNotBlank(cronTab)){
						message.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, cronTab);
					}
					message.setText(msg);
					producer.send(message);
				} catch (Exception e) {
					logger.error("activeMq发送消息：{}失败：{}", msgKey, e.getMessage());
					throw new RuntimeException("发送text消息失败", e) ;
				}
				return null;
			}
		});
	}
	
	/**
	 * 发送Map消息
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 */
	private static void sendMapMsg(final String msgKey, final Map<String, Object> msg, final DeliveryType deliveryType, final long delay, final String cronTab) {
		if (StringUtils.isBlank(msgKey)) {
			throw new IllegalArgumentException("消息路由key不能为空");
		}
		if (msg == null) {
			throw new IllegalArgumentException("消息不能为空");
		}
		if (deliveryType == null) {
			throw new IllegalArgumentException("消息投递类型不能为空");
		}
		ActiveMqTemplate.excute(MQRole.PRODUCER, new ActiveMqCallBack() {
			@Override
			public String call(Connection connection , Session session) {
				try {
					Destination destination = null;
					if (DeliveryType.QUEUE.equals(deliveryType)) {
						destination = session.createQueue(msgKey);
					} else if (DeliveryType.TOPIC.equals(deliveryType)) {
						destination = session.createTopic(msgKey);
					}
					MessageProducer producer = session.createProducer(destination);
					producer.setDeliveryMode(DeliveryMode.PERSISTENT);
					MapMessage mapMessage = session.createMapMessage();
					for (String key : msg.keySet()) {
						mapMessage.setObject(key, msg.get(key));
					}
					if(delay > 0){
						mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
					}
					if(StringUtils.isNotBlank(cronTab)){
						mapMessage.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, cronTab);
					}
					producer.send(mapMessage);
				} catch (Exception e) {
					logger.error("activeMq发送消息：{}失败：{}", msgKey, e.getMessage());
					throw new RuntimeException("发送map消息失败", e) ;
				}
				return null;
			}
		});
	}

	/**
	 * 验证cronTab是否合法
	 * @param cronTab
	 */
	private static void validateCronTab(final String cronTab) {
		if(StringUtils.isBlank(cronTab)){
			throw new IllegalArgumentException("cron表达式不能为空");
		}
		try {
			CronParser.getNextScheduledTime(cronTab, System.currentTimeMillis()) ;
		} catch (MessageFormatException e1) {
			throw new IllegalArgumentException("cron表达式格式错误");
		}
	}

}
