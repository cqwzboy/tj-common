package com.tj.common.partner.phone;

/**
 * 号码归属地信息
 * @author silongz
 *
 */
public class PhoneInfo {

	/**
	 * 所属省份
	 */
	private String province ;
	
	/**
	 * 所属城市
	 */
	private String city ;
	
	/**
	 * 运营商
	 */
	private String sp ;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}

	@Override
	public String toString() {
		return "PhoneInfo [province=" + province + ", city=" + city + ", sp=" + sp + "]";
	}
	
}
