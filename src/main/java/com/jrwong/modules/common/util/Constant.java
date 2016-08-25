package com.jrwong.modules.common.util;

/**
 * 常量定义
 * @author liangyi
 * @Time 2016年1月5日上午9:56:58
 *
 */
public final class Constant {
	// 标准格式
	public static final String FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";
	// 只包含年月日的格式
	public static final String FORMAT_DAY = "yyyy-MM-dd";
	// 不包含秒的格式,常用
	public static final String FORMAT_ROUTINE = "yyyy-MM-dd HH:mm";
	
	// 用于校验用户
	public static final String ACCESS_ID = "VQfR98TGCGBnbf8K";
	public static final String ACCESS_KEY = "HWTFvJ9tY7EziLIjhzqy2fR2GV6A6V";
	// oss中本公司的硬盘
	public static final String BUCKET_NAME = "oss-upcera";
	// 阿里云OSS主机"http://oss-upcera.oss-cn-shenzhen.aliyuncs.com";
	public static final String OSS_HOST = "http://oss-cn-shenzhen.aliyuncs.com";
	// 爱尔创 CDN CName 域名
	// private static final String OSS_HOST = "http://ddrace.updcc.com";
		
	// 为即时通讯系统提供上传的文件所在的文件夹
	public static final String IM_FOLDER = "im/";
}
