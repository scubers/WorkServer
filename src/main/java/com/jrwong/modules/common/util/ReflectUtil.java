package com.jrwong.modules.common.util;

import org.apache.commons.collections.map.HashedMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

/**
 * Created by J on 16/8/29.
 */
public class ReflectUtil {

    public static <T> Map<String, Object> beanToMap(T obj) throws IllegalAccessException {
        Class clazz = obj.getClass();
        Map<String, Object> map = new HashedMap();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields ) {
            field.setAccessible(true);

            if (field.get(obj) != null) {
                map.put(field.getName(), field.get(obj));
            }
        }
        return map;
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Object value = map.get(fields[i]);
            if (value != null) {
                fields[i].setAccessible(true);
                fields[i].set(obj, value);
            }
        }
        return obj;
    }


    public static <T> Properties beanToProperties(T obj) throws IllegalAccessException {
        Properties prop = new Properties();
        Map<String, Object> map = beanToMap(obj);
        for (Map.Entry entry: map.entrySet()) {
            prop.setProperty(entry.getKey() + "", entry.getValue() + "");
        }
        return prop;
    }

    public static <T> T propertiesToBean(Properties prop, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Object value = prop.getProperty(fields[i].getName());
            if (value != null) {
                fields[i].setAccessible(true);
                fields[i].set(obj, value);
            }
        }
        return obj;
    }


}
