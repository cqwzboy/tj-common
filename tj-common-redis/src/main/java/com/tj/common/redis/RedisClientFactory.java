package com.tj.common.redis;

import com.tj.common.redis.sentinel.RedisSentinelClient;
import com.tj.common.redis.simple.RedisSimpleClient;

/**
 * 获取Redis客户端实例的工厂类
 * @author silongz
 *
 */
public class RedisClientFactory {

	/**
	 * 获取客户端实例
	 * @param redisClientType
	 * @return
	 */
	public static RedisClient getClient(RedisClientType redisClientType){
		switch (redisClientType) {
		case SIMPLE:
			return new RedisSimpleClient() ;
		case SENTINEL:
			return new RedisSentinelClient() ;
		default:
			break;
		}
		return null ;
	}
}
