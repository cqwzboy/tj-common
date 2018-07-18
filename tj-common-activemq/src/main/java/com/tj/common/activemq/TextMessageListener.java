package com.tj.common.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文本消息监听器
 * @author silongz
 *
 */
public class TextMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger("mq");
	
	private ReceiveCallBack callBack ;
	
	public TextMessageListener(ReceiveCallBack callBack){
		this.callBack = callBack ;
	}
	
	@Override
	public void onMessage(Message message) {
		String msg = "" ;
		if(message instanceof TextMessage){
			try {
				msg = ((TextMessage)message).getText() ;
				callBack.call(msg);
			} catch (JMSException e) {
				logger.error("activeMq接收处理消息出错："+e.getMessage());
			}
		}

	}

}
