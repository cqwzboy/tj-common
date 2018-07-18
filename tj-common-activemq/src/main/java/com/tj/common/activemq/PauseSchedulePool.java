package com.tj.common.activemq;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.tj.common.thread.TJThreadFactory;

/**
 * activemq链接暂停后重调度池
 * 
 * @author silongz
 *
 */
public class PauseSchedulePool {

	private final static int THREAD_COUNT = 5;
	
	public static List<String> inPoolMsgKeys = new CopyOnWriteArrayList<String>() ;

	/**
	 * 启动线程池，并设定所有线程为daemon线程
	 */
	private static ScheduledExecutorService reconnectService = Executors.newScheduledThreadPool(THREAD_COUNT,
			new TJThreadFactory("activeMq-pause-reconnect-thread", true));

	public static void put(ReceiveMsgCacheInfo receiveMsgCacheInfo, long pauseTime, TimeUnit unit) {
		if(inPoolMsgKeys.contains(receiveMsgCacheInfo.getMsgKey())){
			return ;
		}
		reconnectService.schedule(new ReconnectThread(receiveMsgCacheInfo), pauseTime, unit);
	}
}
