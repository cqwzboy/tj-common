package com.tj.common.zookeeper.discovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.x.discovery.ProviderStrategy;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.strategies.RandomStrategy;
import org.apache.curator.x.discovery.strategies.RoundRobinStrategy;
import org.apache.curator.x.discovery.strategies.StickyStrategy;

/**
 * 服务调用方
 * 
 * @author silongz
 *
 */
public class ServiceCaller {

	private final static String KEY_SPLIT = ":::";

	private static Map<String, ServiceProvider<ServiceInfo>> providers = new ConcurrentHashMap<String, ServiceProvider<ServiceInfo>>();

	/**
	 * 按指定策略获取服务实例，如果指定策略为空，则随机返回一台服务实例
	 * 
	 * @param serviceName
	 * @param strategyType
	 * @return
	 */
	public static ServiceInfo getServiceInfo(String systemCode, String serviceName, ProviderStrategyType strategyType) {
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setSystemCode(systemCode);
		serviceInfo.setServiceName(serviceName);
		ServiceProvider<ServiceInfo> provider = getServiceProvider(systemCode, serviceName, strategyType, null);
		try {
			ServiceInstance<ServiceInfo> serviceInstance = provider.getInstance();
			if (serviceInstance != null) {
				serviceInfo.setId(serviceInstance.getId());
				serviceInfo.setIp(serviceInstance.getAddress());
				serviceInfo.setPort(serviceInstance.getPort());
				serviceInfo.setNodeData(serviceInstance.getPayload().getNodeData());
			}
		} catch (Exception e) {
			throw new RuntimeException("获取系统【" + systemCode + "】下的服务【" + serviceName + "]实例异常", e);
		}
		return serviceInfo;
	}

	/**
	 * 按自定义策略获取服务实例，如果指定策略为空，则随机返回一台服务实例
	 * @param serviceName
	 * @param strategyType
	 * @return
	 */
	public static ServiceInfo getServiceInfoByExtStrategy(String systemCode, String serviceName, ProviderStrategy<ServiceInfo> extStrategy) {
		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setSystemCode(systemCode);
		serviceInfo.setServiceName(serviceName);
		ServiceProvider<ServiceInfo> provider = getServiceProvider(systemCode, serviceName, null, extStrategy);
		try {
			ServiceInstance<ServiceInfo> serviceInstance = provider.getInstance();
			if (serviceInstance != null) {
				serviceInfo.setId(serviceInstance.getId());
				serviceInfo.setIp(serviceInstance.getAddress());
				serviceInfo.setPort(serviceInstance.getPort());
			}
		} catch (Exception e) {
			throw new RuntimeException("获取系统【" + systemCode + "】下的服务【" + serviceName + "]实例异常", e);
		}
		return serviceInfo;
	}

	/**
	 * 获取指定service当前所有实例
	 * 
	 * @param systemCode
	 * @param serviceName
	 * @return
	 */
	public static List<ServiceInfo> getAllServiceInfo(String systemCode, String serviceName) {
		List<ServiceInfo> list = new ArrayList<ServiceInfo>();
		ServiceProvider<ServiceInfo> provider = getServiceProvider(systemCode, serviceName, null, null);
		try {
			Collection<ServiceInstance<ServiceInfo>> allServiceInstance = provider.getAllInstances();
			if (allServiceInstance != null) {
				for (ServiceInstance<ServiceInfo> serviceInstance : allServiceInstance) {
					ServiceInfo serviceInfo = new ServiceInfo();
					serviceInfo.setSystemCode(systemCode);
					serviceInfo.setServiceName(serviceName);
					serviceInfo.setId(serviceInstance.getId());
					serviceInfo.setIp(serviceInstance.getAddress());
					serviceInfo.setPort(serviceInstance.getPort());
					list.add(serviceInfo);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("获取系统【" + systemCode + "】下的服务【" + serviceName + "]所有实例异常", e);
		}
		return list;

	}

	/**
	 * 关闭serviceProvider
	 * 
	 * @param systemCode
	 * @param serviceName
	 */
	public static void closeServiceProvider(String systemCode, String serviceName) {
		ServiceProvider<ServiceInfo> provider = null;
		if (providers.containsKey(getMapKey(systemCode, serviceName))) {
			provider = providers.get(serviceName);
			try {
				provider.close();
			} catch (IOException e) {
				throw new RuntimeException("关闭系统【" + systemCode + "】下的服务【" + serviceName + "]的serviceProvider异常", e);
			}
		}
	}

	/**
	 * 关闭当前系统中所有的serviceProvider
	 * 
	 * @param systemCode
	 * @param serviceName
	 */
	public static void closeAllServiceProvider() {
		if (providers.size() > 0) {
			for (Entry<String, ServiceProvider<ServiceInfo>> entry : providers.entrySet()) {
				String key = entry.getKey();
				ServiceProvider<ServiceInfo> serviceProvider = entry.getValue();
				try {
					serviceProvider.close();
				} catch (IOException e) {
					throw new RuntimeException("关闭【" + key + "】下的serviceProvider异常", e);
				}
			}
		}
	}

	/**
	 * 获取serviceProvider实例
	 * 
	 * @param systemCode
	 * @param serviceName
	 * @param strategyType
	 * @return
	 */
	private static ServiceProvider<ServiceInfo> getServiceProvider(String systemCode, String serviceName,
			ProviderStrategyType strategyType, ProviderStrategy<ServiceInfo> providerStrategy) {
		ServiceProvider<ServiceInfo> provider = null;
		String key = getMapKey(systemCode, serviceName);
		if (providers.containsKey(key)) {
			provider = providers.get(key);
		} else {
			try {
				ProviderStrategy<ServiceInfo> strategy = null;
				if (providerStrategy != null) {
					strategy = providerStrategy;
				} else {
					strategy = getProviderStrategy(strategyType);
				}
				ServiceDiscovery<ServiceInfo> serviceDiscovery = ServiceDiscoveryFactory.getInstance(systemCode);
				provider = serviceDiscovery.serviceProviderBuilder().serviceName(serviceName).providerStrategy(strategy).build();
				provider.start();
				providers.put(key, provider);
			} catch (Exception e) {
				throw new RuntimeException("系统【" + systemCode + "】下的服务【" + serviceName + "]对应的ServiceProvider启动异常", e);
			}
		}
		return provider;
	}

	/**
	 * 获取对应的分发策略
	 * 
	 * @param strategyType
	 * @return
	 */
	private static ProviderStrategy<ServiceInfo> getProviderStrategy(ProviderStrategyType strategyType) {
		switch (strategyType) {
		case RANDOM:
			return new RandomStrategy<ServiceInfo>();
		case ROUND_ROBIN:
			return new RoundRobinStrategy<ServiceInfo>();
		case STICKY:
			return new StickyStrategy<ServiceInfo>(new RandomStrategy<ServiceInfo>());
		default:
			break;
		}
		return new RandomStrategy<ServiceInfo>();
	}

	/**
	 * 拼接serviceProvider的key
	 * 
	 * @param systemCode
	 * @param serviceName
	 * @return
	 */
	private static String getMapKey(String systemCode, String serviceName) {
		return systemCode.concat(KEY_SPLIT).concat(serviceName);
	}
}
