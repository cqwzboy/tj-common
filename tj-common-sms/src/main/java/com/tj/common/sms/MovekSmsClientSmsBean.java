package com.tj.common.sms;

import java.util.Date;

/**
 * 普通短信实例
 * @author wangch
 * 
 */
public class MovekSmsClientSmsBean {
	/*名称		属性		长度			选项		描 述
	action		字符型	send		必填		方式
	userid		数字型	系统分配		必填		特服号
	account		字符型	系统分配		必填		序列号
	password	字符型	3 至 32 位		选填		密码或32位md5
	mobile		字符型	不限			必填		手机号码。多个号码以逗号或分号分隔
	content		字符型	1 至 500 位		必填		短信内容
	sendtime	数字型	固定长度		选填		定时发送，为空立即发送(YY-MM-DD HH:MM:SS)
	extno		数字型	1 至 10 位		选填		扩展号（需申请通道支持）
	json		string	1 位			选填		1*/

	
	/**
	 * 方式   send
	 */
	private String action;
	
	/**
	 * 特服号   系统分配
	 */
	private Integer userid;
	/**
	 * 序列号  系统分配
	 */
	private Integer account; 
	/**
	 * 密码   
	 */
	private String password;
	
	/**
	 * 手机号码。多个号码以逗号或分号分隔
	 */
	private String mobile;
	
	/**
	 * 短信内容
	 */
	private String content;
	
	/**
	 * 定时发送，为空立即发送(YY-MM-DD HH:MM:SS)
	 */
	private String sendtime;
	
	/**
	 * 扩展号（需申请通道支持）
	 */
	private Integer extno;
	
	/**
	 * 值为 1  返回格式为json   不传则返回xml
	 */
	private Integer json;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getAccount() {
		return account;
	}
	public void setAccount(Integer account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public Integer getExtno() {
		return extno;
	}
	public void setExtno(Integer extno) {
		this.extno = extno;
	}
	public Integer getJson() {
		return json;
	}
	public void setJson(Integer json) {
		this.json = json;
	}
}
