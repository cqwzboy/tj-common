package com.tj.common.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 基于Math.random()实现的权重随机工具类
 * @author silongz
 *
 */
public class RandomUtil {
	
	/**
	 * 基于权重，随机选择一个
	 * @param weightList --- 权重列表
	 * @return 权重列表的索引
	 */
	public static int pickWithWeight(List<Double> weightList) {
		if (weightList == null || weightList.isEmpty()) {
			return -1;
		}

		int blockSize = weightList.size();

		// 计算总权重
		double sumRate = 0d;
		for (double rate : weightList) {
			sumRate += rate;
		}

		//对应权重分块
		List<Double> blockList = new ArrayList<Double>(blockSize);
		Double tempSumRate = 0d;
		for (double rate : weightList) {
			tempSumRate += rate;
			blockList.add(tempSumRate / sumRate);
		}

		// java的random平均概率返回0到1之间的数字
		double nextDouble = Math.random();
		//把随机数加入区块并排序，并返回区块索引
		blockList.add(nextDouble);
		Collections.sort(blockList);
		return blockList.indexOf(nextDouble);
	}
	
	/**
	 * 假定四个小组的权重比为1,3,5,10，调用1000000万次，运行以下测试类会得到类似如下结果
	 *  
	 *  小组=0
		小组被选中次数=52822
		小组概率=5.29%
		=============分割线===========
		小组=1
		小组被选中次数=157935
		小组概率=15.80%
		=============分割线===========
		小组=2
		小组被选中次数=263384
		小组概率=26.34%
		=============分割线===========
		小组=3
		小组被选中次数=525859
		小组概率=52.59%
		=============分割线===========
	 */
	public static void main(String[] args) {
		List<Double> weightList = new ArrayList<Double>() ;
		weightList.add(1d) ;
		weightList.add(3d) ;
		weightList.add(5d) ;
		weightList.add(10d) ;
		int totalTime = 1000000 ;
		Map<Integer,Integer> map = new HashMap<Integer,Integer>() ;
		for (int i = 0; i < totalTime; i++) {
			int x = pickWithWeight(weightList) ;
			if(map.containsKey(x)){
				int time = map.get(x) ;
				time ++ ;
				map.put(x, time) ;
			}else{
				map.put(x, 1) ;
			}
		}
		for (Entry<Integer,Integer> entry : map.entrySet()) {
			System.out.println("小组=" + entry.getKey());
			System.out.println("小组被选中次数=" + entry.getValue());
			BigDecimal groupTimes = new BigDecimal(entry.getValue() * 100) ;
			BigDecimal total = new BigDecimal(totalTime) ;
			System.out.println("小组概率=" + groupTimes.divide(total, 2, RoundingMode.CEILING) +"%");
			System.out.println("=============分割线===========");
		}
	}
}