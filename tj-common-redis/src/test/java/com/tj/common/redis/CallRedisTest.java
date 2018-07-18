package com.tj.common.redis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;

public class CallRedisTest {

	public static void main(String[] args) throws IOException {

		RedisClient client = RedisClientFactory.getClient(RedisClientType.SIMPLE) ;
		
		File file = new File("F:\\makecallaction.csv") ;
		if(!file.exists()){
			file.createNewFile() ;
		}
		OutputStream os = new FileOutputStream(file) ;
		String s = "";
		for (int i = 1000; i < 1200; i++) {
			String userdata = getCallId() ;
			String calloutid= "calloutid" + i ;
			JSONObject callinfo = new JSONObject();
			callinfo.put("to", "11110000");
			callinfo.put("supplier", "rl");
			callinfo.put("merchant_id", 387);
			callinfo.put("call_type", "1");
			callinfo.put("agent_id", 1538);
			callinfo.put("merchantCalloutId", "123456");	
			client.setStr("call-" + userdata, callinfo.toJSONString()) ;
			if(i != 599){
				s += userdata + "," + calloutid + "\r\n" ; 
			}else{
				s += userdata + "," + calloutid ;
			}
		}
		os.write(s.getBytes());
		os.flush();
		os.close();
	}

	private static String getCallId(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-","");
	}
}
