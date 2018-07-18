package com.tj.common.partner.phone;

import java.util.ArrayList;
import java.util.List;

/**
 * 电话号码查询工具类
 * @author silongz
 *
 */
public class PhoneUtil {

	private static List<PhoneQuerier> phoneQuerierChain = new ArrayList<PhoneQuerier>() ;
	
	static{
		phoneQuerierChain.add(new BaiduPhoneQuerier()) ;
		phoneQuerierChain.add(new ThreeSixZeroPhoneQuerier()) ;
		//TODO 在责任链上自行添加其它的第三方实现
	}
	
	/**
	 * 查询号码归属地
	 * @param phoneNo
	 * @return
	 */
	public static PhoneInfo queryPhone(String phoneNo) {
		if(phoneNo == null || "".equals(phoneNo.trim())){
			return null ;
		}
		for (PhoneQuerier phoneQuerier : phoneQuerierChain) {
			PhoneInfo p = phoneQuerier.queryPhone(phoneNo) ;
			if(p != null){
				return p ;
			}
		}
		return null ;
	}
}
