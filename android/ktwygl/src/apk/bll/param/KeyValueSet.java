package apk.bll.param;

import java.util.LinkedHashMap;

public class KeyValueSet extends LinkedHashMap<String, Object>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void set(String key, String value)
	{
		if(value == null)
			this.put(key, "");
		else
			this.put(key, value);
	}
	
	
	
}
