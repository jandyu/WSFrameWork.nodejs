package apk.net;

import java.util.Map;

import android.content.Context;
import apk.dal.ConfigDAL;
import apk.model.framework.JsonData;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;


/**
 * <p>异步请求服务</p>
 * @author DongJiande
 * @version 1.0 2013年9月6日
 */
public class BaseAsyncNet extends BaseNet implements HttpAsyncEvent 
{
	
	private HttpAsyncHelper _httpAsync;
	private HttpAsyncEvent _httpAsyncEvent;
	
	/**
	 * @param urlParam url参数
	 */
	public BaseAsyncNet(Context context, String urlParam)
	{
		this._serverBaseURL = ConfigDAL.getServerURL();
		this._urlParam = urlParam;
		
		this._httpAsync = new HttpAsyncHelper(context);
		this._httpAsync.setPostBackEvent(this);
	}
	
	/**
	 * 普通POST请求
	 * @param pars 参数
	 * @return 结果
	 */
	public void Post(Object pars)
	{
		String url = this.formatURL();
		this._httpAsync.Post(url, pars);
	}
	
	
	/**
	 * 普通POST请求
	 * @param parsMap 参数
	 */
	public void Post(Map<String, Object> parsMap)
	{
		String url = this.formatURL();
		this._httpAsync.Post(url, parsMap);
	}
	
	/**
	 * 普通POST请求
	 * @param stringContent 参数
	 * @return 结果
	 */
	public void Post(String stringContent)
	{
		String url = this.formatURL();
		this._httpAsync.Post(url, stringContent);
	}
	
	
	
	/**
	 * 设置Post回调函数
	 * @param httpAsyncEvent 回调函数
	 */
	public void setPostBackEvent(HttpAsyncEvent httpAsyncEvent)
	{
		this._httpAsyncEvent = httpAsyncEvent;
	}


	@Override
	public void postBackEvent(JsonData jsonData)
	{
		if(this._httpAsyncEvent != null)
		{
			this._httpAsyncEvent.postBackEvent(jsonData);
		}
	}
}
