package com.tj.common.partner.ronglian;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;
import com.tj.common.http.HttpClientUtil;
import com.tj.common.http.HttpConfig;
import com.tj.common.http.HttpProcessException;

public class YtxIMUtil {


	private static final String token = "9d6f0977f2854eca909051c81fc46ec1";
	
	public static String ivrPath;
	
	
	public  void setIvrPath(String ivrPath) {
		YtxIMUtil.ivrPath = ivrPath;
	}


	/**
	 * 主账号鉴权方式推送消息
	 * @param receiver
	 * @param content
	 * @param domain 附属信息
	 */
	public static String pushMsg(String receiver, String content, String domain) {
		// get timestamp
		String timestamp = HttpUtil.getTimeStamp();
		try {
			content = Base64.encodeBase64String(content.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// build url
		String url = YTXConfig.getYtxUrl() + "/Accounts/" + YTXConfig.getAccount() + "/IM/PushMsg?sig="
				+ HttpUtil.getSigParameter(YTXConfig.getAccount(), YTXConfig.getToken(), timestamp);
		String[] _reciever={receiver};
		JSONObject param = new JSONObject();
		param.put("pushType", 1);
		param.put("appId", YTXConfig.getAppid());
		param.put("sender", YTXConfig.getSyssender());
		param.put("receiver", _reciever);
		param.put("msgType", "1");
		param.put("msgContent", content);
		param.put("msgDomain", domain);
		String res = HttpUtil.doPost(url, YTXConfig.getAccount(), YTXConfig.getToken(), timestamp,
				param);
		return res;
	}
	
	public static String getSubAccount(Long customerid){
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("agentid",customerid) ;
		HttpConfig config = HttpConfig.custom().url(ivrPath+"/call/agents").map(map) ;
		String respStr = null;
		try {
			respStr = HttpClientUtil.post(config);
		} catch (HttpProcessException e) {
			e.printStackTrace();
		}
		return respStr;
	}
	
	/**
	 * 注意：sig参数由用户请求自己的服务器生成，sig规则：MD5(appid+userName+timestamp(yyyyMMddHHmmss)+apptoken); MD5为标准MD5格式。
	 * timestamp和LoginBuilder中设置的时间戳必须一样。当通讯账号密码登录时，可以不传sig
	 */
	public static String getSig(String userName,String dateyyyyMMddHHmmss){
		String s=YTXConfig.getAppid()+userName+dateyyyyMMddHHmmss+YTXConfig.getToken();
		return MD5Util.MD5(s);
	}
	
	
	/**
	 * 淘金客当前规则为:前缀+tjkId
	 * @param param
	 * @return
	 */
	public static String getTjkUserName(String param){
		return YTXConfig.getTjkUserAccountPrefix()+param;
	}
	
//	private static String getSysSender(){//特殊发送者
//		return "wxsender_9999";
//	}
	public static void main(String[] args) {
		getSig("111","222");
//		pushMsg("2689802fa8054ead8d02bc570d3bc840", "test", "");
	}
}
