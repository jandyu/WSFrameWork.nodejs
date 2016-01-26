package apk.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader
{
	private JSONObject _jsonObject;
	public JSONReader(JSONObject jsonObject)
	{
		this._jsonObject = jsonObject;
	}
	
	public JSONReader(String jsonString)
	{
		try
		{
			this._jsonObject = new JSONObject(jsonString);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	public Object getObject(String key, Object defaultValue)
	{
		if(this._jsonObject == null)
			return defaultValue;
		
		try
		{
			if(this._jsonObject.has(key))
			{
				Object o = this._jsonObject.get(key);
				if(o == null)
					return defaultValue;
				else
					return o;
			}
			else
				return defaultValue;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	public String getString(String key, String defaultValue)
	{
		return this.getObject(key, defaultValue).toString();
	}
	
	public long getLong(String key, long defaultValue)
	{
		String value = this.getString(key, null);
		
		if(value == null)
			return defaultValue;
		
		try
		{
			return Long.parseLong(value, 10);
		}
		catch(Exception ex)
		{
			return defaultValue;
		}
	}
	public int getInt(String key, int defaultValue)
	{
		String value = this.getString(key, null);
		
		if(value == null)
			return defaultValue;
		
		try
		{
			return Integer.parseInt(value, 10);
		}
		catch(Exception ex)
		{
			return defaultValue;
		}
	}
	
	public JSONArray getJSONArray(String key)
	{
		if(this._jsonObject == null)
			return null;
		
		if(this._jsonObject.has(key))
		{
			try
			{
				return this._jsonObject.getJSONArray(key);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
