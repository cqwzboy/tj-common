package com.tj.common.activemq;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文本消息监听器
 * @author silongz
 *
 */
public class MapMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger("mq");
	
	private ReceiveMapMsgCallBack callBack ;
	
	public MapMessageListener(ReceiveMapMsgCallBack callBack){
		this.callBack = callBack ;
	}
	
	@Override
	public void onMessage(Message message) {
		MapMessage msg = null ;
		Map<String,Object> mapMsg = new HashMap<String,Object>() ;
		if(message instanceof MapMessage){
			try {
				msg = (MapMessage)message ;
				@SuppressWarnings("unchecked")
				Enumeration<String> mapKeys = msg.getMapNames() ;
				while(mapKeys != null && mapKeys.hasMoreElements()){
					String key = mapKeys.nextElement() ;
					Object value = msg.getObject(key) ;
					mapMsg.put(key, value) ;
				}
				callBack.call(mapMsg);
			} catch (JMSException e) {
				logger.error("activeMq接收处理消息出错："+e.getMessage());
			}
		}

	}

}
