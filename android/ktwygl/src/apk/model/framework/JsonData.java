package apk.model.framework;

/**
 * @author dongjd
 *
 */
public class JsonData
{
	
	/**
	 * 处理结果枚举
	 * @author dongjd
	 *
	 */
	public enum JsonDataState
	{
		
		/**
		 * 成功
		 */
		OK,
		/**
		 * 失败，网络不可用
		 */
		NetworkError,
		/**
		 * 失败，服务器内部错误
		 */
		ServerError,
		/**
		 * 失败，客户端内部错误
		 */
		ClientError
	}
	private JsonDataState state;
	private int code;
	private String json;
	private String message;
	
	
	/**
	 * 获取处理状态
	 * @return 处理状态
	 */
	public JsonDataState getState()
	{
		return state;
	}
	/**
	 * 设置处理状态
	 * @param state 处理状态
	 */
	public void setState(JsonDataState state)
	{
		this.state = state;
	}
	/**
	 * 获取服务端返回结果状态编号
	 * @return 服务端返回结果状态编号
	 */
	public int getCode()
	{
		return code;
	}
	
	/**
	 * 设置服务端返回结果状态编号
	 * @param code 服务端返回结果状态编号
	 */
	public void setCode(int code)
	{
		this.code = code;
	}
	
	/**
	 * 获取JSON字符串
	 * @return JSON字符串
	 */
	public String getJson()
	{
		return json;
	}
	
	/**
	 * 设置JSON字符串
	 * @param json JSON字符串
	 */
	public void setJson(String json)
	{
		this.json = json;
	}
	/**
	 * 获取消息
	 * @return 消息
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * 设置消息
	 * @param message 消息
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	
	
}
