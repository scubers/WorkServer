package com.jrwong.modules.common.util;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 公共的工具类
 * @author liangyi
 * @Time 2015-8-27下午4:24:08
 */
public final class Tools {
	
	/**
	 * 判断对象是否为空
	 * @author liangyi
	 * @Time 2015-8-20上午11:02:28
	 *
	 * @param obj
	 * @return 空:true 非空:false
	 */
	public static boolean isEmpty(Object obj) {
		boolean flag = true;
		if (obj != null) {
			if (obj instanceof String) {
				if (!"".equals(obj.toString().trim())) {
					flag = false;
				}
			} else {
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 判断字符串是否为数字
	 * @author liangyi
	 * @Time 2015-8-20下午4:02:21
	 *
	 * @param str
	 * @return true:是数字 false:不是数字
	 */
	public static boolean isNumeric(String str){
    	if (isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
    }
	
	/**
	 * 针对浏览器的差异解决乱码问题
	 * @author liangyi
	 * @Time 2015-8-21下午2:36:27
	 *
	 * @param request
	 * @param messyStr
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toUTF8(HttpServletRequest request, String messyStr)
			throws UnsupportedEncodingException {
		String correctStr = null;  
		String agent = request.getHeader("USER-AGENT");  
		if (agent != null){  
		    if (-1 != agent.indexOf("Firefox")) {// Firefox  
		    	correctStr = "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(messyStr.getBytes("UTF-8"))))+ "?=";  
		    } else if (-1 != agent.indexOf("Chrome")) {// Chrome  
		    	correctStr = new String(messyStr.getBytes(), "ISO8859-1");  
		    } else if (agent.indexOf("Trident") > 0) {// 判断浏览器是否是 IE，如果是就对其进行 URL编码
	    		correctStr = java.net.URLEncoder.encode(messyStr, "UTF-8");  
	    	} else {// 其它浏览器直接按UTF-8编码
	    		correctStr = new String(messyStr.getBytes(),"UTF-8");
	    	}
		} else {  
		    correctStr = messyStr;
		}  
		return correctStr; 
	}
	
	/**
	 * 转换为String类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:25:55
	 * @param obj
	 * @return
	 */
	public static String convertToString(Object obj){
		try {
			if (!isEmpty(obj)) {
				return String.valueOf(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 转换为Integer类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:26:22
	 * @param obj
	 * @return
	 */
	public static Integer convertToInteger(Object obj){
		try {
			if (!isEmpty(obj)) {
				return Integer.valueOf(convertToString(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 转换为Long类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:33:06
	 * @param obj
	 * @return
	 */
	public static Long convertToLong(Object obj){
		try {
			if (!isEmpty(obj)) {
				return Long.valueOf(convertToString(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 转换为Double类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:33:36
	 * @param obj
	 * @return
	 */
	public static Double convertToDouble(Object obj){
		try {
			if (!isEmpty(obj)) {
				return Double.valueOf(convertToString(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 转换为Short类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:35:10
	 * @param obj
	 * @return
	 */
	public static Short convertToShort(Object obj){
		try {
			if (!isEmpty(obj)) {
				return Short.valueOf(convertToString(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 转换为Boolean类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:35:48
	 * @param obj
	 * @return
	 */
	public static Boolean convertToBoolean(Object obj){
		try {
			if (!isEmpty(obj)) {
				return Boolean.valueOf(convertToString(obj));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取ip地址
	 * @author liangyi
	 * @Time 2015-9-15上午11:12:23
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request){    
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");    
		}    
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");    
		}    
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");    
		}    
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");    
		}    
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();    
		}    
		return ip;    
	}  
	
	/**
	 * 判断请求是否为ajax请求
	 * @author liangyi
	 * @Time 2015-9-16上午10:17:37
	 * @param request
	 * @return true:是ajax请求  false:不是ajax请求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(header)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 使用UUID生成唯一的ID
	 * @author liangyi
	 * @Time 2015-9-28下午2:49:24
	 * @return
	 */
	public static String getUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	//测试
	public static void main(String[] args) {
		System.out.println(getUniqueId());
	}
}
