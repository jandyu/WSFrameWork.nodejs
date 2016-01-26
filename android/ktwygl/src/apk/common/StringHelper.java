package apk.common;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import android.util.Base64;

/**
 * @author dongjd
 *
 */
public class StringHelper
{
	/**
	 * 判断字符串是否为空或空字符串
	 * @param str 源字符串
	 * @return 结果
	 */
	public static boolean IsNullOrEmpty(String str)
	{
		if(str == null || str.length() == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 判断字符串是否为空或空字符串或只包含空格
	 * @param str 源字符串
	 * @return 结果
	 */
	public static boolean IsNullOrEmptyOrBlank(String str)
	{
		if(str == null || str.trim().length() == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * base64String 转 String
	 * @param base64String base64String
	 * @return String
	 */
	public static String Base64StringToString(String base64String)
	{
		byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
		try
		{
			return new String(bytes, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			//MCMLog.LogError("", "", "", "Base64StringToString", base64String, e);
			return "";
		}
	}
	
	/**
	 * String 转 base64String
	 * @param string string
	 * @return String
	 */
	public static String StringToBase64String(String string)
	{
		try
		{
			byte[] bytes = string.getBytes("utf-8");
			return Base64.encodeToString(bytes, Base64.NO_WRAP);
		}
		catch (UnsupportedEncodingException e)
		{
			//MCMLog.LogError("", "", "", "Base64StringToString", base64String, e);
			return "";
		}
	}
	
	/**
	 * 将第一个字母转小写
	 * @param str 原字符串
	 * @return 转换后的字符串
	 */
	public static String StringWithFirstLetterLower(String str)
	{
		if(str == null)
			return null;
		return str.substring(0, 1).toLowerCase(Locale.getDefault()) + str.substring(1);
	}
	
	/**
	 * 将第一个字母转大写
	 * @param str 原字符串
	 * @return 转换后的字符串
	 */
	public static String StringWithFirstLetterUpper(String str)
	{
		if(str == null)
			return null;
		return str.substring(0, 1).toUpperCase(Locale.getDefault()) + str.substring(1);
	}
	
	/**
	 * 将字符串左边增长到指定的长度
	 * @param str
	 * @param totalWidth
	 * @param paddingChar
	 * @return str
	 */
	public static String PadLeft(String str, int totalWidth, char paddingChar)
	{
		int length = str.length();
		if(length < totalWidth)
		{
			StringBuffer s = new StringBuffer(str);
			for(;length <totalWidth;length ++)
			{
				s.insert(0, paddingChar);
			}
			return s.toString();
		}
		return str;
	}
	/**
	 * 将字符串左边增长到指定的长度
	 * @param str
	 * @param totalWidth
	 * @return str
	 */
	public static String PadLeft(String str, int totalWidth)
	{
		int length = str.length();
		if(length < totalWidth)
		{
			StringBuffer s = new StringBuffer(str);
			for(;length <totalWidth;length ++)
			{
				s.insert(0, ' ');
			}
			return s.toString();
		}
		return str;
	}
	/**
	 * 将字符串右边增长到指定的长度
	 * @param str
	 * @param totalWidth
	 * @param paddingChar
	 * @return str
	 */
	public static String PadRight(String str, int totalWidth, char paddingChar)
	{
		int length = str.length();
		if(length < totalWidth)
		{
			StringBuffer s = new StringBuffer(str);
			for(;length <totalWidth;length ++)
			{
				s.append(paddingChar);
			}
			return s.toString();
		}
		return str;
	}
	/**
	 * 将字符串右边增长到指定的长度
	 * @param str
	 * @param totalWidth
	 * @return str
	 */
	public static String PadRight(String str, int totalWidth)
	{
		int length = str.length();
		if(length < totalWidth)
		{
			StringBuffer s = new StringBuffer(str);
			for(;length <totalWidth;length ++)
			{
				s.append(' ');
			}
			return s.toString();
		}
		return str;
	}
	
	public static int parseInt(String str, int defaultValue)
	{
		if(IsNullOrEmptyOrBlank(str))
			return defaultValue;
		
		try
		{
			return Integer.parseInt(str, 10);
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	public static float parseFloat(String str, float defaultValue)
	{
		if(IsNullOrEmptyOrBlank(str))
			return defaultValue;
		
		try
		{
			return Float.parseFloat(str);
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	public static long parseLong(String str, long defaultValue)
	{
		if(IsNullOrEmptyOrBlank(str))
			return defaultValue;
		
		try
		{
			return Long.parseLong(str, 10);
		}
		catch(Exception e)
		{
			return defaultValue;
		}
	}
	
	public static String toLowerCase(String str)
	{
		return str.toLowerCase(Locale.CHINESE);
	}
}
