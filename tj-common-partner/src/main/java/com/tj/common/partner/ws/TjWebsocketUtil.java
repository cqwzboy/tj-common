package com.tj.common.partner.ws;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tj.common.activemq.ActiveMqClient;
import com.tj.common.activemq.DeliveryType;

public class TjWebsocketUtil {


	private static Logger logger = LoggerFactory.getLogger(TjWebsocketUtil.class);

	private static final String MSG_MQ = "wsdc_ws_in";
	/**
	 * 主账号鉴权方式推送消息
	 * @param receiver
	 * @param content
	 * @param domain
	 * @param bool 消息是否加密 
	 */
	public static String pushMsgMq(String receiver, String content, String domain, boolean bool) {
		// get timestamp
		try {
			if(bool){
				content = Base64.encodeBase64String(content.getBytes("GBK"));
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Encoding message content failed", e);
		}
		String timestamp = String.valueOf(new Date().getTime());
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("biz", 1);
		jsonMsg.put("target", receiver);
		jsonMsg.put("mid", timestamp);
		jsonMsg.put("msgContent", content);
		jsonMsg.put("msgDomain", domain);
		jsonMsg.put("msgDateCreated", timestamp);
//		logger.info("----发送消息："+jsonMsg.toJSONString());
		ActiveMqClient.sendMsg(MSG_MQ, jsonMsg.toJSONString(), DeliveryType.TOPIC);
		String res = "";
		return res;
	}
}
