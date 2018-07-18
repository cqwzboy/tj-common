package com.tj.common.kafka;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		Calendar cl = Calendar.getInstance() ;
//		cl.setTimeInMillis(1516271282 * 1000);
		BigInteger bi = new BigInteger(String.valueOf(1516271282)) ;
		BigInteger bii = new BigInteger(String.valueOf(1000)) ;
		long ss = bi.multiply(bii).longValue() ;
		cl.setTimeInMillis(ss);

		System.out.println(cl.getTime());
	}

}
