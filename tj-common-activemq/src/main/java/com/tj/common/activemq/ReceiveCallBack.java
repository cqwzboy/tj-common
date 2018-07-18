package com.tj.common.activemq;

/**
 * 客户端接收到消息后的回调接口定义
 * @author silongz
 *
 */
public interface ReceiveCallBack {

	void call(String msg) ;
}
