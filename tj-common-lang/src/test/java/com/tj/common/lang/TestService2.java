package com.tj.common.lang;


public class TestService2 implements TestService {
	
	public void test() {
		System.out.println("我是实现2");
	}

	public boolean isMatched(int type) {
		if(type == 2){
			return true;
		}
		return false ;
	}
	
}
