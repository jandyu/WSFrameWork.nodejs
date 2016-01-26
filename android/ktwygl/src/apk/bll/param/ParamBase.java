package apk.bll.param;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public abstract class ParamBase
{
	protected String _defid;
	protected String _fmtid;
	protected String _dStyle;
	
	
	public ParamBase()
	{
		//this._postData = new JSONObject();
		//this._postData.put("defid", "");
		//this._postData.put("fmtid", "");
		//this._postData.put("strparam", null);
		//this._postData.put("dStyle", "");
	}
	
	/**
	 * 将POST内容序列化
	 * @return postString
	 */
	public abstract String toPOSTString();
	
	protected String escape(String stringParam)
	{
		try
		{
			return URLEncoder.encode(stringParam, "UTF-8");//.replace("%28", "(").replace("%29", ")");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 将POST内容序列化
	 */
	protected String toPOSTString(String strparam)
	{
		return "defid=" + this._defid + "&fmtid=" + this._fmtid + "&dStyle=" + this._dStyle + "&strparam=" + this.escape(strparam);
	}
	
	protected KeyValueSet simpleClone(String c0, KeyValueSet keyValueSet)
	{
		KeyValueSet copyKeyValueSet = new KeyValueSet();
		
		Iterator<String> keyIter = keyValueSet.keySet().iterator();
		String key;
		Object value;
		copyKeyValueSet.put("c0", c0);
		while (keyIter.hasNext())
		{
			key = (String) keyIter.next();
			value = keyValueSet.get(key);
			copyKeyValueSet.put(key, value);
		}
		
		return copyKeyValueSet;
	}
}
