package com.tj.common.web.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

public class SpringContextHolder {

	private static ApplicationContext applicationContext;

	public static void setApplicationContext(ApplicationContext context) {
		if (applicationContext == null) {
			applicationContext = context;
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static void destoryBean(String beanName){
		if(StringUtils.isBlank(beanName)){
			return ;
		}
		Object beanInstance = applicationContext.getBean(beanName) ;
		if(beanInstance == null){
			return ;
		}
		DefaultListableBeanFactory acf = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
		acf.destroyBean(beanName, beanInstance);
	}
}
