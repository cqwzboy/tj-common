package com.tj.common.zookeeper;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;

import com.tj.common.zookeeper.base.ZkClient;

public class NodeTest {

	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	public static void main(String[] args) throws Exception {
		CuratorFramework client = ZkClient.getClient() ;
//		client.create().creatingParentsIfNeeded().forPath("/login_wangwang/192.168.0.1/wangwang1") ;
//		client.delete().deletingChildrenIfNeeded().forPath("/login_wangwang/192.168.0.1/wangwang2") ;
		//		client.create().creatingParentsIfNeeded().forPath("/login_wangwang/192.168.0.1/wangwang3") ;
//		client.create().creatingParentsIfNeeded().forPath("/login_wangwang/192.168.0.1/wangwang4") ;
        final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/login_wangwang/192.168.0.1", true);
		pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {

				System.out.println(" catch children change ");
				System.out.println("update event type:" + event.getType());

				List<ChildData> childDataList = pathChildrenCache.getCurrentData();
				if (childDataList != null && childDataList.size() > 0) {
					System.out.println("path all children list:");
					for (ChildData childData : childDataList) {
						System.out.println("path:" + childData.getPath() + "," + new String(childData.getData(), CHARSET));
					}
				}
			}
		});
		pathChildrenCache.start();
		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/login_wangwang/192.168.0.1/wangwang2") ;
//		client.close();
//		pathChildrenCache.close();
		while(true){
			
		}
	}
}
