package com.tj.common.http;


public class CommonResponse<T> {

	/**
	 * 成功
	 */
	public final static int SUCESS = 200 ;
	
	/**
	 * 权限错误
	 */
	public final static int NOT_AUTH = 400 ;
	
	/**
	 * 服务器异常
	 */
	public final static int ERROR = 500 ;
	
	/**
	 * 默认成功
	 */
	private int statusCode = SUCESS ;
	
	/**
	 * 只有失败时该字段才有值
	 */
	private String msg ;
	
	private T data ;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
