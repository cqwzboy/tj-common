package com.tj.common.activemq;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ActiveMQTest {

	public static void main(String[] args) throws InterruptedException {
		
//		ActiveMqClient.receiveMsg("ZSL.TEST.MASTER-SLAVE", DeliveryType.QUEUE, new ReceiveCallBack() {
//			
//			@Override
//			public void call(String msg) {
//				System.out.println(DateUtils.getNowTime(null));
//				System.out.println(msg);
//			}
//		}) ;
//		ActiveMqClient.receiveMapMsg("ZSL_MAP_MSG_TEST", DeliveryType.QUEUE, new ReceiveMapMsgCallBack() {
//			
//			@Override
//			public void call(Map<String, Object> mapMsg) {
//				for (Entry<String,Object> entry : mapMsg.entrySet()) {
//					System.out.println(entry.getKey() + ":" + entry.getValue());
//				}
//			}
//		}) ;
//		for (int i = 0; i < 20; i++) {
//			ActiveMqClient.sendMsg("ZSL.TEST.PAUSE", "你好" + i, DeliveryType.QUEUE);
//		}
//		
//		for (int i = 0; i < 20; i++) {
//			ActiveMqClient.sendMsg("ZSL.TEST.PAUSE_xxx", "你好xxx" + i, DeliveryType.QUEUE);
//		}
//		
//		ActiveMqClient.receiveMsg("ZSL.TEST.PAUSE", DeliveryType.QUEUE, new ReceiveCallBack() {
//			@Override
//			public void call(String msg) {
//				System.out.println(msg);
//				
//			}
//		}) ;
//		
//		ActiveMqClient.receiveMsg("ZSL.TEST.PAUSE_xxx", DeliveryType.QUEUE, new ReceiveCallBack() {
//			@Override
//			public void call(String msg) {
//				System.out.println(msg);
//				
//			}
//		}) ;
//		
//		ActiveMqClient.pauseReceiveMsg("ZSL.TEST.PAUSE", 10, TimeUnit.SECONDS);
//
//		Thread.sleep(5000);
//		
//		for (int i = 30; i < 50; i++) {
//			ActiveMqClient.sendMsg("ZSL.TEST.PAUSE", "你好" + i, DeliveryType.QUEUE);
//		}
//		
//		for (int i = 30; i < 50; i++) {
//			ActiveMqClient.sendMsg("ZSL.TEST.PAUSE_xxx", "你好xxx" + i, DeliveryType.QUEUE);
//		}
//		
		
//		for (int i = 0; i < 10; i++) {
//			ActiveMqClient.sendMsg("netty_ws_test", "发了个新消息给你" + i, DeliveryType.TOPIC);
//		}
//		ActiveMqClient.receiveMsg("netty_ws_test", DeliveryType.TOPIC, new ReceiveCallBack() {
//			
//			@Override
//			public void call(String msg) {
//				System.out.println(msg);
//				
//			}
//		}) ;
//		ActiveMqClient.sendMsg("netty_ws_test", "发了个新消息给你", DeliveryType.TOPIC);
//		for (int i = 0; i < 10; i++) {
//			ActiveMqClient.sendMsg("netty_ws_test", "发了个新消息给你" + i, DeliveryType.TOPIC);
//		}
		
		ActiveMqClient.receiveMsg("ZSL_TEST_MSG", DeliveryType.QUEUE, new ReceiveCallBack(){

			@Override
			public void call(String msg) {
				System.out.println(msg);
				
			}
			
		}) ;
	}

	public static Map<String, Object> getCutMonetyMap(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "CutMoneyEvent");
		map.put("plantform", 111);
		map.put("cutType", "222");
		map.put("userid", "333");
		map.put("amount", 444d);
		map.put("score", 555);
		return map ;
	}
	
	public static Map<String, Object> getRegister1Map(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "TJKRegisterEvent");
		map.put("plantform", 1);
		map.put("registerName", "注册1名称");
		map.put("registerTime", System.currentTimeMillis());
		map.put("userid", "user001");
		map.put("operateUrl", "http://www.test1.com");
		return map ;
	}
	
	public static Map<String, Object> getRegister3Map(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "TJKRegisterEvent");
		map.put("plantform", 3);
		map.put("registerName", "注册3名称");
		map.put("registerTime", System.currentTimeMillis());
		map.put("userid", "user003");
		map.put("operateUrl", "http://www.test3.com");
		return map ;
	}
	
	public static Map<String, Object> getTJKexamResultMap(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "TJKexamResultEvent");
		map.put("plantform", 1);
		map.put("score", 100);
		map.put("userid", "examUser001");
		return map ;
	}
	
	public static Map<String, Object> getTJKGuideMap(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "TJKGuideEvent");
		map.put("plantform", 1);
		map.put("operateUrl", "http://www.test.com");
		map.put("userid", "user张三");
		map.put("text", "text文本");
		return map ;
	}
	
	public static Map<String, Object> getNewDocMap(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "NewDocEvent");
		map.put("plantform", 1);
		map.put("merchantName", "商家名称");
		map.put("userid", "user001");
		map.put("docName", "doc名称");
		return map ;
	}
	
	public static Map<String, Object> getPerformanceMap(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "PerformanceEvent");
		map.put("plantform", 1);
		map.put("timeString", "2016-12-29");
		map.put("userid", "user001");
		map.put("detail", "detail信息");
		return map ;
	}
	
	public static Map<String, Object> getWxMessageMap(){
		Map<String, Object> map  =new HashMap<String, Object>();
		map.put("com.espertech.esper.adapter.InputAdapter_maptype", "WxMessageEvent");
		map.put("merchantId", 11111);
		map.put("msgId", "msg001");
		map.put("params", "params参数");
		return map ;
	}
}
