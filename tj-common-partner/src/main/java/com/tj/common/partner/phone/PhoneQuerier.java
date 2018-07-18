package com.tj.common.partner.phone;

import com.tj.common.partner.phone.PhoneInfo;

/**
 * 号码查询器
 * @author silongz
 *
 */
public interface PhoneQuerier {

	PhoneInfo queryPhone(String phoneNo) ;
}
