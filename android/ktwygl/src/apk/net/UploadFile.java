package apk.net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import apk.common.ImageHelper;
import apk.common.StringHelper;
import apk.dal.ConfigDAL;
import apk.model.Size;



public class UploadFile
{
	
	public interface OnUploadFileFinishListener
	{
		public void OnUploadFileFinish(String responseText);
	}
	
	private OnUploadFileFinishListener _onUploadFileFinishListener;
	public void setOnUploadFileFinishListener(OnUploadFileFinishListener onUploadFileFinishListener)
	{
		this._onUploadFileFinishListener = onUploadFileFinishListener;
	}
	
	public void beginFormUpload(final String urlStr, final Map<String, String> textMap, final Map<String, String> fileMap, final Size picMaxSize)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				String responseText = formUpload(urlStr, textMap, fileMap, picMaxSize);
				if(_onUploadFileFinishListener != null)
				{
					_onUploadFileFinishListener.OnUploadFileFinish(responseText);
				}
			}
		});
		thread.start();
	}
	
	
	
	/**
	 * 上传图片
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, Size picMaxSize)
	{
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try
		{
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null)
			{
				StringBuffer strBuf = new StringBuffer();
				Iterator<Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext())
				{
					Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null)
					{
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			
			// file
			if (fileMap != null)
			{
				Iterator<Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext())
				{
					Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null)
					{
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = "application/octet-stream";//= new MimetypesFileTypeMap().getContentType(file);
					if (StringHelper.toLowerCase(filename).endsWith(".png"))
					{
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals(""))
					{
						contentType = "application/octet-stream";
					}
					
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					
					out.write(strBuf.toString().getBytes());
					
					Size picSize;
					
					if(picMaxSize == null || picMaxSize.width <= 0 || picMaxSize.height <= 0)
					{
						picSize = ConfigDAL.getUploadPictureMaxSize();
					}
					else
					{
						picSize = picMaxSize;
					}
					
					
					Bitmap bitmap = ImageHelper.zoomImage(inputValue, picSize.width, picSize.height);
					
					if(StringHelper.toLowerCase(inputValue).endsWith(".png"))
					{
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
					}
					else
					{
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
					}
					bitmap.recycle();
					
//					DataInputStream in = new DataInputStream(new FileInputStream(file));
//					int readedSize = 0;
//					byte[] bufferOut = new byte[1024];
//					while ((readedSize = in.read(bufferOut)) != -1)
//					{
//						out.write(bufferOut, 0, readedSize);
//					}
//					in.close();
				}
			}
			
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();
			
			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		}
		catch (Exception e)
		{
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		}
		finally
		{
			if (conn != null)
			{
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
}
