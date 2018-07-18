package com.tj.common.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.tj.common.lang.PropertiesUtils;

public class MovekSmsConfig {
	/**
	 * sms配置文件
	 */
	private final static String MOVEKSMS_PROPERTIES = "movekSms.properties";
	private static Properties prop;
	
	private static Map<String,Object> map;
	

	private static String SIMPLE_URL = "simple_url";
	private static String PARA_URL = "para_url";
	private static String STATUS_URL = "status_url";
	private static String ACCOUNT = "account";
	private static String PASSWORD = "password";
	private static String USERID = "userid";
	private static String JSON = "json";
	
	private static String simpleUrl;
	private static String paraUrl;
	private static String statusUrl;
	private static String account;
	private static String password;
	private static String action;
	private static Integer userid;
	private static Integer json;
	
	
	static {
		prop = PropertiesUtils.loadProperties(MOVEKSMS_PROPERTIES) ;
		simpleUrl = prop.getProperty(SIMPLE_URL);
		paraUrl = prop.getProperty(PARA_URL);
		statusUrl = prop.getProperty(STATUS_URL);
		account = prop.getProperty(ACCOUNT);
		password = prop.getProperty(PASSWORD);
		if (StringUtils.isNumeric(prop.getProperty(USERID))) {
			userid = Integer.parseInt(prop.getProperty(USERID));
		}
		if (StringUtils.isNumeric(prop.getProperty(JSON))) {
			 json = Integer.parseInt(prop.getProperty(JSON));
		}
		map = new HashMap<String, Object>();
		map.put("userid" ,userid);
		map.put("account" ,account);
		map.put("password" ,password);
		map.put("json" ,json);
	}


	public static String getStatusUrl() {
		return statusUrl;
	}


	public static void setStatusUrl(String statusUrl) {
		MovekSmsConfig.statusUrl = statusUrl;
	}


	public static String getParaUrl() {
		return paraUrl;
	}


	public static void setParaUrl(String paraUrl) {
		MovekSmsConfig.paraUrl = paraUrl;
	}


	public static Map<String, Object> getMap() {
		return map;
	}


	public static void setMap(Map<String, Object> map) {
		MovekSmsConfig.map = map;
	}


	public static Properties getProp() {
		return prop;
	}


	public static void setProp(Properties prop) {
		MovekSmsConfig.prop = prop;
	}


	public static String getSimpleUrl() {
		return simpleUrl;
	}


	public static void setSimpleUrl(String simpleUrl) {
		MovekSmsConfig.simpleUrl = simpleUrl;
	}


	public static String getAccount() {
		return account;
	}


	public static void setAccount(String account) {
		MovekSmsConfig.account = account;
	}


	public static String getPassword() {
		return password;
	}


	public static void setPassword(String password) {
		MovekSmsConfig.password = password;
	}


	public static String getAction() {
		return action;
	}


	public static void setAction(String action) {
		MovekSmsConfig.action = action;
	}


	public static Integer getUserid() {
		return userid;
	}


	public static void setUserid(Integer userid) {
		MovekSmsConfig.userid = userid;
	}


	public static Integer getJson() {
		return json;
	}


	public static void setJson(Integer json) {
		MovekSmsConfig.json = json;
	}
	
	
	
}
