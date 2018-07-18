package com.tj.common.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件加载工具类
 * @author silongz
 *
 */
public class PropertiesUtils {

	public static Properties loadProperties(String fileName){
		InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		if(ins == null){
			throw new IllegalArgumentException(String.format("文件【%s】在classpath中不存在",fileName));
		}
		Properties prop = new Properties();
		try {
			prop.load(ins);
			return prop ;
		} catch (IOException e) {
			throw new IllegalArgumentException(String.format("加载【%s】资源文件出错", fileName), e);
		}
	}
}
