package com.tj.common.web.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.alibaba.fastjson.JSONObject;
import com.tj.common.lang.JsonResultBuilder;

/**
 * spring异常处理器
 * 
 * @author silongz
 *
 */
public class SpringExceptionResolver extends AbstractHandlerExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(SpringExceptionResolver.class);
	private final static String CONTENT_JOSON = "application/json;utf-8";

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception e) {
		logger.error(e.getMessage(), e);
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType(CONTENT_JOSON);
			response.getWriter().print(JSONObject.toJSON(JsonResultBuilder.buildFailure(e.getMessage())));
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e1) {
			logger.error(e1.getMessage());
		} 
		return null;
	}

	/**
	 * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#buildLogMessage(java.lang.Exception,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String buildLogMessage(Exception ex, HttpServletRequest request) {
		return ex.getMessage();
	}

	/**
	 * @see org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver#getOrder()
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
