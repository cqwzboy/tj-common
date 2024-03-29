package com.tj.common.activemq;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * actvieMq template类
 * 
 * @author silongz
 *
 */
public class ActiveMqTemplate {

	private static final Logger logger = LoggerFactory
			.getLogger(ActiveMqTemplate.class);

	/**
	 * jms连接池(消息producer用)
	 */
	private static PooledConnectionFactory pooledConnectionFactory;

	/**
	 * jms连接工厂（消息consumer，长连接）
	 */
	private static ActiveMQConnectionFactory connectionFactory;

	/**
	 * 默认异步发送
	 */
	private static boolean useAsyncSend = true;

	/**
	 * consumer消息预取size
	 */
	private static int PREFETCH_SIZE = 200;
	
	private ActiveMqTemplate() {
	}

	/**
	 * template excute
	 * 
	 * @param role
	 * @param callBack
	 */
	public static String excute(MQRole role, ActiveMqCallBack callBack) {
		if(connectionFactory == null){
			initConnetionPool();
		}
		Connection connection = null;
		if (role == null) {
			throw new IllegalArgumentException("消息角色不能为空");
		}
		if (MQRole.PRODUCER.equals(role)) {
			connection = getProducerConnection();
		} else if (MQRole.CONSUMER.equals(role)) {
			connection = getConsumerConnection();
		}
		if (connection == null) {
			throw new RuntimeException("获取activeMq conneciton为null");
		}
		Session session = getSession(connection);
		String result = callBack.call(connection , session);
		// 消息consumer不需要关闭连接，以保持消息监听
		if (MQRole.PRODUCER.equals(role)) {
			closeSession(session);
		}
		return result ;
	}

	/**
	 * 初始化消息producer连接池
	 */
	private static void initConnetionPool() {
		connectionFactory = new ActiveMQConnectionFactory(ActiveMqConfig.getUserName(), ActiveMqConfig.getPassword(),
				ActiveMqConfig.getBrokerUrl());
		// 默认异步
		connectionFactory.setUseAsyncSend(useAsyncSend);

		pooledConnectionFactory = new PooledConnectionFactory(connectionFactory);
		pooledConnectionFactory.setMaxConnections(ActiveMqConfig.getMaxConnections());
		pooledConnectionFactory
				.setMaximumActiveSessionPerConnection(ActiveMqConfig.getMaxActiveSessionPerConnection());
		pooledConnectionFactory.setExpiryTimeout(60 * 1000);
	}

	/**
	 * 获取producer连接
	 */
	private static Connection getProducerConnection() {
		Connection connection = null;
		// 从连接池工厂中获取一个连接
		try {
			connection = pooledConnectionFactory.createConnection();
			connection.start();
		} catch (JMSException e) {
			logger.error("创建activeMq Connetion出错:{}", e.getMessage());
		}
		return connection;
	}

	/**
	 * 获取consumer连接
	 */
	private static Connection getConsumerConnection() {
		Connection connection = null;
		// 从连接池工厂中获取一个连接
		try {
			connection = connectionFactory.createConnection();
			// activeMQ预取策略
			ActiveMQPrefetchPolicy prefetchPolicy = new ActiveMQPrefetchPolicy();
			prefetchPolicy.setQueuePrefetch(PREFETCH_SIZE);
			((ActiveMQConnection) connection).setPrefetchPolicy(prefetchPolicy);
			connection.setExceptionListener(new ExceptionListener() {
				@Override
				public void onException(JMSException e) {
					logger.error("activeMq consumer获取Connetion出错:{}",
							e.getMessage());
				}
			});
			connection.start();
		} catch (JMSException e) {
			logger.error("创建activeMq consumer Connetion出错:{}", e.getMessage());
		}

		return connection;
	}

	/**
	 * 获取session会话
	 */
	private static Session getSession(Connection connection) {
		// false 参数表示 为非事务型消息，后面的参数表示消息的确认类型
		Session session = null;
		try {
			session = connection.createSession(Boolean.FALSE,
					Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			logger.error("创建activeMq session出错:{}", e.getMessage());
		}
		return session;
	}

	/**
	 * 关闭session会话
	 */
	public static void closeSession(Session session) {
		try {
			if (session != null) {
				session.close();
			}
		} catch (Exception e) {
			logger.error("关闭activeMq session出错:{}", e.getMessage());
		}
	}

	/**
	 * 关闭连接
	 */
	public static void closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.stop();
				connection.close();
			}
		} catch (Exception e) {
			logger.error("关闭activeMq connection出错:{}", e.getMessage());
		}
	}
}
