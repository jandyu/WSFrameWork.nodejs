package apk.bll;

import android.content.Context;
import apk.dal.ConfigDAL;

public class FlashPageService extends BaseService
{
//	public static boolean initAppConfig()
//	{
//		//return initAppConfigFromServer() && initWyUnitFromServer();
//		
//		return initAppConfigFromServer();
//	}
//	
//	private static boolean initAppConfigFromServer()
//	{
//		int version = Util.getVersionCode();
//		
//		ParamSelect pp = new ParamSelect();
//		pp.setDefid("db_app.app_config");
//		pp.setFormatId("json");
//		pp.setDStyle("json");
//		
//		Where where = pp.newWhere();
//		where.addCondition("ver", "<=", String.valueOf(version), AndOr.AND);
//		where.addCondition(true, "pf", "=", "normal", false, AndOr.OR);
//		where.addCondition(false, "pf", "=", "android", true, AndOr.NULL);
//		
//		pp.setCommonSelect(where);
//		
//		BaseSyncNet post = new BaseSyncNet("callback=rtn");
//		JsonData jsonData = post.Post(pp.toPOSTString());
//		
//		try
//		{
//			SelectResponseData rd = new SelectResponseData(jsonData.getJson());
//			if(rd.getIsSuccess())
//			{
//				JSONArray rows = rd.getFirstTableDataList();
//				if(ConfigDAL.save(rows))
//				{
//					if(AppPictureService.downLoadPicture())//下载不存在的文件
//					{
//						return true;
//					}
//				}
//			}
//			
//			return !ConfigDAL.isDataNull();//第一次用时必须下载数据
//		}
//		catch(Exception e)
//		{
//			Log.e("initAppConfigFromServer", e.getMessage());
//			return false;
//		}
//	}
	
	
	
	
	
	public static boolean initAppConfigFromXML(Context context)
	{
		return ConfigDAL.init(context);
	}
	
//	private static boolean initWyUnitFromServer()
//	{
//		ParamSelect pp = new ParamSelect();
//		pp.setDefid("db_app.app_wy_unit");
//		pp.setFormatId("json");
//		pp.setDStyle("json");
//		
//		Where where = pp.newWhere();
//		
//		
//		pp.setCommonSelect(where);
//		
//		BaseSyncNet post = new BaseSyncNet("callback=rtn");
//		JsonData jsonData = post.Post(pp.toPOSTString());
//		
//		try
//		{
//			SelectResponseData rd = new SelectResponseData(jsonData.getJson());
//			if(rd.getIsSuccess())
//			{
//				JSONArray rows = rd.getFirstTableDataList();
//				if(WyUnitDAL.save(rows))
//				{
//					return true;
//				}
//			}
//			
//			return !WyUnitDAL.isDataNull();//第一次用时必须下载数据
//		}
//		catch(Exception e)
//		{
//			Log.e("initWyUnitFromServer", e.getMessage());
//			return false;
//		}
//	}
}
