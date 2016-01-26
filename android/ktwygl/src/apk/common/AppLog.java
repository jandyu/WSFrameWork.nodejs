package apk.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import apk.dal.ConfigDAL;
import apk.dal.LogDAL;

/**
 * @author dongjd
 *
 */
public class AppLog extends LogDAL
{
	private static String logPath;
	
	/**
	 * 初始化
	 * @param context Context
	 */
	public static void Init(Context context)
	{
		logPath = ConfigDAL.getLogpath();
	}
	/**
	 * 记录一条日志到数据库
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param log 日志内容
	 * @return 是否成功
	 */
	private static boolean log(String type, String loginUserId, String userId, String storeId, String option, String log)
	{
		if(logPath == null)
			return false;
		
		File filePath = new File(logPath);
		if(!filePath.exists())
		{
			if(!filePath.mkdir())//文件夹不存在且创建失败
				return false;
		}
		
		//文件夹已存在
		Date date = new Date();
		File file = new File(logPath + DateHelper.FormatDate(date, "yyyyMMdd") + ".log");
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		
		//文件已存在
		try
		{
			FileWriter writer = new FileWriter(file);
			String logText = DateHelper.FormatDate(date, "yyyy-MM-dd HH:mm:ss.SSS") + 
					"\r\ntype:" + type + 
					"\r\noption" + option +
					"\r\nloginUserId:" + loginUserId + "userId:" + userId + ",storeId:" + storeId + 
					"\r\nlog:" + log;
			writer.write(logText);
			writer.flush();
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 记录一条错误日志
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param ex 错误对象
	 * @return 是否成功
	 */
	public static boolean LogError(String loginUserId, String userId, String storeId, String option, Exception ex)
	{
		String log = ex.getMessage();
		if(LogDAL.LogError(loginUserId, userId, storeId, option, log))
			return true;
		else
			return AppLog.log("error", loginUserId, userId, storeId, option, log);
	}
	
	/**
	 * 记录一条错误日志
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param information 消息
	 * @param ex 错误对象
	 * @return 是否成功
	 */
	public static boolean LogError(String loginUserId, String userId, String storeId, String option, String information, Exception ex)
	{
		String log = information + "\r\n" + ex.getMessage();
		if(LogDAL.LogError(loginUserId, userId, storeId, option, log))
			return true;
		else
			return AppLog.log("error", loginUserId, userId, storeId, option, log);
	}
	
	/**
	 * 记录一条错误日志
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param information 消息
	 * @param ex 错误对象
	 * @return 是否成功
	 */
	public static boolean LogErrorWithFile(String loginUserId, String userId, String storeId, String option, String information, Exception ex)
	{
		String log = information + "\r\n" + ex.getMessage();
		return AppLog.log("error", loginUserId, userId, storeId, option, log);
	}
	
	/**
	 * 记录一条调试日志
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param log 日志内容
	 * @return 是否成功
	 */
	public static boolean LogDebug(String loginUserId, String userId, String storeId, String option, String log)
	{
		if(LogDAL.LogDebug(loginUserId, userId, storeId, option, log))
			return true;
		else
			return AppLog.log("debug", loginUserId, userId, storeId, option, log);
	}
	
	/**
	 * 记录一条HTTP请求日志
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param log 日志内容
	 * @return 是否成功
	 */
	public static boolean LogHttp(String loginUserId, String userId, String storeId, String option, String log)
	{
		if(LogDAL.LogHttp(loginUserId, userId, storeId, option, log))
			return true;
		else
			return AppLog.log("http", loginUserId, userId, storeId, option, log);
	}
}
