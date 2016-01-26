package apk.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author dongjd
 * 日期处理
 */
public class DateHelper
{
	/**
	 * 格式化日期
	 * @param date Date
	 * @param format 格式化字符串
	 * @return 结果
	 */
	public static String FormatDate(Date date, String format)
	{
		if(date == null)
			return null;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		return simpleDateFormat.format(date);
	}
	
	/**
	 * 字符串转日期
	 * @param date 字符串
	 * @param format 格式
	 * @return 日期
	 */
	public static Date StringToDate(String date, String format)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
		try
		{
			return simpleDateFormat.parse(date);
		}
		catch (ParseException e)
		{
			return new Date(0);
		}
	}
	
	/**
	 * 字符串转日期
	 * @param date 字符串
	 * @return 日期
	 */
	public static Date StringToDate(String date)
	{
		SimpleDateFormat simpleDateFormat;
		if(date.matches("[0-9]{4,4}-[0-9]{1,2}-[0-9]{1,2}"))//yyyy-MM-dd  或 yyyy-M-d
		{
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		}
		else if(date.matches("[0-9]{4,4}-[0-9]{1,2}-[0-9]{1,2}\\s[0-9]{1,2}\\:[0-9]{1,2}\\:[0-9]{1,2}"))//yyyy-MM-dd HH:mm:ss 或 yyyy-M-d H:m:s
		{
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		}
		else if(date.matches("[0-9]{4,4}[0-9]{2}[0-9]{2}"))//yyyyMMdd
		{
			simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		}
		else if(date.matches("[0-9]{4,4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}"))//yyyyMMddHHmmss
		{
			simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
		}
		else
		{
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		}
		
		
		try
		{
			return simpleDateFormat.parse(date);
		}
		catch (ParseException e)
		{
			return new Date(0);
		}
	}
	
	/**
	 * 获取今天
	 * @return 2013-09-10
	 */
	public static String GetNowYYYYMMDD()
	{
		return GetNow("yyyy-MM-dd");
	}
	
	/**
	 * 获取现在时间
	 * @return 2013-09-10 12:15:53
	 */
	public static String GetNowYYYYMMDDHHMMSS()
	{
		return GetNow("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 获取今天加一天
	 * @return Date
	 */
	public static String GetTomorrowYYYYMMDD()
	{
		return FormatDate(GetTomorrow(), "yyyy-MM-dd");
	}
	/**
	 * 获取现在加一天
	 * @return Date
	 */
	public static Date GetTomorrow()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前时间，并格式化成字符串
	 * @param format 格式化
	 * @return 返回值
	 */
	public static String GetNow(String format)
	{
		return FormatDate(GetNow(), format);
	}
	
	
	/**
	 * 获取当前时间
	 * @return 当前时间
	 */
	public static Date GetNow()
	{
		return new Date();
	}
	
	/**
	 * 比较两个日期是否为同一天
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 是否为同一天
	 */
	public static boolean IsSameDate(Date date1, Date date2)
	{
		return FormatDate(date1, "yyyyMMdd").equals(FormatDate(date2, "yyyyMMdd"));
	}
	
	/**
	 * 整型转Date
	 * @param year
	 * @param month
	 * @param day
	 * @return Date
	 */
	public static Date ToDate(int year, int month, int day)
	{
		return StringToDate(year + "-" + (month + 1) + "-" + day, "yyyy-MM-dd");
	}
	
	
	public static Date parseDate(String dateTimeString, String format, Date defaultValue)
	{
		if(StringHelper.IsNullOrEmpty(dateTimeString))
			return defaultValue;
		
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		try
		{
			return sdf.parse(dateTimeString);
		}
		catch (ParseException e)
		{
			return defaultValue;
		}
	}
	
}
