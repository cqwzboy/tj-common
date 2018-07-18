package com.tj.common.activemq.prototype;

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
	private static Properties prop;

	private static String KEY_BROKER_URL = "broker_url";
	private static String KEY_USER_NAME = "user_name";
	private static String KEY_PASSWORD = "password";
	private static String KEY_MAX_CONNECTIONS = "max_connections";
	private static String KEY_MAX_ACTIVE_SESSION_PER_CONNECTION = "max_active_session_per_connection";

	private String brokerUrl;
	private String userName;
	private String password;
	private int maxConnections = 10;
	private int maxActiveSessionPerConnection = 50;

	public ActiveMqConfig(String configPath) {
		prop = PropertiesUtils.loadProperties(configPath) ;
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

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public int getMaxActiveSessionPerConnection() {
		return maxActiveSessionPerConnection;
	}

}
