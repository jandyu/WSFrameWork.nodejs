package apk.bll;

import android.content.Context;
import apk.bll.param.KeyValueSet;
import apk.bll.param.ParamUpdate;
import apk.bll.param.UpdateResponseData;
import apk.common.DateHelper;
import apk.dal.MessageDAL;
import apk.model.MessageData;
import apk.model.framework.JsonData;
import apk.net.BaseAsyncNet;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;

public class MessageServer extends BaseService
{
	public void setRead(Context context, String rid)
	{
		MessageData md = MessageDAL.getMessageByRid(rid);
		if(!md.isRead())
		{
			this.beginSetReadToServer(context, rid);
		}
	}
	
	private void setReadLocal(String rid)
	{
		MessageDAL.setRead(rid);
	}
	
	private void beginSetReadToServer(Context context, final String rid)
	{
		//long userId = UserDAL.getUserIId();
		
		String userId = CurrUserInfoService.getUserId(context);
		
		
		ParamUpdate update = new ParamUpdate();
		update.setDefid("db_app.app_resource_check");
		
		KeyValueSet item = new KeyValueSet();
		item.put("rid", rid);
		item.put("user_iid", userId);
		item.put("checkdtm", DateHelper.GetNowYYYYMMDDHHMMSS() );
		
		update.addInsertRow("db_app.app_resource_check", item);
		
		String jsonStringResource = update.toPOSTString();
		
		BaseAsyncNet post = new BaseAsyncNet(context, "");
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				String json = jsonData.getJson();
				UpdateResponseData updateResponseData = new UpdateResponseData(json);
				boolean isSuccess = updateResponseData.getIsSuccess();
				if (isSuccess)
				{
					setReadLocal(rid);
				}
			}
		});
		
		post.Post(jsonStringResource);
	}
}
