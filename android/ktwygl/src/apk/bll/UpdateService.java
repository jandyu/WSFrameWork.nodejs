package apk.bll;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import apk.bll.param.ParamSelect;
import apk.bll.param.SelectResponseData;
import apk.bll.param.ParamSelect.AndOr;
import apk.bll.param.ParamSelect.Where;
import apk.common.JSONReader;
import apk.common.Util;
import apk.dal.ConfigDAL;
import apk.dal.LogHelper;
import apk.model.framework.JsonData;
import apk.net.BaseAsyncNet;
import apk.net.DownLoadFile;
import apk.net.DownLoadFile.OnDownLoadFileFinishListener;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;

public class UpdateService
{
//	public static boolean checkUpdate()
//	{
//		return ConfigDAL.getAppUpdateVersion() > Util.getVersionCode();
//	}
	
	
	
	
	
	public interface OnCheckUpdate
	{
		public void OnCheckUpdateFinish(boolean success, boolean haveNewVersion);
	}
	
	private OnCheckUpdate _onCheckUpdate;
	public void setOnCheckUpdate(OnCheckUpdate onCheckUpdate)
	{
		this._onCheckUpdate = onCheckUpdate;
	}
	
	public void beginCheckUpdate(Context context)
	{
		if(_onCheckUpdate != null)
		{
			int version = Util.getVersionCode();
			
			ParamSelect pp = new ParamSelect();
			pp.setDefid("db_app.app_config");
			pp.setFormatId("json");
			pp.setDStyle("json");
			
			Where where = pp.newWhere();
			where.addCondition("ver", "<=", String.valueOf(version), AndOr.AND);
			where.addCondition("config_id", "=", ConfigDAL.getAppUpdateVersionKey(), AndOr.AND);
			where.addCondition(true, "pf", "=", "normal", false, AndOr.OR);
			where.addCondition(false, "pf", "=", "android", true, AndOr.NULL);
			
			pp.setCommonSelect(where);
			
			BaseAsyncNet post = new BaseAsyncNet(context, "callback=rtn");
			
			post.setPostBackEvent(new HttpAsyncEvent()
			{
				@Override
				public void postBackEvent(JsonData jsonData)
				{
					try
					{
						SelectResponseData rd = new SelectResponseData(jsonData.getJson());
						if(rd.getIsSuccess())
						{
							JSONReader jsonReader = new JSONReader( rd.getFirstTableFirstRow() );
							//ConfigDAL.saveUpdateVersion(jsonReader.getString("val", "-1"));
							
							int newVersion = jsonReader.getInt("val", -1);
							
							_onCheckUpdate.OnCheckUpdateFinish(true, newVersion > Util.getVersionCode());
						}
						else
						{
							_onCheckUpdate.OnCheckUpdateFinish(false, false);
						}
					}
					catch(Exception e)
					{
						LogHelper.e("checkUpdate", e.getMessage());
					}
				}
			});
			
			post.Post(pp.toPOSTString());
		}
	}
	
	
	
	
	
	
	
	public interface OnDownLoadAPK
	{
		public void OnDownLoadAPKFinish(boolean success, String fileName);
	}
	
	private OnDownLoadAPK _onDownLoadAPK;
	public void setOnDownLoadAPK(OnDownLoadAPK onDownLoadAPK)
	{
		this._onDownLoadAPK = onDownLoadAPK;
	}
	
	public void beginDownLoadAPK(Context context)
	{
		String appUpdateUrl = ConfigDAL.getAppUpdateUrl();
		if (appUpdateUrl != null)
		{
			// String updatePath = Util.getAbsolutePath() +
			// "/update/";
			// 获取跟目录
			// "/mnt/sdcard/Pictures/update/";
			String updatePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/update/";
			File path = new File(updatePath);
			if (!path.exists())
			{
				path.mkdirs();
			}
			final String fileName = updatePath + "Update.apk";
			
			File file = new File(fileName);
			if (file.exists())
			{
				if (file.isFile())
				{
					file.delete();
				}
			}
			
			DownLoadFile downLoadFile = new DownLoadFile();
			downLoadFile.setOnDownLoadFileFinishListener(new OnDownLoadFileFinishListener()
			{
				@Override
				public void OnDownLoadFileFinish(boolean isSuccess)
				{
					if(_onDownLoadAPK != null)
					{
						_onDownLoadAPK.OnDownLoadAPKFinish(isSuccess, fileName);
					}
				}
			});
			downLoadFile.beginDownLoadFile(context, appUpdateUrl, fileName);
		}
	}
	
}
