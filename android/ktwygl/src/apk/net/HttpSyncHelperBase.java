package apk.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.http.conn.ConnectTimeoutException;

import apk.common.AppLog;
import apk.dal.ConfigDAL;
import apk.model.framework.StreamData;
import apk.model.framework.StreamData.StreamDataState;


/**
 * @author dongjd
 *
 */
public class HttpSyncHelperBase
{

	/**
	 * 普通POST请求
	 * @param method Method
	 * @param contentType ContentType
	 * @param url 请求地址
	 * @param streamData 流
	 * @return 结果
	 */
	protected StreamData ask(String method, String url, InputStream stream)
	{		
		try
		{
			URL uri = new URL(url);
			HttpURLConnection httpConn = (HttpURLConnection)uri.openConnection();
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod(method);
			
			httpConn.setConnectTimeout(ConfigDAL.getTimeOutHttpNormalPost());
			httpConn.setReadTimeout(ConfigDAL.getTimeOutHttpNormalPost());
			
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			
			if("POST".equals(method))
			{
				//httpConn.setRequestProperty("Content-Type", "application/octet-stream");
				httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				
				//httpConn.setRequestProperty("compress", "1");//经过压缩
				
				httpConn.setRequestProperty("Charset", "UTF-8");
				//httpConn.setRequestProperty("Content-length", String.valueOf(stream.available()));
				
				//httpConn.connect();
				OutputStream outputStream = null;
				//GZIPOutputStream gzipOutputStream = null;
				try
				{
					outputStream = httpConn.getOutputStream();
					//gzipOutputStream = new GZIPOutputStream(outputStream);
					
					byte[] buffer = new byte[1024];
					int readedSize;
					while((readedSize = stream.read(buffer, 0, buffer.length)) > 0)
					{
						//gzipOutputStream.write(buffer, 0, readedSize);
						outputStream.write(buffer, 0, readedSize);
					}
					//gzipOutputStream.flush();
					outputStream.flush();
				}
				catch(Exception ex)
				{
					throw ex;
				}
				finally
				{
//					if(gzipOutputStream != null)
//					{
//						try
//						{
//							gzipOutputStream.close();
//						}
//						catch (IOException e)
//						{
//							AppLog.LogError(null, null, null, "关闭gzipOutputStream流时", e);
//						}
//					}
					if(outputStream != null)
					{
						try
						{
							outputStream.close();
						}
						catch (IOException e)
						{
							AppLog.LogError(null, null, null, "关闭outputStream流时", e);
						}
					}
				}
			}
			
			StreamData streamData = new StreamData();
			if(httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
//				String headerAccess = httpConn.getHeaderField("access");
//				String headerCode = httpConn.getHeaderField("code");
//				String headerMessage = httpConn.getHeaderField("message");
//				
//				if("1".equals(headerAccess))
//					streamData.setState(StreamDataState.OK);
//				else
//					streamData.setState(StreamDataState.ServerError);
//				
//				streamData.setCode(Integer.parseInt(headerCode, 10));
//				streamData.setMessage(StringHelper.Base64StringToString(headerMessage));
				
				
				streamData.setState(StreamDataState.OK);
				streamData.setStream(httpConn.getInputStream());
			}
			else
			{
				streamData.setState(StreamDataState.ServerError);
				streamData.setCode(0);
				streamData.setMessage("服务器处理失败，请与管理员联系！");
				streamData.setStream(httpConn.getErrorStream());
			}
			
			return streamData;
		}
		catch (ConnectTimeoutException e)
		{
			e.printStackTrace();
			StreamData streamData = new StreamData();
			streamData.setState(StreamDataState.NetworkError);//网络不可用
			streamData.setMessage(e.getMessage());
			return streamData;
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
			StreamData streamData = new StreamData();
			streamData.setState(StreamDataState.NetworkError);//网络不可用
			streamData.setMessage(e.getMessage());
			return streamData;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			StreamData streamData = new StreamData();
			streamData.setState(StreamDataState.ClientError);
			streamData.setMessage(e.getMessage());
			return streamData;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			StreamData streamData = new StreamData();
			streamData.setState(StreamDataState.ClientError);
			streamData.setMessage(e.getMessage());
			return streamData;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			StreamData streamData = new StreamData();
			streamData.setState(StreamDataState.ClientError);
			streamData.setMessage(e.getMessage());
			return streamData;
		}
	}

}
