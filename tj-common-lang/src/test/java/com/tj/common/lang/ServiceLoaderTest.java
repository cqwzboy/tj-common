package com.tj.common.lang;

import java.util.ServiceLoader;

public class ServiceLoaderTest {

	public static void main(String[] args) {
		TestService testService = getTestService(1) ;
		testService.test();
		testService = getTestService(2) ;
		testService.test();
	}
	
	private static TestService getTestService(int type){
        
		ServiceLoader<TestService> testServices = ServiceLoader.load(TestService.class,Thread.currentThread().getContextClassLoader()) ;
		for (TestService testService : testServices) {
			if(testService.isMatched(type)){
				return testService ;
			}
		}
		return null ;
	}
}
