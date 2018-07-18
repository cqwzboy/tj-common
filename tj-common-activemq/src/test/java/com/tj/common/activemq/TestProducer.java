package com.tj.common.activemq;

import com.alibaba.fastjson.JSON;

public class TestProducer {

	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 1; i++) {
//			long start = System.currentTimeMillis() ;
//			ActiveMqClient.sendCronTabMsg("ZSL.TEST.MASTER-SLAVE", "你好yyy" + i, DeliveryType.QUEUE , "*/1 * * * *");
//			System.out.println(DateUtils.getNowTime(null));
//			System.out.println("发送"+i+"完毕，耗时=" + (System.currentTimeMillis() - start + "毫秒"));
//		}
		for (int i = 0; i < 1000; i++) {
//			Map<String,Object> map = new HashMap<String,Object>() ;
//			map.put("key1", "value1_" + i) ;
//			map.put("key2", "value2_" + i) ;
//			map.put("key3", "value3_"+ i) ;
//			map.put("key4", "value4_"+ i) ;
//			MessageBody msgBody = new MessageBody() ;
//			msgBody.setMsgId("msg"+i);
//			msgBody.setSessionPrefix("zsl_test_session");
//			msgBody.setMsgContent("测试消息【"+i+"】");
//			msgBody.setClientId("zsl001");
//			if(i%3==0){
//				msgBody.setClientId("zsl001");
//			}else if(i%3==1){
//				msgBody.setClientId("zsl002");
//			}else if(i%3==2){
//				msgBody.setClientId("zsl003");
//			}
//			String json = JSON.toJSONString(msgBody);
//			ActiveMqClient.sendMsg("wsdc_ws_in", json, DeliveryType.TOPIC);
//			Thread.sleep(1000);
			ActiveMqClient.sendMsg("ZSL_TEST_MSG", "曾司龙的测试消息=" + i, DeliveryType.QUEUE);
		}
		
	}
}
