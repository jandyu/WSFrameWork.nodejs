package apk.dal;
//package apk.platform.youjish.com.dal;
//
//import apk.platform.youjish.com.model.UserInfoData;
//
//public class UserDAL
//{
//	static
//	{
//		UserDAL.createTable();
//	}
//	
//	private static boolean dropTable()
//	{
//		String dropTableSQL = "drop table if exists AppUser";
//		return DataBaseHelper.ExecuteSQL(dropTableSQL);
//	}
//	
//	public static boolean createTable()
//	{
//		dropTable();
//		
//		String createTable = "create table if not exists AppUser(" +
//				"iid bigint," +
//				"phone_no varchar(100) primary key," +
//				"passwd varchar(100)," +
//				"name varchar(100)," +
//				"nick_name varchar(100)," +//签名
//				"sign_name varchar(100)," +//昵称
//				"birthday varchar(10)," +//生日
//				"head_pic varchar(100)," +
//				"device_id varchar(100)," +
////				"user_group_id varchar(100)," +
//				"unit_id bigint," +
//				"units varchar(1000)," +//小区及父节点
//				"oid bigint," +
//				"status char(1)," +//审核状态
//				"memo0 varchar(1024)," +
//				"memo1 varchar(100)," +
//				"memo2 varchar(100)," +
//				"memo3 varchar(100)," +
//				"memo4 varchar(100)," +
//				"memo5 varchar(100)," +
//				"memo6 varchar(100)," +
//				"memo7 varchar(100)," +
//				"memo8 varchar(100)," +
//				"memo9 varchar(100)" +
//				")";
//		
//		return DataBaseHelper.ExecuteSQL(createTable);
//	}
//	
//	public static boolean delete()
//	{
//		String sql = "delete from AppUser";
//		return DataBaseHelper.ExecuteSQL(sql);
//	}
//	
//	public static boolean save(UserInfoData userInfoData)
//	{
//		String sql = "delete from AppUser";
//		if(!DataBaseHelper.BeginTransaction())
//			return false;
//		if(!DataBaseHelper.ExecuteSQL(sql))
//		{
//			DataBaseHelper.RollBackTransaction();
//			return false;
//		}
//		sql = "insert into AppUser(iid,phone_no,passwd,name,nick_name,sign_name,birthday,head_pic,device_id,unit_id,units,oid,status,memo0,memo1,memo2,memo3,memo4,memo5,memo6,memo7,memo8,memo9)"
//				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		if(!DataBaseHelper.ExecuteSQL(sql, new Object[]{
//				userInfoData.getIid(),
//				userInfoData.getPhone_no(), 
//				userInfoData.getPasswd(),
//				userInfoData.getName(),
//				userInfoData.getNick_name(), 
//				userInfoData.getSign_name(), 
//				userInfoData.getBirthday(),
//				userInfoData.getHead_pic(),
//				userInfoData.getDevice_id(),
////				userInfoData.getUser_group_id(),
//				userInfoData.getUnit_id(),
//				userInfoData.getUnits(),
//				userInfoData.getOid(),
//				userInfoData.getStatus(),
//				userInfoData.getMemo0(),
//				userInfoData.getMemo1(),
//				userInfoData.getMemo2(),
//				userInfoData.getMemo3(),
//				userInfoData.getMemo4(),
//				userInfoData.getMemo5(),
//				userInfoData.getMemo6(),
//				userInfoData.getMemo7(),
//				userInfoData.getMemo8(),
//				userInfoData.getMemo9()
//				}))
//		{
//			DataBaseHelper.RollBackTransaction();
//			return false;
//		}
//		
//		if(!DataBaseHelper.CommonTransaction())
//		{
//			DataBaseHelper.RollBackTransaction();
//			return false;
//		}
//		return true;
//	}
//	
//	public static UserInfoData getUserInfo()
//	{
//		String sql = "select * from AppUser";
//		
//		UserInfoData h = DataBaseHelper.SelectTop1(UserInfoData.class, sql);
//		
//		return h;
//	}
//	
//	public static boolean isLogin(UserInfoData userData)
//	{
//		if(userData != null && "1".equals(userData.getStatus()))
//			return true;
//		else
//			return false;
//	}
//	
//	public static long getUserIId()
//	{
//		UserInfoData userData = getUserInfo();
//		if(userData != null)
//		{
//			return userData.getIid();
//		}
//		else
//		{
//			return -1;
//		}
//	}
//}
