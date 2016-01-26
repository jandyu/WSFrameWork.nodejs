package apk.net;

/**
 * <p>类名:BaseNet</p>
 * @author DongJiande
 * @version 1.0 2013年9月6日
 */
public class BaseNet
{
	protected String _serverBaseURL;
	protected String _urlParam;
	
	protected String formatURL()
	{
		if(this._serverBaseURL.indexOf("\\?") > -1)
			return this._serverBaseURL + "&" + this._urlParam;
		else
			return this._serverBaseURL + "?" + this._urlParam;
	}
}
