package com.tj.common.redis;

import java.io.Serializable;

/**
 * redis hash存储对应的数据结构
 * @author silongz
 *
 */
public class BatchInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -250937078508698508L;

	private String key ;
	
	private String field ;
	
	private Object value ;
	
	private BatchCommandType batchCommandType ;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public BatchCommandType getBatchCommandType() {
		return batchCommandType;
	}

	public void setBatchCommandType(BatchCommandType batchCommandType) {
		this.batchCommandType = batchCommandType;
	}

	public enum BatchCommandType {
		SET,SET_STR ,HSET,HSET_STR,DEL,HDEL,INCRBY,HINCRBY;
	}
}
