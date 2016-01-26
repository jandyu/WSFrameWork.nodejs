package apk.net;

import java.util.EventListener;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import apk.model.framework.JsonData;

/**
 * <p>类名:HttpAsyncHelper</p>
 * @author DongJiande
 * @version 1.0 2013年9月6日
 */
public class HttpAsyncHelper
{
	private Context _context;
	private HttpAsyncEvent _httpAsyncEvent;
	private Handler _uiHandler;
	
//	@SuppressLint("HandlerLeak")
//	private class UIHandler extends Handler
//	{
//		public void handleMessage(Message msg)
//		{
//			super.handleMessage(msg);
//			JsonData jsonData = (JsonData)msg.obj;
//			
//			if(_httpAsyncEvent != null)
//			{
//				_httpAsyncEvent.postBackEvent(jsonData);
//			}
//		}
//	}
	
	
	/**
	 * <p>类名:HttpAsyncEvent</p>
	 * @author DongJiande
	 * @version 1.0 2013年9月6日
	 */
	public interface HttpAsyncEvent extends EventListener
	{
		/**
		 * 监听事件
		 * @param jsonData JsonData
		 */
		public void postBackEvent(JsonData jsonData);
	}
//	/**
//	 * <p>类名:JsonDataEventData</p>
//	 * @author DongJiande
//	 * @version 1.0 2013年9月6日
//	 */
//	public class JsonDataEventData extends EventObject
//	{
//		/**
//		 * @param source source
//		 */
//		public JsonDataEventData(Object source)
//		{
//			super(source);
//		}
//
//		/**
//		 * 
//		 */
//		private static final long	serialVersionUID	= -5359495341787257336L;
//		
//	}
	
	/**
	 * 构造函数
	 */
	public HttpAsyncHelper(Context context)
	{
		this._context = context;
		//this._uiHandler = new UIHandler();
		this._uiHandler = new Handler(this._context.getMainLooper())
		{
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				JsonData jsonData = (JsonData)msg.obj;
				
				if(_httpAsyncEvent != null)
				{
					_httpAsyncEvent.postBackEvent(jsonData);
				}
			}
		};
	}
	
	
	/**
	 * 普通POST请求
	 * @param url 请求地址
	 * @param pars 参数
	 */
	public void Post(final String url, final Object pars)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				HttpSyncHelper httpHelper = new HttpSyncHelper();
				JsonData jsonData = httpHelper.Post(url, pars);
				
				Message msg = new Message();
				msg.obj = jsonData;
				
				_uiHandler.sendMessage(msg);
			}
		});
		thread.start();
	}
	
	
	/**
	 * 普通POST请求
	 * @param url 请求地址
	 * @param parsMap 参数
	 */
	public void Post(final String url, final Map<String, Object> parsMap)
	{
		
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				HttpSyncHelper httpHelper = new HttpSyncHelper();
				JsonData jsonData = httpHelper.Post(url, parsMap);
				
				Message msg = new Message();
				msg.obj = jsonData;
				
				_uiHandler.sendMessage(msg);
			}
		});
		thread.start();
	}
	
	/**
	 * 普通POST请求
	 * @param url 请求地址
	 * @param stringContent 参数
	 */
	public void Post(final String url, final String stringContent)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				HttpSyncHelper httpHelper = new HttpSyncHelper();
				JsonData jsonData = httpHelper.Post(url, stringContent);
				
				Message msg = new Message();
				msg.obj = jsonData;
				
				_uiHandler.sendMessage(msg);
			}
		});
		thread.start();
	}
	
	
	
	
	/**
	 * 设置Post回调函数
	 * @param httpAsyncEvent 回调函数
	 */
	public void setPostBackEvent(HttpAsyncEvent httpAsyncEvent)
	{
		this._httpAsyncEvent = httpAsyncEvent;
	}


}
