package com.tj.common.io.ssh2;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;

/**
 * ssh操作模板类
 * @author silongz
 *
 */
public class SshTemplate {

	private SshTemplate(){
		
	}
	
	/**
	 * 执行命令的模板方法
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @param callback
	 * @return
	 */
	public static <T> T executeCommand(String ip, int port, String username, String password , SshExecuteCallBack callback){
		Connection conn= getConnection(ip, port, username, password) ;
		Session session = openSession(conn) ;
		T result = callback.excute(session) ;
		close(conn, session);
		return result ;
	}
	
	/**
	 * 获取连接
	 * @param ip
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	private static Connection getConnection(String ip, int port, String username, String password){
		if (StringUtils.isBlank(ip)) {
			throw new IllegalArgumentException( "ip为空" );
		}
		if (port < 0) {
			throw new IllegalArgumentException( "端口为负" );
		}
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException( "用户名为空" );
		}
		Connection conn = null;
		try {
			conn = new Connection( ip, port );
			conn.connect( null, 2000, 2000 );
			boolean isAuthenticated = conn.authenticateWithPassword( username, password );
			if ( isAuthenticated == false ) {
				throw new IllegalArgumentException( "SSH 认证失败 [ username: " + username + ", password: " + password + "]" );
			}
			return conn ;
		}catch( IOException e ){
			throw new RuntimeException( "获取SSH Connection 异常",e );
		}
	}
	
	/**
	 * 打开session
	 * @param conn
	 * @return
	 */
	private static Session openSession(Connection conn){
		try {
			return conn.openSession();
		} catch (IOException e) {
			throw new RuntimeException( "打开SSH Session 异常",e );
		}
	}
	
	/**
	 * 关闭资源
	 * @param conn
	 * @param session
	 */
	private static void close(Connection conn,Session session) {
		if(conn != null){
			conn.close(); 
		}
		if(session != null){
			session.close(); 
		}
	}
}
