package com.jrwong.modules.common.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
 
/**
 * 使用com.fasterxml.jackson进行json的转换
 * @author liangyi
 * @Time 2016年1月5日上午10:07:52
 *
 */
public final class JsonUtils {
	private static ObjectMapper objectmapper;
	static {
		objectmapper = new ObjectMapper();
		objectmapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		// 无效的映射字段不会抛出异常
		objectmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	/**
	 * 将obj转换为json字符串
	 * @author liangyi
	 * @Time 2016年1月5日上午10:08:56
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object obj) throws Exception {
		return objectmapper.writeValueAsString(obj);
	}
	
	/**
	 * 将json字符串转换为javabean
	 * @author liangyi
	 * @Time 2016年1月5日上午10:09:27
	 *
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T toBean(String jsonStr, Class<T> clazz) throws Exception {
		return objectmapper.readValue(jsonStr, clazz);
	}
	
}
