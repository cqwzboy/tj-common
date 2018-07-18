package com.tj.common.activemq;

import javax.jms.Connection;
import javax.jms.Session;

public class ReceiveMsgCacheInfo {

	private Connection connection;
	
	private Session session;
	
	private String msgKey;
	
	private DeliveryType deliveryType;
	
	private ReceiveCallBack receiveCallBack;
	
	private ReceiveMapMsgCallBack receiveMapMsgCallBack;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

	public DeliveryType getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(DeliveryType deliveryType) {
		this.deliveryType = deliveryType;
	}

	public ReceiveCallBack getReceiveCallBack() {
		return receiveCallBack;
	}

	public void setReceiveCallBack(ReceiveCallBack receiveCallBack) {
		this.receiveCallBack = receiveCallBack;
	}

	public ReceiveMapMsgCallBack getReceiveMapMsgCallBack() {
		return receiveMapMsgCallBack;
	}

	public void setReceiveMapMsgCallBack(ReceiveMapMsgCallBack receiveMapMsgCallBack) {
		this.receiveMapMsgCallBack = receiveMapMsgCallBack;
	}

}
