package com.tj.common.redis;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.tj.common.lang.PropertiesUtils;

/**
 * Redis配置
 * @author silongz
 *
 */
public class RedisConfig {

	private static final String REDIS_PROPERTIES_FILE = "redis.properties";
	
	private static final String SPLIT_SENTINELS_CHAR = ";";
	
	private static Properties prop;
	
	private static final String KEY_HOST = "redis.host";
	private static final String KEY_PORT = "redis.port";
	private static final String KEY_SENTINEL = "redis.sentinel";
	private static final String KEY_MASTER_NAME = "redis.masterName";
	private static final String KEY_TIMEOUT = "redis.timeout";
	private static final String KEY_PASSWORD = "redis.password";
	private static final String KEY_MAX_TOTAL = "redis.maxTotal";
	private static final String KEY_MAX_IDLE = "redis.maxIdle";
	private static final String KEY_MAX_WAIT_MILLIS = "redis.maxWaitMillis";
	private static final String KEY_TEST_ON_BORROW = "redis.testOnBorrow";
	private static final String KEY_DB_INDEX = "redis.dbIndex";
	private static final String KEY_CHARSET = "redis.charset";

	private static String host;
	private static int port = 6379;
	private static String masterName = "tj-master";
	private static Set<String> sentinelSet = new HashSet<String>();
	private static int timeout = 5000 ;
	private static String password;
	private static int maxTotal = 100;
	private static int maxIdle = 20;
	private static long maxWaitMillis = 2000;
	private static boolean testOnBorrow = false;
	private static int dbIndex = 0;
	private static String charset ;
	
	//静态块加载配置
	static {
		prop = PropertiesUtils.loadProperties(REDIS_PROPERTIES_FILE) ;
		host = prop.getProperty(KEY_HOST);
		if(StringUtils.isNotBlank(prop.getProperty(KEY_PORT))){
			port = Integer.parseInt(prop.getProperty(KEY_PORT));
		}
		String sentinel = prop.getProperty(KEY_SENTINEL);
		if(StringUtils.isNotBlank(sentinel)){
			String [] sentinels = sentinel.split(SPLIT_SENTINELS_CHAR) ;
			for (String eachSentinel : sentinels) {
				if(StringUtils.isNotBlank(sentinel)){
					sentinelSet.add(eachSentinel) ;
				}
			}
		}
		if(StringUtils.isNotBlank(prop.getProperty(KEY_MASTER_NAME))){
			masterName = prop.getProperty(KEY_MASTER_NAME);
		}
		if(StringUtils.isNumeric(prop.getProperty(KEY_TIMEOUT))){
			timeout = Integer.parseInt(prop.getProperty(KEY_TIMEOUT));
		}
		password = prop.getProperty(KEY_PASSWORD);
		if(StringUtils.isNumeric(prop.getProperty(KEY_MAX_TOTAL))){
			maxTotal = Integer.parseInt(prop.getProperty(KEY_MAX_TOTAL));
		}
		if(StringUtils.isNumeric(prop.getProperty(KEY_MAX_IDLE))){
			maxIdle = Integer.parseInt(prop.getProperty(KEY_MAX_IDLE));
		}
		if(StringUtils.isNumeric(prop.getProperty(KEY_MAX_WAIT_MILLIS))){
			maxWaitMillis = Long.parseLong(prop.getProperty(KEY_MAX_WAIT_MILLIS));
		}
		if(StringUtils.isNotBlank(prop.getProperty(KEY_TEST_ON_BORROW))){
			testOnBorrow = Boolean.parseBoolean(prop.getProperty(KEY_TEST_ON_BORROW));
		}
		if(StringUtils.isNumeric(prop.getProperty(KEY_DB_INDEX))){
			dbIndex = Integer.parseInt(prop.getProperty(KEY_DB_INDEX));
		}
		charset = prop.getProperty(KEY_CHARSET);
	}

	public static String getHost() {
		return host;
	}

	public static int getPort() {
		return port;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static String getPassword() {
		return password;
	}

	public static int getMaxTotal() {
		return maxTotal;
	}

	public static int getMaxIdle() {
		return maxIdle;
	}

	public static long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public static boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public static int getDbIndex() {
		return dbIndex;
	}

	public static String getMasterName() {
		return masterName;
	}
	
	public static Set<String> getSentinelSet() {
		return sentinelSet;
	}

	public static String getCharset() {
		return charset;
	}
}
