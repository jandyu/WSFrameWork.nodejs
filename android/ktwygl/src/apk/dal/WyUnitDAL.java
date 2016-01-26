package apk.dal;
//package apk.platform.youjish.com.dal;
//
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import apk.platform.youjish.com.model.WyUnitData;
//
//public class WyUnitDAL
//{
//	static
//	{
//		WyUnitDAL.createTable();
//	}
//	
////	private static boolean dropTable()
////	{
////		String dropTableSQL = "drop table if exists WyUnit";
////		return DataBaseHelper.ExecuteSQL(dropTableSQL);
////	}
//	
//	public static boolean createTable()
//	{
////		dropTable();
//		
//		String createTable = "create table if not exists WyUnit(" +
//				"iid bigint primary key," +
//				"pid bigint," +
//				"title varchar(100)" +
//				")";
//		
//		return DataBaseHelper.ExecuteSQL(createTable);
//	}
//	
////	public static boolean delete()
////	{
////		String sql = "delete from WyUnit";
////		return DataBaseHelper.ExecuteSQL(sql);
////	}
//	
//	public static boolean save(JSONArray jsonArray)
//	{
//		if(jsonArray != null)
//		{
//			if(DataBaseHelper.BeginTransaction())
//			{
//				String sql = "delete from WyUnit";
//				if(! DataBaseHelper.ExecuteSQL(sql))
//				{
//					DataBaseHelper.RollBackTransaction();
//					return false;
//				}
//				for(int i=0,j=jsonArray.length();i<j;i++)
//				{
//					try
//					{
//						JSONObject o = (JSONObject) jsonArray.get(i);
//						sql = "insert into WyUnit(iid,pid,title)values(?,?,?)";
//						if(!DataBaseHelper.ExecuteSQL(sql, new Object[]{o.get("iid"), o.get("pid"), o.get("title")}))
//						{
//							DataBaseHelper.RollBackTransaction();
//							return false;
//						}
//					}
//					catch(Exception e)
//					{
//						DataBaseHelper.RollBackTransaction();
//						return false;
//					}
//				}
//				if(!DataBaseHelper.CommonTransaction())
//				{
//					DataBaseHelper.RollBackTransaction();
//					return false;
//				}
//			}
//		}
//		return true;
//	}
//	
//	public static WyUnitData getData(long iid)
//	{
//		String sql = "select * from WyUnit where iid = ?";
//		
//		WyUnitData h = DataBaseHelper.SelectTop1(WyUnitData.class, sql, new String[]{ String.valueOf(iid)});
//		
//		return h;
//	}
//	
//	public static List<WyUnitData> getDataListByPid(long pid)
//	{
//		String sql = "select * from WyUnit where pid = ?";
//		
//		List<WyUnitData> list = DataBaseHelper.Select(WyUnitData.class, sql, new String[]{ String.valueOf(pid)});
//		
//		return list;
//	}
//	
//	public static String getTitle(long iid)
//	{
//		WyUnitData h = getData(iid);
//		
//		if(h != null)
//		{
//			return h.getTitle();
//		}
//		
//		return "";
//	}
//	
//	public static boolean isDataNull()
//	{
//		String sql = "select count(*) from WyUnit";
//		Object o = DataBaseHelper.SelectToObject(sql, null);
//		if(o == null || "0".equals(o.toString()))
//			return true;
//		else
//			return false;
//	}
//	
//	
//}
