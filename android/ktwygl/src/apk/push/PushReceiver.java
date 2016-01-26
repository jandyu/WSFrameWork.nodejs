package apk.push;

import java.util.Date;

import org.json.JSONObject;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import apk.activity.FlashNativeActivity;
import apk.activity.MainActivity;
import apk.common.JSONReader;
import apk.common.StringHelper;
import apk.common.Util;
import apk.dal.DataBaseHelper;
import apk.dal.LogHelper;
import apk.dal.MessageDAL;
import apk.model.MessageData;
//import apk.platform.youjish.com.model.MessageTypeData;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver
{
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		//Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
		

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
		{
			//String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			//Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...
		}
		else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			//Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			//processCustomMessage(context, bundle);
		}
		else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
		{
			//Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			//int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			//Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			
			
			String jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
			String message = bundle.getString(JPushInterface.EXTRA_ALERT);
			String messageTitle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			try
			{
				JSONReader jsonReader = new JSONReader(jsonString);
				
				String url = jsonReader.getString("url", "");
				String rid = jsonReader.getString("rid", "");
				
//				MessageTypeData mtd = new MessageTypeData();
//				mtd.setMessageTypeId(MessageTypeData.PUSH);
//				mtd.setMessageTypeTitle("推送消息");
				
				MessageData md = new MessageData();
				md.setRid(rid);
//				md.setMessageTypeId(mtd.getMessageTypeId());
				md.setMessageTitle(messageTitle);
				md.setMessageText(message);
				md.setMessageData(url);
				md.setMessageTime(new Date());
				
				DataBaseHelper.InitInstance(context);
				boolean b = MessageDAL.addMessageData(md);
				LogHelper.d("s", b?"true":"false");
			}
			catch(Exception e)
			{
				LogHelper.d("savePushMessage", e.getMessage());
			}
		}
		else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			//Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			
			String jsonString = bundle.getString(JPushInterface.EXTRA_EXTRA);
			

			JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));
			
			
			if(!StringHelper.IsNullOrEmptyOrBlank(jsonString))
			{
				try
				{
					JSONObject jobject = new JSONObject(jsonString);
					String url = jobject.getString("url");
					String rid = jobject.getString("rid");
					
					MessageData md = new MessageData();
					md.setRid(rid);
					md.setMessageData(url);
					
					
					Util.setNotice(md);
					
					if(Util.isAppOnRunning())//没有退出程序
					{
						ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
						ComponentName cn = am.getRunningTasks(2).get(0).topActivity;
						
						if(MainActivity.class.getName().equals(cn.getClassName()))//当前显示的页为MainActivity时
						{
							MainActivity activity = MainActivity.getIntance();
							activity.doNotice();
						}
						else//当要打开的页面不是当前显示的页面时
						{
							Intent i = new Intent(context, MainActivity.class);
							i.putExtras(bundle);
							i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							
							context.startActivity(i);
						}
					}
					else//已退出程序，则要先打开flash页以加载数据
					{
						Intent i = new Intent(context, FlashNativeActivity.class);
						i.putExtras(bundle);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						context.startActivity(i);
					}
				}
				catch(Exception e)
				{
					LogHelper.d("", e.getMessage());
				}
			}
		}
		else
		{
			if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
			{
				LogHelper.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
				// 在这里根据 JPushInterface.EXTRA_EXTRA
				// 的内容处理代码，比如打开新的Activity， 打开一个网页等..
			}
			else
			{
				LogHelper.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
			}
		}
	}

	// 打印所有的 intent extra 数据
//	private static String printBundle(Bundle bundle)
//	{
//		StringBuilder sb = new StringBuilder();
//		for (String key : bundle.keySet())
//		{
//			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
//			{
//				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//			}
//			else
//			{
//				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//			}
//		}
//		return sb.toString();
//	}

	// send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle)
//	{
//		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//		Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//		msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//		if (!StringHelper.IsNullOrEmpty(extras))
//		{
//			try
//			{
//				JSONObject extraJson = new JSONObject(extras);
//				if (null != extraJson && extraJson.length() > 0)
//				{
//					msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//				}
//			}
//			catch (JSONException e)
//			{
//
//			}
//		}
//		context.sendBroadcast(msgIntent);
//	}
}
