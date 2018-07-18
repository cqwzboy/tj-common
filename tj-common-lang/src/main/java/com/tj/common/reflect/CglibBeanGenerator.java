package com.tj.common.reflect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;

/**
 * 借助cglib动态代理机制，动态生成bean
 * @author silongz
 *
 */
public class CglibBeanGenerator {


	/**
	 * 私有化构造方法
	 * @param propertyMap
	 */
	private CglibBeanGenerator() {
	
	}

	public static Object generateObject(Map<String,String> paramMap){
		if(paramMap == null || paramMap.size() == 0){
			return null ;
		}
		Map<String, Class<String>> propertyMap = new HashMap<String, Class<String>>() ;
		for (Entry<String,String> entry : paramMap.entrySet()) {
			String key = entry.getKey() ;
			//指定属性和属性类型（统一指定为String类型）
			propertyMap.put(key, String.class) ;
		}
		// 生成动态 Bean
		Object object = generateBean(propertyMap);
		BeanMap beanMap = BeanMap.create(object);
		for (Entry<String,String> entry : paramMap.entrySet()) {
			String key = entry.getKey() ;
			String value = entry.getValue() ;
			//给bean属性赋值
			beanMap.put(key , value);
		}
		return object ;
	}

	/**
	 * @param propertyMap
	 * @return
	 */
	private static Object generateBean(Map<String, Class<String>> propertyMap) {
		BeanGenerator generator = new BeanGenerator();
		Set<String> keySet = propertyMap.keySet();
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			String key = (String) i.next();
			generator.addProperty(key, propertyMap.get(key));
		}
		return generator.create();
	}

}
