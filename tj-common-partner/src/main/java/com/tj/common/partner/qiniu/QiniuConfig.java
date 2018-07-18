package com.tj.common.partner.qiniu;

import java.util.Properties;

import com.qiniu.util.Auth;
import com.tj.common.lang.PropertiesUtils;

public class QiniuConfig {

	/**
	 * 七牛配置文件
	 */
	private final static String QINIU_PROPERTIES = "qiniu.properties";
	
	private final static String KEY_ACCESS_KEY = "access_key";
	
	private final static String KEY_SECRET_KEY = "secret_key";
	
	private final static String KEY_BUCKET = "bucket";
	
	private final static String KEY_DOMAIN = "domain";
	
	private static Properties prop;
	
	private static String accessKey ;
	
	private static String secretKey ;
	
	private static String bucket ;
	
	private static String domain ;
	
	private static Auth auth = null;
	
	private QiniuConfig(){
		
	}
	
	static {
		if(prop == null){
			prop = PropertiesUtils.loadProperties(QINIU_PROPERTIES) ;
			accessKey = prop.getProperty(KEY_ACCESS_KEY) ;
			secretKey = prop.getProperty(KEY_SECRET_KEY) ;
			bucket = prop.getProperty(KEY_BUCKET) ;
			domain = prop.getProperty(KEY_DOMAIN) ;
			auth = Auth.create(accessKey, secretKey);
		}
	}

	public static Auth getAuth() {
		return auth;
	}

	public static String getBucket() {
		return bucket;
	}

	public static String getDomain() {
		return domain;
	}

	public static String getAccessKey() {
		return accessKey;
	}

	public static String getSecretKey() {
		return secretKey;
	}

}
