package com.tj.common.partner.ronglian;

import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5 hash工具类
 * @author silongz
 *
 */
public class MD5Util {

	/**
	 * MD5 hash
	 * @param key
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encodeMD5(String key , String str) throws Exception {
		if(str == null || "".equals(str.trim())){
			return null ;
		}
		if(key == null || "".equals(key.trim())){
			return DigestUtils.md5Hex(str) ;
		}else{
			return DigestUtils.md5Hex(key.concat(str).concat(key)) ;
		}
	}
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void main(String[] args) throws Exception {
		System.out.println(encodeMD5("11111111", "userName张三"));
	}
}
