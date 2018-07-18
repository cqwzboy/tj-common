package com.tj.common.zookeeper;

import java.nio.charset.Charset;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.utils.ThreadUtils;
import org.apache.zookeeper.data.Stat;

import com.tj.common.zookeeper.base.ZKListenerCallBack;
import com.tj.common.zookeeper.base.ZkClient;

public class Client1 {

	public static final Charset CHARSET = Charset.forName("UTF-8");
	
	private static final String PATH = "/auth_test/111" ;

	private final static ConnectionStateListener connectionStateListener = new ConnectionStateListener() {
		@Override
		public void stateChanged(CuratorFramework client, ConnectionState newState) {
			System.out.println(newState.toString());
			if ((newState == ConnectionState.RECONNECTED) || (newState == ConnectionState.CONNECTED)) {
				try {
					System.out.println("创建连接");
				} catch (Exception e) {
					ThreadUtils.checkInterrupted(e);
					e.printStackTrace();
				}
			}
			if(newState == ConnectionState.LOST){
				System.out.println("丢失连接");
			}
		}
	};

	public static void main(String[] args) throws Exception {
		CuratorFramework client = ZkClient.getClient();
		client.getConnectionStateListenable().addListener(connectionStateListener);
		Stat stat = client.checkExists().forPath(PATH);
		if (stat == null) {
			client.create().creatingParentsIfNeeded().forPath(PATH);
		}
		ZkClient.addListener(new ZKListenerCallBack() {
			
			@Override
			public void updateNode(ChildData data) {
				System.out.println("更新节点");
				System.out.println(new String(data.getData()));
			}
			
			@Override
			public void removeNode(ChildData data) {
				System.out.println(data.getPath());
				System.out.println("删除节点");
			}
			
			@Override
			public String getListenPath() {
				return PATH ;
			}
			
			@Override
			public void addNode(ChildData data) {
				System.out.println("新增节点");
				
			}
		});
		client.setData().forPath(PATH, "123".getBytes()) ;
		while(true){
			
		}
	}
}
