package apk.common;

import android.content.Context;
import apk.dal.ConfigDAL;


public class HybridPageHelper
{
	public static String getHybridPageString(Context context)
	{
		//String str = context.getResources().getString(R.string.hybridPageString);
		
		String str = ConfigDAL.getHybridPageString();
		
		String deviceId = Util.getDeviceId();
		if(!StringHelper.IsNullOrEmpty(deviceId))
			str = str.replace("#_DEVICE_UDID_#", deviceId);
		
//		String userInfo = CurrUserInfoService.getUserInfo(context);
//		if(!StringHelper.IsNullOrEmpty(userInfo))
//			str = str.replace("#_userinfo_#", userInfo);
		
		return str;
	}
}
