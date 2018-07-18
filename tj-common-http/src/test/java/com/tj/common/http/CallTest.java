package com.tj.common.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public class CallTest {

	public static void main(String[] args) throws Exception {
//		String url = "http://192.168.0.229:8189/call/ivr/startservice?appid=appid001&callid=callid001&from=from001&to=01083035991&direction=0" ;
//		String url = "http://192.168.0.229:8189/call/ivr/agentstate?agentid=24&agentstate=2&callid=callid001&number=15984623155" ;
//		String url = "http://192.168.0.229:8189/call/ivr/agentstate?agentid=24&agentstate=3&callid=callid001&number=15984623155" ;
//		String url = "http://192.168.0.229:8189/call/ivr/makecallhangup?agentid=24&endtype=1&callid=callid001&recordurl=http://test.url.com" ;
//		String url = "http://192.168.0.229:8189/call/ivr/stopservice?appid=appid001&agentid=24&callduration=100&callid=callid001&recordurl=http://test123.url.com&recordid=recordid001" ;
//		String url = "http://192.168.0.229:8189/call/ivr/agentstate?agentid=24&agentstate=0&callid=callid001&number=15984623155" ;
//		String url = "http://192.168.0.229:8189/call/ivr/makecallaction/c491e052aa974925a0c45df47a4942a3?appid=appid001&callid=callout001&agentid=24&type=1&number=15984623155" ;
		Map<String,String> map = new HashMap<String,String>() ;
		map.put("number", "10010") ;
		map.put("user_id", "0") ;
		JSONArray ja = new JSONArray() ;
		ja.add(map) ;
		String param = ja.toJSONString();
		System.out.println("param=" + param);
		Map<String,Object> paramMap = new HashMap<String,Object>() ;
		paramMap.put("text", "102") ;
		paramMap.put("param", param) ;
		paramMap.put("port", "10") ;
 		String url = "http://192.168.0.80/api/send_sms" ;
		HttpConfig config = HttpConfig.custom().url(url).map(paramMap);
		String result = HttpClientUtil.post(config) ;
		System.out.println(result);
	}
}
