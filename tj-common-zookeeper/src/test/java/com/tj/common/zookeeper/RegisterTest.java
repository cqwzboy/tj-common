package com.tj.common.zookeeper;

import com.tj.common.zookeeper.discovery.ServiceInfo;
import com.tj.common.zookeeper.discovery.ServiceRegister;

public class RegisterTest {

	public static void main(String[] args) {
		for (int i = 1; i <= 10; i++) {
			ServiceInfo serviceInfo = new ServiceInfo() ;
			serviceInfo.setId(String.valueOf(i));
			serviceInfo.setIp("127.0.0." + i);
			serviceInfo.setPort(1000 + i);
			serviceInfo.setSystemCode("robot");
			serviceInfo.setServiceName("fetchJob");
			serviceInfo.setNodeData("account" + i);
			Thread t = new Thread(new TestThread(serviceInfo)) ;
			t.start();
		}
		
		while(true){
			
		}
	}
	
	static class TestThread implements Runnable{
        private ServiceInfo serviceInfo ;
		public TestThread(ServiceInfo serviceInfo){
			this.serviceInfo = serviceInfo ;
		}
		@Override
		public void run() {
			ServiceRegister.register(serviceInfo);
		}
		
	}
}
