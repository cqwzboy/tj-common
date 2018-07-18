package com.tj.common.zookeeper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.tj.common.zookeeper.discovery.ProviderStrategyType;
import com.tj.common.zookeeper.discovery.ServiceCaller;
import com.tj.common.zookeeper.discovery.ServiceInfo;

public class CallerTest {

	private static Map<String,Integer> map = new ConcurrentHashMap<String,Integer>() ;
	
	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			ServiceInfo info = ServiceCaller.getServiceInfo("robot", "fetchJob", ProviderStrategyType.ROUND_ROBIN) ;
			System.out.println(info.getNodeData());
			String id = info.getId() ;
			if(map.containsKey(id)){
				Integer index = map.get(id) + 1 ;
				map.put(id, index) ;
			}else{
				map.put(id, 1) ;
			}
		}
		
		int total = 0 ;
		for (Entry<String,Integer> entry : map.entrySet()) {
			System.out.println("服务器id=" + entry.getKey());
			System.out.println("服务器被调用次数=" + entry.getValue());
			total += entry.getValue() ;
		}
		
		System.out.println("被调用的服务器个数=" + map.size());
		System.out.println("总调用次数=" + total);
	}
}
