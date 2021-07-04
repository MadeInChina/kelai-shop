/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package cn.com.kelaikewang.commons.lang;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2017-1-4
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	private DateUtils() {
		// Cannot be instantiated
	}
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM",
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM.dd HH", "yyyy.MM", 
		"yyyy年MM月dd日", "yyyy年MM月dd日 HH时mm分ss秒", "yyyy年MM月dd日 HH时mm分", "yyyy年MM月dd日 HH时", "yyyy年MM月",
		"yyyy"};
	// Grace style
	public static final String PATTERN_GRACE = "yyyy/MM/dd HH:mm:ss";
	public static final String PATTERN_GRACE_NORMAL = "yyyy/MM/dd HH:mm";
	public static final String PATTERN_GRACE_SIMPLE = "yyyy/MM/dd";

	// Classical style
	public static final String PATTERN_CLASSICAL = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_CLASSICAL_NORMAL = "yyyy-MM-dd HH:mm";
	public static final String PATTERN_CLASSICAL_SIMPLE = "yyyy-MM-dd";


	
	/**
	 * 得到日期字符串 ，转换格式（yyyy-MM-dd）
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyy-MM-dd");
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(long dateTime, String pattern) {
		return formatDate(new Date(dateTime), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, String pattern) {
		String formatDate = null;
		if (date != null){
//			if (StringUtils.isNotBlank(pattern)) {
//				formatDate = DateFormatUtils.format(date, pattern);
//			} else {
//				formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
//			}
			if (StringUtils.isBlank(pattern)) {
				pattern = "yyyy-MM-dd";
			}
			formatDate = FastDateFormat.getInstance(pattern).format(date);
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}
    
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
//		return DateFormatUtils.format(new Date(), pattern);
		return FastDateFormat.getInstance(pattern).format(new Date());
	}
	
	/**
	 * 得到当前日期前后多少天，月，年的日期字符串
	 * @param pattern 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 * @param amont 数量，前为负数，后为正数
	 * @param type 类型，可参考Calendar的常量(如：Calendar.HOUR、Calendar.MINUTE、Calendar.SECOND)
	 * @return
	 */
	public static String getDate(String pattern, int amont, int type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(type, amont);
//		return DateFormatUtils.format(calendar.getTime(), pattern);
		return FastDateFormat.getInstance(pattern).format(calendar.getTime());
	}
	
	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式   see to DateUtils#parsePatterns
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = System.currentTimeMillis()-date.getTime();
		return t/(60*1000);
	}
    
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * 获取某月有几天
	 * @param date 日期
	 * @return 天数
	 */
	public static int getMonthHasDays(Date date){
//		String yyyyMM = new SimpleDateFormat("yyyyMM").format(date);
		String yyyyMM = FastDateFormat.getInstance("yyyyMM").format(date);
		String year = yyyyMM.substring(0, 4);
		String month = yyyyMM.substring(4, 6);
		String day31 = ",01,03,05,07,08,10,12,";
		String day30 = "04,06,09,11";
		int day = 0;
		if (day31.contains(month)) {
			day = 31;
		} else if (day30.contains(month)) {
			day = 30;
		} else {
			int y = Integer.parseInt(year);
			if ((y % 4 == 0 && (y % 100 != 0)) || y % 400 == 0) {
				day = 29;
			} else {
				day = 28;
			}
		}
		return day;
	}
	
	/**
	 * 获取日期是当年的第几周
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取一天的开始时间（如：2015-11-3 00:00:00.000）
	 * @param date 日期
	 * @return
	 */
	public static Date getDayBegin(Date date) {
		if (date == null){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取一天的最后时间（如：2015-11-3 23:59:59.999）
	 * @param date 日期
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		if (date == null){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	/**
	 * 获取服务器启动时间
	 * @param date
	 * @return
	 */
	public static Date getServerStartDate(){
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return new Date(time);
	}
	
	/**
	 * 格式化为日期范围字符串
	 * @param beginDate 2018-01-01
	 * @param endDate 2018-01-31
	 * @return 2018-01-01 ~ 2018-01-31
	 * @author ThinkGem
	 */
	public static String formatDateBetweenString(Date beginDate, Date endDate){
		String begin = DateUtils.formatDate(beginDate);
		String end = DateUtils.formatDate(endDate);
		if (StringUtils.isNoneBlank(begin, end)){
			return begin + " ~ " + end;
		}
		return null;
	}
	
	/**
	 * 解析日期范围字符串为日期对象
	 * @param dateString 2018-01-01 ~ 2018-01-31
	 * @return new Date[]{2018-01-01, 2018-01-31}
	 * @author ThinkGem
	 */
	public static Date[] parseDateBetweenString(String dateString){
		Date beginDate = null; Date endDate = null;
		if (StringUtils.isNotBlank(dateString)){
			String[] ss = StringUtils.split(dateString, "~");
			if (ss != null && ss.length == 2){
				String begin = StringUtils.trim(ss[0]);
				String end = StringUtils.trim(ss[1]);
				if (StringUtils.isNoneBlank(begin, end)){
					beginDate = DateUtils.parseDate(begin);
					endDate = DateUtils.parseDate(end);
				}
			}
		}
		return new Date[]{beginDate, endDate};
	}



	/**
	 * 根据默认格式将指定字符串解析成日期
	 *
	 * @param str
	 *            指定字符串
	 * @return 返回解析后的日期
	 */
	public static Date parse(String str) {
		return parse(str, PATTERN_CLASSICAL);
	}

	/**
	 * 根据指定格式将指定字符串解析成日期
	 *
	 * @param str
	 *            指定日期
	 * @param pattern
	 *            指定格式
	 * @return 返回解析后的日期
	 */
	public static Date parse(String str, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据默认格式将日期转格式化成字符串
	 *
	 * @param date
	 *            指定日期
	 * @return 返回格式化后的字符串
	 */
	public static String format(Date date) {
		return format(date, PATTERN_CLASSICAL);
	}

	/**
	 * 根据指定格式将指定日期格式化成字符串
	 *
	 * @param date
	 *            指定日期
	 * @param pattern
	 *            指定格式
	 * @return 返回格式化后的字符串
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	// 获得当天0点时间
	public static Date getTimesMorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	// 获得昨天0点时间
	public static Date getYesterdayMorning() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getTimesMorning().getTime()-3600*24*1000);
		return cal.getTime();
	}
	// 获得当天近7天时间
	public static Date getWeekFromNow() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis( getTimesMorning().getTime()-3600*24*1000*7);
		return cal.getTime();
	}

	// 获得当天24点时间
	public static Date getTimesNight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获取一周的第一天
	 * @return
	 */
	public static Date getFirstDayOfWeekStartTime(){
		//Date time = new Date();
		Calendar cal = Calendar.getInstance();
		//Date time = sdf.parse("2017-4-16 22:22:47");
		//cal.setTime(time);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		//System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期

		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		if(1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

		int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		//System.out.println("所在周星期一的日期："+sdf.format(cal.getTime()));

		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		return cal.getTime();
	}

	/**
	 * 获取一周的最后一天
	 * @return
	 */
	public static Date getLastDayOfWeekEndTime(){
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
		//Date time = new Date();

		Calendar cal = Calendar.getInstance();
		//Date time = sdf.parse("2017-4-16 22:22:47");
		//cal.setTime(time);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		//判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		if(1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}

		cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

		int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

		cal.add(Calendar.DATE, 6);
		cal.set(Calendar.HOUR_OF_DAY,24);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		return cal.getTime();
		//System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));
	}



	/**
	 * 当前季度的结束时间，即2012-03-31 23:59:59
	 *
	 * @return
	 */
    /*
    public static Date getCurrentQuarterEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getCurrentQuarterStartTime());
        cal.add(Calendar.MONTH, 3);
        return cal.getTime();
    }
    */

	/**
	 * 当年的开始时间
	 * @return
	 */
	public static Date getCurrentYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), 0, 0, 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
		return cal.getTime();
	}

	/**
	 * 当年的结束时间
	 * @return
	 */
	public static Date getCurrentYearEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

	/**
	 * 当年第一个月的开始时间
	 * @return
	 */
	public static Date getCurrentYearFirstMonthStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));
		return cal.getTime();
	}

	/**
	 * 当年最后一个月的结束时间
	 * @return
	 */
	public static Date getCurrentYearLastMonthEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearFirstMonthStartTime());
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

	/**
	 * 获取去年的开始时间
	 * @return
	 */
	public static Date getLastYearStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getCurrentYearStartTime());
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	/**
	 * 获取时间date1与date2相差的秒数
	 *
	 * @param date1
	 *            起始时间
	 * @param date2
	 *            结束时间
	 * @return 返回相差的秒数
	 */
	public static int getOffsetSeconds(Date date1, Date date2) {
		int seconds = (int) ((date2.getTime() - date1.getTime()) / 1000);
		return seconds;
	}

	/**
	 * 获取时间date1与date2相差的分钟数
	 *
	 * @param date1
	 *            起始时间
	 * @param date2
	 *            结束时间
	 * @return 返回相差的分钟数
	 */
	public static int getOffsetMinutes(Date date1, Date date2) {
		return getOffsetSeconds(date1, date2) / 60;
	}

	/**
	 * 获取时间date1与date2相差的小时数
	 *
	 * @param date1
	 *            起始时间
	 * @param date2
	 *            结束时间
	 * @return 返回相差的小时数
	 */
	public static int getOffsetHours(Date date1, Date date2) {
		return getOffsetMinutes(date1, date2) / 60;
	}

	/**
	 * 获取时间date1与date2相差的天数数
	 *
	 * @param date1
	 *            起始时间
	 * @param date2
	 *            结束时间
	 * @return 返回相差的天数
	 */
	public static int getOffsetDays(Date date1, Date date2) {
		return getOffsetHours(date1, date2) / 24;
	}

	/**
	 * 获取时间date1与date2相差的周数
	 *
	 * @param date1
	 *            起始时间
	 * @param date2
	 *            结束时间
	 * @return 返回相差的周数
	 */
	public static int getOffsetWeeks(Date date1, Date date2) {
		return getOffsetDays(date1, date2) / 7;
	}

	/**
	 * 获取重置指定日期的时分秒后的时间
	 *
	 * @param date
	 *            指定日期
	 * @param hour
	 *            指定小时
	 * @param minute
	 *            指定分钟
	 * @param second
	 *            指定秒
	 * @return 返回重置时分秒后的时间
	 */
	public static Date getResetTime(Date date, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.SECOND, minute);
		cal.set(Calendar.MINUTE, second);
		return cal.getTime();
	}

	/**
	 * 返回指定日期的起始时间
	 *
	 * @param date
	 *            指定日期（例如2014-08-01）
	 * @return 返回起始时间（例如2014-08-01 00:00:00）
	 */
	public static Date getIntegralStartTime(Date date) {
		return getResetTime(date, 0, 0, 0);
	}

	/**
	 * 返回指定日期的结束时间
	 *
	 * @param date
	 *            指定日期（例如2014-08-01）
	 * @return 返回结束时间（例如2014-08-01 23:59:59）
	 */
	public static Date getIntegralEndTime(Date date) {
		return getResetTime(date, 23, 59, 59);
	}

	/**
	 * 获取指定日期累加年月日后的时间
	 *
	 * @param date
	 *            指定日期
	 * @param year
	 *            指定年数
	 * @param month
	 *            指定月数
	 * @param day
	 *            指定天数
	 * @return 返回累加年月日后的时间
	 */
	public static Date rollDate(Date date, int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	/**
	 * 获取指定日期累加指定月数后的时间
	 *
	 * @param date
	 *            指定日期
	 * @param month
	 *            指定月数
	 * @return 返回累加月数后的时间
	 */
	public static Date rollMonth(Date date, int month) {
		return rollDate(date, 0, month, 0);
	}

	/**
	 * 获取指定日期累加指定天数后的时间
	 *
	 * @param date
	 *            指定日期
	 * @param day
	 *            指定天数
	 * @return 返回累加天数后的时间
	 */
	public static Date rollDay(Date date, int day) {
		return rollDate(date, 0, 0, day);
	}

	/**
	 * 计算指定日期所在月份的天数
	 *
	 * @param date
	 *            指定日期
	 * @return 返回所在月份的天数
	 */
	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int dayOfMonth = cal.getActualMaximum(Calendar.DATE);
		return dayOfMonth;
	}

	/**
	 * 获取当月第一天的起始时间，例如2014-08-01 00:00:00
	 *
	 * @return 返回当月第一天的起始时间
	 */
	public static Date getMonthStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 获取当月最后一天的结束时间，例如2014-08-31 23:59:59
	 *
	 * @return 返回当月最后一天的结束时间
	 */
	public static Date getMonthEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
		return getIntegralEndTime(cal.getTime());
	}

	/**
	 * 获取上个月第一天的起始时间，例如2014-07-01 00:00:00
	 *
	 * @return 返回上个月第一天的起始时间
	 */
	public static Date getLastMonthStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 获取上个月最后一天的结束时间，例如2014-07-31 23:59:59
	 *
	 * @return 返回上个月最后一天的结束时间
	 */
	public static Date getLastMonthEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
		return getIntegralEndTime(cal.getTime());
	}

	/**
	 * 获取下个月第一天的起始时间，例如2014-09-01 00:00:00
	 *
	 * @return 返回下个月第一天的起始时间
	 */
	public static Date getNextMonthStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 获取下个月最后一天的结束时间，例如2014-09-30 23:59:59
	 *
	 * @return 返回下个月最后一天的结束时间
	 */
	public static Date getNextMonthEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
		return getIntegralEndTime(cal.getTime());
	}

	/**
	 * 获取当前季度第一天的起始时间
	 *
	 * @return 返回当前季度第一天的起始时间
	 */
	public static Date getQuarterStartTime() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (month < 3) {
			cal.set(Calendar.MONTH, 0);
		} else if (month < 6) {
			cal.set(Calendar.MONTH, 3);
		} else if (month < 9) {
			cal.set(Calendar.MONTH, 6);
		} else {
			cal.set(Calendar.MONTH, 9);
		}
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 获取当前季度最后一天的结束时间
	 *
	 * @return 返回当前季度最后一天的结束时间
	 */
	public static Date getQuarterEndTime() {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		if (month < 3) {
			cal.set(Calendar.MONTH, 2);
		} else if (month < 6) {
			cal.set(Calendar.MONTH, 5);
		} else if (month < 9) {
			cal.set(Calendar.MONTH, 8);
		} else {
			cal.set(Calendar.MONTH, 11);
		}
		cal.set(Calendar.DAY_OF_MONTH, getDayOfMonth(cal.getTime()));
		return getIntegralEndTime(cal.getTime());
	}

	/**
	 * 获取前一个工作日
	 *
	 * @return 返回前一个工作日
	 */
	public static Date getPrevWorkday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DAY_OF_MONTH, -2);
		}
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 获取下一个工作日
	 *
	 * @return 返回下个工作日
	 */
	public static Date getNextWorkday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			cal.add(Calendar.DAY_OF_MONTH, 2);
		}
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 获取当周的第一个工作日
	 *
	 * @return 返回第一个工作日
	 */
	public static Date getFirstWorkday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 获取当周的最后一个工作日
	 *
	 * @return 返回最后一个工作日
	 */
	public static Date getLastWorkday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		return getIntegralStartTime(cal.getTime());
	}

	/**
	 * 判断指定日期是否是工作日
	 *
	 * @param date
	 *            指定日期
	 * @return 如果是工作日返回true，否则返回false
	 */
	public static boolean isWorkday(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return !(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
	}

	/**
	 * 获取指定日期是星期几
	 *
	 * @param date
	 *            指定日期
	 * @return 返回星期几的描述
	 */
	public static String getWeekdayDesc(Date date) {
		final String[] weeks = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四",
				"星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		return weeks[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 获取指定日期距离当前时间的时间差描述（如3小时前、1天前）
	 *
	 * @param date
	 *            指定日期
	 * @return 返回时间差的描述
	 */
	public static String getTimeOffsetDesc(Date date) {
		int seconds = getOffsetSeconds(date, new Date());
		if (Math.abs(seconds) < 60) {
			return Math.abs(seconds) + "秒" + (seconds > 0 ? "前" : "后");
		}
		int minutes = seconds / 60;
		if (Math.abs(minutes) < 60) {
			return Math.abs(minutes) + "分钟" + (minutes > 0 ? "前" : "后");
		}
		int hours = minutes / 60;
		if (Math.abs(hours) < 60) {
			return Math.abs(hours) + "小时" + (hours > 0 ? "前" : "后");
		}
		int days = hours / 24;
		if (Math.abs(days) < 7) {
			return Math.abs(days) + "天" + (days > 0 ? "前" : "后");
		}
		int weeks = days / 7;
		if (Math.abs(weeks) < 5) {
			return Math.abs(weeks) + "周" + (weeks > 0 ? "前" : "后");
		}
		int monthes = days / 30;
		if (Math.abs(monthes) < 12) {
			return Math.abs(monthes) + "个月" + (monthes > 0 ? "前" : "后");
		}
		int years = monthes / 12;
		return Math.abs(years) + "年" + (years > 0 ? "前" : "后");
	}
/*	public static Date localDateTimeToDate(LocalDateTime localDateTime){
		if (localDateTime == null){
			return null;
		}
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zone).toInstant();
		java.util.Date date = Date.from(instant);
		return date;
	}
	public static Date localDateTimeToDate(Optional<LocalDateTime> localDateTime){
		if (localDateTime.isPresent()) {
			return localDateTimeToDate(localDateTime.get());
		}
		return null;
	}*/
	/**
	 * 获取指定日期所在月份开始的时间戳
	 * @param date 指定日期
	 * @return
	 */
	public static Date getMonthBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		//设置为1号,当前日期既为本月第一天
		c.set(Calendar.DAY_OF_MONTH, 1);
		//将小时至0
		c.set(Calendar.HOUR_OF_DAY, 0);
		//将分钟至0
		c.set(Calendar.MINUTE, 0);
		//将秒至0
		c.set(Calendar.SECOND,0);
		//将毫秒至0
		c.set(Calendar.MILLISECOND, 0);
		// 获取本月第一天的时间戳
		return c.getTime();
	}
	/**
	 * 获取指定日期所在月份结束的时间戳
	 * @param date 指定日期
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		//设置为当月最后一天
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		//将小时至23
		c.set(Calendar.HOUR_OF_DAY, 23);
		//将分钟至59
		c.set(Calendar.MINUTE, 59);
		//将秒至59
		c.set(Calendar.SECOND,59);
		//将毫秒至999
		c.set(Calendar.MILLISECOND, 999);
		// 获取本月最后一天的时间戳
		return c.getTime();
	}
}
