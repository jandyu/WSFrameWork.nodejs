package apk.model;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import apk.activity.AboutActivity;
import apk.activity.InformationCollectActivity;
import apk.activity.MessageListActivity;
import apk.activity.PictureShowActivity;
import apk.activity.UserEditActivity;
import apk.activity.WebBrowserActivity;
import apk.barcode.CaptureActivity;
import apk.common.JSONHelper;
import apk.common.StringHelper;
import apk.common.Util;
import apk.customerview.DialogInputBox;
import apk.customerview.DialogSelect;
import apk.customerview.MessageBox;
import apk.customerview.MessageBoxButtons;
import apk.customerview.MessageBoxIcon;
import apk.customerview.MessageBoxResult;
import apk.customerview.WebBrowser;
import apk.customerview.WebBrowserView;
import apk.customerview.DialogInputBox.OnDialogInputBoxOkListener;
import apk.customerview.DialogSelect.SelectedListener;
import apk.dal.ConfigDAL;
import apk.dal.LogHelper;
import apk.dal.MessageDAL;
import apk.i.IActivity;

public class Protocol
{
	public static final int Protocol_Result_Photo_Load_Image = 501;
	public static final int Protocol_Result_Photo_Take_Photo = 502;
	public static final int Protocol_Result_Scan = 503;
	public static final String TakePhotoFileName = "apk.platform.youjish.com_temp.jpg";
	
	private IActivity _activity;
	private WebBrowser _webView;
	
	public void setCurrentWebView(WebBrowser webView)
	{
		this._webView = webView;
	}
	
	/**
	 * 设置用于接收onActivityResult回调的窗体
	 * @param fragment fragment
	 */
	public void setActivity(IActivity activity)
	{
		this._activity = activity;
	}
	
	private String _returnObjectId;
	public String getReturnObjectId()
	{
		return this._returnObjectId;
	}

	private StringHashMap _paramHashMap;
	public StringHashMap getParamHashMap()
	{
		return this._paramHashMap;
	}
	
	/*
	 * 选择图片
	 */
	public void ChooseOrTakePhoto(String arg)
	{
		this._paramHashMap=new StringHashMap();
		this._paramHashMap=JSONHelper.parseObject(arg, StringHashMap.class);
		if(this._paramHashMap==null)
		{
			this._paramHashMap=new StringHashMap();
		}
		this._paramHashMap.put("size", "200");
		DialogSelect _dialogPhotoGetSelect = new DialogSelect(this._activity.getContext(), new int[] { 0, 1 }, new String[] { "拍照", "从相册选择" });
		_dialogPhotoGetSelect.setSelectedListener(new SelectedListener()
		{
			@Override
			public void onSelected(int value)
			{
				Intent intent;
				switch (value)
				{
					case 0:
						intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), TakePhotoFileName)));
						_activity.startActivityForResult(intent, Protocol.Protocol_Result_Photo_Take_Photo);
						break;
					case 1:
						intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						_activity.startActivityForResult(intent, Protocol.Protocol_Result_Photo_Load_Image);
						break;
				}
			}
		});
		_dialogPhotoGetSelect.showAtLocation(this._webView.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	/*
	 * 图片浏览
	 */
	public void PictureView(String arg)
	{
		this._paramHashMap=new StringHashMap();
		this._paramHashMap=JSONHelper.parseObject(arg, StringHashMap.class);
		String img1 = ConfigDAL.getServerMainUrl() + this._paramHashMap.get("img1");
		Intent intent = new Intent(this._activity.getContext(), PictureShowActivity.class);
		intent.putExtra("url", img1);
		this._activity.getContext().startActivity(intent);
	}
	
	/*
	 * 关闭
	 */
	public void CloseWebBrower()
	{
		this._activity.finish();
	}
	
	/*
	 * 扫描条码
	 */
	public void ScanBarcode(String arg)
	{
		this._paramHashMap=new StringHashMap();
		this._paramHashMap=JSONHelper.parseObject(arg, StringHashMap.class);
		/*
		if(this._paramHashMap != null)
		{
			this._returnObjectId = this._paramHashMap.get("rid");
		}*/
		
		Class<?> newFormClass;
		newFormClass = CaptureActivity.class;
				
		Intent intent = new Intent(this._activity.getContext(), newFormClass);
		this._activity.startActivityForResult(intent, Protocol_Result_Scan);
		
	}
	
	
	/*
	 * 打开新窗口
	 */
	public void NavToNewWebPage(String arg)
	{
		this._paramHashMap=new StringHashMap();
		this._paramHashMap=JSONHelper.parseObject(arg, StringHashMap.class);
		String paramURL = this._paramHashMap.get("url");
		String newForm="";//如果等于self就是在自身打开
		String newURL="";		
		
		if(paramURL.toLowerCase().startsWith("http")==true)
		{		
			newURL=paramURL;
		}
		else
		{
			newURL=ConfigDAL.getServerMainUrl()+"/"+paramURL;
		}
			
		if("self".equalsIgnoreCase(newForm) && this._webView != null)//非新窗口
		{
			this._webView.loadUrl(newURL);
		}
		else//新窗口打开
		{
			Class<?> newFormClass;
			if("web1".equalsIgnoreCase(newForm))
			{
				newFormClass = WebBrowserActivity.class;
			}
			else
			{
				newFormClass = WebBrowserActivity.class;
			}
			Intent intent = new Intent(this._activity.getContext(), newFormClass);
			intent.putExtra("url", newURL);
			this._activity.getContext().startActivity(intent);
		}
	}
	
	/*
	 * 设置消息数量
	 */
	public void SetBadge(String arg)
	{
		this._paramHashMap=new StringHashMap();
		this._paramHashMap=JSONHelper.parseObject(arg, StringHashMap.class);
		try
		{
			String btnname = this._paramHashMap.get("btnname");
			String number = this._paramHashMap.get("number");
			int tabIndex = -1;
			int numValue = 0;
			
			if(!StringHelper.IsNullOrEmpty(btnname) && !StringHelper.IsNullOrEmpty(number))
			{
				btnname = StringHelper.toLowerCase(btnname);
				numValue = Integer.parseInt(number, 10);
				
				if(btnname.startsWith("tabbar"))//tabbar
				{
					tabIndex = Integer.parseInt(btnname.replace("tabbar", "") , 10);
					if(this._activity.getMainActivity() != null)
					{
						//this._activity.getMainActivity().setTabNumValue(tabIndex - 1, numValue);
						String msg="{func:'setbadge',tabIndex:'"+(tabIndex - 1)+"',num:'"+numValue+"'}";
						this._activity.getMainActivity().UpdUI(msg);
					}
				}
				else if("rightbtn".equalsIgnoreCase(btnname))//标题栏右边菜单
				{
					if(this._activity != null)
					{
						//this._activity.setNumText(numValue);
						String msg="{func:'setbadge',num:'"+numValue+"'}";
						this._activity.UpdUI(msg);
					}
				}
			}
		}
		catch(Exception e)
		{
			LogHelper.e("SetBadge", e.getMessage());
		}
	}
	
	/*
	 * 打开消息列表窗口
	 */
	public void OpenMessageView(String arg)
	{
		Class<?> newFormClass;		
		newFormClass = MessageListActivity.class;		
		Intent intent = new Intent(this._activity.getContext(), newFormClass);
		this._activity.getContext().startActivity(intent);
	}
	
	/*
	 * 设置RightButton
	 */
	public void SetRightButton(String arg)
	{
		this._paramHashMap=new StringHashMap();
		this._paramHashMap=JSONHelper.parseObject(arg, StringHashMap.class);
		try
		{
						
			String jstr= "{\"rightbtn\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\",\"righttitle\":\"菜 单\"," +
	"menu:[" +
	"	{\"title\":\"打开百度\",\"protocol\":\"hybridPage.Native.NavToNewWebPage(\"v1/d2.aspx\")\"}"+ 
	"	]" +
"}";
			String msg="{func:'SetRightButton',jstr:\'"+jstr+"\'}";
			this._activity.UpdUI(msg);			
		}
		catch(Exception e)
		{
			LogHelper.e("SetRightButton", e.getMessage());
		}
	}
	
	public boolean getProtocol(String url)
	{
		String urlProtocol = ConfigDAL.getUrlProtocol();//协议
		String newForm;//是否在新窗口中打开
		if(StringHelper.toLowerCase(url).startsWith(urlProtocol))//如果是ngsj://打头的
		{
			url = url.substring(urlProtocol.length());
			String operate = url.substring(0, url.indexOf(":"));
			url = url.substring(operate.length() + 1);
			newForm = url.substring(0, url.indexOf(":"));
			url = url.substring(newForm.length() + 1);
			
			String base64JsonParam = url;
			this._paramHashMap = null;
			
			if(base64JsonParam != null && base64JsonParam.length() > 0)
			{
				try
				{
					String jsonParam = StringHelper.Base64StringToString(base64JsonParam);
					if(jsonParam != null && jsonParam.length() > 0)
					{
						StringHashMap paramHashMap = JSONHelper.parseObject(jsonParam, StringHashMap.class);
						
						if(paramHashMap == null)//如果参数转对象失败（非法参数放弃处理），这个值就是null。
						{
							return true;
						}
						
						this._paramHashMap = paramHashMap;
					}
				}
				catch(Exception e)
				{
					this._paramHashMap = null;
					return true;
				}
			}
			
			
			if("url".equalsIgnoreCase(operate))//url
			{
				String paramURL = this._paramHashMap.get("url");
				
				String newURL;
				
				if(this._webView != null)
					newURL = Util.getNewURL(this._webView.getUrl(), paramURL);
				else
					newURL = paramURL;
				 
				if("self".equalsIgnoreCase(newForm) && this._webView != null)//非新窗口
				{
					this._webView.loadUrl(newURL);
				}
				else//新窗口打开
				{
					Class<?> newFormClass;
					if("web1".equalsIgnoreCase(newForm))
					{
						newFormClass = WebBrowserActivity.class;
					}
					else
					{
						newFormClass = WebBrowserActivity.class;
					}
					Intent intent = new Intent(this._activity.getContext(), newFormClass);
					intent.putExtra("url", newURL);
					this._activity.getContext().startActivity(intent);
				}
			}
			else if("exec".equalsIgnoreCase(operate))
			{
				if("execscript".equalsIgnoreCase(newForm))
				{
					String callBack = this._paramHashMap.get("callback");
					this._webView.getNativeInterface().jsCall(callBack);
				}
				else if("inputstring".equalsIgnoreCase(newForm))
				{
					String title = this._paramHashMap.get("title");
					final String callBack = this._paramHashMap.get("callback");
					
					DialogInputBox msgBox = new DialogInputBox(this._activity.getContext(), title);
					msgBox.setOnDialogInputBoxOkListener(new OnDialogInputBoxOkListener()
					{
						@Override
						public void OnOk(String value)
						{
							_webView.getNativeInterface().jsCall(callBack.replace("#_val_#", value));
						}
					});
					msgBox.show();
				}
				else if("closewebbrower".equalsIgnoreCase(newForm))
				{
//					if(this._activity.getContext() != null)
//					{
//						Activity act = (Activity) this._context;
//						act.finish();
//					}
					this._activity.finish();
				}
				else if("pictureview".equalsIgnoreCase(newForm))//打开一个全屏的图片查看，显示img1图片，可以缩放，移动，单击退出
				{
					String img1 = ConfigDAL.getServerMainUrl() + this._paramHashMap.get("img1");
					Intent intent = new Intent(this._activity.getContext(), PictureShowActivity.class);
					intent.putExtra("url", img1);
					this._activity.getContext().startActivity(intent);
				}
				else if("nativealert".equalsIgnoreCase(newForm))
				{
					String title = this._paramHashMap.get("title");
					String callBack = this._paramHashMap.get("callback");
					
					if(MessageBox.Show(this._activity.getContext(), title, "", MessageBoxButtons.OKCancel, MessageBoxIcon.Question) == MessageBoxResult.OK)
					{
						this._webView.getNativeInterface().jsCall(callBack);
					}
				}
				else if("emptymessage".equalsIgnoreCase(newForm))//初始化消息，即删除所有消息
				{
					MessageDAL.empty();
				}
				else if("badge".equalsIgnoreCase(newForm))//设置消息数量
				{
					try
					{
						String btnname = this._paramHashMap.get("btnname");
						String number = this._paramHashMap.get("number");
						int tabIndex = -1;
						int numValue = 0;
						
						if(!StringHelper.IsNullOrEmpty(btnname) && !StringHelper.IsNullOrEmpty(number))
						{
							btnname = StringHelper.toLowerCase(btnname);
							numValue = Integer.parseInt(number, 10);
							
							if(btnname.startsWith("tabbar"))//tabbar
							{
								tabIndex = Integer.parseInt(btnname.replace("tabbar", "") , 10);
								if(this._activity.getMainActivity() != null)
								{
									this._activity.getMainActivity().setTabNumValue(tabIndex - 1, numValue);
								}
							}
							else if("rightbtn".equalsIgnoreCase(btnname))//标题栏右边菜单
							{
								if(this._activity != null)
								{
									this._activity.setNumText(numValue);
								}
							}
						}
					}
					catch(Exception e)
					{
						LogHelper.e("ngsj://exec:badge:", e.getMessage());
					}
				}
			}
//			else if("login".equalsIgnoreCase(operate))//登录
//			{
//				if("self".equalsIgnoreCase(newForm))//非新窗口
//				{
//					//view.loadUrl(newURL);
//				}
//				else
//				{
//					Class<?> newFormClass;
//					if("login1".equalsIgnoreCase(newForm))
//					{
//						newFormClass = LoginActivity.class;
//					}
//					else
//					{
//						newFormClass = LoginActivity.class;
//					}
//					Intent intent = new Intent(this._context, newFormClass);
//					this._context.startActivity(intent);
//				}
//			}
			else if("userInformation".equalsIgnoreCase(operate))//用户信息
			{
				if("self".equalsIgnoreCase(newForm))//非新窗口
				{
					
				}
				else
				{
					Class<?> newFormClass;
					if("userInformation1".equalsIgnoreCase(newForm))
					{
						newFormClass = UserEditActivity.class;
					}
					else
					{
						newFormClass = UserEditActivity.class;
					}
					Intent intent = new Intent(this._activity.getContext(), newFormClass);
					this._activity.getContext().startActivity(intent);
				}
			}
			else if("about".equalsIgnoreCase(operate))//关于
			{
				if("self".equalsIgnoreCase(newForm))//非新窗口
				{
					
				}
				else
				{
					Class<?> newFormClass;
					if("about1".equalsIgnoreCase(newForm))
					{
						newFormClass = AboutActivity.class;
					}
					else
					{
						newFormClass = AboutActivity.class;
					}
					Intent intent = new Intent(this._activity.getContext(), newFormClass);
					this._activity.getContext().startActivity(intent);
				}
			}
			else if("messageShow".equalsIgnoreCase(operate))//消息浏览
			{
				if("self".equalsIgnoreCase(newForm))//非新窗口
				{
					
				}
				else
				{
					Class<?> newFormClass;
					if("messageShow1".equalsIgnoreCase(newForm))
					{
						//newFormClass = MessageTypeListActivity.class;
						newFormClass = MessageListActivity.class;
					}
					else
					{
//						newFormClass = MessageTypeListActivity.class;
						newFormClass = MessageListActivity.class;
					}
					Intent intent = new Intent(this._activity.getContext(), newFormClass);
					this._activity.getContext().startActivity(intent);
				}
			}
			else if("informationCollect".equalsIgnoreCase(operate))//信息采集
			{
				if("self".equalsIgnoreCase(newForm))//非新窗口
				{
					
				}
				else
				{
					if("informationCollect1".equalsIgnoreCase(newForm))
					{
						DialogSelect _dialogPhotoGetSelect = new DialogSelect(this._activity.getContext(), new int[] { 0, 1 }, new String[] { "拍照", "从相册选择" });
						_dialogPhotoGetSelect.setSelectedListener(new SelectedListener()
						{
							@Override
							public void onSelected(int value)
							{
								Intent intent;
								switch (value)
								{
									case 0:
										intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
										intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), TakePhotoFileName)));
										_activity.startActivityForResult(intent, Protocol.Protocol_Result_Photo_Take_Photo);
										break;
									case 1:
										intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
										_activity.startActivityForResult(intent, Protocol.Protocol_Result_Photo_Load_Image);
										break;
								}
							}
						});
						_dialogPhotoGetSelect.showAtLocation(this._webView.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
					}
					else
					{
						Class<?> newFormClass = InformationCollectActivity.class;
						Intent intent = new Intent(this._activity.getContext(), newFormClass);
						this._activity.getContext().startActivity(intent);
					}
				}
			}
			else if("message".equalsIgnoreCase(operate))//发消息
			{
				if("self".equalsIgnoreCase(newForm))//非新窗口
				{
					String title = this._paramHashMap.get("title");
					
					DialogInputBox msgBox = new DialogInputBox(this._activity.getContext(), title);
					msgBox.show();
				}
				else
				{
//					Class<?> newFormClass;
//					if("login1".equalsIgnoreCase(newForm))
//					{
//						newFormClass = CaptureActivity.class;
//					}
//					else
//					{
//						newFormClass = CaptureActivity.class;
//					}
//					Context context = WebBrowser.this.getContext();
//					Intent intent = new Intent(context, newFormClass);
//					//intent.putExtra("url", newURL);
//					context.startActivity(intent);
				}
			}
			else if("scan".equalsIgnoreCase(operate))//扫描
			{
				if(this._paramHashMap != null)
				{
					this._returnObjectId = this._paramHashMap.get("rid");
				}
				if("self".equalsIgnoreCase(newForm))//非新窗口
				{
					//view.loadUrl(newURL);
				}
				else
				{
					Class<?> newFormClass;
					if("scan1".equalsIgnoreCase(newForm))
					{
						newFormClass = CaptureActivity.class;
					}
					else
					{
						newFormClass = CaptureActivity.class;
					}
					
					Intent intent = new Intent(this._activity.getContext(), newFormClass);
					this._activity.startActivityForResult(intent, Protocol_Result_Scan);
				}
			}
			else if("photo".equalsIgnoreCase(operate))//拍照
			{
				if("self".equalsIgnoreCase(newForm))//非新窗口
				{
					//view.loadUrl(newURL);
				}
				else
				{
					Class<?> newFormClass;
					if("login1".equalsIgnoreCase(newForm))
					{
						newFormClass = InformationCollectActivity.class;
					}
					else
					{
						newFormClass = InformationCollectActivity.class;
					}
					Intent intent = new Intent(this._activity.getContext(), newFormClass);
					this._activity.getContext().startActivity(intent);
				}
			}
			return true;
		}
		else//非ngsj://打头
		{
			if(this._webView == null)//当前没有WebView，则新开一个窗口
			{
				Intent intent = new Intent(this._activity.getContext(), WebBrowserActivity.class);
				intent.putExtra("url", url);
				this._activity.getContext().startActivity(intent);
				return true;
			}
			else
			{
				//this._webView.loadUrl(url);
				return false;
			}
		}
	}
}
