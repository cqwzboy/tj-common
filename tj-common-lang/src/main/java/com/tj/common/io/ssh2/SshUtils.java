package com.tj.common.io.ssh2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;
import com.trilead.ssh2.util.IOUtils;

/**
 * 基于ssh的客户端工具类
 * @author silongz
 *
 */
public class SshUtils {

	public static final String NEXT_LINE = "\r\n";

	/**
	 * SSH 方式登录远程主机，执行命令,方法内部会关闭所有资源，调用方无须关心。
	 * 
	 * @param ip
	 *            主机ip
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param command
	 *            要执行的命令
	 */
	public static String execute(String ip, int port, String username, String password, final String command) {

		return SshTemplate.executeCommand(ip, port, username, password, new SshExecuteCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public String excute(Session session) {
				BufferedReader read = null;
				try {
					session.execCommand(command);
					StringBuffer sb = new StringBuffer();
					read = new BufferedReader(new InputStreamReader(new StreamGobbler(session.getStdout())));
					String line = "";
					while ((line = read.readLine()) != null) {
						sb.append(line).append(NEXT_LINE);
					}
					return sb.toString();
				} catch (IOException e) {
					throw new RuntimeException("执行ssh命令【" + command + "】异常", e);
				} finally {
					IOUtils.closeQuietly(read);
				}
			}
		});
	}
	
	public static void main(String[] args) {
		String s = execute("192.168.4.251", 22, "root", "741852963", "echo cons | nc 192.168.4.253 2181") ;
		System.out.println(s);
	}
}
