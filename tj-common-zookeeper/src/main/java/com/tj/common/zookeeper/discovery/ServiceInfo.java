package com.tj.common.zookeeper.discovery;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * 注册服务信息类
 * @author silongz
 *
 */
@JsonRootName("tj_service_info")
public class ServiceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1454156523096611047L;

	/**
	 * 服务器id标识，不指定时，默认使用ip
	 */
	private String id ;
	
	/**
	 * 服务器ip
	 */
	private String ip ;
	
	/**
	 * 服务端口
	 */
	private int port ;
	
	/**
	 * 服务所属系统编码
	 */
	private String systemCode ;
	
	/**
	 * 注册服务的名称
	 */
	private String serviceName ;
	
	private String createTime;
	private String updateTime;
	/**
	 * 节点附属数据
	 * */
	private Object nodeData;
	
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Object getNodeData() {
		return nodeData;
	}

	public void setNodeData(Object nodeData) {
		this.nodeData = nodeData;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

}
