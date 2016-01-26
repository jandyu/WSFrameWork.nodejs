package apk.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import apk.dal.LogHelper;
import apk.model.MessageData;

/**
 * @author yangyu 功能描述：常量工具类
 */
public class Util
{
	
	
	
	/**
	 * 得到设备屏幕的宽度
	 * 
	 * @param context context
	 * @return screenWidth
	 */
	public static int getScreenWidth(Context context)
	{
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 得到设备屏幕的高度
	 */
	public static int getScreenHeight(Context context)
	{
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 得到设备的密度
	 */
	public static float getScreenDensity(Context context)
	{
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 把密度转换为像素
	 */
	public static int dip2px(Context context, float px)
	{
		final float scale = getScreenDensity(context);
		return (int) (px * scale + 0.5);
	}

	// /**
	// * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	// * @param context Context
	// * @param dpValue dp值
	// * @return px值
	// */
	// public static int dip2px(Context context, float dpValue)
	// {
	// final float scale = context.getResources().getDisplayMetrics().density;
	// return (int) (dpValue * scale + 0.5f);
	// }

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * 
	 * @param context Context
	 * @param pxValue px值
	 * @return dip值
	 */
	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param context Context
	 * @param pxValue px值
	 * @return sp值
	 */
	public static int px2sp(Context context, float pxValue)
	{
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param context Context
	 * @param spValue sp值
	 * @return px值
	 */
	public static int sp2px(Context context, float spValue)
	{
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	private static int _versionCode;
	private static String _versionName;
	private static String _deviceId;
	private static String _absolutePath;
	private static boolean _isAppOnRunning = false;;
	public static void init(Context context)
	{
		try
		{
			PackageManager pm = context.getPackageManager();
			PackageInfo packInfo = pm.getPackageInfo(context.getPackageName(), 0);
			_versionCode = packInfo.versionCode;
			_versionName = packInfo.versionName;
			_absolutePath = context.getApplicationContext().getFilesDir().getAbsolutePath();
			_isAppOnRunning = true;
		}
		catch (Exception e)
		{
			LogHelper.d("读取版本号时出错", e.getMessage());
		}

		_deviceId = Util.getDeviceId(context);
	}

	public static int getVersionCode()
	{
		return _versionCode;
	}
	public static String getVersionName()
	{
		return _versionName;
	}


	
	public static String getAbsolutePath()
	{
		return _absolutePath;
	}
	
	public static boolean isAppOnRunning()
	{
		return _isAppOnRunning;
	}
	
	private static MessageData _messageData;
	public static void setNotice(MessageData messageData)
	{
		_messageData = messageData;
	}
	public static MessageData getNotice()
	{
		return _messageData;
	}

	public static String getNewURL(String thisURL, String newURL)
	{
		String newURLStr;
		try
		{
			URI uri = new URI(thisURL);
			newURLStr = uri.resolve(newURL).toString();
		}
		catch (Exception e)
		{
			newURLStr = "";
		}
		return newURLStr;
	}

	private static String getDeviceId(Context _context)
	{
//		TelephonyManager tm = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
//		String deviceId = tm.getDeviceId();
//		if (deviceId == null || deviceId.trim().length() == 0)
//		{
//			deviceId = String.valueOf(System.currentTimeMillis());
//		}
//		return deviceId;
		
		
		OpenUDID_manager.sync(_context);
		//OpenUDID_manager.isInitialized();
		return OpenUDID_manager.getOpenUDID();
		
	}
	/**
	 * 获取设备唯一编号
	 * @return 设备唯一编号
	 */
	public static String getDeviceId()
	{
		return _deviceId;
	}
	
	public static int getMaxValue(int value1, int value2)
	{
		if(value1 > value2)
			return value1;
		else
			return value2;
	}
	/**
	 * 获取文件名，如：http://m.youjish.cn/web/native_pic/bottom_menu_icon1.png 返回 bottom_menu_icon1.png
	 * @param fileFullName 文件全名名
	 * @return 文件名
	 */
	public static String getFileName(String fileFullName)
	{
		if(fileFullName == null)
			return null;
		
		int lastP1 = fileFullName.lastIndexOf("/");
		int lastP2 = fileFullName.lastIndexOf("\\");
		int lastP = getMaxValue(lastP1, lastP2);
		
		if(lastP < 0)
			return fileFullName;
		
		return fileFullName.substring(lastP + 1);
	}
	
	/**
	 * 获取文件路径，如：http://m.youjish.cn/web/native_pic/bottom_menu_icon1.png 返回 http://m.youjish.cn/web/native_pic/
	 * @param fileFullName 文件全名名
	 * @return 文件名
	 */
	public static String getPath(String fileFullName)
	{
		if(fileFullName == null)
			return null;
		
		int lastP1 = fileFullName.lastIndexOf("/");
		int lastP2 = fileFullName.lastIndexOf("\\");
		int lastP = getMaxValue(lastP1, lastP2);
		
		if(lastP < 0)
			return fileFullName;
		
		return fileFullName.substring(0, lastP + 1);
	}
	
	public static boolean createIfNotExistsPath(String fileFullName)
	{
		File path = new File(Util.getPath(fileFullName));
		if(!path.exists())
		{
			try
			{
				path.mkdirs();
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public static boolean copyFile(String sourceFile, String targetFile, boolean isOverWrite)
	{
		File fileTarget = new File(targetFile);
		if(!isOverWrite)
		{
			if(fileTarget.exists())
				return true;
		}
		
		File fileSource = new File(sourceFile);
		if(!fileSource.exists())
		{
			return false;
		}
		
		try
		{
			FileInputStream in = new FileInputStream(fileSource);
			FileOutputStream out = new FileOutputStream(targetFile);
			byte[] buffer = new byte[1024];
			int readedSize;
			while((readedSize = in.read(buffer, 0, 1024)) > 0)
			{
				out.write(buffer, 0, readedSize);
			}
			out.flush();
			out.close();
			in.close();
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}
	
	
	public static void killSelf(Context context)
	{
		ActivityManager activityMan = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> process = activityMan.getRunningAppProcesses();
		
		int len = process.size();
		String currentProcess = context.getApplicationInfo().processName;
		for (int i = 0; i < len; i++)
		{
			if (process.get(i).processName.equals(currentProcess))
			{
				android.os.Process.killProcess(process.get(i).pid);
			}
		}
	}
	/**
	 * 网络是否连接
	 * @param context context
	 * @return
	 */
	public static boolean isNetConnect(Context context)
	{
		boolean bisConnFlag=false;
        ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if(network!=null)
        {
            bisConnFlag=conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
	}
	
	/**
	 * 文件是否存在
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExists(String fileName)
	{
		File file = new File(fileName);
		return file.exists();
	}
	
	
	public static String isNull(Object o, String defaultValue)
	{
		if(o == null)
			return defaultValue;
		else
			return o.toString();
	}
	
	public static long isNull(Object o, long defaultValue)
	{
		if(o == null)
			return defaultValue;
		else
			return Long.parseLong(o.toString(), 10);
	}
	
	
	
	
	
	public static void gc()
	{
		System.gc();
	}
}