package com.jrwong.modules.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author liangyi
 * @Time 2015-8-27下午4:23:53
 */
public final class DateUtils {
	
	/**
	 * 将日期字符串转为时间格式
	 * @author liangyi
	 * @Time 2015-8-28下午4:45:13
	 * @param dateStr 待转换的日期字符串
	 * @param pattern 需要转换的格式,默认为yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException 
	 */
	public static Date parseToDate(String dateStr, String pattern) throws ParseException{
		if (!Tools.isEmpty(dateStr)) {
			if (dateStr.length() < 12) {
				dateStr += " 00:00:00";
			}
			if (Tools.isEmpty(pattern)) {
				pattern = Constant.FORMAT_NORMAL;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			return dateFormat.parse(dateStr);
		}
		return null;
	}
	
	/**
	 * 将日期格式转为字符串
	 * @author liangyi
	 * @Time 2015-8-28下午5:00:21
	 * @param date 待转换的日期
	 * @param pattern 需要转换的格式,默认为yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String parseToString(Date date, String pattern){
		if (!Tools.isEmpty(date)) {
			if (Tools.isEmpty(pattern)) {
				pattern = Constant.FORMAT_NORMAL;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			return dateFormat.format(date);
		}
		return null;
	}
	
	/**
	 * 将时间字符串转为Timestamp类型
	 * @author liangyi
	 * @Time 2015-8-31上午9:06:46
	 * @param dateStr 待转换的时间字符串
	 * @param pattern 需要转换的格式,默认为yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp parseToTimeStamp(String dateStr, String pattern) throws ParseException{
		Timestamp timestamp = null;
		if (!Tools.isEmpty(dateStr)) {
			if (Tools.isEmpty(pattern)) {
				pattern = Constant.FORMAT_NORMAL;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			timestamp = new Timestamp(dateFormat.parse(dateStr).getTime());
		}
		return timestamp;
	}
	
	/**
	 * 将Timestamp转为Date格式
	 * @author liangyi
	 * @Time 2015-8-31上午9:14:48
	 * @param timestamp 待转换的时间戳
	 * @return
	 */
	public static Date parseTimestampToDate(Timestamp timestamp){
		if (!Tools.isEmpty(timestamp)) {
			return new Date(timestamp.getTime());
		}
		return null;
	}
	
	/**
	 * 获取指定日期前后N分钟的日期
	 * @author liangyi
	 * @Time 2015-8-31上午9:27:28
	 * @param date 指定的日期
	 * @param minuteNum 相差的天数
	 * @return
	 */
	public static Date getDateByMinuteDiffer(Date date, int minuteNum){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minuteNum);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期前后N小时的日期
	 * @author liangyi
	 * @Time 2015-8-31上午9:27:28
	 * @param date 指定的日期
	 * @param hourNum 相差的天数
	 * @return
	 */
	public static Date getDateByHourDiffer(Date date, int hourNum){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hourNum);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期前后N天的日期
	 * @author liangyi
	 * @Time 2015-8-31上午9:27:28
	 * @param date 指定的日期
	 * @param dayNum 相差的天数
	 * @return
	 */
	public static Date getDateByDayDiffer(Date date, int dayNum){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, dayNum);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期前后N月的日期
	 * @author liangyi
	 * @Time 2015-8-31上午9:27:28
	 * @param date 指定的日期
	 * @param monthNum 相差的月数
	 * @return
	 */
	public static Date getDateByMonthDiffer(Date date, int monthNum){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, monthNum);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期前后N年的日期
	 * @author liangyi
	 * @Time 2015-8-31上午9:27:28
	 * @param date 指定的日期
	 * @param yearNum 相差的年份
	 * @return
	 */
	public static Date getDateByYearDiffer(Date date, int yearNum){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, yearNum);
		return calendar.getTime();
	}
	
	/**
	 * 比较两个日期的大小
	 * @author liangyi
	 * @Time 2015-8-31上午9:35:25
	 * @param dateStr1 日期字符串1
	 * @param dateStr2 日期字符串2
	 * @return 1:前者大于后者; 2:大于等于; 0:相等; -1:前者小于后者; -2:小于等于
	 * @throws ParseException
	 */
	public static String compareDate(String dateStr1, String dateStr2) throws ParseException{
		long date1 = parseToDate(dateStr1, null).getTime();
		long date2 = parseToDate(dateStr2, null).getTime();
		if (date1 > date2) {
			return "1";
		}else if (date1 >= date2) {
			return "2";
		}
		else if (date1 < date2) {
			return "-1";
		}
		else if (date1 <= date2) {
			return "-2";
		}else {
			return "0";
		}
	}
	
	/**
	 * 计算两个时间之间相差的天数
	 * @author liangyi
	 * @Time 2015-8-31上午10:00:35
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return date2 - date1
	 */
	public static int getDayMinus(Date date1, Date date2){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		long millis1 = calendar.getTimeInMillis();
		calendar.setTime(date2);
		long millis2 = calendar.getTimeInMillis();
		long hourMinus = (millis2 - millis1)/(1000 * 3600 * 24);
		return Integer.valueOf(String.valueOf(hourMinus));
	}
	
	/**
	 * 计算两个时间之间相差的小时数
	 * @author liangyi
	 * @Time 2015-8-31上午10:00:35
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return date2 - date1
	 */
	public static int getHourMinus(Date date1, Date date2){
		long millis1 = date1.getTime();
		long millis2 = date2.getTime();
		long hourMinus = (millis2 - millis1)/(1000 * 3600);
		return Integer.valueOf(String.valueOf(hourMinus));
	}
	
	/**
	 * 计算两个时间之间相差的分钟数
	 * @author liangyi
	 * @Time 2015-8-31上午10:00:35
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return date2 - date1
	 */
	public static int getMinuteMinus(Date date1, Date date2){
		long millis1 = date1.getTime();
		long millis2 = date2.getTime();
		long minuteMinus = (millis2 - millis1)/(1000 * 60);
		return Integer.valueOf(String.valueOf(minuteMinus));
	}
	
	/**
	 * 计算两个时间之间相差的秒数
	 * @author liangyi
	 * @Time 2015-8-31上午10:00:35
	 * @param date1 时间1
	 * @param date2 时间2
	 * @return date2 - date1
	 */
	public static int getSecondMinus(Date date1, Date date2){
		long millis1 = date1.getTime();
		long millis2 = date2.getTime();
		long secondMinus = (millis2 - millis1)/(1000);
		return Integer.valueOf(String.valueOf(secondMinus));
	}
	
	// 测试
	public static void main(String[] args) throws Exception {
		Date date1 = new Date();
		Date date2 = parseToDate("2015-10-9 16:01:19", null);
		System.out.println(parseToString(date1, null));
		System.out.println(date1.getTime() - date2.getTime());
		System.out.println(getMinuteMinus(date1, date2));
		System.out.println(getSecondMinus(date1, date2));
		System.out.println(getHourMinus(date1, date2));
	}
	
}
