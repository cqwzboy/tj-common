package com.tj.common.sms;

import java.util.Date;

/**
 * 带参数短信实例
 * @author wangch
 * 
 */
public class MovekParamsSmsClientSmsBean {
	/*名称		属性		长度		选项		描 述
	action		string	var		必填		变量短信
	userid		string	系统分配	必填		特服号
	account		string	系统分配	必填		序列号
	password	string	3 至 32 位	选填		密码或32位md5
	msg			string			必填		短信模板(见下表)
	params		string			选填		变量短信的参数组(见下表)
	json		string	1		选填		Json返回*/


	
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
	 * 短信模板
	 * 短信模板。其中的变量用“{$var}”来替代。例如：“{$var},你好！，请你于{$var}日参加活动”，该短信中具有两个变量参数。编码为UTF-8格式。
	 */
	private String msg;
	
	/**
	 *  变量短信的参数组
	 *  编码为UTF-8格式
	 *	每一组参数之间用英文“；”间隔
	 *	每一组参数内部用英文“，”间隔，其中第一个参数为手机号码，第二个参数为模板中第一个变量，第三个参数为模板中第二个变量，以此类推。
	 *	例如：13800210000,李先生,2013-01-01;13500210000,王先生，2013-01-15   
	 *  表明提交了两个变量组，其中手机号码分别为：13800210000和13500210000。模板变量分别为：李先生，2013-01-01和王先生，2013-01-15
	 *	格式不符的参数，系统自动过滤掉。
	 */
	private String params;
	
	
	
	/**
	 * 定时发送，为空立即发送(YY-MM-DD HH:MM:SS)
	 */
	private String sendtime;
	
	
	
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



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	public String getParams() {
		return params;
	}



	public void setParams(String params) {
		this.params = params;
	}



	public String getSendtime() {
		return sendtime;
	}



	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}



	public Integer getJson() {
		return json;
	}



	public void setJson(Integer json) {
		this.json = json;
	}
	
	
	
}
