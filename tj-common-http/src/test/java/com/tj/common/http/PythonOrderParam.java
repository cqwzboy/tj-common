/**
 * 
 */
package com.tj.common.http;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.tj.common.http.HttpClientUtil;

/**
 * @author xiafan 2017年11月9日
 *
 */
public class PythonOrderParam implements Serializable{

	private static final long serialVersionUID = 6946354663723042128L;
	
	//订单id
	private String ordId;
	
	//订单状态
	private Integer ordSta;
	
	//订单是否更改(0未更改，1更改)
	private Integer ordChg;
	
	//订单成功时间
	private Long ordSuc;
	
	//订单付款时间
	private Long ordPay;

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public Integer getOrdSta() {
		return ordSta;
	}

	public void setOrdSta(Integer ordSta) {
		this.ordSta = ordSta;
	}

	public Integer getOrdChg() {
		return ordChg;
	}

	public void setOrdChg(Integer ordChg) {
		this.ordChg = ordChg;
	}

	public Long getOrdSuc() {
		return ordSuc;
	}

	public void setOrdSuc(Long ordSuc) {
		this.ordSuc = ordSuc;
	}

	public Long getOrdPay() {
		return ordPay;
	}

	public void setOrdPay(Long ordPay) {
		this.ordPay = ordPay;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ordId=" + ordId + "+ordSta=" + ordSta + "+ordChg=" + ordChg + "+ordSuc=" + ordSuc
				+ "+ordPay=" + ordPay ;
	}
	
	public static void main(String[] args) throws Exception {
//		PythonOrderParam parma = new PythonOrderParam();
//		parma.setOrdId("哈哈哈哈哈哈哈");
//		HttpConfig config = HttpConfig.custom().url("http://192.168.0.229:1080/tj-um-web/test1") ;
//		String s = HttpClientUtil.sendJson(config, JSON.toJSONString(parma)) ;
//		System.out.println(s);
		HttpConfig config = HttpConfig.custom().url("http://192.168.8.117:8000/") ;
		String s = HttpClientUtil.sendJson(config, "{\"que\":\"撒打算打算大所多\",\"ordDtl\":[{\"ordChg\":0,\"ordId\":\"88088960760752579\",\"ordPay\":0,\"ordSta\":-2,\"ordSuc\":0}],\"hisAmnt\":0,\"hisDtl\":[9999,7701],\"ordAmnt\":5}") ;
		System.out.println(s);
	}
}
