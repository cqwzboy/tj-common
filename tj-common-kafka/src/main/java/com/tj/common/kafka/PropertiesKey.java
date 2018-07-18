package com.tj.common.kafka;

/**
 * 属性文件key
 * @author silongz
 *
 */
public class PropertiesKey {

	/**
	 * kafka consumer分组id
	 */
	public static final String GROUP_ID = "group_id" ;
	
	/**
	 * offset不存在时，重置为何种状态
	 */
	public static final String AUTO_OFFSET_RESET = "auto_offset_reset" ;
	
	/**
	 * 初始化连接的brokers，ip:port,ip:port的格式，不用把所有broker都配置出来，只需配置一个即可，初始化后kafka本身可以自动发现所有broker
	 */
	public static final String BOOTSTRAP_BROKERS = "bootstrap_brokers" ;
	
	/**
	 * 每次poll的最大数量
	 */
	public static final String MAX_POLL_RECORDS = "max_poll_records" ;
	
	/**
	 * session过期时间
	 */
	public static final String SESSION_TIMEOUT_MS = "session_timeout_ms" ;
	
}
