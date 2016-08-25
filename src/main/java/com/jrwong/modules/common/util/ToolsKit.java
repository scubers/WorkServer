package com.jrwong.modules.common.util;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 * 工具類
 * @author 金德志
 *
 */
public class ToolsKit {
	private static Log log = LogFactory.getLog(jsonUitl.class);
	/***
	 * JSON and bean 互转工具
	 * **/
	public static class jsonUitl{
		private static ObjectMapper objectmapper;
		static {
			objectmapper = new ObjectMapper();
			objectmapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
			objectmapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		public static String toJson(Object javaObj)  throws Exception{
				return objectmapper.writeValueAsString(javaObj);
		}
		public static <T> T toBean(Class<T> className,  String content) throws Exception {
			return objectmapper.readValue(content, className);
		}
		@SuppressWarnings("deprecation")
		public static <T> List<T> toBeanList(Class<T> className,  String content){
			try {
				return objectmapper.readValue(content,
						TypeFactory.collectionType(ArrayList.class, className));
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		//获取报文中的参数
		public static String getParamValue(String inPackage, String paramKey) {
			String value = "";
			try {
				if (EmptyCheckUtil.isNotEmpty(inPackage) && EmptyCheckUtil.isNotEmpty(paramKey)) {
					int paramKey_index = inPackage.indexOf(paramKey);
					int index = -1;
					if (paramKey_index < 0) {
						paramKey_index = inPackage.indexOf(paramKey.toUpperCase());
					}
					if (paramKey_index > 0) {
						index = inPackage.indexOf(':', paramKey_index);
						if (-1 == index) {
							int no_index = inPackage.indexOf('>', paramKey_index);
							if ((no_index + 1) < inPackage.length() && (no_index + 12) < inPackage.length())
								return inPackage.substring(no_index + 1, no_index + 12);
						}
					}
					int begin = 0, end = 0;
					if (index > 0) {
						begin = inPackage.indexOf(':', index);
					}
					if (begin > 0) {
						end = inPackage.indexOf(',', begin + 1);
						//如果根据“,”获取不到下标，说明此字段在json的末尾（大括号之前）
						if(end == -1){
							end = inPackage.indexOf('}', begin + 1);
						}
					}
					if (end > 0) {
						if ((begin + 1) < inPackage.length() && end <= inPackage.length()) {
							value = inPackage.substring(begin + 1, end);
							// 如果结尾为"}",那么end-1
							if (inPackage.substring(end - 1, end).equals("}")) {
								value = inPackage.substring(begin + 1, end - 1);
							}
						}
					}
					if (value != null && value.length() > 0) {
						value = value.replaceAll("\"", "");
						value = value.trim();
					}
				}
			} catch (Exception e) {
			}
			return value;
		}
	}
	public static class EmptyCheckUtil {
		@SuppressWarnings("unchecked")
		public static boolean isEmpty(Object obj)  throws Exception{
			if (obj == null) {
				return true;
			}
			if (obj instanceof Collection) {
				// 集合
				return ((Collection) obj).size() == 0;
			} else if (obj instanceof Map) {
				// Map
				return ((Map) obj).isEmpty();
			} else if (obj.getClass().isArray()) {
				// 数组
				Class cmpType = obj.getClass().getComponentType();
				if (cmpType == long.class) {
					return ((long[]) obj).length == 0;
				} else if (cmpType == int.class) {
					return ((int[]) obj).length == 0;
				} else if (cmpType == short.class) {
					return ((short[]) obj).length == 0;
				} else if (cmpType == double.class) {
					return ((double[]) obj).length == 0;
				} else if (cmpType == float.class) {
					return ((float[]) obj).length == 0;
				} else {
					return ((Object[]) obj).length == 0;
				}
			} else {
				// String
				return obj.toString().trim().length() == 0;
			}
		}

		public static boolean isNotEmpty(Object obj)  throws Exception{
			return !isEmpty(obj);
		}
	}

	/**
	 *数据类型转换
	 **/
	public static class TypeConversionUtil {
		// 将字CLOB转成STRING类型
		public static String ClobToString(Clob clob) throws Exception {
			String reString = "";
			if(EmptyCheckUtil.isNotEmpty(clob)){
				Reader is = clob.getCharacterStream();// 得到流
				BufferedReader br = new BufferedReader(is);
				String s = br.readLine();
				StringBuffer sb = new StringBuffer();
				while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
					sb.append(s);
					s = br.readLine();
				}
				reString = sb.toString();
			}
			return reString;
		} 
		public static String asString(Object obj)  {
			try {
				if (EmptyCheckUtil.isNotEmpty(obj)) {
					return String.valueOf(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		public static Integer asInteger(Object obj)  {
			try {
				if (EmptyCheckUtil.isNotEmpty(obj)) {
					return Integer.valueOf(asString(obj));
				}
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage());
			}
			return null;
		}
		public static Long asLong(Object obj)  {
			try {
				if (EmptyCheckUtil.isNotEmpty(obj)) {
					return Long.valueOf(asString(obj));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		public static Short asShort(Object obj){
			try {
				if (EmptyCheckUtil.isNotEmpty(obj)) {
					return Short.valueOf(asString(obj));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
		public static Double asDouble(Object obj) {
			try {
				if (EmptyCheckUtil.isNotEmpty(obj)) {
					return Double.valueOf(asString(obj));
				}
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		public static boolean asBoolean(Object obj) {
			try {
				if (EmptyCheckUtil.isNotEmpty(obj)) {
					return Boolean.valueOf(asString(obj));
				}
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		public static void replaceAll(StringBuffer sb, String oldStr, String newStr) {
			int i = sb.indexOf(oldStr);
			int oldLen = oldStr.length();
			int newLen = newStr.length();
			while (i > -1) {
				sb.delete(i, i + oldLen);
				sb.insert(i, newStr);
				i = sb.indexOf(oldStr, i + newLen);
			}
		}
	}
	/**
	 *HTTP客户端工具
	 **/
	public static class HttpUtil {
		private static void setHeader(Map Header,HttpMethodBase method){
			if(null!=Header && !Header.isEmpty()){
				Iterator it = Header.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry entry = (Map.Entry) it.next();
					String key = String.valueOf(entry.getKey());
					String value = String.valueOf(entry.getValue());
					log.info( "http request Header："+key+"="+value);
					method.addRequestHeader(key,value);
				}
			}
		}
		public static String doGet(String url,Map<String , Object> Header,String Parameters) throws Exception{
			String result = "";
			HttpClient client = new HttpClient();
			//超时时间120秒
			client.setTimeout(120000);
			GetMethod method = new GetMethod(url);
			method.setQueryString(Parameters);
			setHeader(Header, method);
			log.info("http dopost go ims :"+url);
			long t1= System.currentTimeMillis();
			int statusCode = client.executeMethod(method);
			log.info("invoke imscost："+(System.currentTimeMillis()-t1)+"ms");
			if (statusCode == 200) {
				result = method.getResponseBodyAsString();
			}else{
				throw new Exception("http request fail，return code:"+statusCode);
			}
			//??method.releaseConnection();//释放连接
			return result;
		}
		public static String doPost(String Url,Map<String, Object> Parameters,Map Header) throws Exception {
			String result = "";
			HttpClient client = new HttpClient();
			//超时时间120秒
			client.setTimeout(120000);
			PostMethod method = new PostMethod(Url);
			if(null!=Parameters && !Parameters.isEmpty()){
				Iterator it = Parameters.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry entry = (Map.Entry) it.next();
					String key = String.valueOf(entry.getKey());
					String value = String.valueOf(entry.getValue());
					log.info( "http request Parameters："+key+"="+value);
					method.addParameter(key, value);
				}
			}
			setHeader(Header, method);
			log.info("http dopost go ims :"+Url);
			long t1= System.currentTimeMillis();
			int statusCode = client.executeMethod(method);
			log.info("invoke imscost："+(System.currentTimeMillis()-t1)+"ms");
			if (statusCode == 200) {
				result = method.getResponseBodyAsString();
			}else{
				throw new Exception("http request fail，return code:"+statusCode);
			}
			//??method.releaseConnection();//释放连接
			return result;
		
		}
		@SuppressWarnings({ "deprecation", "unchecked" })
		public static String doPost(String Url,String Parameters,Map Header) throws Exception {

			String result = "";
			HttpClient client = new HttpClient();
			//超时时间120秒
			client.setTimeout(120000);
			PostMethod method = new PostMethod(Url);
			RequestEntity objRequestEntity = new ByteArrayRequestEntity(Parameters.getBytes("UTF-8"));
			method.setRequestEntity(objRequestEntity);
			setHeader(Header, method);
			log.info("http dopost go ims :"+Url);
			long t1= System.currentTimeMillis();
			int statusCode = client.executeMethod(method);
			log.info("invoke imscost："+(System.currentTimeMillis()-t1)+"ms");
			if (statusCode == 200) {
				result = method.getResponseBodyAsString();
			}else{
				throw new Exception("http request fail，return code:"+statusCode);
			}
			//??method.releaseConnection();//释放连接
			return result;
		}
		@SuppressWarnings({ "deprecation", "unchecked" })
		public static InputStream doPostAsStream(String Url,String Parameters,Map Header) throws Exception {

			InputStream result = null;
			HttpClient client = new HttpClient();
			//超时时间120秒
			client.setTimeout(120000);
			PostMethod method = new PostMethod(Url);
			RequestEntity objRequestEntity = new ByteArrayRequestEntity(Parameters.getBytes("UTF-8"));
			method.setRequestEntity(objRequestEntity);
			setHeader(Header, method);
			log.info("http dopost go ims :"+Url);
			long t1= System.currentTimeMillis();
			int statusCode = client.executeMethod(method);
			log.info("invoke imscost："+(System.currentTimeMillis()-t1)+"ms");
			if (statusCode == 200) {
				result = method.getResponseBodyAsStream();
			}else{
				throw new Exception("http request fail，return code:"+statusCode);
			}
			//??method.releaseConnection();//释放连接
			return result;
		}
	}
	public static class DateUtil {
		
		public static final String SSSS = "yyyyMMddHHmmssSSSS";
		
		public static final String HOUR = "HH";
		
		public static final String PATTERN_FULL_DAY = "yyyyMMdd";
		
		public static final String PATTERN_SIMPLE = "yyyy-MM-dd";
		
		public static final String PATTERN_MONITOR_TIME = "yy-MM-dd HH";
		
		public static final String PATTERN_FULL_HOUR = "yyyy-MM-dd HH";

		public static final String PATTERN_NORMAL = "yyyy-MM-dd HH:mm:ss";

		public static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss S";
		
		public static final String PATTERN_FULL_SIMPLE = "yyyyMMddHHmmss";

		public static final String ORACLE_FORMAT = "YYYY-MM-DD HH24:MI:SS";

		public static Date parse(String src, String pattern) throws ParseException {
			SimpleDateFormat util = new SimpleDateFormat();
			util.applyPattern(pattern);
			return util.parse(src);
		}

		public static Date parse(String src) throws Exception {
			SimpleDateFormat util = new SimpleDateFormat(PATTERN_SIMPLE);
			Date ret = null;
			if (EmptyCheckUtil.isNotEmpty(src)) {
				try {
					util.applyPattern(PATTERN_NORMAL);
					ret = util.parse(src);
				} catch (Exception ex) {
				}
				if (ret == null) {
					try {
						util.applyPattern(PATTERN_SIMPLE);
						ret = util.parse(src);
					} catch (Exception ex) {
					}
				}
				if (ret == null) {
					try {
						util.applyPattern(PATTERN_FULL);
						ret = util.parse(src);
					} catch (Exception ex) {
					}
				}
			}
			if (ret == null) {
				throw new IllegalArgumentException("## cant parse to Date . not supported by default pattern: $" + src + "$");
			}
			return ret;
		}

		public static Date parse(Long timeMills) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timeMills);
			// taobao 使用估计标准时间，北京时间需+8小时
			calendar.add(Calendar.HOUR_OF_DAY, 8);
			return calendar.getTime();
		}

		public static Date getDayBegin(Date date) {
			Calendar calendar = Calendar.getInstance();
			if (null == date) {
				date = calendar.getTime();
			}
			return clear(date);
		}

		public static Date clear(Date date) {
			if (null == date) {
				throw new IllegalArgumentException();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return clear(calendar);
		}

		public static Date clear(Calendar calendar) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.clear(Calendar.MINUTE);
			calendar.clear(Calendar.SECOND);
			calendar.clear(Calendar.MILLISECOND);
			return calendar.getTime();
		}

		public static Date getDayEnd(Date date) {
			Date ret = getDayBegin(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(ret);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			calendar.add(Calendar.SECOND, -1);
			return calendar.getTime();
		}

		public static Date add(Date date, int timeUnit, int value) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(timeUnit, value);
			return calendar.getTime();
		}

		public static Date addMinutes(Date date, int minutes) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, minutes);
			return calendar.getTime();
		}

		public static Date addSeconds(Date date, int seconds) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.SECOND, seconds);
			return calendar.getTime();
		}

		public static Date addHours(Date date, int hours) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.HOUR, hours);
			return calendar.getTime();
		}
		
		public static Date addDay(Date date, int day) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, day);
			return calendar.getTime();
		}

		public static String formatDate(Date date) {
			String str = formatDate(date, PATTERN_NORMAL);
			return str;
		}

		public static String formatDate(Date date, String pattern) {
			SimpleDateFormat util = new SimpleDateFormat(pattern);
			String str = util.format(date);
			return str;
		}

		public static Date get6DaysAgo(Date date) {
			Date d = clear(date);
			return add(d, Calendar.DAY_OF_YEAR, -6);
		}

		public static Date getNDaysAgo(Date date, int n) {
			Date d = clear(date);
			return add(d, Calendar.DAY_OF_YEAR, -1 * n);
		}

		public static Date getNMinutesAgo(Date date, int n) {
			return add(date, Calendar.MINUTE, -1 * n);
		}

		public static Date get3MonthsAgo(Date date) {
			Date d = clear(date);
			return add(d, Calendar.MONTH, -3);
		}

		public static Date clearMinutesAndSeconds(Date date) {
			if (null == date) {
				throw new IllegalArgumentException();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.clear(Calendar.MINUTE);
			calendar.clear(Calendar.SECOND);
			calendar.clear(Calendar.MILLISECOND);
			return calendar.getTime();
		}

		public static Date clearSeconds(Date date) {
			if (null == date) {
				throw new IllegalArgumentException();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.clear(Calendar.SECOND);
			calendar.clear(Calendar.MILLISECOND);
			return calendar.getTime();
		}

		public static boolean isInSameDay(Date date1, Date date2) {
			Calendar c = Calendar.getInstance();
			c.setTime(date1);
			int day1 = c.get(Calendar.DAY_OF_YEAR);
			c.clear();
			c.setTime(date2);
			int day2 = c.get(Calendar.DAY_OF_YEAR);
			return day1 == day2;
		}

		public static String generateCacheKeyByPayTime(Date payTime) {
			StringBuffer key = new StringBuffer();
			String format = "yyyMMddHH";
			key.append(formatDate(payTime, format));
			payTime = addHours(payTime, 1);
			Calendar c = Calendar.getInstance();
			c.setTime(payTime);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			key.append(hour);
			return key.toString();
		}

		public static int getMinutes(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			return c.get(Calendar.MINUTE);
		}
		
		public static String  now(){
			return formatDate( Calendar.getInstance().getTime(), PATTERN_FULL_SIMPLE);
		}
		public static Date  nowDate(){
			return Calendar.getInstance().getTime();
		}
		public static String  day(){
			return formatDate( Calendar.getInstance().getTime(), PATTERN_FULL_DAY);
		}
		public static String  ssss(){
			return formatDate( Calendar.getInstance().getTime(), SSSS);
		}
		public static int getWEEK(Date date) {
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal.get(Calendar.DAY_OF_WEEK);
		}
		
		/**
		 * 
		 * @Title: getFirstMonthDay 
		 * @Description: 获取当月第一天
		 * @author ERIC 
		 * @date 2014-8-6上午08:56:25
		 * @return String
		 */
		public static String getFirstMonthDay(){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal_1 = Calendar.getInstance();// 获取当前日期
			cal_1.add(Calendar.MONTH, -1);
			cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			return format.format(cal_1.getTime());
		}
		
		/**
		 * 
		 * @Title: getLastMonthDay 
		 * @Description: 获取当月最后一天
		 * @author ERIC 
		 * @date 2014-8-6上午08:56:45
		 * @return String
		 */
		public static String getLastMonthDay(){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cale = Calendar.getInstance();
			cale.set(Calendar.DAY_OF_MONTH, 0);// 设置为1号,当前日期既为本月第一天
			return format.format(cale.getTime());
		}
		
		/**
		 * 
		 * @Title: getLastMonthDayd 
		 * @Description: 获取当月最后一天Date
		 * @author ERIC 
		 * @date 2014-9-10下午01:43:02
		 * @return Date
		 */
		public static Date getLastMonthDayd() throws Exception{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cale = Calendar.getInstance();
			cale.setTime(nowDate());
	        cale.set(Calendar.DAY_OF_MONTH, 1);
	        cale.add(Calendar.MONTH, 1);
	        cale.add(Calendar.MILLISECOND, -1);
	        return parse(format.format((cale.getTime())));
		}
		
		/**
		 * 
		 * @Title: getNextMonthFirst 
		 * @Description: 获得下个月第一天的日期
		 * @author ERIC 
		 * @date 2014-8-6上午09:13:06
		 * @return String
		 */
	    public static String getNextMonthFirst(){  
			String str = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar lastDate = Calendar.getInstance();
			//将小时至0  
			lastDate.set(Calendar.HOUR_OF_DAY, 0);  
			//将分钟至0  
			lastDate.set(Calendar.MINUTE, 0);  
			//将秒至0  
			lastDate.set(Calendar.SECOND,0);  
			//将毫秒至0  
			lastDate.set(Calendar.MILLISECOND, 0);  
			lastDate.add(Calendar.MONTH, 1);// 减一个月
			lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
			str = sdf.format(lastDate.getTime());
			return str;  
	    }
	    
	    /**
	     * 
	     * @Title: getNextMonthFirstDate 
	     * @Description: 获得下个月第一天的日期
	     * @author ERIC 
	     * @date 2014-8-6上午09:18:15
	     * @return Date
	     */
	    public static Date getNextMonthFirstDate(){
	    	try {
	    		String str = "";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar lastDate = Calendar.getInstance();
				//将小时至0  
				lastDate.set(Calendar.HOUR_OF_DAY, 0);  
				//将分钟至0  
				lastDate.set(Calendar.MINUTE, 0);  
				//将秒至0  
				lastDate.set(Calendar.SECOND,0);  
				//将毫秒至0  
				lastDate.set(Calendar.MILLISECOND, 0);
				lastDate.add(Calendar.MONTH, 1);// 减一个月
				lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
				str = sdf.format(lastDate.getTime());
				return parse(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	    }
	    
	    /**
	     * 
	     * @Title: getFirstMonthDate 
	     * @Description: 获取当月第一天
	     * @author ERIC 
	     * @date 2014-8-6上午09:20:19
	     * @return Date
	     */
	    public static Date getFirstMonthDate(){
	    	try {
	    		String str = "";
	    		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal_1 = Calendar.getInstance();// 获取当前日期
				//将小时至0  
				cal_1.set(Calendar.HOUR_OF_DAY, 0);  
				//将分钟至0  
				cal_1.set(Calendar.MINUTE, 0);  
				//将秒至0  
				cal_1.set(Calendar.SECOND,0);  
				//将毫秒至0  
				cal_1.set(Calendar.MILLISECOND, 0);
				cal_1.add(Calendar.MONTH, 0);
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				str = format.format(cal_1.getTime());
				return parse(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	    }

	    /**
	     * 
	     * @Title: getMondayOFWeek 
	     * @Description: 获得本周一的日期
	     * @author ERIC 
	     * @date 2014-8-6上午09:26:17
	     * @return Date
	     */
	    public static Date getMondayOFWeek(){  
	         try {
				int mondayPlus = getMondayPlus();
				GregorianCalendar currentDate = new GregorianCalendar();
				currentDate.add(GregorianCalendar.DATE, mondayPlus);
				Date monday = currentDate.getTime();

				DateFormat df = DateFormat.getDateInstance();
				String preMonday = df.format(monday);
				return parse(preMonday);
			} catch (Exception e) {
				e.printStackTrace();
			}  
			return null;
	    } 
	    
	    /**
	     * 
	     * @Title: getNextMonday 
	     * @Description: 获得下周星期一的日期
	     * @author ERIC 
	     * @date 2014-8-6上午09:30:51
	     * @return Date
	     */
	    public static Date getNextMonday() {  
	        try {
				int mondayPlus = getMondayPlus();
				GregorianCalendar currentDate = new GregorianCalendar();
				currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
				Date monday = currentDate.getTime();
				DateFormat df = DateFormat.getDateInstance();
				String preMonday = df.format(monday);
				return parse(preMonday);
			} catch (Exception e) {
				e.printStackTrace();
			}  
			return null;
	    }  

	    
	    /**
	     * 
	     * @Title: getMondayPlus 
	     * @Description: 获得当前日期与本周日相差的天数
	     * @author ERIC 
	     * @date 2014-8-6上午09:27:10
	     * @return int
	     */
	    private static int getMondayPlus() {  
	        Calendar cd = Calendar.getInstance();  
	        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......  
	        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;         //因为按中国礼拜一作为第一天所以这里减1  
	        if (dayOfWeek == 1) {  
	            return 0;  
	        } else {  
	            return 1 - dayOfWeek;  
	        }  
	    }
	    
	    /**
	     * 
	     * @Title: getWeekCn 
	     * @Description: 获取指定日期所处星期几
	     * @author ERIC 
	     * @date 2014-11-8下午02:26:09
	     * @return String
	     */
	    public static String getWeekCn(Date date){
	    	String weekCn = null;
	    	SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
	    	try {
				if(EmptyCheckUtil.isNotEmpty(date)){
					weekCn = dateFm.format(date);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return weekCn;
	    }


	    
	}
	/***
	 * 文件工具
	 * **/
	public static class FileUtil{
		/**
		 * 创建文件
		 * @param 文件路径
		 * @param 文件名
		 * @return file
	     * @throws ParseException 
		 */
		public static File createFileAsFile(String filepath,String FileName) throws Exception{
			File file = null;
			try {
				if(EmptyCheckUtil.isNotEmpty(filepath.trim())){
					String ch = filepath.substring(filepath.length()-1, filepath.length());
					if(!ch.equals("/") && !ch.equals("\\")){
						filepath = filepath+"/";
					}
					if(!new File(filepath).isDirectory()){
						file = new File(filepath);
						file.mkdirs();
					}
				}
				if(EmptyCheckUtil.isNotEmpty(FileName)){
					file = new File(filepath + FileName);
					file.createNewFile();
				}
			} catch (Exception e) {
				throw e;
			}
			return file;
		}
		 /**
		 * 获取文件的InputStream
		 * @param 文件名（带路径）
		 * @return InputStream
	     * @throws ParseException 
		 */
		public static InputStream getInputStream(String FileName){
			return Thread.currentThread().getContextClassLoader().getResourceAsStream(FileName);
		}
		 /**
		 * 获取文件的OutputStream
		 * @param 文件名（带路径）
		 * @param 是否追加
		 * @return InputStream
	     * @throws ParseException 
		 */
		public static OutputStream getFileOutputStream(String FileName,boolean isAppend) throws FileNotFoundException{
			try {
				//URL is = Thread.currentThread().getContextClassLoader().getResource(FileName);
				return new FileOutputStream(FileName,isAppend);
			} catch (FileNotFoundException e) {
				throw e;
			}
		}
		/**
		 * 创建文件
		 * @param 文件路径
		 * @param 文件名
	     * @throws ParseException 
		 */
		public static boolean CreateFile(String filepath,String FileName) throws Exception{
			try {
				if(filepath!=null && !"".equals(filepath.trim())){
					String ch = filepath.substring(filepath.length()-1, filepath.length());
					if(!ch.equals("/") && !ch.equals("\\")){
						filepath = filepath+"/";
					}
				}
				if(!new File(filepath).isDirectory()){
					new File(filepath).mkdirs();
				}
				if(!new File(filepath + FileName).isFile()){
					return new File(filepath + FileName).createNewFile();
				}
			} catch (Exception e) {
				throw e;
			}
			return true;
		}
		/**
		 * 将字符串写入文件
		 * @param 文件名（带路径）
		 * @param 写入的字符串
		 * @param 是否追加
	     * @throws ParseException 
		 */
		public static void writeFile(String fileName,String source,boolean isAppend) throws Exception{
			byte[] bytes = source.getBytes();
			OutputStream os = getFileOutputStream(fileName, isAppend);
			os.write(bytes);
			os.close();
		}
		/**
		 * 读取文件
		 * @param 文件名（带路径）
		 * @return String
		 * @author jindezhi
		 */
		public static String readFile(String fileName) throws Exception {
			String output = "";
			File file = new File(fileName);
			if (file.exists()) {
				if (file.isFile()) {
						BufferedReader input = new BufferedReader(new FileReader(file));
						StringBuffer buffer = new StringBuffer();
						String text = "";
						while ((text = input.readLine()) != null){
							buffer.append(text).append("\n");
						}
						output = buffer.toString();
				} else if (file.isDirectory()) {
					String[] dir = file.list();
					output += "Directory contents:\n";
					for (int i = 0; i < dir.length; i++) {
						output += dir[i] + "\n";
					}
				}
			} else {
				throw new Exception("file:"+fileName+" Does not exist!");
			}
			return output;
		}
		/**
		 * 删除文件
		 * @param 文件名（带路径）
		 * @return bollean
	     * @throws ParseException 
		 */
		public static boolean deleteFile(String filename){
			File file = new File(filename);

			if(file.exists()){
				if(file.isFile()){
					return file.delete();
				}else{
					return false;
				}
			}else{
				return false;
			}
		 }
		/**
		 * 遍历文件夹下所有匹配文件
		 * 
		 * @param file File 起始文件夹
		 * @param p Pattern 匹配类型
		 * @return ArrayList 其文件夹下的文件夹
		 */
		@SuppressWarnings("unchecked")
		private static ArrayList filePattern(File file, Pattern p) {
		     if (file == null) {
		       return null;
		     }else if (file.isFile()) {
		       Matcher fMatcher = p.matcher(file.getName());
		       if (fMatcher.matches()) {
		         ArrayList list = new ArrayList();
		         list.add(file);
		         return list;
		       }
		     }else if (file.isDirectory()) {
		       File[] files = file.listFiles();
		       if (files != null && files.length > 0) {
		         ArrayList list = new ArrayList();
		         for (int i = 0; i < files.length; i++) {
		           ArrayList rlist = filePattern(files[i], p);
		           if (rlist != null) {
		             list.addAll(rlist);
		           }
		         }
		         return list;
		       }
		     }
		     return null;
		   }
		/**	
		 * 查找文件
		 * */
		public static File[] refreshFileList(String...para) {
			String strPath = "";
			String s="";
			if(para.length>1){
				strPath=para[0];
				s=para[1];
			}else if(para.length==1){
				strPath=para[0];
				s="*";
			}else if(para.length==0){
				return null;
			}
			
	        File dir = new File(strPath); 
	        
			s = s.replace('.', '#');
			s = s.replaceAll("#", "\\\\.");
			s = s.replace('*', '#');
			s = s.replaceAll("#", ".*");
			s = s.replace('?', '#');
			s = s.replaceAll("#", ".?");
			s = "^" + s + "$";
			
	        Pattern p = Pattern.compile(s);
	        ArrayList<?> list = filePattern(dir,p); 
	        int filesize = 0;
	        if(list != null){
	        	filesize = list.size();

	            File[] files = new File[filesize];
	            list.toArray(files);

	            if (files == null || files.length == 0){
	                return null; 
	            }
	            return files;       
	        }else{
	        	return null;
	        }
	    }
		
		/**
		 * 
		 * @Title: createDir 
		 * @Description: 判断文件夹是否存在，不存在则创建
		 * @author ERIC 
		 * @date 2014-7-22下午04:20:58
		 * @return void
		 */
		public static void createDir(String dirName) {
			if (null != dirName) {
				File file = new File(dirName);
				if (!file.exists()) {
					file.mkdirs();
				}
			}
		}
	}
	
	/**
	 * 图像帮助类
	 */
	public static class ImageHelper {

		/**
		 * 生成缩略图 <br/>
		 * 保存:ImageIO.write(BufferedImage, imgType[jpg/png/...], File);
		 * 
		 * @param source
		 *            原图片
		 * @param width
		 *            缩略图宽
		 * @param height
		 *            缩略图高
		 * @param b
		 *            是否等比缩放
		 * */
		public static BufferedImage thumb(BufferedImage source, int width, int height, boolean b) {
			// targetW，targetH分别表示目标长和宽
			int type = source.getType();
			BufferedImage target = null;
			double sx = (double) width / source.getWidth();
			double sy = (double) height / source.getHeight();

			if (b) {
				if (sx > sy) {
					sx = sy;
					width = (int) (sx * source.getWidth());
				} else {
					sy = sx;
					height = (int) (sy * source.getHeight());
				}
			}

			if (type == BufferedImage.TYPE_CUSTOM) { // handmade
				ColorModel cm = source.getColorModel();
				WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
				boolean alphaPremultiplied = cm.isAlphaPremultiplied();
				target = new BufferedImage(cm, raster, alphaPremultiplied, null);
			} else
				target = new BufferedImage(width, height, type);
			Graphics2D g = target.createGraphics();
			// smoother than exlax:
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
			g.dispose();
			return target;
		}

		/**
		 * 图片水印
		 * 
		 * @param imgPath
		 *            待处理图片
		 * @param markPath
		 *            水印图片
		 * @param x
		 *            水印位于图片左上角的 x 坐标值
		 * @param y
		 *            水印位于图片左上角的 y 坐标值
		 * @param alpha
		 *            水印透明度 0.1f ~ 1.0f
		 * */
		public static void waterMark(String imgPath, String markPath, int x, int y, float alpha) {
			try {
				// 加载待处理图片文件
				Image img = ImageIO.read(new File(imgPath));
				BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.drawImage(img, 0, 0, null);
				// 加载水印图片文件
				Image src_biao = ImageIO.read(new File(markPath));
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
				g.drawImage(src_biao, x, y, null);
				g.dispose();
				// 保存处理后的文件
				/*FileOutputStream out = new FileOutputStream(imgPath);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(image);*/
				
				String formatName = imgPath.substring(imgPath.lastIndexOf(".") + 1);
		         //FileOutputStream out = new FileOutputStream(dstName);
		         //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		         //encoder.encode(dstImage);
		         ImageIO.write(image,  formatName , new File(imgPath) );
				
				//out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 文字水印
		 * 
		 * @param imgPath
		 *            待处理图片
		 * @param text
		 *            水印文字
		 * @param font
		 *            水印字体信息
		 * @param color
		 *            水印字体颜色
		 * @param x
		 *            水印位于图片左上角的 x 坐标值
		 * @param y
		 *            水印位于图片左上角的 y 坐标值
		 * @param alpha
		 *            水印透明度 0.1f ~ 1.0f
		 */

		public static void textMark(String imgPath, String text, Font font, Color color, int x, int y, float alpha) {
			try {
				Font Dfont = (font == null) ? new Font("宋体", 20, 13) : font;

				Image img = ImageIO.read(new File(imgPath));

				BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();

				g.drawImage(img, 0, 0, null);
				g.setColor(color);
				g.setFont(Dfont);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
				g.drawString(text, x, y);
				g.dispose();
				/*FileOutputStream out = new FileOutputStream(imgPath);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(image);*/
				
				String formatName = imgPath.substring(imgPath.lastIndexOf(".") + 1);
		         //FileOutputStream out = new FileOutputStream(dstName);
		         //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		         //encoder.encode(dstImage);
		         ImageIO.write(image,  formatName , new File(imgPath) );
				
				//out.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		/**
		 * 判断文件是否为图片<br>
		 * <br>
		 * 
		 * @param pInput
		 *            文件名<br>
		 * @param pImgeFlag
		 *            判断具体文件类型<br>
		 * @return 检查后的结果<br>
		 * @throws Exception
		 */
		public static boolean isPicture(String pInput, String pImgeFlag) throws Exception {
			// 文件名称为空的场合
			if (EmptyCheckUtil.isEmpty(pInput)) {
				// 返回不和合法
				return false;
			}
			// 获得文件后缀名
			String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
			// 声明图片后缀名数组
			String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" }, { "gif", "2" }, { "jfif", "3" }, { "jpe", "4" }, { "jpeg", "5" },
					{ "jpg", "6" }, { "png", "7" }, { "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
			// 遍历名称数组
			for (int i = 0; i < imgeArray.length; i++) {
				// 判断单个类型文件的场合
				if (!EmptyCheckUtil.isEmpty(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())
						&& imgeArray[i][1].equals(pImgeFlag)) {
					return true;
				}
				// 判断符合全部类型的场合
				if (EmptyCheckUtil.isEmpty(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())) {
					return true;
				}
			}
			return false;
		}
	}




}
