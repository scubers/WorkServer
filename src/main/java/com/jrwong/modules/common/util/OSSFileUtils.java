package com.jrwong.modules.common.util;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PolicyConditions;

public final class OSSFileUtils {
	
	// 创建阿里云文件上传所需参数
	public static Map<String,String> createPostPolicyAndSign() throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		
		OSSClient client = new OSSClient(Constant.OSS_HOST, Constant.ACCESS_ID, Constant.ACCESS_KEY);
		//client = setOSSCors(client);
		
		// 设置过期时间
		Date expiration = DateUtils.parseToDate("2026-12-31 18:00:00", null);
		
        PolicyConditions policyConds = new PolicyConditions();
		policyConds.addConditionItem("bucket", Constant.BUCKET_NAME);
		// 添加精确匹配条件项 “$”必须紧接大括号
		// policyConds.addConditionItem(MatchMode.Exact, PolicyConditions.COND_KEY, Common.USER_FOLDER + "${filename}");
		// 添加前缀匹配条件项
		// policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, Common.USER_FOLDER);
		// policyConds.addConditionItem(MatchMode.StartWith, "x-oss-meta-tag", "dummy_etag");
		// 添加范围匹配条件项
		policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 1, 10485760);
		
		// 生成Post Policy字符串
		String postPolicy = client.generatePostPolicy(expiration, policyConds);

		// 计算policy Base64编码
		byte[] binaryData = postPolicy.getBytes("utf-8");
		String encodedPolicy = BinaryUtil.toBase64String(binaryData);
		
		map.put("OSSAccessKeyId", Constant.ACCESS_ID);
		map.put("bucket", Constant.BUCKET_NAME);
		map.put("policy", encodedPolicy);
		
		// 传入Post Policy原json字串，生成postSignature
		String postSignature = client.calculatePostSignature(postPolicy);
		map.put("sign", postSignature);
		// setOSSCors(client);
		return map;
	}
	
	// 创建文件下载链接
	public static String createOSSDownloadUrl(String key) throws Exception{
    	URL url = null;
    	// 设置过期时间
        Date expiration = DateUtils.parseToDate("2026-12-31 18:00:00", null);
        
        // OSS client
        OSSClient client = new OSSClient(Constant.OSS_HOST, Constant.ACCESS_ID, Constant.ACCESS_KEY);
        //client = setOSSCors(client);
    	// 产生带签名的下载 URL
    	GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(Constant.BUCKET_NAME, key,HttpMethod.GET);
    	// 设置签名有效期
    	req.setExpiration(expiration);
//    	// 产生响应标头，用于方便指定中文下载文件名
//    	ResponseHeaderOverrides rsp = new ResponseHeaderOverrides();  
//    	// 在响应标头中设置 IE 浏览器的下载文件名
//    	
//    	if (request.getHeader("user-agent").indexOf("Trident") > 0) {    // 判断浏览器是否是 IE，如果是就对文件名进行 URL 编码
//    	    rename = java.net.URLEncoder.encode(rename, "utf-8");
//    	}
//    	
//    	rsp.setContentDisposition("attachment; filename=" + rename);/*StringUtil.encodeChineseDownloadFileName(request, rename));*/
//    	req.setResponseHeaders(rsp);				// 请求中设置响应标头
    	// 产生带签名的下载 URL
    	url = client.generatePresignedUrl(req);   
    	return url != null ? url.toString() : null;
    }
	
	/*// 设置跨域规则
	private static OSSClient setOSSCors(OSSClient client){
		SetBucketCORSRequest request = new SetBucketCORSRequest();
		request.setBucketName(Constant.BUCKET_NAME);
		ArrayList<CORSRule> putCorsRules = new ArrayList<CORSRule>();
		//CORS规则的容器,每个bucket最多允许10条规则

		CORSRule corRule = new CORSRule();    
		ArrayList<String> allowedOrigin = new ArrayList<String>();
		//指定允许跨域请求的来源
		allowedOrigin.add( "http://*"); 
		ArrayList<String> allowedMethod = new ArrayList<String>();
		//指定允许的跨域请求方法(GET/PUT/DELETE/POST/HEAD)
		allowedMethod.add("POST");
		allowedMethod.add("GET"); 
		ArrayList<String> allowedHeader = new ArrayList<String>();
		//控制在OPTIONS预取指令中Access-Control-Request-Headers头中指定的header是否允许。
		allowedHeader.add("x-oss-meta");       
		ArrayList<String> exposedHeader = new ArrayList<String>();
		//指定允许用户从应用程序中访问的响应头
		exposedHeader.add("x-oss-meta-tag");      
		corRule.setAllowedMethods(allowedMethod);
		corRule.setAllowedOrigins(allowedOrigin);
		corRule.setAllowedHeaders(allowedHeader);
		corRule.setExposeHeaders(exposedHeader);
		//指定浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间,单位为秒。
		corRule.setMaxAgeSeconds(10);          
		//最多允许10条规则
		putCorsRules.add(corRule);            
		request.setCorsRules(putCorsRules);
		client.setBucketCORS(request);
		return client;
	}*/
}
