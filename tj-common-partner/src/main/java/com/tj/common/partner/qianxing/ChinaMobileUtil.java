package com.tj.common.partner.qianxing;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tj.common.http.HttpClientUtil;
import com.tj.common.http.HttpConfig;
import com.tj.common.http.HttpProcessException;

public class ChinaMobileUtil {
	
	private static final String url = "http://wangting.18pingtai.cn:8080/tbphone/query?sign=%s&timestamp=%s&v=1.0&sell_id=1050035697&method=queryBalance&format=json&biz_paras=%s&sign_method=MD5";
	
	public static String getBalance(String phoneNo){
		if (StringUtils.isBlank(phoneNo)) {
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("isp", "");
		params.put("queryType", "1");
		params.put("busiCode", "100000");
		params.put("phoneNo", phoneNo);
		Map<String, String> sign = new HashMap<String, String>();
		sign.put("timestamp", DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		sign.put("content", JSON.toJSONString(params));
		String orignSign = String.format("biz_paras=%s&format=json&method=queryBalance&sell_id=1050035697&sign_method=MD5&timestamp=%s&v=1.0sdf556sdfdfd8854544sdf",
				sign.get("content"),sign.get("timestamp"));
		sign.put("sign", DigestUtils.md5Hex(orignSign.getBytes()));
		String signedUrl = null;
		try {
			signedUrl = String.format(url, sign.get("sign"), sign.get("timestamp"), URLEncoder.encode(sign.get("content"), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String resultStr = null;
		try {
			resultStr = HttpClientUtil.get(HttpConfig.custom().url(signedUrl));
		} catch (HttpProcessException e) {
			e.printStackTrace();
		}
		JSONObject result = JSON.parseObject(resultStr).getJSONObject("result");
		if ("0000".equals(result.get("code"))) {
			return result.getJSONObject("bizResp").getString("accountBalance");
		}
		return null;
	}
	
}
