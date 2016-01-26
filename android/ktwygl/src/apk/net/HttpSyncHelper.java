package apk.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.entity.StringEntity;

import apk.common.JSONHelper;
import apk.dal.LogHelper;
import apk.model.framework.JsonData;
import apk.model.framework.StreamData;
import apk.model.framework.JsonData.JsonDataState;
import apk.model.framework.StreamData.StreamDataState;

/**
 * <p>类名:HttpHelper</p>
 * @author DongJiande
 * @version 1.0 2013年9月6日
 */
public class HttpSyncHelper extends HttpSyncHelperBase
{
	
	/**
	 * 普通POST请求
	 * @param url 请求地址
	 * @param pars 参数
	 * @return 结果
	 */
	public JsonData Post(String url, Object pars)
	{
		
		String postContent = JSONHelper.toJSON(pars); //JsonUtil.HashMapToJson(parsMap).toString();
		
		LogHelper.d("postData", postContent);

		try
		{
			StringEntity stringEntity = new StringEntity(postContent);
			StreamData streamData = this.ask("POST", url, stringEntity.getContent());
			
			if(streamData.getState() == StreamDataState.OK)
			{
				JsonData jsonData = new JsonData();
				jsonData.setCode(streamData.getCode());
				jsonData.setMessage(streamData.getMessage());
				jsonData.setState(JsonDataState.OK);
				jsonData.setJson(this.readStreamDataStream(streamData));
				return jsonData;
			}
			else
			{
				JsonData jsonData = new JsonData();
				jsonData.setState(JsonDataState.ServerError);
				jsonData.setMessage(streamData.getMessage());
				return jsonData;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JsonData jsonData = new JsonData();
			jsonData.setState(JsonDataState.ClientError);
			jsonData.setMessage(e.getMessage());
			return jsonData;
		}
	}
	
//	/**
//	 * 普通POST请求
//	 * @param url 请求地址
//	 * @param parsMap 参数
//	 * @return 结果
//	 */
//	public JsonData Post(String url, Map<String, Object> parsMap)
//	{
//		
//		String postContent = JsonUtil.HashMapToJson(parsMap).toString();
//
//		try
//		{
//			StringEntity stringEntity = new StringEntity(postContent);
//			StreamData streamData = this.ask("POST", url, stringEntity.getContent());
//			
//			if(streamData.getState() == StreamDataState.OK)
//			{
//				JsonData jsonData = new JsonData();
//				jsonData.setCode(streamData.getCode());
//				jsonData.setMessage(streamData.getMessage());
//				jsonData.setState(JsonDataState.OK);
//				jsonData.setJson(this.readStreamDataStream(streamData));
//				return jsonData;
//			}
//			else
//			{
//				JsonData jsonData = new JsonData();
//				jsonData.setState(JsonDataState.ServerError);
//				jsonData.setMessage(streamData.getMessage());
//				return jsonData;
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			JsonData jsonData = new JsonData();
//			jsonData.setState(JsonDataState.ClientError);
//			jsonData.setMessage(e.getMessage());
//			return jsonData;
//		}
//	}
	
	
	/**
	 * 普通POST请求
	 * @param url 请求地址
	 * @param postContent POST内容
	 * @return 结果
	 */
	public JsonData Post(String url, String postContent)
	{
		try
		{
			StringEntity stringEntity = new StringEntity(postContent);
			
			StreamData streamData = this.ask("POST", url, stringEntity.getContent());
			
			if(streamData.getState() == StreamDataState.OK)
			{
				JsonData jsonData = new JsonData();
				jsonData.setCode(streamData.getCode());
				jsonData.setMessage(streamData.getMessage());
				jsonData.setState(JsonDataState.OK);
				jsonData.setJson(this.readStreamDataStream(streamData));
				return jsonData;
			}
			else
			{
				JsonData jsonData = new JsonData();
				jsonData.setState(JsonDataState.ServerError);
				jsonData.setMessage(streamData.getMessage());
				return jsonData;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JsonData jsonData = new JsonData();
			jsonData.setState(JsonDataState.ClientError);
			jsonData.setMessage(e.getMessage());
			return jsonData;
		}
	}
	
	
	
	
	
	
	
	/**
	 * Post获取流
	 * @param url 请求地址
	 * @param pars 参数
	 * @return 结果
	 */
	public StreamData PostAndGetStream(String url, Object pars)
	{
		String postContent = JSONHelper.toJSON(pars);// JsonUtil.HashMapToJson(parsMap).toString();

		try
		{
			StringEntity stringEntity = new StringEntity(postContent);
			return this.ask("POST", url, stringEntity.getContent());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			StreamData streamData = new StreamData();
			streamData.setState(StreamDataState.ClientError);
			streamData.setMessage(e.getMessage());
			return streamData;
		}
	}
	
	private String readStreamDataStream(StreamData streamData)
	{
		InputStream stream = streamData.getStream();
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader bReader = new BufferedReader(reader);
		
		try
		{
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bReader.readLine()) != null)
			{
				sb.append(line);
			}
			
			return sb.toString();
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			
			if(bReader != null)
			{
				try
				{
					bReader.close();
				}
				catch (IOException e)
				{
					//MCMLog.LogError(null, null, null, "关闭bReader流时", e);
				}
			}
			
			
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					//MCMLog.LogError(null, null, null, "关闭reader流时", e);
				}
			}
			
			
			if(stream != null)
			{
				try
				{
					stream.close();
				}
				catch (IOException e)
				{
					//MCMLog.LogError(null, null, null, "关闭stream流时", e);
				}
			}
			
		}
	}
	
	
	
	
	
	
	
	
}
