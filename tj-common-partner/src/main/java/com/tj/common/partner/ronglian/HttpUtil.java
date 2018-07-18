package com.tj.common.partner.ronglian;


import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	public static String doGet(String url, String accountid, String token, String timestamp) {
		HttpClientBuilder httpClientBuilder = null;
		CloseableHttpClient httpClient = null;  
	    HttpGet httpGet = null;  
	    String result = null;  
	    try{
	    	httpClientBuilder = HttpClientBuilder.create();
	        httpClient = httpClientBuilder.build();  
	        httpGet = new HttpGet(url);
	        logger.debug("[INFO] get url:" + url);
	        httpGet.setHeader("Accept","application/json");
	        httpGet.setHeader("Content-Type","application/json;charset=utf-8;");
	        httpGet.setHeader("Authorization",getAuthorization(accountid,timestamp));
	        
	        HttpResponse response = httpClient.execute(httpGet);  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,Charset.forName("utf-8"));  
	            }  
	        }
	        logger.debug("[INFO] rst:" + result);
	    }catch(Exception ex){  
	        ex.printStackTrace();  
	    }finally{
	    	close(httpClient);
	    }
	    return result;  
	}
	
	public static String doPost(String url,String accountid, String token, String timestamp,Map<String,String> param){
		CloseableHttpClient httpClient = null;  
	    HttpPost httpPost = null;  
	    String result = null;  
	    try{
	        logger.debug("[INFO] post url:" + url);
	        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
	        httpClient = httpClientBuilder.build();  
	        httpPost = new HttpPost(url);
	        httpPost.setHeader("Accept","application/json");
	        httpPost.setHeader("Content-Type","application/json;charset=utf-8;");
	        httpPost.setHeader("Authorization",getAuthorization(accountid,timestamp));
	        
	        //设置参数  
	        List<NameValuePair> list = new ArrayList<NameValuePair>();  
	        Iterator<Entry<String, String>> iterator = param.entrySet().iterator();  
	        while(iterator.hasNext()){  
	            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
	            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
	        }  
	        if(list.size() > 0){  
	            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,Charset.forName("utf-8"));  
	            httpPost.setEntity(entity);  
	        }  
	        HttpResponse response = httpClient.execute(httpPost);  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,Charset.forName("utf-8"));  
	            }  
	        }
	        logger.debug("[INFO] rst:" + result);
	    }catch(Exception ex){  
	        ex.printStackTrace();  
	    }finally{
	    	close(httpClient);
	    }
	    
	    return result;  
	}
	
	private static void close(CloseableHttpClient client){
		if(client != null){
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String getAuthorization(String accountid, String timestamp) {
		String rst = Base64Util.getBase64(accountid+":"+timestamp);
		//logger.debug("accountid:" + accountid);
		//logger.debug("timestamp:" + timestamp);
		//logger.debug("Authorization:" + rst);
		return rst;
	}

	public static String getTimeStamp(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
		return formatter.format(new Date());
	}

	public static String getSigParameter(String accountid,String token,String timestamp){
		return MD5Util.MD5(accountid+token+timestamp).toUpperCase();
	}
	
	public static String getAuthSig(String appid,String username,String timestamp,String token){
		return MD5Util.MD5(appid+username+timestamp+token).toUpperCase();
	}


	public static String doPost(String url, String accountid, String token, String timestamp, JSONObject param) {
		return doPost(url,accountid,token,timestamp,param,false);
	}
	
	public static String doPostXml(String url, String accountid, String token, String timestamp, String param) {
		return doPost(url,accountid,token,timestamp,param,true);
	}
	
	private static String doPost(String url, String accountid,String token, String timestamp, Object param,Boolean isxml){
		CloseableHttpClient httpClient = null;  
	    HttpPost httpPost = null;  
	    String result = null;  
	    try{
	        logger.debug("[INFO] post url:" + url);
	        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
	        httpClient = httpClientBuilder.build();  
	        httpPost = new HttpPost(url);
	        httpPost.setHeader("Accept","application/json");
	        if(isxml)
	        	httpPost.setHeader("Content-Type","application/xml;charset=utf-8;");
	        else
	        	httpPost.setHeader("Content-Type","application/json;charset=utf-8;");
	        httpPost.setHeader("Authorization",getAuthorization(accountid,timestamp));
	        
	        //设置参数 
	        String paramstr;
	        if(isxml)
	        	paramstr = (String)param;
	        else
	        	paramstr = ((JSONObject)param).toJSONString();
	        logger.debug("[INFO] param:"+paramstr);
	        HttpEntity ent = new StringEntity(paramstr,Charset.forName("utf-8"));
	        httpPost.setEntity(ent);
	        HttpResponse response = httpClient.execute(httpPost);  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,Charset.forName("utf-8"));  
	            }  
	        }
	        logger.debug("[INFO] rst:" + result);
	    }catch(Exception ex){  
	        ex.printStackTrace();  
	    }finally{
	    	close(httpClient);
	    }
	    
	    return result;
	}
	
	public static String doPostStream(String url, String accountid, String token, String timestamp, byte[] bt){
		CloseableHttpClient httpClient = null;  
	    HttpPost httpPost = null;  
	    String result = null;  
	    try{
	        logger.debug("[INFO] poststream url:" + url);
	        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
	        httpClient = httpClientBuilder.build();  
	        httpPost = new HttpPost(url);
	        httpPost.setHeader("Accept","application/json");
	        httpPost.setHeader("Content-Type","application/octet-stream;charset=utf-8;");
	        httpPost.setHeader("Authorization",getAuthorization(accountid,timestamp));
	        HttpEntity ent = new ByteArrayEntity(bt);
	        httpPost.setEntity(ent);
	        HttpResponse response = httpClient.execute(httpPost);  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,Charset.forName("utf-8"));  
	            }  
	        }
	        logger.debug("[INFO] rst:" + result);
	    }catch(Exception ex){  
	        ex.printStackTrace();  
	    }finally{
	    	close(httpClient);
	    }
	    
	    return result;
	}
}
