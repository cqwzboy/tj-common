package com.tj.common.lang;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Bean工具
 *
 * @author Lilx
 * @since 2017/8/18
 */
public abstract class BeanUtils extends org.springframework.beans.BeanUtils {
    private static String FASTJSON_AUTOTYPE_ERROR_KEY_WORDS = "autoType is not support";

    /**
     * 深度拷贝对象
     *
     * @param object 要拷贝的对象
     * @return 返回对象的一个深度拷贝
     */
    public static <T> T deepClone(T object) {
        try {
            return JSON.parseObject(JSON.toJSONString(object, SerializerFeature.WriteClassName), new TypeReference<T>() {});
        } catch (JSONException e) {
            if (e.getMessage().contains(FASTJSON_AUTOTYPE_ERROR_KEY_WORDS)) {
                fastjsonAutoTypeSupport(object);
                return JSON.parseObject(JSON.toJSONString(object, SerializerFeature.WriteClassName), new TypeReference<T>() {});
            } else {
                throw e;
            }
        }
    }

    /**
     * 深度拷贝对象
     *
     * @param object 要拷贝的对象
     * @param filter FastJson的序列化过滤器,可用以过滤不需要克隆的属性
     * @return 返回对象的一个过滤后的深度拷贝
     */
    public static <T> T deepClone(T object, SerializeFilter filter) {
        try {
            return JSON.parseObject(JSON.toJSONString(object, filter, SerializerFeature.WriteClassName), new TypeReference<T>() {});
        } catch (JSONException e) {
            if (e.getMessage().contains(FASTJSON_AUTOTYPE_ERROR_KEY_WORDS)) {
                fastjsonAutoTypeSupport(object);
                return JSON.parseObject(JSON.toJSONString(object, filter, SerializerFeature.WriteClassName), new TypeReference<T>() {});
            } else {
                throw e;
            }
        }
    }

    private static void fastjsonAutoTypeSupport(Object object) {
        // fastJson安全配置
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String packageName = object.getClass().getPackage().getName();
        ParserConfig.getGlobalInstance().addAccept(packageName);
    }

	public static void copyPropertiesIgnoreNull(Object source, Object target) throws BeansException {
		copyPropertiesIgnoreNull(source, target, null, (String[]) null);
	}

	public static void copyPropertiesIgnoreNull(Object source, Object target, Class<?> editable) throws BeansException {
		copyPropertiesIgnoreNull(source, target, editable, (String[]) null);
	}

	public static void copyPropertiesIgnoreNull(Object source, Object target, String... ignoreProperties) throws BeansException {
		copyPropertiesIgnoreNull(source, target, null, ignoreProperties);
	}

	public static void copyPropertiesIgnoreNull(Object source, Object target, Class<?> editable, String... ignoreProperties)
			throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
						"] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null &&
							ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							if (null == value) {
								continue;
							}
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
						catch (Throwable ex) {
							throw new FatalBeanException(
									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
	}
}
