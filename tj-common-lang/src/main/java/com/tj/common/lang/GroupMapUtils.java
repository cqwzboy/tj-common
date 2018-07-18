package com.tj.common.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * 分组数据
 * 
 * @author Wen
 *
 * @CreateDate 2016年11月9日
 */
public class GroupMapUtils {

	/**
	 * 把list整理为KEY-List结构
	 * 
	 * @param schList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, T> Map<K, List<T>> trimGroupMap(String attrKey, List<T> schList) {
		Map<K, List<T>> map = new HashMap<K, List<T>>((int) (schList.size() * 1.5));
		List<T> temp = null;
		for (T s : schList) {
			K key = null;
			try {
				key = (K) PropertyUtils.getNestedProperty(s, attrKey);
			} catch (Exception e) {
				throw new IllegalArgumentException("无法找到对应的属性 " + attrKey,e);
			}
			temp = map.get(key);
			if (temp == null) {
				temp = new ArrayList<T>();
				map.put(key, temp);
			}
			temp.add(s);
		}
		return map;
	}
	
	/**
	 * 根据list某个key转map
	 * @param attrKey
	 * @param schList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K, T> Map<K, T> conventListToMap(String attrKey, List<T> schList) {
		Map<K, T> map = new HashMap<K, T>((int) (schList.size() * 1.5));
		for (T s : schList) {
			K key = null;
			try {
				key = (K) PropertyUtils.getNestedProperty(s, attrKey);
			} catch (Exception e) {
				throw new IllegalArgumentException("无法找到对应的属性 " + attrKey,e);
			}
			map.put(key, s);
		}
		return map;
	}
}
