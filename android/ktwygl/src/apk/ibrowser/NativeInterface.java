package apk.ibrowser;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import apk.bll.CurrUserInfoService;
import apk.common.HybridPageHelper;
import apk.common.JSONHelper;
import apk.common.StringHelper;
import apk.common.Util;
import apk.customerview.DialogSelect;
import apk.customerview.MessageBox;
import apk.customerview.MessageBoxButtons;
import apk.customerview.MessageBoxIcon;
import apk.customerview.MessageBoxResult;
import apk.customerview.WebBrowser;
import apk.customerview.WebBrowserView;
import apk.customerview.DialogSelect.SelectedListener;
import apk.dal.LogHelper;
import apk.model.Protocol;
import apk.model.StringHashMap;

@SuppressLint("DefaultLocale")
public class NativeInterface extends BaseInterface
{
	
	public final static String InstanceName = "nativeInterface";
	
	private WebBrowserView _webBrowserView;
	private Protocol _protocol;
	public NativeInterface(WebBrowserView webBrowserView, WebBrowser webView)
	{
		
		super(webBrowserView.getContext(), webView);		
		this._webBrowserView = webBrowserView;
		_protocol=_webBrowserView.GetWebBrowser().getProtocol();
	}
	
	//@JavascriptInterface  
    public String toString() { return NativeInterface.InstanceName; }  
	
	private String encode(String jsonString)
	{
		if(jsonString == null)
			return "";
		return StringHelper.StringToBase64String(jsonString);
	}
	
	
	
	@JavascriptInterface
	public String jsGet(String id, String defaultValue)
	{
		return "";
	}
	@JavascriptInterface
	public String jsSet(String id, String value)
	{
		return "";
	}
	
	/*
	 * 消息参数
	 */
	class PostMsg
	{
		private String func="";
		public String getFunc()
		{
			return func;
		}
		public void setFunc(String func)
		{
			this.func = func;
		}
		public String getArg()
		{
			return arg;
		}
		public void setArg(String arg)
		{
			this.arg = arg;
		}
		private String arg="";
	}
	
	/*
	 * 消息处理函数
	 */	
	@JavascriptInterface
	public void postMessage(String msg)
	{
		//PostMsg pm=Util.gson.fromJson(msg, PostMsg.class);
		
	    StringHashMap pm=JSONHelper.parseObject(msg, StringHashMap.class);
		
		String func=pm.get("func").toLowerCase();
		String pm_arg=pm.get("arg");
		String arg="";
		switch(func)
		{
			case "functionofchooseortakephoto":
				arg="{callback:'"+pm_arg+"'}";
				_protocol.ChooseOrTakePhoto(arg);
				break;
			case "functionofpictureview":
				arg="{img1:'"+pm_arg+"'}";
				_protocol.PictureView(arg);
				break;
			case "functionofclosewebbrower":				
				_protocol.CloseWebBrower();
				break;
			case "functionofscanbarcode":
				arg="{callback:'"+pm_arg+"'}";
				_protocol.ScanBarcode(arg);
				break;
			case "functionofnavtonewwebpage":
				arg="{url:'"+pm_arg+"'}";
				_protocol.NavToNewWebPage(arg);//打开新窗口
				break;
			case "functionofsetbadge":
				arg=pm_arg;
				_protocol.SetBadge(arg);//打开新窗口
				break;
			//OpenMessageView
			case "functionofopenmessageview":
				arg=pm_arg;
				_protocol.OpenMessageView(arg);//打开新窗口
				break;
			case "functionofsetrightbutton":
				arg=pm_arg;
				_protocol.SetRightButton(arg);//打开新窗口
				break;
			default:
				break;
		}
	}
	
	
//	private String decode(String dataString)
//	{
//		return StringHelper.Base64StringToString(dataString);
//	}
	
//	//调JavaScript
//	public void beginPageTo(String url)
//	{
//		//String callBackFunction = "window." + InstanceName + ".nativeLoaded";//JS回调方法
//		this.callJavaScript("pageTo('" + url + "')");
//	}
	
	public void sendHybridPage()
	{
		this.callJavaScript("var hybridPage = " + HybridPageHelper.getHybridPageString(this._webBrowserView.getContext()));
	}
	
	@JavascriptInterface
	public String getUserInfo()
	{
//		Context context = this._webBrowserView.getContext();
//		String userInfo = HybridPageHelper.getHybridPageString(context);
//		
//		Resources res = context.getResources();
//		String userInfoKey = res.getString(R.string.user_info_key);
//		
//		String userInfoString = null;
//		try
//		{
//			userInfoString = userInfo + MD5.getMD5(userInfo + userInfoKey);
//		}
//		catch (NoSuchAlgorithmException e)
//		{
//			e.printStackTrace();
//		}
//		
//		return userInfoString;
		
		return CurrUserInfoService.getUserInfo(this._webBrowserView.getContext());
	}
	
	
	
	public void ngsjLoad()
	{
		String callBackFunction = "window." + InstanceName + ".ngsjLoadCallback";//JS回调方法
		this.callJavaScript("ngsjload('" + callBackFunction + "')");
	}
	
	public void sendMessage(String message)//回贴
	{
		this.callJavaScript("nativeSendMessage('" + encode(message) + "')");
	}
	
	public void refreshPage()
	{
		this.callJavaScript("refreshPage()");
		//this.callJavaScript("alert('refreshPage')");
	}
	
	public void jsCall(String js)
	{
		this.callJavaScript(js);
	}
	
	//调JavaScript
	public void beginNativeLoaded(String jsonString)
	{
		String callBackFunction = "window." + InstanceName + ".nativeLoaded";//JS回调方法
		this.callJavaScript("nativeLoaded('" + encode(jsonString) + "','" + callBackFunction + "')");
	}
	//JavaScript回调方法
	@JavascriptInterface
	public void nativeLoaded(String jsonBase64String)
	{
		if(jsonBase64String == null || jsonBase64String.length() < 1)
			return;
		
		String jsonString = StringHelper.Base64StringToString(jsonBase64String);
		this.javaScriptCallBack(1, jsonString);
	}
	
	//JavaScript回调方法
	@JavascriptInterface
	public void ngsjLoadCallback(String jsonBase64String)
	{
		if(jsonBase64String == null || jsonBase64String.length() < 1)
			return;
		
		String jsonString = StringHelper.Base64StringToString(jsonBase64String);
		this.javaScriptCallBack(4, jsonString);
	}
	
	@Override
	protected void javaScriptCallBackMainThread(int callBackType, String jsonString)
	{
		switch(callBackType)
		{
			case 1://nativeLoaded
				this.endNativeLoaded(jsonString);
				break;
			case 2://nativeGet
				this.endNativeGet(jsonString);
				break;
			case 3://nativeSet
				this.endNativeSet(jsonString);
				break;
			case 4://ngsjLoadCallback
				this.endNgsjLoadCallback(jsonString);
				break;
		}
	}
	
	//设置HTML页面值
	public void beginNativeSet(String id, String value)
	{
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("id", id);
		hm.put("value", value);
		String jsonString = JSONHelper.toJSON(hm);
		
		String callBackFunction = "window." + InstanceName + ".nativeSet";//JS回调方法
		this.callJavaScript("nativeSet('" + encode(jsonString) + "','" + callBackFunction + "')");
	}
	//JavaScript回调方法
	@JavascriptInterface
	public void nativeSet(String jsonBase64String)
	{
		if(jsonBase64String == null || jsonBase64String.length() < 1)
			return;
		
		String jsonString = StringHelper.Base64StringToString(jsonBase64String);
		this.javaScriptCallBack(3, jsonString);
	}
	
	
	//获取HTML页面值
	public void beginNativeGet(String id, String defaultValue)
	{
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("id", id);
		hm.put("value", defaultValue);
		String jsonString = JSONHelper.toJSON(hm);
		
		String callBackFunction = "window." + InstanceName + ".nativeGet";//JS回调方法
		this.callJavaScript("nativeGet('" + encode(jsonString) + "','" + callBackFunction + "')");
	}
	//JavaScript回调方法
	@JavascriptInterface
	public void nativeGet(String jsonBase64String)
	{
		if(jsonBase64String == null || jsonBase64String.length() < 1)
			return;
		
		String jsonString = StringHelper.Base64StringToString(jsonBase64String);
		this.javaScriptCallBack(2, jsonString);
	}
	//native调用JS的nativeLoaded方法后，JS回调此方法
	@JavascriptInterface
	public void endNativeLoaded(String jsonString)
	{
		this._webBrowserView.onDataFromWebView(jsonString);
	}
	
	//native调用JS的nativeGet方法后，JS回调此方法
	@JavascriptInterface
	public void endNativeGet(String jsonString)
	{
		LogHelper.d("json", jsonString);
	}
	//native调用JS的nativeSet方法后，JS回调此方法
	@JavascriptInterface
	public void endNativeSet(String jsonString)
	{
		LogHelper.d("json", jsonString);
	}
	
	@JavascriptInterface
	public void endNgsjLoadCallback(String jsonString)
	{
		this._webBrowserView.setOkButton(jsonString);
	}
	
	//js调用的协议
	@JavascriptInterface
	public void jsPageTo(String url)
	{
		this._webView.getProtocol().getProtocol(url);
	}
	
	/**
	 * js调用，上一页
	 */
	public void jsNativeBack()
	{
		this._webBrowserView.jsNativeBack();
	}
	
	//js调用的消息框，模态
	public int jsMessageBoxModel(String title, String message, int button, int icon)
	{
		MessageBoxButtons buttonType;
		MessageBoxIcon iconType;
		
		switch(button)
		{
			case 1:
			default:
				buttonType = MessageBoxButtons.OK;
				break;
			case 2:
				buttonType = MessageBoxButtons.OKCancel;
				break;
			case 3:
				buttonType = MessageBoxButtons.YesNo;
				break;
			case 4:
				buttonType = MessageBoxButtons.YesNoCancel;
				break;
		}
		
		switch(icon)
		{
			case 1:
				iconType = MessageBoxIcon.Information;
				break;
			case 2:
				iconType = MessageBoxIcon.Question;
				break;
			case 3:
				iconType = MessageBoxIcon.Error;
				break;
			case 4:
				iconType = MessageBoxIcon.Warning;
				break;
			default:
				iconType = MessageBoxIcon.None;
				break;
		}
		
		MessageBoxResult res = MessageBox.Show(this._webBrowserView.getContext(), title, message, buttonType, iconType);
		
		switch(res)
		{
			case OK:
				return 1;
			case Yes:
				return 2;
			case No:
				return 3;
			case Cancel:
				return 4;
			default:
				return -1;
		}
	}
	//js调用的消息框，Toast
	@JavascriptInterface
	public void jsMessageBoxToast(String message, int time)
	{
		if(time == 1)
		{
			Toast.makeText(this._webBrowserView.getContext(), message, Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(this._webBrowserView.getContext(), message, Toast.LENGTH_SHORT).show();
		}
	}
	
	private ProgressDialog _progressDialog;
	//js调用开始等待
	@JavascriptInterface
	public void jsBeginWaite(String message)
	{
		this._progressDialog = new ProgressDialog(this._webBrowserView.getContext());
		this._progressDialog.setMax(100);
		this._progressDialog.setMessage(message);
		this._progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this._progressDialog.setCancelable(false);
		this._progressDialog.show();
	}
	//js调用结束等待
	@JavascriptInterface
	public void jsEndWaite()
	{
		this._progressDialog.dismiss();
	}
}
