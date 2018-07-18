package com.tj.common.lang;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5 hash工具类
 * @author silongz
 *
 */
public class MD5Utils {

	/**
	 * MD5 hash
	 * @param key
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encodeMD5(String key , String str) throws Exception {
		if(str == null || "".equals(str.trim())){
			return null ;
		}
		if(key == null || "".equals(key.trim())){
			return DigestUtils.md5Hex(str) ;
		}else{
			return DigestUtils.md5Hex(key.concat(str).concat(key)) ;
		}
	}
	
	/**
	 * 
	 * @param paramMap -- 参数列表
	 * @param md5Key -- md5key
	 * @param filterParamNames -- 不需要md5签名校验的参数集合
	 * @param charset -- 生产环境下支付系统获取客户端请求中文参数的时候会乱码，需要设定utf-8转码，客户端系统传null即可
	 * @return
	 * @throws Exception 
	 */
	public static String getMD5Sign(Map<String,Object> paramMap , String md5Key , List<String> filterParamNames , String charset) throws Exception{
		paramMap = sortMap(paramMap,filterParamNames) ;
		String paramString = getParamString(paramMap) ;
		
		if(charset != null && !charset.trim().equals("")){
			paramString = new String(paramString.getBytes("ISO-8859-1"), charset);
		}
		return encodeMD5(md5Key, paramString) ;
	}
	
	/**
	 * 参数排序
	 * 
	 * @param map
	 * @return
	 */
	private static Map<String, Object> sortMap(Map<String, Object> map , List<String> filterParamNames) {
		if (map == null || map.isEmpty()) {
			return map;
		}
		Map<String, Object> sortedMap = new TreeMap<String, Object>(new Comparator<String>() {
			public int compare(String key1, String key2) {
				return key1.compareToIgnoreCase(key2);
			}
		});
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String paramName = entry.getKey();
			//如果参数不需要md5签名校验
			if (filterParamNames != null && filterParamNames.contains(paramName)) {
				continue;
			}
			sortedMap.put(paramName, entry.getValue());
		}
		return sortedMap;
	}
	
	/**
	 * 构建参数字符串形式
	 * @param paramMap
	 * @return
	 */
	private static String getParamString(Map<String, Object> paramMap){
		StringBuilder sb = new StringBuilder();
		if (!paramMap.isEmpty()) {
			for (Map.Entry<String, Object> en : paramMap.entrySet()) {
				String key = en.getKey();
				Object value = en.getValue();
				if (value != null) {
					if(value instanceof Object [] && ((Object[]) value).length > 0){
						for (Object eaceValue : (Object[]) value) {
							sb.append(key);
							sb.append(eaceValue);
						}
					}else{
						sb.append(key);
						sb.append(value);
					}
				}
			}
		}
		return sb.toString() ;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(encodeMD5("7516C749723F46A3B22007E6DA6786F3", "147258"));
	}
}
