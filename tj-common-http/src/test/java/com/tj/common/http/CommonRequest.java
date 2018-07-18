package com.tj.common.http;

import java.io.Serializable;

public class CommonRequest<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4250352910198922738L;

	/**
	 * 系统编号
	 */
	private String systemId ;
	
	/**
	 * 用户id
	 */
	private String userId ;
	
	/**
	 * 用户密码
	 */
	private String password ;
	
	private T parameter ;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public T getParameter() {
		return parameter;
	}

	public void setParameter(T parameter) {
		this.parameter = parameter;
	}
	
}
