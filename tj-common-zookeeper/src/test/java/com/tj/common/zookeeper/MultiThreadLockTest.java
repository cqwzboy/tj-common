package com.tj.common.zookeeper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tj.common.zookeeper.lock.DistributionMutexLock;
import com.tj.common.zookeeper.lock.LockCallBack;

public class MultiThreadLockTest {

	public static void main(String[] args) throws Exception {
		ExecutorService service = Executors.newFixedThreadPool(20);
		for (int i = 0; i < 20; i++) {
			Callable<Void> task = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					DistributionMutexLock<Integer> lock = new DistributionMutexLock<Integer>("lock_systemCode", "lockTestName") ;
					int i = lock.lock(new TestCallBack()) ;
					System.out.println("i=" + i);
					return null;
				} 
			} ;
			service.submit(task) ;
		}
	}
	
	static class TestCallBack implements LockCallBack<Integer>{

		private static int i ;
		
		@Override
		public Integer call() {
			// TODO Auto-generated method stub
			return i ++ ;
		}
		
	}
}
