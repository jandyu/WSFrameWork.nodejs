package apk.ibrowser;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import apk.customerview.WebBrowser;

public class BaseInterface
{
	protected Context _context;
	protected WebBrowser _webView;
	private Handler _uiHanlder;
	
	public BaseInterface(Context context, WebBrowser webView)
	{
		this._context = context;
		this._webView = webView;
		
		this._uiHanlder = new Handler(context.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				
				Bundle bundle = msg.getData();
				switch(bundle.getInt("type"))
				{
					case 1://callJavaScript
						String javaScript = bundle.getString("javaScript");
						_webView.loadUrl("javascript:" + javaScript + "");
						break;
					case 2://JavaScriptCallBack
						String jsonString = bundle.getString("jsonString");
						javaScriptCallBackMainThread(bundle.getInt("callBackType"), jsonString);
						break;
				}
			}
		};
	}
	
	public void callJavaScript(String javaScript)
	{
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putInt("type", 1);//callJavaScript
		bundle.putString("javaScript", javaScript);
		msg.setData(bundle);
		this._uiHanlder.sendMessage(msg);
		
//		_webView.loadUrl("javascript:" + javaScript + "");
	}
	
	

	
	
	protected void javaScriptCallBack(int callBackType, String jsonString)
	{
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putInt("type", 2);//JavaScriptCallBack
		bundle.putInt("callBackType", callBackType);
		bundle.putString("jsonString", jsonString);
		msg.setData(bundle);
		this._uiHanlder.sendMessage(msg);
		
//		javaScriptCallBackMainThread(callBackType, jsonString);
	}
	
	//回调主线程
	protected void javaScriptCallBackMainThread(int callBackType, String jsonString)
	{}
}
