package com.tj.common.web.spring;

import org.springframework.beans.factory.BeanFactory;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.web.context.support.SpringBeanAutowiringSupport;  
  
 
public class SpringBeanUtils extends SpringBeanAutowiringSupport {  
  
    @Autowired  
    private BeanFactory beanFactory;  
  
    private static SpringBeanUtils instance;  
   
    private SpringBeanUtils(){
    	
    }
    
    static {  
        instance = new SpringBeanUtils();  
    }  
  
    public Object getBean(String beanId) {  
        return beanFactory.getBean(beanId) ;
    }  
  
    public static SpringBeanUtils getInstance() {  
        return instance;  
    }  
  
}  