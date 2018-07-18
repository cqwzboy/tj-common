package com.tj.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TJExcutor<I,O> {

	private final static int THREAD_COUNT = 10;
	
	/**
	 * 启动线程池，并设定所有线程为daemon线程
	 */
	private static ExecutorService sendExecutorService = Executors.newFixedThreadPool(THREAD_COUNT, new TJThreadFactory(
			"build-taobao-items-tree", true));
	
}
