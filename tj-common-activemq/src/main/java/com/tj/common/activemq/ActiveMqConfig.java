package com.tj.common.activemq;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.tj.common.lang.PropertiesUtils;

/**
 * activeMq配置
 * @author silongz
 *
 */
public class ActiveMqConfig {

	/**
	 * activeMq配置文件
	 */
	private final static String ACTIVEMQ_PROPERTIES = "activemq.properties";

	private static Properties prop;

	private static String KEY_BROKER_URL = "broker_url";
	private static String KEY_USER_NAME = "user_name";
	private static String KEY_PASSWORD = "password";
	private static String KEY_MAX_CONNECTIONS = "max_connections";
	private static String KEY_MAX_ACTIVE_SESSION_PER_CONNECTION = "max_active_session_per_connection";

	private static String brokerUrl;
	private static String userName;
	private static String password;
	private static int maxConnections = 10;
	private static int maxActiveSessionPerConnection = 50;

	static {
		prop = PropertiesUtils.loadProperties(ACTIVEMQ_PROPERTIES) ;
		brokerUrl = prop.getProperty(KEY_BROKER_URL);
		userName = prop.getProperty(KEY_USER_NAME);
		password = prop.getProperty(KEY_PASSWORD);
		if (StringUtils.isNumeric(prop.getProperty(KEY_MAX_CONNECTIONS))) {
			maxConnections = Integer.parseInt(prop.getProperty(KEY_MAX_CONNECTIONS));
		}
		if (StringUtils.isNumeric(prop.getProperty(KEY_MAX_ACTIVE_SESSION_PER_CONNECTION))) {
			maxConnections = Integer.parseInt(prop.getProperty(KEY_MAX_ACTIVE_SESSION_PER_CONNECTION));
		}
	}

	public static String getBrokerUrl() {
		return brokerUrl;
	}

	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static int getMaxConnections() {
		return maxConnections;
	}

	public static int getMaxActiveSessionPerConnection() {
		return maxActiveSessionPerConnection;
	}

}
