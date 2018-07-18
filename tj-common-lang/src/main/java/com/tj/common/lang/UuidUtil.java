package com.tj.common.lang;

import java.util.UUID;

/**
 * 
 * @Author: wangbing
 * @CreatDate: 2016年03月02日
 * @Version:
 */
public class UuidUtil {
	
	public static String getUuid(){
		UUID uuid = UUID.randomUUID();
		if(uuid == null){
			return null;
		}else{
			return uuid.toString().replace("-", "");
		}
		
	}

}
