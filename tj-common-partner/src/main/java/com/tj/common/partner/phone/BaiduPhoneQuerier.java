package com.tj.common.partner.phone;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tj.common.http.HttpClientUtil;
import com.tj.common.http.HttpConfig;
import com.tj.common.partner.phone.PhoneInfo;

public class BaiduPhoneQuerier implements PhoneQuerier {

	private static String checkPhoneUrl = "http://mobsec-dianhua.baidu.com/dianhua_api/open/location?tel=";

	@Override
	public PhoneInfo queryPhone(String phoneNo) {
		try {
			String url = checkPhoneUrl + phoneNo;
			HttpConfig config = HttpConfig.custom().url(url);
			String temp = HttpClientUtil.get(config);
			JSONObject obj = JSONObject.parseObject(temp);
			Integer status = obj.getJSONObject("responseHeader").getInteger("status") ;
			if(status != 200) {
				return null ;
			}
			//解析json结果
			//{"response":{"18108096562":{"detail":{"area":[{"city":"成都"}],"province":"四川","type":"domestic","operator":"电信"},"location":"四川成都电信"}},"responseHeader":{"status":200,"time":1523411466214,"version":"1.1.0"}}
			JSONObject detailJson = obj.getJSONObject("response").getJSONObject(phoneNo).getJSONObject("detail") ;
			String province = detailJson.getString("province");
			String sp = detailJson.getString("operator");
			JSONArray cityArray = detailJson.getJSONArray("area") ;
			JSONObject cityJson = cityArray.getJSONObject(0) ;
			String city = cityJson.getString("city") ;
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
