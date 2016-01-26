package apk.net;

import java.util.Map;

import apk.dal.ConfigDAL;
import apk.model.framework.JsonData;
import apk.model.framework.StreamData;


/**
 * <p>同步请求服务</p>
 * @author DongJiande
 * @version 1.0 2013年9月6日
 */
public class BaseSyncNet extends BaseNet
{
	/**
	 * @param urlParam url参数
	 */
	public BaseSyncNet(String urlParam)
	{
		this._serverBaseURL = ConfigDAL.getServerURL();
		this._urlParam = urlParam;
	}
	
	/**
	 * 普通POST请求
	 * @param pars 参数
	 * @return 结果
	 */
	public JsonData Post(Object pars)
	{
		String url = this.formatURL();
		HttpSyncHelper httpSync = new HttpSyncHelper();
		return httpSync.Post(url, pars);
	}
	
	/**
	 * 普通POST请求
	 * @param parsMap 参数
	 * @return 结果
	 */
	public JsonData Post(String action, Map<String, Object> parsMap)
	{
		String url = this.formatURL();
		HttpSyncHelper httpSync = new HttpSyncHelper();
		return httpSync.Post(url, parsMap);
	}
	
	/**
	 * 普通POST请求
	 * @param stringContent 参数
	 * @return 结果
	 */
	public JsonData Post(String stringContent)
	{
		String url = this.formatURL();
		HttpSyncHelper httpSync = new HttpSyncHelper();
		return httpSync.Post(url, stringContent);
	}
	
	/**
	 * POST请求，返回流
	 * @param parsMap 参数
	 * @return 结果流
	 */
	public StreamData PostAndGetStream(Map<String, Object> parsMap)
	{
		String url = this.formatURL();
		HttpSyncHelper httpSync = new HttpSyncHelper();
		return httpSync.PostAndGetStream(url, parsMap);
	}
}
