package com.tj.common.web.servlet;

import groovy.lang.GroovyShell;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * groovy方式执行请求处理器
 * @author silongz
 *
 */
public class GroovyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7423643710619841902L;

	private static final String GROOVY_USER = "groovy";

	private static final String GROOVY_PWD = "groovy_itaojin";

	private final static GroovyShell groovy = new GroovyShell();
	
	private final static String DEFAULT_PACKAGE = "import com.tj.common.web.spring.* ;" ;
	
	private boolean isPermitAccess(HttpServletRequest request) {
		String userName = request.getParameter("user");
		String password = request.getParameter("password");
		if (GROOVY_USER.equals(userName) && GROOVY_PWD.equals(password)) {
			return true;
		}
		return false;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		if (isPermitAccess(request)) {
			String groovyInput = (request.getParameter("groovyInput") == null) ? "" : (String) request.getParameter("groovyInput");

			Runtime r = Runtime.getRuntime();
			long m = 1024 * 1024;
			long freeMemory = r.freeMemory() / m;
			long totalMemory = r.totalMemory() / m;

			if (StringUtils.isNotBlank(groovyInput)) {
				String groovyOutput = "";
				try {
					if (groovy != null) {
						Object obj = groovy.evaluate(DEFAULT_PACKAGE.concat(groovyInput));
						if(obj != null){
							groovyOutput = JSON.toJSONString(obj) ;
						}
					} else {
						groovyOutput = "Groovy is null!";
					}
				} catch (Throwable e) {
					groovyOutput = e.getMessage();
				} finally {
					try {
						StringBuilder sb = new StringBuilder();
						sb.append("【totalMemory=");
						sb.append(totalMemory);
						sb.append("】");
						sb.append("\r\n");
						sb.append("【freeMemory=");
						sb.append(freeMemory);
						sb.append("】");
						sb.append("\r\n");
						sb.append("【groovy result=");
						sb.append(groovyOutput);
						sb.append("】");
						response.setContentType("text/html; charset=UTF-8");
						response.getWriter().print(sb.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}

}