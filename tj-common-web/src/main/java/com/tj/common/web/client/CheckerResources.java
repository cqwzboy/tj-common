package com.tj.common.web.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ResourceBundle;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tj.common.http.HttpClientUtil;
import com.tj.common.http.HttpConfig;
import com.tj.common.http.HttpHeader;
import com.tj.common.http.HttpHeader.Headers;
import com.tj.common.http.HttpProcessException;

public class CheckerResources {

	private static Logger logger = LoggerFactory.getLogger(CheckerResources.class);

	private static ResourceBundle resource = ResourceBundle.getBundle("config");

	private static String osBit = System.getProperty("sun.arch.data.model");

	/**
	 * 获取最新版本号
	 * 
	 * @return
	 */
	public static String getLatestVersion() {
		String latestVersionUrl = resource.getString("latestVersionUrl");
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Header[] headers = HttpHeader.custom().cacheControl(Headers.NO_CACHE).build();
		try {
			HttpClientUtil.down(HttpConfig.custom().url(latestVersionUrl + "?v=" + System.currentTimeMillis())
					.headers(headers).out(bout));
		} catch (HttpProcessException e) {
			logger.error("获取最新版本号异常", e);
			return null;
		}
		byte[] data = bout.toByteArray();
		return new String(data);
	}

	/**
	 * 获取最新的文件url路径
	 * 
	 * @return
	 */
	public static String getLatestFileUrl() {
		return resource.getString("latestFileUrl") + "?v=" + System.currentTimeMillis();
	}

	/**
	 * 获取应用名称
	 * 
	 * @return
	 */
	public static String getAppName() {
		return resource.getString("appName");
	}

	public static String getSystemBit() {
		return osBit;
	}

	/**
	 * 从版本文件中读取当前版本号
	 * @param versionFilePath
	 * @return
	 */
	public static String getCurrentVersion(String versionFilePath) {
		try {
			File versionFile = new File(versionFilePath);
			if (!versionFile.exists()) {
				return null;
			}
			FileReader reader = new FileReader(versionFile);
			BufferedReader bReader = new BufferedReader(reader);
			String line = null ;
			String version = null ;
			while((line = bReader.readLine()) != null){
				version = line ;
			}
			reader.close();
			bReader.close(); 
			return version;
		} catch (Exception e) {
			logger.error("获取当前版本号异常", e);
			return null;
		}
	}

}
