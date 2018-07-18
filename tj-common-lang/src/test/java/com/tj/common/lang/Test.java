package com.tj.common.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test {

	public static void main(String[] args) throws Exception {
		File f = new File("D:\\dubbo.txt") ;
		FileReader fr = new FileReader(f) ;
		BufferedReader bfr = new BufferedReader(fr) ;
		List<String> list = new ArrayList<String>() ;
		String s = "" ;
		while((s = bfr.readLine()) != null){
			list.add(s) ;
		}
		bfr.close();
		Collections.sort(list,new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				// TODO Auto-generated method stub
				return o1.compareToIgnoreCase(o2);
			}
		});
		
		for (String string : list) {
			System.out.println(string);
		}
	}
}
