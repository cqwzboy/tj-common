/**
 * 
 */
package com.tj.common.lang;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * aes加密算法
 * 
 * @author silongz
 *
 */
public class AESUtils {

	/**
	 * 加密
	 * 
	 * @param content
	 *            需要加密的内容
	 * @param keySeed
	 *            加密密码
	 * @return
	 * @throws Exception 
	 */
	public static String encrypt(String content, String keySeed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");

		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(keySeed.getBytes());

		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(byteContent);

		return Base64.encodeBase64String(result);
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param keySeed
	 *            解密密钥
	 * @return
	 */
	public static String decrypt(String content, String keySeed) throws Exception{

		byte[] contentBytes = Base64.decodeBase64(content);
		KeyGenerator kgen = KeyGenerator.getInstance("AES");

		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(keySeed.getBytes());

		kgen.init(128, secureRandom);
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
		Cipher cipher = Cipher.getInstance("AES");// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(contentBytes);

		return new String(result, "utf-8");
	}

}
