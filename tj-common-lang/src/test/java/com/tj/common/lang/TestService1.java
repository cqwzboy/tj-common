package com.tj.common.lang;


public class TestService1 implements TestService {

	public void test() {
		System.out.println("我是实现1");
	}

	public boolean isMatched(int type) {
		if(type == 1){
			return true;
		}
		return false ;
	}

}
