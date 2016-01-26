package apk.bll.param;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import apk.bll.param.ParamSelect.AndOr;
import apk.bll.param.ParamSelect.Where;
import apk.dal.LogHelper;
import apk.model.framework.JsonData;
import apk.net.BaseAsyncNet;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;

public class Test
{

	public static void select(Context context)
	{
		ParamSelect pp = new ParamSelect();
		pp.setDefid("db_app.app_config");
		pp.setFormatId("json");
		pp.setDStyle("json");
		
		Where where = pp.newWhere();
		where.addCondition("iid", "=", "1", AndOr.AND);
		where.addCondition("config_id", "=", "app title", AndOr.NULL);
		
		pp.setCommonSelect(where, 1, 10);
		
		String jsonString = pp.toPOSTString();
		
		LogHelper.d("json", jsonString);
		
		BaseAsyncNet baseAsync = new BaseAsyncNet(context, "callback=rtn ");
		
		baseAsync.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				try
				{
					SelectResponseData rd = new SelectResponseData(jsonData.getJson());
					
					JSONArray rows = rd.getFirstTableDataList();
					if (rows != null && rows.length() > 0)
					{
						JSONObject row = (JSONObject) rows.get(0);
						LogHelper.d("val", row.getString("val"));
					}
				}
				catch(JSONException je)
				{
					
				}
			}
		});
		baseAsync.Post(pp.toPOSTString());
	}
	
	public static void update(Context context)
	{
		try
		{
			ParamUpdate update = new ParamUpdate();
			update.setDefid("db_app.app_sys_user");
			
			
			KeyValueSet item = new KeyValueSet();
			item.put("UserID", "2");
			item.put("NickName", "djd");
			item.put("FullName", "djd");
			item.put("Passwd", "djd");
			item.put("DepID", "djd");
			item.put("RoleID", "djd");
			item.put("status", "1");
			item.put("empid", "djd");
			
			
//			update.addDeleteRow("db_app.app_sys_user", "19");
//			update.addDeleteRow("db_app.app_sys_user", "21");
			
			
//			update.addInsertRow("db_app.app_sys_user", item);
//			
//			item.put("iid", "19");
//			item.put("FullName", "abcdefg");
//			update.addUpdateRow("app_sys_user", item);
			
			
			LogHelper.d("json", update.toPOSTString());
			
			
			BaseAsyncNet baseAsync = new BaseAsyncNet(context, "");
			
			baseAsync.setPostBackEvent(new HttpAsyncEvent()
			{
				@Override
				public void postBackEvent(JsonData jsonData)
				{
					LogHelper.d("update", jsonData.getJson());
				}
			});
			
			baseAsync.Post(update.toPOSTString());
		}
		catch(Exception je)
		{
			
		}
	}
	
	public static void executeProcedure(Context context)
	{
		try
		{
			KeyValueSet pars = new KeyValueSet();
			pars.put("iid", 1);
			
			ParamProcedure procedure = new ParamProcedure();
			procedure.setDefid("");
			procedure.setDStyle("xml");
			procedure.addProcedure("procedureName", pars);
			
			BaseAsyncNet baseAsync = new BaseAsyncNet(context, "");
			
			baseAsync.setPostBackEvent(new HttpAsyncEvent()
			{
				@Override
				public void postBackEvent(JsonData jsonData)
				{
					LogHelper.d("exec", jsonData.getJson());
				}
			});
			
			baseAsync.Post(procedure.toPOSTString());
		}
		catch(Exception e)
		{}
	}
	
	public static void execute(Context context)
	{
		try
		{
			KeyValueSet pars = new KeyValueSet();
			pars.put("iid", 1);
			
			ParamExecute execute = new ParamExecute();
			execute.setAssembly("namespace");
			execute.setClassName("ClassName");
			execute.setParam(pars);
			
			BaseAsyncNet baseAsync = new BaseAsyncNet(context, "");
			
			baseAsync.setPostBackEvent(new HttpAsyncEvent()
			{
				@Override
				public void postBackEvent(JsonData jsonData)
				{
					LogHelper.d("execute", jsonData.getJson());
				}
			});
			
			baseAsync.Post(execute.toPOSTString());
			
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static void test()
	{
//		select();
//		update();
//		executeProcedure();
//		execute();
	}
}
