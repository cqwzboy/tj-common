package com.tj.common.activemq;

import java.util.concurrent.Callable;

/**
 * 重连线程
 * 
 * @author silongz
 *
 */
public class ReconnectThread implements Callable<Void> {
	private ReceiveMsgCacheInfo receiveMsgCacheInfo;

	public ReconnectThread(ReceiveMsgCacheInfo receiveMsgCacheInfo) {
		this.receiveMsgCacheInfo = receiveMsgCacheInfo;
	}

	@Override
	public Void call() throws Exception {
		if(receiveMsgCacheInfo.getReceiveCallBack() != null){
			ActiveMqClient.receiveMsg(receiveMsgCacheInfo.getMsgKey(), receiveMsgCacheInfo.getDeliveryType(),
					receiveMsgCacheInfo.getReceiveCallBack());
		}
		if(receiveMsgCacheInfo.getReceiveMapMsgCallBack() != null){
			ActiveMqClient.receiveMapMsg(receiveMsgCacheInfo.getMsgKey(), receiveMsgCacheInfo.getDeliveryType(),
					receiveMsgCacheInfo.getReceiveMapMsgCallBack());
		}
		//重新监听消息之后，从待连接池里面删除该消息key
		PauseSchedulePool.inPoolMsgKeys.remove(receiveMsgCacheInfo.getMsgKey()) ;
		return null;
	}

}
