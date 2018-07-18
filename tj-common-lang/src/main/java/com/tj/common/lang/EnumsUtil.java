package com.tj.common.lang;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;

import com.tj.common.bean.EnumsItem;

/**
 * 枚举类型转换工具类，可将枚举转换成list 或 map
 * @author luhl
 * 2016年9月9日
 */
public class EnumsUtil {
	/**
	 * 把enum封成list，便于前端调用
	 *
	 * @param clazz
	 * @param <T>
	 * @return 例如： model.addAttribute("cateList",Enums.toList(Enums.BrandCate.class));
	 *         <select cname="type" > 
	 *         <c:forEach var="type" items="${cateList}">
	 *         <option value="${type.code}">${type.cname}</option>
	 *         </c:forEach>
	 *         </select>
	 */
	public static <T extends Enum<?>> List<EnumsItem> toList(Class<T> clazz) {
		List<EnumsItem> list= new ArrayList<EnumsItem>();
		if(clazz.isEnum()){  
            T[] ts = clazz.getEnumConstants() ;   
            for(T t : ts){  
                String name = getInvokeValue(t, "getName") ;   
                Enum<?> tempEnum = (Enum<?>) t ;  
                if(name == null){  
                	name = tempEnum.name() ;  
                }  
                String code = getInvokeValue(t, "getCode") ;   
                if(code == null){  
                    code = tempEnum.ordinal() + "";  
                }  
               EnumsItem item =new EnumsItem(name, code);   
               list.add(item);
            }  
        }  
		return list;
	}
	
	/**
	 * 
	 * @author luhl
	 * 2016年9月6日
	 * @param clazz
	 * @return map<code , name>
	 */
	public static <T extends Enum<?>> Map<String,String> toMap(Class<T> clazz) {
		Map<String,String> map = new HashMap<String,String>();
		if(clazz.isEnum()){  
            T[] ts = clazz.getEnumConstants() ;   
            for(T t : ts){  
                String name = getInvokeValue(t, "getName") ;   
                Enum<?> tempEnum = (Enum<?>) t ;  
                if(name == null){  
                	name = tempEnum.name() ;  
                }  
                String code = getInvokeValue(t, "getCode") ;   
                if(code == null){  
                    code = tempEnum.ordinal() + "";  
                }  
                map.put(code, name);
            }  
        }  
		return map;
	}
	
	 static <T> String getInvokeValue(T t , String methodName){   
	        Method method = MethodUtils.getAccessibleMethod( t.getClass() , methodName);   
	        if(null == method){  
	            return null ;   
	        }  
	        try {  
	            String text = method.invoke( t )+"" ;   
	            return text ;   
	        } catch (Exception e) {  
	            return null ;  
	        }  
	    }  
}
