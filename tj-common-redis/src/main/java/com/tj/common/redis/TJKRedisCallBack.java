package com.tj.common.redis;

import redis.clients.jedis.Jedis;

/**
 * TJKRedisTemplate的回调接口
 * @author silongz
 *
 */
public interface TJKRedisCallBack {

	/**
	 * 获取到redis连接后需要执行的操作
     * @param jedis
     */
	<T> T call(Jedis jedis) ;
}
