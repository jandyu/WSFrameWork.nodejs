package apk.model.framework;

import java.io.InputStream;

/**
 * @author dongjd
 *
 */
public class StreamData
{
	/**
	 * 处理结果枚举
	 * @author dongjd
	 *
	 */
	public enum StreamDataState
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
	private StreamDataState state;
	private InputStream stream;
	private int code;
	private String message;
	
	/**
	 * 
	 */
	public StreamData()
	{}
	
	
	/**
	 * 设置处理状态
	 * @param state 处理状态
	 */
	public void setState(StreamDataState state)
	{
		this.state = state;
	}
	/**
	 * 获取处理状态
	 * @return 处理状态
	 */
	public StreamDataState getState()
	{
		return this.state;
	}
	
	/**
	 * 设置结果流
	 * @param stream 结果流
	 */
	public void setStream(InputStream stream)
	{
		this.stream = stream;
	}
	
	/**
	 * 获取结果流
	 * @return 结果流
	 */
	public InputStream getStream()
	{
		return this.stream;
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
