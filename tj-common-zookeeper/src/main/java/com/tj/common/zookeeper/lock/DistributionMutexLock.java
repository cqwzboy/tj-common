package com.tj.common.zookeeper.lock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import com.tj.common.zookeeper.base.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分布式排它锁
 * @author silongz
 *
 */
public class DistributionMutexLock<T> {

	private Logger logger = LoggerFactory.getLogger(DistributionMutexLock.class) ;

	private final static String SLASH = "/" ;
	
	private static Map<String,InterProcessMutex> map = new ConcurrentHashMap<String,InterProcessMutex>() ;
	
	/**
	 * 系统编码
	 */
	private String systemCode ;
	
	/**
	 * 锁的名称
	 */
	private String lockName ;
	
	public DistributionMutexLock(String systemCode , String lockName){
		if(StringUtils.isBlank(systemCode) || StringUtils.isBlank(lockName)){
			throw new IllegalArgumentException("systemCode和lockName都不能为空") ;
		}
		this.systemCode = systemCode ;
		this.lockName = lockName ;
	}
	
	public T lock(LockCallBack<T> callBack) throws Exception {
		InterProcessMutex lock = getLock() ;
		try {
			lock.acquire();
		} catch (Exception e) {
			logger.error("获取系统【"+systemCode+"】的分布式锁【" +lockName+"】失败",e);
			throw new RuntimeException("获取系统【"+systemCode+"】的分布式锁【" +lockName+"】失败" + e.getMessage(),e) ;
		}
		try {
			return callBack.call() ;
		} catch (Exception e) {
			logger.error("系统【"+systemCode+"】的分布式锁【" +lockName+"】回调方法执行失败",e);
			throw new RuntimeException("系统【"+systemCode+"】的分布式锁【" +lockName+"】回调方法执行失败" + e.getMessage(),e) ;
		}
		finally {
			lock.release();
		}
	}
	
	/**
	 * 构建排它锁实例
	 * @return
	 */
	private InterProcessMutex getLock(){
		String key = SLASH.concat(systemCode).concat(SLASH).concat(lockName) ;
		if(map.containsKey(key)){
			return map.get(key) ;
		}else{
			InterProcessMutex lock = new InterProcessMutex(ZkClient.getClient(), key);
			map.put(key, lock) ;
			return lock ;
		}
	}
	
	
}
