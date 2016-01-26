package apk.customerview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import apk.R;
import apk.bll.CurrUserInfoService;
import apk.common.JSONReader;
import apk.common.StringHelper;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.customerview.TitlePopup.OnItemOnClickListener;
import apk.customerview.WebBrowser.OnWebViewListener;
import apk.dal.ConfigDAL;
import apk.dal.LogHelper;
import apk.i.IActivity;
import apk.ibrowser.NativeInterface;
import apk.model.MainMenuData.TitleAlign;


public class WebBrowserView extends RelativeLayout
{
	private IActivity _activity;
	private TitleBar _titleBar;
	private WebBrowser _mainWebWebBrowser;
	private boolean _changeTitleWithHTML = true;//根据HTML标题改变标题
	
	// 定义标题栏弹窗按钮
	private TitlePopup _titlePopup;
	
	
	private NativeInterface _nativeInterface;

	public WebBrowserView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_webbrowser, this);
		
	
		this._titlePopup = new TitlePopup(this.getContext(), LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		/*
		 * rightbutton 下拉popu中listview的item的 click事件
		 */
		this._titlePopup.setItemOnClickListener(new OnItemOnClickListener()
		{
			@Override
			public void onItemClick(ActionItem item, int position)
			{
				//_mainWebWebBrowser.getProtocol().getProtocol(item.mProtocol);
				if(_nativeInterface!=null)
				{
					//_nativeInterface.postMessage(item.);
					_nativeInterface.callJavaScript(item.mProtocol);
				}
			}
		});
		
		
		this._mainWebWebBrowser = (WebBrowser) this.findViewById(R.id.mainWebView);
		
		this._mainWebWebBrowser.setOnWebViewListener(new OnWebViewListener()
		{
			@Override
			public void OnReceivedTitle(String title)
			{
				//_titleBar.setTitle(title);
			}

			@Override
			public void OnLoaded()
			{
				_nativeInterface.beginNativeLoaded(CurrUserInfoService.getUserInfo(_mainWebWebBrowser.getContext()));				
				//_nativeInterface.beginNativeLoaded("{}");
				_nativeInterface.sendHybridPage();
				_nativeInterface.ngsjLoad();
			}
		});
		
		
		
		//令WebView可调用Android代码，如：window.barcodeInterface.openBarcode()
		//this._mainWebView.addJavascriptInterface(this._barcodeInterface = new BarcodeInterface(this.getActivity(), this._mainWebView), "barcodeInterface");
		//this._mainWebView.loadUrl(this._url);
		//this._mainWebView.loadUrl("file:///android_asset/html/index.html");
		
		this._nativeInterface = new NativeInterface(this, this._mainWebWebBrowser);
		
		this._mainWebWebBrowser.addJavascriptInterface(this._nativeInterface, NativeInterface.InstanceName);
		
		this._mainWebWebBrowser.setNativeInterface(this._nativeInterface);
		
		this._titleBar = (TitleBar) this.findViewById(R.id.titleBar);
		//this._titleBar.setTitle("主页");
		
		
		
		
		this._titleBar.setOnTitleBarListener(new OnTitleBarListener()
		{
			@Override
			public void OnBackClick(View v)
			{
				if(_mainWebWebBrowser.canGoBack())
				{
					_mainWebWebBrowser.goBack();
				}
				else
				{
//					if(_fragment != null)
//					{
//						
//					}
//					else
//					{
//						((Activity) _context).finish();
//					}
					
					_activity.finish();
				}
			}

			@Override
			public void OnPopupMenuClick(View v)
			{
				//WebBrowserView.this.showMenu();
				WebBrowserView.this._titlePopup.show(v);
			}

			@Override
			public void OnOk(View v, String clickUrl)
			{
				if(!StringHelper.IsNullOrEmptyOrBlank(clickUrl))
				{
					_mainWebWebBrowser.getProtocol().getProtocol(clickUrl);
				}
			}
		});
	}
	
	public void onDataFromWebView(String jsonString)
	{
		try
		{
			if(jsonString == null || jsonString.length() < 1)
				return;
				
			JSONObject jsonObject = new JSONObject(jsonString);
			
			if(this._changeTitleWithHTML)
			{
				this._titleBar.setTitle(jsonObject.getString("title"));
			}
		}
		catch(Exception e)
		{
			LogHelper.d("", e.getMessage());
		}
	}
	
	
	public WebBrowser GetWebBrowser()
	{
		return _mainWebWebBrowser; 
	}
	//此方法用于网页中请求打开的native页面返回值时调用。
	//本来应该写到具体的Interface类里的，但是Interface里无法注册页面返回方法。
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		this._mainWebWebBrowser.onActivityResult(requestCode, resultCode, data);
	}
	
	public void setActivity(IActivity activity)
	{
		this._activity = activity;
		this._mainWebWebBrowser.setActivity(activity);
	}
	public void showLogo(boolean isShowLogo)
	{
		this._titleBar.showLogo(isShowLogo);
	}
	public void setLogo(String picName)
	{
		this._titleBar.setLogo(picName);
	}
	public void setChangeTitleWithHTML(boolean changeTitleWithHTML)
	{
		this._changeTitleWithHTML = changeTitleWithHTML;
	}
	public void setTitleVisible(boolean titleVisible)
	{
		if(!titleVisible)
		{
			this._titleBar.setVisibility(View.INVISIBLE);
			
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)this._mainWebWebBrowser.getLayoutParams();
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		}
	}
	public void setTitle(String title, float fontSize, int fontColor, TitleAlign align, String titlePic)
	{
		this._titleBar.setTitle(title, fontSize, fontColor, align, titlePic);
	}
	public void setTitleBackgroundColor(int color)
	{
		this._titleBar.setBackgroundColor(color);
	}
	
	public void setOkButton(String jsonString)
	{
		
//		jsonString = "{\"rightbtn\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\",\"righttitle\":\"菜 单\"," +
//	"menu:[" +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}," +
//	"	{\"title\":\"打开百度\",\"protocol\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\"}" +
//	"	]" +
//"}";
		
		jsonString= "{\"rightbtn\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\",\"righttitle\":\"菜 单\"," +
				"menu:[" +
				"	{\"title\":\"打开百度\",\"protocol\":\"hybridPage.Native.NavToNewWebPage(\'v1/d2.aspx\')\"}"+ 
				"	]" +
			"}";
		
		jsonString= "{\"rightbtn\":\"ngsj://url:web1:eyJ1cmwiOiJzZW5kLmFzcHgifQ==\",\"righttitle\":\"菜 单\"" +				
			"}";
		
		JSONReader jsonReader = new JSONReader(jsonString);
		String rightbtn = jsonReader.getString("rightbtn", "");
		String righttitle = jsonReader.getString("righttitle", "");
		String rightimg = jsonReader.getString("rightimg", "");
		String title = jsonReader.getString("title", "");
		JSONArray menuArray = jsonReader.getJSONArray("menu");
		
		if(menuArray != null)//如果是菜单
		{
			if(!StringHelper.IsNullOrEmpty(righttitle))//如果是文字按钮
			{
				this._titleBar.showMenuButton(true);
				this._titleBar.setMenuButtonTitle(righttitle);
				
				for(int i =0;i<menuArray.length();i++)
				{
					try
					{
						JSONReader menuJsonReader = new JSONReader(menuArray.getJSONObject(i));
						
						String titleStr = menuJsonReader.getString("title", "");
						String protocol = menuJsonReader.getString("protocol", "");
						
						this._titlePopup.addAction(new ActionItem(this.getContext(), titleStr, protocol));
						
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			if(!StringHelper.IsNullOrEmptyOrBlank(rightbtn))//如果是按钮
			{
				if(!StringHelper.IsNullOrEmpty(righttitle))//如果是文字按钮
				{
					this._titleBar.showOkButton(true);
					this._titleBar.setOkButtonTitle(righttitle, rightbtn);
				}
				else
				{
					if(!StringHelper.IsNullOrEmpty(rightimg))//如果是图片按钮
					{
						String rightimgLower = StringHelper.toLowerCase(rightimg); 
						if(!rightimgLower.startsWith("http://") && !rightimgLower.startsWith("https://"))
						{
							rightimg = ConfigDAL.getServerMainUrl() + rightimg;
						}
						this._titleBar.showOkButtonImage(true);
						this._titleBar.setOkButtonImage(rightimg, rightbtn);
					}
				}
			}
		}
		if(!StringHelper.IsNullOrEmpty(title))
		{
			this._titleBar.setTitle(title);
		}
	}
	
	public void setNumText(int value)
	{
		this._titleBar.setNumText(value);
	}
	
	/**
	 * 回帖
	 * @param message
	 */
	public void sendMessage(String message)
	{
		this._nativeInterface.sendMessage(message);
	}
	
	public void jsNativeBack()
	{
//		if(this._fragment == null)
//		{
//			Activity act = (Activity) this._context;
//			act.finish();
//		}
		
		this._activity.finish();
	}
	
	public void refreshPage()
	{
		this._nativeInterface.refreshPage();
	}
	
	public void setURL(String url)
	{
		this._mainWebWebBrowser.loadUrl(url);
	}
}
