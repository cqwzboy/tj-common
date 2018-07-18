package com.tj.common.lang;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * RSA加解密工具类
 * @author silongz
 *
 */
public class RSAUtils {

	private static final int DEFAULT_KEY_SIZE = 1024 ;
	
	private static final String ALGORITHM = "RSA" ;
	
	/**
	 * 生成密钥对数组
	 * @param keySize - 默认1024
	 * @return 数组[0]为公钥，数组[1]为私钥
	 * @throws Exception
	 */
	public static String[] generateKeyPair(Integer keySize) throws Exception{
		if(keySize == null || keySize <= 0){
			keySize = DEFAULT_KEY_SIZE ;
		}
		KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		kpGenerator.initialize(keySize);
		KeyPair keyPair = kpGenerator.generateKeyPair();
		byte[] publicKeyByte = keyPair.getPublic().getEncoded();
		String publicKey = new String(Base64.encodeBase64(publicKeyByte)) ;
		byte[] privateKeyByte = keyPair.getPrivate().getEncoded();
		String privateKey = new String(Base64.encodeBase64(privateKeyByte)) ;
		String [] keyPairString = new String[2] ;
		keyPairString[0] = publicKey ;
		keyPairString[1] = privateKey ;
		return keyPairString ;
	}
	
	/**
	 * 公钥加密
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String data , String publicKey) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		return Base64.encodeBase64String(cipher.doFinal(data.getBytes()));
	}
	
	/**
	 * 私钥解密
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String dencryptByPrivateKey(String data , String privateKey) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
		return new String(cipher.doFinal(Base64.decodeBase64(data)), "UTF-8");
	}
	
	/**
	 * 
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	private static PublicKey getPublicKey(String publicKey) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		return pubKey;
	}
	
	/**
	 * 
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey getPrivateKey(String privateKey) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(privateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PrivateKey priKey = keyFactory.generatePrivate(keySpec);
		return priKey;
	}
	
	public static void main(String[] args) throws Exception {
		String [] keyPair = generateKeyPair(0) ;
		String publicKey = keyPair[0] ;
		String privateKey = keyPair[1] ;
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCT5OW5zfb371ypBuRL5+8Ae9Fj4c7Y2LqSkrKT3GFWsyuhBMmu0XT4/X0i8rPKdjqh30GvTrrheunivdNBcTwhh3ZM2JwI36GQGx/MWe/Zh+XIm+srOG3XOGPkNPe1VngviDKZ45Js2APF64ldx7QHw6h5rqZpU7VCkYZ3qDrk+wIDAQAB" ;
//		String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJPk5bnN9vfvXKkG5Evn7wB70WPhztjYupKSspPcYVazK6EEya7RdPj9fSLys8p2OqHfQa9OuuF66eK900FxPCGHdkzYnAjfoZAbH8xZ79mH5cib6ys4bdc4Y+Q097VWeC+IMpnjkmzYA8XriV3HtAfDqHmupmlTtUKRhneoOuT7AgMBAAECgYBWEQArqBQQZ2jaKNuU3O22r1fEs7VHdqDlCaAI8lhnNy+trfac2OIkwIghXXKJ4VHvv9ge6EdTTs9rl6KoU/h0ewD6zpRSqiakJ1zOO6djhfFyrozuHlVRywwvWIAho1vgWJXvQk6B4F1/yTl8RaL1RGK6+19v7YiG5QJp7CH64QJBANgw4Wj1yy/jmwtGkAXjZXWgD7dEkliN8zDXNcSrYpQo/4sAQItSqd8AhlZpG9V7xCKYcfxrS90yZOjlibM4o0sCQQCvIIsrB128B7kpUm3C4LcGNC9/0/zmNvmiRlBXWGlDFMW6/BnwVnKq8IUjGj8sVU8T+GZ8iVhE/dh1tQbNYwcRAkEArbuKSUjNAi0MWaFY+AJndIEuX/6xBIz+U89+nA+GZr4TreaD9/J0JItNaX+KKKMPWSXc5BZtntJymV5srCPlxQJBAIuU6bdbAKfclAVOcLORve8Q6wb8ZKbtDFz9pA1qJ0PTK6JUSyxXCsAaQlNvGQjx2bx2EOGT6qnalYuDxK8xDbECQQCdDNml7jBfDiNZQ5aGYoTb6VS6nlKc6Qn5P9v1swacDnZAaQk8dR6LGYAITKKEeZT/rD0oxW7ve51HKtMUrF+V" ;
		System.out.println("公钥="+publicKey);
		System.out.println("私钥="+privateKey);
		String msg = "147258" ;
		msg = MD5Utils.encodeMD5("", msg) ;
		System.out.println(msg);
		String enMsg = encryptByPublicKey(msg, publicKey) ;
		System.out.println("加密后的内容="+enMsg);
		System.out.println(dencryptByPrivateKey(enMsg, privateKey));
	}
}
