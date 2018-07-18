package com.tj.common.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java调用系统命令
 * @author silongz
 *
 */
public class CommandUtils {

	private static Logger logger = LoggerFactory.getLogger(CommandUtils.class);
	
	/**
	 * java执行系统命令
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public static boolean excute(String command) throws IOException {
		
		String osName = System.getProperty("os.name") ;
		try {
			String[] cmdarray = null ;
			if(osName.toLowerCase().contains("windows")){
				cmdarray = new String[]{"cmd.exe","/c",command}; 
			}else{
				cmdarray = new String[]{"/bin/sh","-c",command}; 
			}
			Process process = Runtime.getRuntime().exec(cmdarray);
			String line = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			// 读取ErrorStream很关键，防止线程挂机
			while ((line = br.readLine()) != null) {
				logger.error("本次命令="+command);
				logger.error("命令执行输出错误信息="+line);
			}
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = br.readLine()) != null) {
				logger.info(line);
			}
			if (process.waitFor() != 0) {
				logger.error("命令执行出错");
				logger.error("本次命令="+command);
				return false;
			}
		} catch (Exception e) {
			logger.debug("java exec执行命令异常", e);
			throw new IOException("java exec执行命令异常", e);
		}
		return true;
	}
}
