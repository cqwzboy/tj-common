package com.tj.common.sms;

import java.util.Map;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tj.common.http.HttpClientUtil;
import com.tj.common.http.HttpConfig;
import com.tj.common.http.HttpHeader;
import com.tj.common.http.HttpProcessException;



/**
 * 沃动科技短信客户端
 * @author wangch
 *
 */
public class MovekSmsClient {

	private static final Logger logger = LoggerFactory.getLogger(MovekSmsClient.class);
	
	/**
	 * 发送常规短信 , 只需要传 mobile 和   content<br/>
	 * mobile:手机号码,多个号码以逗号或分号分隔
	 * @param smsBean
	 * 
	 * @return
	 */
	public static JSONObject sendSimpleSms(MovekSmsClientSmsBean smsBean) {
		String post = "";
		Map<String, Object> map = MovekSmsConfig.getMap();
		if (smsBean.getSendtime()!=null) {
			map.put("sendtime", smsBean.getSendtime());
		}
		map.put("mobile" ,smsBean.getMobile());
		map.put("content" ,smsBean.getContent());
		map.put("action" , "send");
		String url = MovekSmsConfig.getSimpleUrl();
		HttpConfig config = HttpConfig.custom().url(url).map(map) ;
		try {
			post = HttpClientUtil.post(config);
		} catch (HttpProcessException e) {
			logger.error("*******************发送短信失败***************"+e.getMessage(),e);
			e.printStackTrace();
		}	
		return JSONObject.parseObject(post);
	} 
	
	
	
	/**
	 * 发送模板短信 , 只需要传 msg(短信模板) 和 params(短信参数) <br/>
	 *  msg(短信模板):其中的变量用“{$var}”来替代。例如：“{$var},你好！，请你于{$var}日参加活动”，该短信中具有两个变量参数。编码为UTF-8格式。<br/>
	 * 	params: 变量短信的参数组  <br/>
	 *  		编码为UTF-8格式 <br/>
	 *			每一组参数之间用英文“；”间隔 <br/>
	 *			每一组参数内部用英文“，”间隔，其中第一个参数为手机号码，第二个参数为模板中第一个变量，第三个参数为模板中第二个变量，以此类推。<br/>
	 *			例如：13800210000,李先生,2013-01-01;13500210000,王先生，2013-01-15   <br/>
	 *  		表明提交了两个变量组，其中手机号码分别为：13800210000和13500210000。模板变量分别为：李先生，2013-01-01和王先生，2013-01-15<br/>
	 *			格式不符的参数，系统自动过滤掉。<br/>
	 * @param smsBean   
	 * @return
	 */
	public static JSONObject sendParaSms(MovekParamsSmsClientSmsBean smsBean) {
		String post = "";
		Map<String, Object> map = MovekSmsConfig.getMap();
		if (smsBean.getSendtime()!=null) {
			map.put("sendtime", smsBean.getSendtime());
		}
		map.put("action" , "var");
		map.put("msg", smsBean.getMsg());
		map.put("params", smsBean.getParams());
		String url = MovekSmsConfig.getParaUrl();
		Header[] headers = HttpHeader.custom().contentType("application/x-www-form-urlencoded").build();
		HttpConfig config = HttpConfig.custom().url(url).map(map).headers(headers);
		try {
			post = HttpClientUtil.post(config);
		} catch (HttpProcessException e) {
			logger.error("*******************发送短信失败***************"+e.getMessage(),e);
			e.printStackTrace();
		}	
		return JSONObject.parseObject(post);
	}
	
	/**
	 * 状态报告抓取
	 * 	注：状态被获取后，将不再显示。暂时无返回状态返回时，显示为空。
	 * 		关机、不在服务区、网络信号不稳定、SIM 卡存储空间满等问题，将导致状态返回较慢。
	 * @param smsBean
	 * @return
	 */
	public static JSONObject getStatus() {
		String post = "";
		Map<String, Object> map = MovekSmsConfig.getMap();
		map.put("action" , "query");
		String url = MovekSmsConfig.getStatusUrl();
		HttpConfig config = HttpConfig.custom().url(url).map(map) ;
		try {
			post = HttpClientUtil.post(config);
		} catch (HttpProcessException e) {
			logger.error("*******************状态报告抓取失败***************"+e.getMessage(), e);
			e.printStackTrace();
		}	
		return JSONObject.parseObject(post);
	}
	
	public static void main(String[] args) {
		JSONObject sendSimpleSms = MovekSmsClient.getStatus();
		System.out.println(sendSimpleSms);
	}
}
