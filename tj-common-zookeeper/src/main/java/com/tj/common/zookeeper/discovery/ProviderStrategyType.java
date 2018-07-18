package com.tj.common.zookeeper.discovery;

/**
 * 服务发现策略类型
 * @author silongz
 *
 */
public enum ProviderStrategyType {

	/**
	 * 随机
	 */
	RANDOM ,
	
	/**
	 * 轮询均分
	 */
	ROUND_ROBIN , 
	
	/**
	 * 保持（第一次随机分配一台服务器，以后每次调用请求都发往同一台服务器）
	 */
	STICKY ;
}
