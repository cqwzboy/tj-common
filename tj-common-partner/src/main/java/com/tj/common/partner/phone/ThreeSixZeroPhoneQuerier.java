package com.tj.common.partner.phone;

import com.alibaba.fastjson.JSONObject;
import com.tj.common.http.HttpClientUtil;
import com.tj.common.http.HttpConfig;
import com.tj.common.partner.phone.PhoneInfo;

/**
 * 360号码归属地查询
 * 
 * @author silongz
 *
 */
public class ThreeSixZeroPhoneQuerier implements PhoneQuerier {

	private static String checkPhoneUrl = "http://cx.shouji.360.cn/phonearea.php?number=";

	@Override
	public PhoneInfo queryPhone(String phoneNo) {
		try {
			String url = checkPhoneUrl + phoneNo;
			HttpConfig config = HttpConfig.custom().url(url);
			String temp = HttpClientUtil.get(config);
			//解析json结果
			//{"code":0,"data":{"province":"\u56db\u5ddd","city":"\u6210\u90fd","sp":"\u7535\u4fe1"}}
			JSONObject obj = JSONObject.parseObject(temp);
			JSONObject dataJson = obj.getJSONObject("data");
			String province = dataJson.getString("province");
			String city = dataJson.getString("city");
			String sp = dataJson.getString("sp");
			PhoneInfo phoneInfo = new PhoneInfo();
			phoneInfo.setProvince(province);
			phoneInfo.setCity(city);
			phoneInfo.setSp(sp);
			return phoneInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
