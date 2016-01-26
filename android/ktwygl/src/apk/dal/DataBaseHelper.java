package apk.dal;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import apk.common.AppLog;
import apk.common.DateHelper;
import apk.common.StringHelper;

/**
 * @author dongjd
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper
{
	private static DataBaseHelper _instance;
	private static SQLiteDatabase _sqliteDataBase;
	/**
	 * 初始化数据库接口实例
	 * @param context Context
	 */
	public static void InitInstance(Context context)
	{
		if(DataBaseHelper._instance == null)
		{
			DataBaseHelper._instance = new DataBaseHelper(context, "apk.platform.youjish.com.db", null, 1);
			DataBaseHelper._sqliteDataBase = DataBaseHelper._instance.getWritableDatabase();
		}
	}
	
	/**
	 * 开启事务
	 * @return 是否成功
	 */
	public static boolean BeginTransaction()
	{
		try
		{
			DataBaseHelper._sqliteDataBase.beginTransaction();
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "数据库事务开启", "", ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 提交事务
	 * @return 是否成功
	 */
	public static boolean CommonTransaction()
	{
		try
		{
			DataBaseHelper._sqliteDataBase.setTransactionSuccessful();
			DataBaseHelper._sqliteDataBase.endTransaction();
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "数据库事务提交", "", ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 回滚事务
	 * @return 是否成功
	 */
	public static boolean RollBackTransaction()
	{
		try
		{
			DataBaseHelper._sqliteDataBase.endTransaction();
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "数据库事务回滚", "", ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 执行一个不带参数的SQL语句
	 * @param sql SQL语句
	 * @return 是事执行成功
	 */
	public static boolean ExecuteSQL(String sql)
	{
		try
		{
			DataBaseHelper._sqliteDataBase.execSQL(sql);
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "执行SQL", sql, ex);
			return false;
		}
		return true;
	}
	/**
	 * 执行一个不带参数的SQL语句
	 * @param sql SQL语句
	 * @return 是事执行成功
	 */
	public static boolean ExecuteSQLWithErrorLogFile(String sql)
	{
		try
		{
			DataBaseHelper._sqliteDataBase.execSQL(sql);
		}
		catch(Exception ex)
		{
			AppLog.LogErrorWithFile("", "", "", "执行SQL", sql, ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 执行一人带参数的SQL语句
	 * @param sql SQL语句
	 * @param attributes 参数
	 * @return 是否成功
	 */
	public static boolean ExecuteSQL(String sql, Object[] attributes)
	{
		try
		{
			DataBaseHelper._sqliteDataBase.execSQL(sql, attributes);
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "执行SQL", sql, ex);
			return false;
		}
		return true;
	}
	/**
	 * 执行一人带参数的SQL语句
	 * @param sql SQL语句
	 * @param attributes 参数
	 * @return 是否成功
	 */
	public static boolean ExecuteSQLWithErrorLogFile(String sql, Object[] attributes)
	{
		try
		{
			DataBaseHelper._sqliteDataBase.execSQL(sql, attributes);
		}
		catch(Exception ex)
		{
			AppLog.LogErrorWithFile("", "", "", "执行SQL", sql, ex);
			return false;
		}
		return true;
	}
	
	/**
	 * 查询
	 * @param sql SQL语句
	 * @param attributes 参数
	 * @return 结果
	 */
	public static List<Map<String, Object>> Select(String sql, String[] attributes)
	{
		try
		{
			Cursor cursor = DataBaseHelper._sqliteDataBase.rawQuery(sql, attributes);
	
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				
			while (cursor.moveToNext())
			{
				Map<String, Object> map = new HashMap<String, Object>();
				
				int columnCount = cursor.getColumnCount();
				for(int i=0;i<columnCount;i++)
				{
					switch(cursor.getType(i))
					{
						case Cursor.FIELD_TYPE_BLOB:
							map.put(cursor.getColumnName(i), cursor.getBlob(i));
							break;
						case Cursor.FIELD_TYPE_FLOAT:
							map.put(cursor.getColumnName(i), cursor.getFloat(i));
							break;
						case Cursor.FIELD_TYPE_INTEGER:
							map.put(cursor.getColumnName(i), cursor.getInt(i));
							break;
						case Cursor.FIELD_TYPE_NULL:
							map.put(cursor.getColumnName(i), null);
							break;
						case Cursor.FIELD_TYPE_STRING:
							map.put(cursor.getColumnName(i), cursor.getString(i));
							break;
					}
				}
				list.add(map);
			}
		
			return list;
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "查询SQL", sql, ex);
			return null;
		}
	}
	
	/**
	 * 查询
	 * @param sql SQL语句
	 * @return 结果
	 */
	public static List<Map<String, Object>> Select(String sql)
	{
		return Select(sql, null);
	}
	
	/**
	 * @param className 填充类名
	 * @param sql SQL
	 * @param attributes 条件
	 * @return 查询列表
	 */
	public static <T> T SelectTop1(Class<T> className, String sql, String[] attributes)
	{
		List<T> userList = Select(className, sql, attributes);
		if(userList.size() > 0)
			return userList.get(0);
		else
			return null;
	}
	
	/**
	 * @param className 填充类名
	 * @param sql SQL
	 * @return 查询列表
	 */
	public static <T> T SelectTop1(Class<T> className, String sql)
	{
		List<T> userList = Select(className, sql);
		if(userList != null && userList.size() > 0)
			return userList.get(0);
		else
			return null;
	}
	
	/**
	 * @param className 填充类名
	 * @param sql SQL
	 * @return 查询列表
	 */
	public static <T> List<T> Select(Class<T> className, String sql)
	{
		return Select(className, sql, null);
	}
	
	/**
	 * @param className 填充类名
	 * @param sql SQL
	 * @param attributes 条件
	 * @return 查询列表
	 */
	public static <T> List<T> Select(Class<T> className, String sql, String[] attributes)
	{
		try
		{
			Cursor cursor = DataBaseHelper._sqliteDataBase.rawQuery(sql, attributes);
			
			List<T> list = new ArrayList<T>();
			
			if(cursor == null)
				return list;
			
			while (cursor.moveToNext())
			{
				T obj = getEntity(cursor, className);
				list.add(obj);
			}
		
			return list;
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "查询SQL", sql, ex);
			return null;
		}
	}
	
	/**
	 * 将数据库记录转换为对象
	 * 
	 * @param cursor cursor
	 * @param className className
	 * @return 结果
	 */
	public static <T> T getEntity(Cursor cursor, Class<T> className)
	{
		try
		{
			//Class<?> entity_class = entity.getClass();
			T entity = className.newInstance();
			
			if(Map.class.isAssignableFrom(className))
			{
				int columnCount = cursor.getColumnCount();
				Method putMethod = className.getMethod("put", Object.class, Object.class);

				for(int i=0;i<columnCount;i++)
				{
					switch(cursor.getType(i))
					{
						case Cursor.FIELD_TYPE_BLOB:
							putMethod.invoke(entity, cursor.getColumnName(i), cursor.getBlob(i));
							break;
						case Cursor.FIELD_TYPE_FLOAT:
							putMethod.invoke(entity, cursor.getColumnName(i), cursor.getFloat(i));
							break;
						case Cursor.FIELD_TYPE_INTEGER:
							putMethod.invoke(entity, cursor.getColumnName(i), cursor.getInt(i));
							break;
						case Cursor.FIELD_TYPE_NULL:
							putMethod.invoke(entity, cursor.getColumnName(i), null);
							break;
						case Cursor.FIELD_TYPE_STRING:
							putMethod.invoke(entity, cursor.getColumnName(i), cursor.getString(i));
							break;
						default:
							putMethod.invoke(entity, cursor.getColumnName(i), cursor.getString(i));
							break;
					}
				}
			}
			else
			{
				Method[] methods = className.getMethods();
				String value;
				for(Method method : methods)
				{
					String methodName = method.getName();
					if(methodName.startsWith("set") && method.getParameterTypes().length == 1)
					{
						String fieldName = getFieldNameFromSetmethod(methodName);
						int fieldIndex = cursor.getColumnIndex(fieldName);
						if(fieldIndex > -1)
						{
							Class<?> type = method.getParameterTypes()[0];
							if(type == String.class)
							{
								method.invoke(entity, cursor.getString(fieldIndex));
							}
							else if(type == int.class || type == Integer.class)
							{
								switch(cursor.getType(fieldIndex))
								{
									case Cursor.FIELD_TYPE_INTEGER:
										method.invoke(entity, cursor.getInt(fieldIndex));
										break;
									case Cursor.FIELD_TYPE_STRING:
										value = cursor.getString(fieldIndex);
										method.invoke(entity, value == null?0: Integer.parseInt(value, 10));
										break;
								}
							}
							else if(type == long.class || type == Long.class)
							{
								switch(cursor.getType(fieldIndex))
								{
									case Cursor.FIELD_TYPE_INTEGER:
										method.invoke(entity, cursor.getInt(fieldIndex));
										break;
									case Cursor.FIELD_TYPE_STRING:
										value = cursor.getString(fieldIndex);
										method.invoke(entity, value == null?0: Long.parseLong(value, 10));
										break;
								}
							}
							else if(type == float.class || type == Float.class)
							{
								switch(cursor.getType(fieldIndex))
								{
									case Cursor.FIELD_TYPE_FLOAT:
										method.invoke(entity, cursor.getFloat(fieldIndex));
										break;
									case Cursor.FIELD_TYPE_STRING:
										value = cursor.getString(fieldIndex);
										method.invoke(entity, value == null?0: Float.parseFloat(value));
										break;
								}
							}
							else if(type == double.class || type == Double.class)
							{
								switch(cursor.getType(fieldIndex))
								{
									case Cursor.FIELD_TYPE_INTEGER:
										method.invoke(entity, cursor.getInt(fieldIndex));
										break;
									case Cursor.FIELD_TYPE_FLOAT:
										method.invoke(entity, cursor.getDouble(fieldIndex));
										break;
									case Cursor.FIELD_TYPE_STRING:
										value = cursor.getString(fieldIndex);
										method.invoke(entity, value == null?0: Double.parseDouble(value));
										break;
								}
							}
							else if(type == Date.class)
							{
								switch(cursor.getType(fieldIndex))
								{
									case Cursor.FIELD_TYPE_INTEGER:
										method.invoke(entity, new Date(cursor.getInt(fieldIndex)));
										break;
									case Cursor.FIELD_TYPE_STRING:
										method.invoke(entity, DateHelper.StringToDate(cursor.getString(fieldIndex)));
										break;
								}
							}
							else if(type == boolean.class || type == Boolean.class)
							{
								switch(cursor.getType(fieldIndex))
								{
									case Cursor.FIELD_TYPE_INTEGER:
										method.invoke(entity, cursor.getInt(fieldIndex) == 1);
										break;
									case Cursor.FIELD_TYPE_STRING:
										method.invoke(entity, "true".equalsIgnoreCase(cursor.getString(fieldIndex)));
										break;
								}
							}
						}
					}
				}
			}
			return entity;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	

	
	@SuppressLint("SimpleDateFormat")
	private static Date stringToDateTime(String s)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (s != null)
		{
			try
			{
				return sdf.parse(s);
			}
			catch (ParseException e)
			{
				//Log.e(tag, "解析时间错误: " + s, e);
			}
		}
		return null;
	}
	
	
	
	/**
	 * 查询
	 * @param sql SQL语句
	 * @param attributes 参数
	 * @return 结果
	 */
	public static Object SelectToObject(String sql, String[] attributes)
	{
		try
		{
			Cursor cursor = DataBaseHelper._sqliteDataBase.rawQuery(sql, attributes);
				
			if (cursor.moveToNext())
			{
				switch(cursor.getType(0))
				{
					case Cursor.FIELD_TYPE_BLOB:
						return cursor.getBlob(0);
					case Cursor.FIELD_TYPE_FLOAT:
						return cursor.getFloat(0);
					case Cursor.FIELD_TYPE_INTEGER:
						return cursor.getInt(0);
					case Cursor.FIELD_TYPE_NULL:
						return null;
					case Cursor.FIELD_TYPE_STRING:
						return cursor.getString(0);
					default:
						return null;
				}
			}
			else
				return null;
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "查询SQL", sql, ex);
			return null;
		}
	}
	
	/**
	 * 查询
	 * @param sql SQL语句
	 * @param attributes 参数
	 * @return 结果
	 */
	public static long SelectToLong(String sql, String[] attributes)
	{
		try
		{
			Cursor cursor = DataBaseHelper._sqliteDataBase.rawQuery(sql, attributes);
				
			if (cursor.moveToNext())
			{
				switch(cursor.getType(0))
				{
					case Cursor.FIELD_TYPE_INTEGER:
						return cursor.getLong(0);
					default:
						return 0;
				}
			}
			else
				return 0;
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "查询SQL", sql, ex);
			return 0;
		}
	}
	
	/**
	 * 查询
	 * @param sql SQL语句
	 * @param attributes 参数
	 * @return 结果
	 */
	public static Cursor SelectToCursor(String sql, String[] attributes)
	{
		try
		{
			Cursor cursor = DataBaseHelper._sqliteDataBase.rawQuery(sql, attributes);
				
			return cursor;
		}
		catch(Exception ex)
		{
			AppLog.LogError("", "", "", "查询SQL", sql, ex);
			return null;
		}
	}

	
	/**
	 * 获取数据库接口实例
	 * @return 数据库接口实例
	 */
	public static SQLiteDatabase GetDataBaseInstance()
	{
		return DataBaseHelper._sqliteDataBase;
	}
	
	
//	private static String getFieldNameFromGetmethod(String getmethodName)
//	{
//		if(getmethodName.startsWith("get"))
//		{
//			return StringHelper.StringWithFirstLetterLower(getmethodName.substring(3));
//		}
//		else if(getmethodName.startsWith("is"))
//		{
//			return StringHelper.StringWithFirstLetterLower(getmethodName.substring(2));
//		}
//		return null;
//	}
	
	private static String getFieldNameFromSetmethod(String setmethodName)
	{
		if(setmethodName.startsWith("set"))
		{
			return StringHelper.StringWithFirstLetterLower(setmethodName.substring(3));
		}
		return null;
	}
	
	
	
	
	

	/**
	 * @param context Context
	 * @param name 数据库
	 * @param factory factory
	 * @param version version
	 */
	public DataBaseHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0)
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		
	}
}
