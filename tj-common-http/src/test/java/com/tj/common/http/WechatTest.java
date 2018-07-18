package com.tj.common.http;

public class WechatTest {

	private static String baseUrl = "http://localhost:6080/weixin/action/fortest/message/wxb50649045095af46?signature=77d48830e335a66b3eaf1eee3cd6145f21ee6816&timestamp=1514525351&nonce=73089323&openid=ooSgytyB6fwp1aOVupyPc1GlNXjE&encrypt_type=a%20es&msg_signature=7d7c61084a4415d75885e69dbf438dfb0e255e47" ;
	public static void main(String[] args) throws Exception {
		HttpConfig config = HttpConfig.custom().url(baseUrl);
		String xml = "<xml>哈哈哈哈</xml>" ;
		String s = HttpClientUtil.sendXml(config, xml) ;
		System.out.println("xxx=" + s);
	}
}
