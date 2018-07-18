package com.tj.common.lang.exception;

/**
 * 业务异常
 * @author Pucq
 *
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 4524053760074995648L;
	
	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}
