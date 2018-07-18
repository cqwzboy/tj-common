package com.tj.common.zookeeper;

import com.tj.common.zookeeper.lock.DistributionMutexLock;
import com.tj.common.zookeeper.lock.LockCallBack;

public class LockTest {

	public static void main(String[] args) throws Exception {
		DistributionMutexLock<String> lock = new DistributionMutexLock<String>("systemCode", "lockName") ;
		lock.lock(new LockCallBack<String>() {
			@Override
			public String call() {
				System.out.println("实现自己的业务逻辑");
				return "返回你自己指定的泛型结果，测试这里返回的是<String>";
			}
		}) ;
	}
}
