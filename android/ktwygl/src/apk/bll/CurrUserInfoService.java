package apk.bll;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import apk.bll.param.ParamSelect;
import apk.bll.param.ParamSelect.AndOr;
import apk.bll.param.ParamSelect.Where;
import apk.common.DateHelper;
import apk.common.JSONReader;
import apk.common.StringHelper;
import apk.common.Util;
import apk.model.framework.JsonData;
import apk.model.framework.JsonData.JsonDataState;
import apk.net.BaseAsyncNet;
import apk.net.BaseSyncNet;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;
import cn.jpush.android.api.JPushInterface;

public class CurrUserInfoService extends BaseService
{
	private static String _uid;
	private static String _userInfoString;
	private static String _unittitle;
	private static Date _expired;//有效时间，过了这个时间，将重新去服务端取用户信息
	
	public static String getUserInfo(Context context)
	{
		CurrUserInfoService.reInitUserInfo(context);
		return _userInfoString;
	}
	
	public static String getUserId(Context context)
	{
		CurrUserInfoService.reInitUserInfo(context);
		return _uid;
	}
	
	private static void reInitUserInfo(final Context context)
	{
		if(_expired == null || DateHelper.GetNow().compareTo(_expired) > 0 || "".equals(_uid))
		{
			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					initUserInfo(context);
				}
			});
			thread.start();
			
			try
			{
				thread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private static void initJPush(Context context)
	{
		Set<String> tags = new LinkedHashSet<String>();
		
		String units = _unittitle;
		if(!StringHelper.IsNullOrEmptyOrBlank(units))
		{
			for(String unit : units.split(","))
			{
				tags.add(unit);
			}
		}
		
		JPushInterface.setAliasAndTags(context, null, tags, null);//Tag 组
		JPushInterface.setAliasAndTags(context, Util.getDeviceId(), null, null);//设备别名
	}
	
	public static void beginInitUserInfo(final Context context)
	{
		ParamSelect pp = new ParamSelect();
		pp.setDefid("hy_app_user_info");
		pp.setFormatId("hy_app_user_info");
		pp.setDStyle("xml");
		
		Where where = pp.newWhere();
		where.addCondition("device_id", "=", Util.getDeviceId(), AndOr.NULL);
		
		pp.setCommonSelect(where, 1, 1);
		
		String json = pp.toPOSTString();
		
		BaseAsyncNet post = new BaseAsyncNet(context, "callback=rtn");
		
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				jsonToData(context, jsonData);
			}
		});
		
		post.Post(json);
	}
	
	private static void initUserInfo(final Context context)
	{
		ParamSelect pp = new ParamSelect();
		pp.setDefid("hy_app_user_info");
		pp.setFormatId("hy_app_user_info");
		pp.setDStyle("xml");
		
		Where where = pp.newWhere();
		where.addCondition("device_id", "=", Util.getDeviceId(), AndOr.NULL);
		
		pp.setCommonSelect(where, 1, 1);
		
		String json = pp.toPOSTString();
		
		BaseSyncNet post = new BaseSyncNet("callback=rtn");
		
		jsonToData(context, post.Post(json));
	}
	
	private static boolean jsonToData(final Context context, JsonData jsonData)
	{
		if(jsonData.getState() == JsonDataState.OK)
		{
			_userInfoString = jsonData.getJson();
			JSONReader reader = new JSONReader(_userInfoString);
			
			_uid = reader.getString("uid", "");
			_unittitle = reader.getString("unittitle", "");//供Jpush用的组编号列表（组编号及所有组祖宗节点编号）
			
			_expired = CurrUserInfoService.getExpiredDateTime(reader.getString("expired", ""));
			
			
			initJPush(context);
			
			return true;
		}
		else
		{
			_uid = "";
			_unittitle = "";
			_expired = DateHelper.GetTomorrow();
			return false;
		}
	}
	
	private static Date getExpiredDateTime(String expired)
	{
		//默认取第二天
		return DateHelper.parseDate(expired, "yyyy-MM-dd HH:mm:ss", DateHelper.GetTomorrow());
	}
}
