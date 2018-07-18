package com.tj.common.zookeeper.discovery;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import com.tj.common.zookeeper.base.ZkClient;

public class ServiceDiscoveryFactory {

	// 每个systemCode对应一个ServiceDiscovery实例
	private static Map<String, ServiceDiscovery<ServiceInfo>> map = new ConcurrentHashMap<String, ServiceDiscovery<ServiceInfo>>();

	private ServiceDiscoveryFactory(){
		
	}
	
	/**
	 * 按basePath获取对应的服务发现实例
	 * @param basePath
	 * @return
	 */
	public static ServiceDiscovery<ServiceInfo> getInstance(String basePath) {
		if (map.containsKey(basePath)) {
			return map.get(basePath);
		} else {
			ServiceDiscovery<ServiceInfo> serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
					.client(ZkClient.getClient()).serializer(new JsonInstanceSerializer<>(ServiceInfo.class)).basePath(basePath).build();
			try {
				serviceDiscovery.start();
				map.put(basePath, serviceDiscovery);
			} catch (Exception e) {
				throw new RuntimeException("启动服务发现实例异常", e);
			}
			return serviceDiscovery;
		}
	}
}
