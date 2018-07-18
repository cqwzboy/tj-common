package com.tj.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Redis管理类
 * 
 * @author silongz
 *
 */
public class RedisTemplate {

	private static final Logger logger = LoggerFactory.getLogger(RedisTemplate.class);

	private static JedisSentinelPool jedisSentinelPool;

	private static JedisPool jedisPool;

	private static JedisPoolConfig jedisPoolConfig;

	/**
	 * 单例私有化构造方法
	 */
	private RedisTemplate() {
	
	}

	/**
	 * 单实例方式，template统一管理connection的open和close，调用方只关注自己要做的事
	 * 
	 * @param redisCallBack
	 */
	public static <T> T excuteBySimple(TJKRedisCallBack redisCallBack) {
		Jedis jedis = null ;
		try {
			if(jedisPool == null){
				initJedisPool();
			}
			jedis = getConnectionBySimple();
			T result = redisCallBack.call(jedis);
			//FIXME 把关闭连接操作移到finally代码块，以防程序异常时，连接资源无法正常释放
//			closeConnection(jedis);
			return result;
		} catch (Exception e) {
			logger.error("redis simple连接操作失败:{}", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			closeConnection(jedis);
		}
	}

	/**
	 * sentinel方式，template统一管理connection的open和close，调用方只关注自己要做的事
	 * 
	 * @param redisCallBack
	 */
	public static <T> T excuteBySentinel(TJKRedisCallBack redisCallBack) {
		Jedis jedis = null ;
		try {
			if(jedisSentinelPool == null){
				initJedisSentinelPool();
			}
			jedis = getConnectionBySentinel();
			T result = redisCallBack.call(jedis);
//			closeConnection(jedis);
			return result;
		} catch (Exception e) {
			logger.error("redis sentinel连接操作失败:{}", e.getMessage());
			throw new RuntimeException(e);
		} finally {
			closeConnection(jedis);
		}
	}

	/**
	 * 获取redis连接
	 * 
	 * @return
	 */
	private static Jedis getConnectionBySimple() {
		if (jedisPool == null) {
			throw new IllegalArgumentException("jedisSentinelPool为空");
		}
		return jedisPool.getResource();
	}

	/**
	 * 获取redis连接
	 * 
	 * @return
	 */
	private static Jedis getConnectionBySentinel() {
		if (jedisSentinelPool == null) {
			throw new IllegalArgumentException("jedisSentinelPool为空");
		}
		return jedisSentinelPool.getResource();
	}

	/**
	 * 关闭连接（还回给连接池）
	 * 
	 * @param jedis
	 */
	private static void closeConnection(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 初始化连接池配置
	 */

	private static void initPoolConfig() {
		jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(RedisConfig.getMaxIdle());
		jedisPoolConfig.setMaxTotal(RedisConfig.getMaxTotal());
		jedisPoolConfig.setMaxWaitMillis(RedisConfig.getMaxWaitMillis());
		jedisPoolConfig.setTestOnBorrow(RedisConfig.isTestOnBorrow());
	}

	/**
	 * 初始化jedis连接池
	 */
	private static void initJedisPool() {
		if(jedisPoolConfig == null){
			initPoolConfig() ;
		}
		jedisPool = new JedisPool(jedisPoolConfig, RedisConfig.getHost(), RedisConfig.getPort(), RedisConfig.getTimeout(),
				RedisConfig.getPassword(), RedisConfig.getDbIndex());
	}

	/**
	 * 初始化jedis连接池
	 */
	private static void initJedisSentinelPool() {
		if(jedisPoolConfig == null){
			initPoolConfig() ;
		}
		jedisSentinelPool = new JedisSentinelPool(RedisConfig.getMasterName(), RedisConfig.getSentinelSet(), jedisPoolConfig,
				RedisConfig.getTimeout(), RedisConfig.getTimeout(), RedisConfig.getPassword(), RedisConfig.getDbIndex());
	}
}
