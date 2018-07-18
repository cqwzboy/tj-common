package com.tj.common.zookeeper;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.tj.common.thread.TJThreadFactory;

public class MqTest {

	private static final BlockingDeque<String> b = new LinkedBlockingDeque<String>(10) ;
	
	private static final AtomicInteger nodeId = new AtomicInteger(0) ;
	
	public static void main(String[] args) {
		//扫描dubbo节点
		ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor(new TJThreadFactory("监控dubbo节点", true)) ;
		es.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					b.offerLast("dubbo服务名"+ nodeId.addAndGet(1), 1, TimeUnit.SECONDS) ;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 0, 5, TimeUnit.SECONDS) ;
		
		//预警dubbo节点
		ScheduledExecutorService es1 = Executors.newSingleThreadScheduledExecutor(new TJThreadFactory("返回给zabbix已经宕机的dubbo节点", true)) ;
		es1.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					String s = b.pollFirst(1, TimeUnit.SECONDS) ;
					System.out.println("已经宕机的dubbo节点名称=" + s);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 0, 4, TimeUnit.SECONDS) ;
		while(true){
			
		}
	}
}
