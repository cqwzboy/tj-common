package com.tj.common.activemq.prototype;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tj.common.activemq.ActiveMqCallBack;
import com.tj.common.activemq.DeliveryType;
import com.tj.common.activemq.MQRole;
import com.tj.common.activemq.ReceiveCallBack;
import com.tj.common.activemq.ReceiveMsgCacheInfo;
import com.tj.common.activemq.TextMessageListener;

/**
 * activeMq客户端
 * 
 * @author silongz
 *
 */
public class ActiveMqClient {

	private static final Logger logger = LoggerFactory.getLogger(ActiveMqClient.class);
	
    private Map<String, ReceiveMsgCacheInfo> receiveMsgCache = new ConcurrentHashMap<String, ReceiveMsgCacheInfo>() ;
    
    private ActiveMqTemplate mqTemplate;
    
    public ActiveMqClient(ActiveMqTemplate mqTemplate) {
    	this.mqTemplate = mqTemplate;
    }
    
	/**
	 * 发送消息
	 * PS:如果DeliveryType是TOPIC类型，那么需要consumer先注册监听，再发送消息
	 * @param msgKey
	 * @param msg
	 * @param deliveryType
	 */
	public void sendMsg(final String msgKey, final String msg, final DeliveryType deliveryType) {
		if (StringUtils.isBlank(msgKey)) {
			throw new IllegalArgumentException("消息路由key不能为空");
		}
		if (deliveryType == null) {
			throw new IllegalArgumentException("消息投递类型不能为空");
		}
		mqTemplate.excute(MQRole.PRODUCER, new ActiveMqCallBack() {
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
					message.setText(msg);
					producer.send(message);
				} catch (Exception e) {
					logger.error("activeMq发送消息：{}失败：{}", msgKey, e.getMessage());
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
	public void sendMsg(final String msgKey, final Map<String, Object> msg, final DeliveryType deliveryType) {
		if (StringUtils.isBlank(msgKey)) {
			throw new IllegalArgumentException("消息路由key不能为空");
		}
		if (msg == null) {
			throw new IllegalArgumentException("消息不能为空");
		}
		if (deliveryType == null) {
			throw new IllegalArgumentException("消息投递类型不能为空");
		}
		mqTemplate.excute(MQRole.PRODUCER, new ActiveMqCallBack() {
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
					producer.send(mapMessage);
				} catch (Exception e) {
					logger.error("activeMq发送消息：{}失败：{}", msgKey, e.getMessage());
				}
				return null;
			}
		});
	}

	/**
	 * 接收消息
	 * 
	 * @param msgKey
	 * @param receiveCallBack
	 */
	public String receiveMsg(final String msgKey, final DeliveryType deliveryType, final ReceiveCallBack receiveCallBack) {
		if (StringUtils.isBlank(msgKey)) {
			throw new IllegalArgumentException("消息路由key不能为空");
		}
		return mqTemplate.excute(MQRole.CONSUMER, new ActiveMqCallBack() {
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
				}
				return null;
			}
		});
	}

}
