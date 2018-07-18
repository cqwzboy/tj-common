package com.tj.common.db.rwsplit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RWSplitAspect {

    private static final Logger logger = LoggerFactory.getLogger(RWSplitAspect.class) ;

    public RWSplitAspect(){
        logger.info("RWSplitAspect 被加载了");
    }

    @Pointcut("@annotation(com.tj.common.db.rwsplit.RWSplit)")
    public void aspect() {
    }

    @Before("aspect()")
    public void before(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature)joinPoint.getSignature()).getMethod().getParameterTypes();
        logger.info("被动态数据源代理的类="+target.getClass().getName());
        logger.info("被动态数据源代理的方法名="+methodName);
        Method method = null;
        try {
            method = target.getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            logger.error("方法="+methodName+"不存在,默认使用写数据源");
            RWSplitDataSourceHolder.setCurrentDataSource(RWSplitDataSourceType.WRITE);
        }

        RWSplit rwSplit = method.getAnnotation(RWSplit.class);
        if(rwSplit != null && rwSplit.value().equals(RWSplitDataSourceType.READ)){
            RWSplitDataSourceHolder.setCurrentDataSource(RWSplitDataSourceType.READ);
        }else{
            RWSplitDataSourceHolder.setCurrentDataSource(RWSplitDataSourceType.WRITE);
        }
    }

    @After("aspect()")
    public void after(JoinPoint point) {
        RWSplitDataSourceHolder.setCurrentDataSource(RWSplitDataSourceType.WRITE);
    }

    @AfterThrowing("aspect()")
    public void afterThrowing(JoinPoint point) {
        RWSplitDataSourceHolder.setCurrentDataSource(RWSplitDataSourceType.WRITE);
    }
}
