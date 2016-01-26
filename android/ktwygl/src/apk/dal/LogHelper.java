package apk.dal;

import android.util.Log;

public class LogHelper
{
	public static void d(String tag, String message)
	{
		Log.d(isNull(tag), isNull(message));
	}
	
	public static void e(String tag, String message)
	{
		Log.e(isNull(tag), isNull(message));
	}
	
	
	private static String isNull(String str)
	{
		if(str == null)
			return "";
		
		return str;
	}
}
