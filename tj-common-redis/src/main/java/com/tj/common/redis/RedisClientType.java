package com.tj.common.redis;

public enum RedisClientType {
	/**
	 * 单实例直连方式
	 */
	SIMPLE , 
	/**
	 * SENTINEL方式
	 */
	SENTINEL ;
}
