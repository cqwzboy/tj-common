package com.tj.common.web.client;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		AutoUpdateChecker.check("F:" + File.separator + "tj-message-log.rar","F:" + File.separator + "tj_robot_version.txt");
		while(true){
		
		}
	}

}
