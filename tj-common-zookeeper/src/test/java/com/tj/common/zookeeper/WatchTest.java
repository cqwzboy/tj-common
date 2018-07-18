package com.tj.common.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.tj.common.zookeeper.base.ZKListenerCallBack;
import com.tj.common.zookeeper.base.ZkClient;

public class WatchTest {

	public static void main(String[] args) throws Exception {
		CuratorFramework client = ZkClient.getClient();
		Stat stat = client.checkExists().forPath("/login_wangwang/192.168.0.1");
		if (stat == null) {
			client.create().creatingParentsIfNeeded().forPath("/login_wangwang/192.168.0.1");
		}
		ZkClient.addListener(new ZKListenerCallBack() {
			
			@Override
			public void updateNode(ChildData data) {
				System.out.println("更新节点");
			}
			
			@Override
			public void removeNode(ChildData data) {
				System.out.println(data.getPath());
				System.out.println("删除节点");
			}
			
			@Override
			public String getListenPath() {
				return "/login_wangwang/192.168.0.1";
			}
			
			@Override
			public void addNode(ChildData data) {
				System.out.println("新增节点");
				
			}
		});
		Thread.sleep(30 * 1000);
		client.create().creatingParentsIfNeeded().forPath("/login_wangwang/192.168.0.1/wangwang120");
		client.create().withMode(CreateMode.EPHEMERAL).forPath("/login_wangwang/192.168.0.1/temp") ;
		Thread.sleep(5 * 1000);
	}
}
