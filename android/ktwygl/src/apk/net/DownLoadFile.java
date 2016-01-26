package apk.net;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import apk.common.Util;

public class DownLoadFile
{
	
	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getImage(String path) throws Exception
	{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			return readStream(inStream);
		}
		return null;
	}
	
	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream getImageStream(String path) throws Exception
	{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(20 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			return conn.getInputStream();
		}
		return null;
	}
	
	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}
	
	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String fileName) throws IOException
	{
//		File dirFile = new File(fileName);
//		if (!dirFile.exists())
//		{
//			dirFile.mkdir();
//		}
		FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
		BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
		bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
		bos.flush();
		bos.close();
	}
	
	
	public static boolean downLoadFile(final String url, final String saveFileName)
	{
		Util.createIfNotExistsPath(saveFileName);
		
		
		try
		{
			File file = new File(saveFileName);
			InputStream inputStream = DownLoadFile.getImageStream(url);
			
			OutputStream outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024 * 10];
			int readedSize;
			while((readedSize = inputStream.read(buffer)) > 0)
			{
				outputStream.write(buffer, 0, readedSize);
			}
			outputStream.flush();
			
			outputStream.close();
			
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public interface OnDownLoadFileFinishListener
	{
		public void OnDownLoadFileFinish(boolean isSuccess);
	}
	
	private OnDownLoadFileFinishListener _onDownLoadFileFinishListener;
	public void setOnDownLoadFileFinishListener(OnDownLoadFileFinishListener onDownLoadFileFinishListener)
	{
		this._onDownLoadFileFinishListener = onDownLoadFileFinishListener;
	}
	
	public void beginDownLoadFile(final Context context, final String url, final String saveFileName)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				final boolean isSuccess = downLoadFile(url, saveFileName);
				
				Handler uiHanlder = new Handler(context.getMainLooper())
				{
					@Override
					public void handleMessage(Message msg)
					{
						super.handleMessage(msg);
						
						if(_onDownLoadFileFinishListener != null)
						{
							_onDownLoadFileFinishListener.OnDownLoadFileFinish(isSuccess);
						}
					}
				};
				uiHanlder.sendMessage(new Message());
			}
		});
		thread.start();
	}
	
	
//	public static void beginDownLoadFile(final String url, final String saveFileName)
//	{
//		DownLoadFile downLoadFile = new DownLoadFile();
//		try
//		{
//			Bitmap mBitmap = BitmapFactory.decodeStream(downLoadFile.getImageStream(url));
//			if(mBitmap != null)
//			{
//				downLoadFile.saveFile(mBitmap, saveFileName);
//				mBitmap.recycle();
//			}
//			else
//			{
//				Log.d("error", "下载文件" + url + "出错");
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
//	private Runnable saveFileRunnable = new Runnable()
//	{
//		@Override
//		public void run()
//		{
//			try
//			{
//				saveFile(mBitmap, mFileName);
//				mSaveMessage = "图片保存成功！";
//			}
//			catch (IOException e)
//			{
//				mSaveMessage = "图片保存失败！";
//				e.printStackTrace();
//			}
//			messageHandler.sendMessage(messageHandler.obtainMessage());
//		}
//	};
//	
//	private Handler messageHandler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			mSaveDialog.dismiss();
//			Log.d(TAG, mSaveMessage);
//			// Toast.makeText(IcsTestActivity.this, mSaveMessage,
//			// Toast.LENGTH_SHORT).show();
//		}
//	};
//	
//	/*
//	 * 连接网络 由于在4.0中不允许在主线程中访问网络，所以需要在子线程中访问
//	 */
//	private Runnable connectNet = new Runnable()
//	{
//		@Override
//		public void run()
//		{
//			try
//			{
//				String filePath = "http://img.my.csdn.net/uploads/201402/24/1393242467_3999.jpg";
//				mFileName = "test.jpg";
//				
//				// 以下是取得图片的两种方法
//				// ////////////// 方法1：取得的是byte数组, 从byte数组生成bitmap
//				byte[] data = getImage(filePath);
//				if (data != null)
//				{
//					mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
//				}
//				else
//				{
//					// Toast.makeText(IcsTestActivity.this, "Image error!",
//					// 1).show();
//				}
//				// //////////////////////////////////////////////////////
//				
//				// ******** 方法2：取得的是InputStream，直接从InputStream生成bitmap
//				// ***********/
//				mBitmap = BitmapFactory.decodeStream(getImageStream(filePath));
//				// ********************************************************************/
//				
//				// 发送消息，通知handler在主线程中更新UI
//				connectHanlder.sendEmptyMessage(0);
//				Log.d(TAG, "set image ...");
//			}
//			catch (Exception e)
//			{
//				// Toast.makeText(IcsTestActivity.this, "无法链接网络！", 1).show();
//				e.printStackTrace();
//			}
//		}
//	};
//	
//	private Handler connectHanlder = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			Log.d(TAG, "display image");
//			// 更新UI，显示图片
//			if (mBitmap != null)
//			{
//				// mImageView.setImageBitmap(mBitmap);// display image
//			}
//		}
//	};
}
