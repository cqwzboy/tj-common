package com.tj.common.activemq;

import java.io.Serializable;

/**
 * 消息体
 * @author silongz
 *
 */
public class MessageBody implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9001777652184199804L;
	
	/**
	 * msgId
	 */
	private String msgId ;
	
	/**
	 * websocket session前缀
	 */
	private String sessionPrefix ;
	
	/**
	 * websocket 客户端id
	 */
	private String clientId ;
	
	/**
	 * websocket消息发送方id
	 */
	private String senderId ;
	
	/**
	 * 消息类型
	 */
	private String msgType ;
	
	/**
	 * 消息内容
	 */
	private String msgContent ;
	
	/**
	 * 如果消息接收端不在线，是否缓存消息
	 */
	private boolean cacheOfflineMsg ;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getSessionPrefix() {
		return sessionPrefix;
	}

	public void setSessionPrefix(String sessionPrefix) {
		this.sessionPrefix = sessionPrefix;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public boolean isCacheOfflineMsg() {
		return cacheOfflineMsg;
	}

	public void setCacheOfflineMsg(boolean cacheOfflineMsg) {
		this.cacheOfflineMsg = cacheOfflineMsg;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
