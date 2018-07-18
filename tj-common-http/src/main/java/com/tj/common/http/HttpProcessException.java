package com.tj.common.http;

/**
 * 异常定义
 * @author silongz
 *
 */
public class HttpProcessException  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2749168865492921426L;

	public HttpProcessException(Exception e){
		super(e);
	}

	/**
	 * @param string
	 */
	public HttpProcessException(String msg) {
		super(msg);
	}
	
	/**
	 * @param message
	 * @param e
	 */
	public HttpProcessException(String message, Exception e) {
		super(message, e);
	}
	
}
