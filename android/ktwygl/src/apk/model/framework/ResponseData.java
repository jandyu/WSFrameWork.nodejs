package apk.model.framework;

/**
 * @author dongjd
 *
 */
public class ResponseData
{
	private boolean _success;
	private int _code;
	private String _message;
	private boolean _isOutOfTime = false;
	/**
	 * @param success 是否成功
	 * @param message 消息
	 */
	public ResponseData(boolean success, String message)
	{
		this._success = success;
		this._message = message;
	}
	/**
	 * @param success 是否成功
	 * @param code 状态码
	 * @param message 消息
	 */
	public ResponseData(boolean success, int code, String message)
	{
		this._success = success;
		this._code = code;
		this._message = message;
	}
	
	/**
	 * @return 是否成功
	 */
	public boolean getIsSuccess()
	{
		return this._success;
	}
	/**
	 * @param success 是否成功
	 */
	public void setIsSuccess(boolean success)
	{
		this._success = success;
	}
	
	
	
	/**
	 * @return 处理状态编号
	 */
	public int getCode()
	{
		return this._code;
	}
	/**
	 * @param code 处理状态编号
	 */
	public void setCode(int code)
	{
		this._code = code;
	}
	
	
	
	/**
	 * @return 消息
	 */
	public String getMessage()
	{
		return this._message;
	}
	/**
	 * @param message 消息
	 */
	public void setMessage(String message)
	{
		this._message = message;
	}
	
	
	/**
	 * 获取是否超时
	 * @return 结果
	 */
	public boolean getIsOutoftime()
	{
		return this._isOutOfTime;
	}
	/**
	 * @param isOutOfTime 是否超时
	 */
	public void setIsOutOfTime(boolean isOutOfTime)
	{
		this._isOutOfTime = isOutOfTime;
	}
}
