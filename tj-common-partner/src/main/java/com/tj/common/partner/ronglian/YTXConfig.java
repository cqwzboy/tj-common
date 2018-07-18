package com.tj.common.partner.ronglian;

import java.util.Properties;

import com.tj.common.lang.PropertiesUtils;

public class YTXConfig {
	private static String YTX_PROPERTIES="ronglianYtx.properties";
	
	private static String IM_YTX_ACCOUNT="im_ytx_account";
	private static String IM_YTX_APPID="im_ytx_appid";
	private static String IM_YTX_TOKEN="im_ytx_token";
	private static String IM_YTX_PUSHER="im_ytx_pusher";
	private static String IM_YTX_URL="im_ytx_url";

	/**
	 * 容联授权账号信息
	 */
	private static String account="";
	private static String appid="";
	private static String token="";
	private static String ytxUrl="";
	
	private static String tjkUserAccountPrefix="tjk_im_user_account_";
	/**
	 * 自定义一个推送的系统账号(需要是登录过的）
	 */
	private static String syssender="";
	
	private static Properties prop;
	
	private YTXConfig(){
		
	}
	static {
		if(prop == null){
			prop = PropertiesUtils.loadProperties(YTX_PROPERTIES) ;
			account = prop.getProperty(IM_YTX_ACCOUNT) ;
			appid = prop.getProperty(IM_YTX_APPID) ;
			token = prop.getProperty(IM_YTX_TOKEN) ;
			syssender = prop.getProperty(IM_YTX_PUSHER) ;
			ytxUrl = prop.getProperty(IM_YTX_URL) ;
		}
	}

	public static String getAccount() {
		return account;
	}

	public static void setAccount(String account) {
		YTXConfig.account = account;
	}

	public static String getAppid() {
		return appid;
	}

	public static void setAppid(String appid) {
		YTXConfig.appid = appid;
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		YTXConfig.token = token;
	}

	public static String getSyssender() {
		return syssender;
	}

	public static void setSyssender(String syssender) {
		YTXConfig.syssender = syssender;
	}

	public static String getTjkUserAccountPrefix() {
		return tjkUserAccountPrefix;
	}

	public static void setTjkUserAccountPrefix(String tjkUserAccountPrefix) {
		YTXConfig.tjkUserAccountPrefix = tjkUserAccountPrefix;
	}

	public static String getYtxUrl() {
		return ytxUrl;
	}

	public static void setYtxUrl(String ytxUrl) {
		YTXConfig.ytxUrl = ytxUrl;
	}
	
}
