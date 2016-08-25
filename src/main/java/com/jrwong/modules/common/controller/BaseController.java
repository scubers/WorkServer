package com.jrwong.modules.common.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jrwong.modules.common.pojo.PublicInfo;
import com.jrwong.modules.common.pojo.Request;
import com.jrwong.modules.common.util.JsonUtils;
import com.jrwong.modules.common.util.Tools;


/**
 * 公共的控制层
 * @author liangyi
 * @Time 2015-8-21下午3:03:34
 *
 */
public class BaseController {

	
	/**
	 * 返回json格式的数据
	 * @author liangyi
	 * @Time 2016年1月5日上午10:28:16
	 *
	 * @param obj
	 * @param response
	 * @throws Exception
	 */
	public static void returnToJson(Object obj, HttpServletResponse response) throws Exception{
		// 清空response
		response.reset();
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter pw = response.getWriter();
		pw.write(JsonUtils.toJson(obj));
		pw.close();
	}
	
	/**
	 * 将参数转换为Integer类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:37:44
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected Integer getParamAsInteger(HttpServletRequest request, String param) throws Exception{
		return Tools.convertToInteger(request.getParameter(param));
	}
	
	/**
	 * 将参数转换为Long类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:37:44
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected Long getParamAsLong(HttpServletRequest request, String param) throws Exception{
		return Tools.convertToLong(request.getParameter(param));
	}
	
	/**
	 * 将参数转换为Double类型
	 * @author liangyi
	 * @Time 2015-8-26上午10:37:44
	 * @param request
	 * @param param
	 * @return
	 * @throws Exception
	 */
	protected Double getParamAsDouble(HttpServletRequest request, String param) throws Exception{
		return Tools.convertToDouble(request.getParameter(param));
	}
	
	static final String http_url = "http://localhost:8080/ddx/open/service.do";
	
	/**
	 * 
	 * @Description: 生成一个请求对象
	 * @author king
	 * @date 2014-6-24 10:33:22
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public Request buildRequestObj(String busiCode, Map<String, Object> params, HttpServletRequest req) {
		Request request = new Request();
		PublicInfo info = new PublicInfo();
		
		info.setBusiCode(busiCode);
		info.setDate(new Date().toLocaleString());
		info.setSerialNumber(UUID.randomUUID().toString());
		info.setToken((String) params.get("token"));
		
		request.setPublicInfo(info);
		request.setParameter(params);
		
		return request;
	}

}
