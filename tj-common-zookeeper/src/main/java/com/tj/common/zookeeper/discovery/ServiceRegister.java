package com.tj.common.zookeeper.discovery;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

/**
 * 服务注册器
 * @author silongz
 *
 */
public class ServiceRegister {
	
	/**
	 * 新注册服务
	 * @param serviceInfo
	 */
	public static void register(ServiceInfo serviceInfo) {
		validateServerInfo(serviceInfo);
		try {
			ServiceDiscovery<ServiceInfo> serviceDiscovery = ServiceDiscoveryFactory.getInstance(serviceInfo.getSystemCode()) ;
			ServiceInstance<ServiceInfo> serviceInstance = buildServiceInstance(serviceInfo);
			serviceDiscovery.registerService(serviceInstance);
		} catch (Exception e) {
			throw new RuntimeException("服务注册异常", e);
		}
	}

	/**
	 * 更新服务（根据id来更新）
	 * @param serverInfo
	 */
	public static void update(ServiceInfo serverInfo) {
		validateServerInfo(serverInfo);
		try {
			ServiceDiscovery<ServiceInfo> serviceDiscovery = ServiceDiscoveryFactory.getInstance(serverInfo.getSystemCode()) ;
			ServiceInstance<ServiceInfo> serviceInstance = buildServiceInstance(serverInfo);
			serviceDiscovery.updateService(serviceInstance);
		} catch (Exception e) {
			throw new RuntimeException("更新服务注册信息异常", e);
		}
	}
	
	/**
	 * 注销服务
	 * @param serviceInfo
	 */
	public static void unregister(ServiceInfo serviceInfo) {
		validateServerInfo(serviceInfo);
		try {
			ServiceDiscovery<ServiceInfo> serviceDiscovery = ServiceDiscoveryFactory.getInstance(serviceInfo.getSystemCode()) ;
			ServiceInstance<ServiceInfo> serviceInstance = buildServiceInstance(serviceInfo);
			serviceDiscovery.unregisterService(serviceInstance);
		} catch (Exception e) {
			throw new RuntimeException("注销服务注册信息异常", e);
		}
	}
	
	/**
	 * 关闭systemCode下的服务发现
	 * @param serverInfo
	 */
	public static void close(ServiceInfo serverInfo){
		validateServerInfo(serverInfo);
		ServiceDiscovery<ServiceInfo> serviceDiscovery = ServiceDiscoveryFactory.getInstance(serverInfo.getSystemCode()) ;
		if(serviceDiscovery != null){
			try {
				serviceDiscovery.close();
			} catch (IOException e) {
				throw new RuntimeException("服务注册器关闭异常", e);
			}
		}
	}
	
    /**
     * 构建服务实例信息	
     * @param serverInfo
     * @return
     * @throws Exception
     */
	private static ServiceInstance<ServiceInfo> buildServiceInstance(ServiceInfo serverInfo) throws Exception {
		String id = serverInfo.getId();
		if (StringUtils.isBlank(id)) {
			id = serverInfo.getIp();
		}
		ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo> builder().id(id).name(serverInfo.getServiceName()).port(serverInfo.getPort())
				.address(serverInfo.getIp()).payload(serverInfo).uriSpec(new UriSpec("{scheme}://{address}:{port}")).build();
		return serviceInstance;
	}

	private static void validateServerInfo(ServiceInfo serverInfo) {
		if (StringUtils.isBlank(serverInfo.getIp())) {
			throw new IllegalArgumentException("注册服务时IP不能为空");
		}

		if (StringUtils.isBlank(serverInfo.getServiceName())) {
			throw new IllegalArgumentException("注册服务时，serviceName不能为空");
		}

		if (StringUtils.isBlank(serverInfo.getSystemCode())) {
			throw new IllegalArgumentException("注册服务时，systemCode不能为空");
		}
	}
}
