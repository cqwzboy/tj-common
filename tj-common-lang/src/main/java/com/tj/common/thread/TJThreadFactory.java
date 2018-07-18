package com.tj.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 * @author silongz
 *
 */
public class TJThreadFactory implements ThreadFactory
{

	/**
	 * 线程数，原子递增
	 */
	private final AtomicInteger trheadNum = new AtomicInteger(1);

	/**
	 * 线程名前缀
	 */
	private final String prefix;

	/**
	 * 是否守护线程
	 */
	private final boolean isDaemon;

	/**
	 * 线程组
	 */
	private final ThreadGroup threadGroup;

	public TJThreadFactory(String prefix,boolean isDaemon)
	{
		this.prefix = prefix + "-thread-";
		this.isDaemon = isDaemon;
        SecurityManager s = System.getSecurityManager();
        threadGroup = ( s == null ) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
	}

	public Thread newThread(Runnable runnable)
	{
		String name = prefix + trheadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup,runnable,name,0);
        ret.setDaemon(isDaemon);
        return ret;
	}

	public ThreadGroup getThreadGroup()
	{
		return threadGroup;
	}
}