package com.tj.common.lang;

import java.util.Random;

/**
 * 生成随机数工具类
 * @author silongz
 *
 */
public class RandomUtils {

	private final static Random rd = new Random();
	private static final String INT = "0123456789";
	private static final String STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * 生成指定长度，且只包含大小写字母的随机字符串
	 * @param length
	 * @return
	 */
	public static String randomStr(int length) {
		return random(length, RandomType.STRING);
	}

	/**
	 * 生成指定长度，且只包含0到9的正整数随机字符串
	 * @param length
	 * @return
	 */
	public static String randomInt(int length) {
		return random(length, RandomType.INT);
	}

	/**
	 * 生成指定长度，包含大小写字母和0到9的正整数的随机字符串
	 * @param length
	 * @return
	 */
	public static String randomAll(int length) {
		return random(length, RandomType.ALL);
	}

	/**
	 * 基于java random实现
	 * @param length
	 * @param rndType
	 * @return
	 */
	private static String random(int length, RandomType rndType) {
		StringBuilder s = new StringBuilder();
		char num = 0;
		for (int i = 0; i < length; i++) {
			if (rndType.equals(RandomType.INT))
				num = INT.charAt(rd.nextInt(INT.length()));// 产生数字0-9的随机数
			else if (rndType.equals(RandomType.STRING))
				num = STR.charAt(rd.nextInt(STR.length()));// 产生字母a-z的随机数
			else {
				num = ALL.charAt(rd.nextInt(ALL.length()));
			}
			s.append(num);
		}
		return s.toString();
	}

	private static enum RandomType {
		INT, STRING, ALL
	}

}
