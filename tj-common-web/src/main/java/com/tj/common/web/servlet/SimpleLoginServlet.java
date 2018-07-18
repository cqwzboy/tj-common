package com.tj.common.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.tj.common.lang.JsonResultBuilder.JsonResult;
import com.tj.common.lang.MD5Utils;
import com.tj.common.web.filter.LoginFilter;

public class SimpleLoginServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6363326879301496484L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		JsonResult<String> result = new JsonResult<String>();
		String userName = request.getParameter("userName") ;
		String password = request.getParameter("password") ;
		if(!LoginFilter.USER_NAME.equals(userName) || !LoginFilter.PASS_WORD.equals(password)){
			result.setSuccess(false);
			result.setMsg("用户名或密码错误");
		}else{
			String loginTicket = null;
			try {
				loginTicket = MD5Utils.encodeMD5(LoginFilter.ENC_SAULT, userName.concat(password));
			} catch (Exception e) {
				throw new RuntimeException("简单登录过滤器生成MD5签名串异常",e) ;
			}
			Cookie cookie = new Cookie(LoginFilter.LOGIN_TICKET, loginTicket);
			response.addCookie(cookie);
		}
		try {
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(JSONObject.toJSON(result));
		} catch (IOException e) {
			throw new RuntimeException("登录时，往前端回写json消息串时异常",e) ;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
