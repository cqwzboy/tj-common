package com.tj.common.partner;

import java.util.Date;
import java.util.List;
//
import com.tj.common.lang.DateUtils;
import com.tj.common.partner.qiniu.QiniuUtil;
//
public class QiniuTest {
//
	public static void main(String[] args) {
		
//		byte[] fileData = "曾司龙的测试文件内容123456".getBytes() ;
		
//		List<String> list = QiniuUtil.listFile(null);
		
//		for (String string : list) {
//			System.out.println(string);
//		}
		
//		String s = QiniuUtil.upload("ddd", "曾司龙测试.txt", fileData) ;
//		System.out.println(s);
//		QiniuUtil.delete("ddd", "曾司龙测试");
//		QiniuUtil.batchdelete("ccc") ;
//		QiniuUtil.delete(config) ;
		
	    System.out.println(DateUtils.getNowTime("yyyy-MM"));
		QiniuUtil.deleteFile("record", 3);
	}

}
