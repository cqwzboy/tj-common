package com.tj.common.bean;

import java.io.Serializable;

public class Result implements Serializable {
	private static final long serialVersionUID = 7279048368914735415L;
	public static final String APP_ERR = "300";
	public static final String EXP_ERR = "400";
	
	private String code;
	private String msg;
	private Object rst;
	
	public Result(){
		this.code = "200";
	}
	
	public Result(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public Result(Exception ex){
		this.code = EXP_ERR;
		this.msg = ex.getMessage();
		this.rst = ex;
		ex.printStackTrace();
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void setAppErrMsg(String msg){
		this.code = APP_ERR;
		this.msg = msg;
		
		//com.tj.tjk.call.utils.log.Loger.get()
		System.out.println("[WARN] " + msg);
	}
	/**
	 * @return the rst
	 */
	public Object getRst() {
		return rst;
	}
	/**
	 * @param rst the rst to set
	 */
	public void setRst(Object rst) {
		this.rst = rst;
	}
	
	public Boolean isok(){
		return "200".equals(this.code);
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		try{
			sb.append(" code:" + this.code);
			sb.append(" msg:" + this.msg);
			sb.append(" rst:" + this.rst);
			if(this.rst != null)
				sb.append(this.rst.toString());
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return sb.toString();
	}
}
