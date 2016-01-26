package apk.dal;

import java.util.Date;

import apk.common.DateHelper;


/**
 * @author dongjd
 *
 */
public class LogDAL
{
	/**
	 * @return 是否成功
	 */
	public static boolean CreateTable()
	{
		String createTable = "create table if not exists Log(" +
				"id integer primary key autoincrement, " +
				"type varchar(10), " +
				"option varchar(1000)," +
				"log varchar(8000), " +
				"time datetime, " +
				"loginUserId nvarchar(36)," +
				"userId nvarchar(36), " +
				"storeId nvarchar(36))";
		return DataBaseHelper.ExecuteSQLWithErrorLogFile(createTable);
	}
	
	/**
	 * 删除表
	 * @return 是否成功
	 */
	public static boolean DropTable()
	{
		String dropTable = "drop table if exists Log";
		return DataBaseHelper.ExecuteSQL(dropTable);
	}
	
	
	/**
	 * 记录一条日志到数据库
	 * @param loginUserId 登录用户编号
	 * @param userId
	 * @param storeId
	 * @param log
	 * @return 是否成功
	 */
	private static boolean log(String type, String loginUserId, String userId, String storeId, String option, String log)
	{
		String insertSQL = "insert into Log(type,option,log,time,loginUserId,userId,storeId) values(?,?,?,?,?,?,?);";
		String time = DateHelper.FormatDate((new Date()), "yyyy-MM-dd HH:mm:ss.SSS");
		String[] attrs = new String[]{type, option, log, time, loginUserId, userId, storeId};
		return DataBaseHelper.ExecuteSQLWithErrorLogFile(insertSQL, attrs);
	}
	
	/**
	 * 记录一条错误日志到数据库
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param log 日志内容
	 * @return 是否成功
	 */
	public static boolean LogError(String loginUserId, String userId, String storeId, String option, String log)
	{
		return log("error", loginUserId, userId, storeId, option, log);
	}
	
	/**
	 * 记录一条调试日志到数据库
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param log 日志内容
	 * @return 是否成功
	 */
	public static boolean LogDebug(String loginUserId, String userId, String storeId, String option, String log)
	{
		return log("debug", loginUserId, userId, storeId, option, log);
	}
	
	/**
	 * 记录一条HTTP请求日志到数据库
	 * @param loginUserId 登录用户编号
	 * @param userId 当前用户编号
	 * @param storeId 当前客户编号
	 * @param option 操作
	 * @param log 日志内容
	 * @return 是否成功
	 */
	public static boolean LogHttp(String loginUserId, String userId, String storeId, String option, String log)
	{
		return log("http", loginUserId, userId, storeId, option, log);
	}
	
	
}
