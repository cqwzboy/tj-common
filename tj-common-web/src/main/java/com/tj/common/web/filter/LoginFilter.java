package com.tj.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tj.common.lang.MD5Utils;

public class LoginFilter implements Filter{

	public static final String LOGIN_TICKET = "login_ticket" ;
	
	public static final String USER_NAME = "admin" ;
	
	public static final String PASS_WORD = "tj_admin123456" ;
	
	public static final String ENC_SAULT = "123456!@#$%^" ;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request ;
		HttpServletResponse httpResponse = (HttpServletResponse)response ;
		String url = httpRequest.getRequestURL().toString() ;
		String contextPath = httpRequest.getContextPath() ;
		if(!url.contains("/login")) {
			Cookie[] cookies = httpRequest.getCookies() ;
			boolean isLogin = false ;
			if(cookies != null && cookies.length > 0){
				for (Cookie cookie : cookies) {
					String cookieName = cookie.getName() ;
					if(LOGIN_TICKET.equals(cookieName)){
						String cookieValue = cookie.getValue() ;
						try {
							String md5Sign = MD5Utils.encodeMD5(ENC_SAULT, USER_NAME.concat(PASS_WORD)) ;
							if(md5Sign.equals(cookieValue)){
								isLogin = true ;
							}
						} catch (Exception e) {
							throw new RuntimeException("验证用户登录签名出错",e) ;
						}
					}
				}
			}
			if (!isLogin) {
				httpResponse.sendRedirect(contextPath.concat("/login.html"));
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}

}
