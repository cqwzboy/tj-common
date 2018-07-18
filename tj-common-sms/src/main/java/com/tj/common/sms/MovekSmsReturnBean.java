package com.tj.common.sms;

import com.alibaba.fastjson.JSONObject;

public class MovekSmsReturnBean {
	private boolean status = true;
	private String message = "成功";
    private Object data;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public String toJsonpStr(String callBack){
		return callBack+"("+JSONObject.toJSONString(this)+")";
	}
	public String toJsonpStr(){
		return toJsonpStr("successCallback");
	}
}
