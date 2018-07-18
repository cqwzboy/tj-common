package com.tj.common.activemq;

import javax.jms.Connection;
import javax.jms.Session;

/**
 * ActiveMq的回调接口
 * @author silongz
 *
 */
public interface ActiveMqCallBack {

	/**
	 * 获取到jms session连接后需要执行的操作
	 * @param jedis
	 */
	String call(Connection connection , Session session) ;
}
