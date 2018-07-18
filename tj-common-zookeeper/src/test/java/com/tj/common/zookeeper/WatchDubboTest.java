package com.tj.common.zookeeper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.zookeeper.data.Stat;

import com.tj.common.zookeeper.base.ZKListenerCallBack;
import com.tj.common.zookeeper.base.ZkClient;

public class WatchDubboTest {

	public static void main(String[] args) throws Exception {
		ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
		es.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					CuratorFramework client = ZkClient.getClient();
					Stat stat = client.checkExists().forPath("/dubbo/com.tj.um.sao.UmService");
					if (stat == null) {
						System.out.println("服务已宕机");
					}else{
						List<String> list = client.getChildren().forPath("/dubbo/com.tj.um.sao.UmService/providers") ;
						if(list == null || list.size() == 0){
							System.out.println("所有服务节点都已宕机");
						}
						System.out.println("节点个数=" + list.size());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 5, TimeUnit.SECONDS);

	}
}
