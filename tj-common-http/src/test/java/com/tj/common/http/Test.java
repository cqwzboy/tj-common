package com.tj.common.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tj.common.http.HttpHeader.Headers;

public class Test {

	private static String baseUrl = "http://localhost:8080/tj-task-order-service/users/" ;
	public static void main(String[] args) throws Exception {
		
//		Map<String,String> formParam = new HashMap<String,String>() ;
//		formParam.put("userName", "曾司龙") ;
//		formParam.put("startDate", "2016-06-22") ;
//		formParam.put("endDate", "2016-06-23") ;
//		formParam.put("reason", "没有理由") ;
//		
//		System.out.println(startProcess(baseUrl, "tjk", "leave_process", "task_order_001", formParam));
//		testRest() ;
		
		String url = "http://wangting.18pingtai.cn:8080/tbphone/query?" ;
		String param = "sign=b4e43ad0dae0fb0911c34463ddac4a5e&timestamp=20170412111114&v=1.0&sell_id=1050035697&method=queryBalance&format=json&biz_paras=%7B%22busiCode%22:%22100000%22,%22isp%22:%22%22,%22phoneNo%22:%2218280302468%22,%22queryType%22:%221%22%7D&sign_method=MD5" ;
//		String param1 = "sign=b4e43ad0dae0fb0911c34463ddac4a5e&timestamp=20170412111114&v=1.0&sell_id=1050035697&method=queryBalance&format=json&biz_paras={\"busiCode\":\"100000\",\"isp\":\"\",\"phoneNo\":\"18280302468\",\"queryType\":\"1\"}&sign_method=MD5" ;
//		String param = URLEncoder.encode(param1, "utf-8") ;
//		System.out.println(param);
		url += param ;
		HttpConfig config = HttpConfig.custom().url(url);
		System.out.println(HttpClientUtil.get(config)) ;
	}
	
	public static String get() throws HttpProcessException{
		HttpConfig config = HttpConfig.custom().url("yourUrl") ;
		return HttpClientUtil.get(config) ;
	}
	
	public static String post() throws HttpProcessException{
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("paramName", "paramValue") ;
		HttpConfig config = HttpConfig.custom().url("yourUrl").map(map) ;
		return HttpClientUtil.post(config) ;
	}
	
	public static String queryProcessByKey(String baseUrl ,  String processKey ){
		String url = baseUrl + "repository/process-definitions?key=" + processKey ;
		HttpConfig config = HttpConfig.custom().url(url) ;
		String s = null ;
		try {
			s = HttpClientUtil.get(config);
		} catch (HttpProcessException e) {
			s = "请求异常：" + e.getMessage() ;
			e.printStackTrace();
		}
		System.out.println(s);
		return s ;
	}
	
	public static String startProcess(String baseUrl , String systemId , String processKey , String businessKey , Map<String,String> formData) throws UnsupportedEncodingException{
		JSONObject jsonObject = new JSONObject() ;
		jsonObject.put("processDefinitionKey", processKey) ;
		jsonObject.put("businessKey", businessKey) ;
		jsonObject.put("tenantId", systemId) ;
		
		JSONArray array = new JSONArray() ;
		array.add(formData) ;
		
		jsonObject.put("variables", array) ;
		
		StringEntity entity = new StringEntity(jsonObject.toJSONString()) ;
		HttpConfig config = HttpConfig.custom().url(baseUrl + "runtime/process-instances").stringEntity(entity) ;
		String s = null ;
		try {
			s = HttpClientUtil.post(config);
		} catch (HttpProcessException e) {
			s = "请求异常：" + e.getMessage() ;
			e.printStackTrace();
		}
		System.out.println(s);
		return s ;
	}
	
	public static void testRest() throws Exception{
		CommonRequest<Object> request = new CommonRequest<Object>() ;
		request.setSystemId("tjk");
		request.setUserId("user1");
		request.setPassword("123");
		request.setParameter("user1");
		StringEntity entity = new StringEntity(JSON.toJSONString(request)) ;
		Header[] headers = HttpHeader.custom().contentType(Headers.APPLICATION_JSON).build() ;
		HttpConfig config = HttpConfig.custom().headers(headers).url(baseUrl + "queryProcessInstanceStartedBy").stringEntity(entity) ;
		String s = HttpClientUtil.post(config) ;
		System.out.println(s);
	} 
}
