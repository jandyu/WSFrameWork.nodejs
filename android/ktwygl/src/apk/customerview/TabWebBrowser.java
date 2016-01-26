package apk.customerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import apk.R;
import apk.activity.MainActivity;
import apk.common.JSONHelper;
import apk.i.IActivity;
import apk.model.StringHashMap;
import apk.model.MainMenuData.TitleAlign;

public class TabWebBrowser extends Fragment implements IActivity
{
	private View _thisView;
	private String _url;
	private String _logo;
	private boolean _titleVisible;
	private String _title;
	private float _titleFontSize;
	private int _titleFontColor;
	private TitleAlign _titleAlign;
	private String _titlePic;
	private int _titleBackgroundColor;
	private int _backgroundColor;
	private boolean _changeTitleWithHTML = true;//根据HTML标题改变标题
	private WebBrowserView _webBrowserView;
	
	private Handler _uiHandler;//跨线程修改UI
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		iniUIHandler();
	}
	public void setLogo(String picName)
	{
		this._logo = picName;
	}
	public void setURL(String url)
	{
		this._url = url;
	}
	public void setChangeTitleWithHTML(boolean changeTitleWithHTML)
	{
		this._changeTitleWithHTML = changeTitleWithHTML;
	}
	public void setTitleVisible(boolean titleVisible)
	{
		this._titleVisible = titleVisible;
	}
	public void setTitle(String title, float fontSize, int fontColor, TitleAlign align, String titlePic)
	{
		this._title = title;
		this._titleFontSize = fontSize;
		this._titleFontColor = fontColor;
		this._titleAlign = align;
		this._titlePic = titlePic;
	}
	public void setTitleBackgroundColor(int color)
	{
		this._titleBackgroundColor = color;
	}
	
	public void setBackgroundColor(int color)
	{
		this._backgroundColor = color;
	}
	
	public void setNumText(int value)
	{
		this._webBrowserView.setNumText(value);
	}
	
	//此方法用于网页中请求打开的native页面返回值时调用。
	//本来应该写到具体的Interface类里的，但是Interface里无法注册页面返回方法。
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		this._webBrowserView.onActivityResult(requestCode, resultCode, data);
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		this._thisView = inflater.inflate(R.layout.tab_webbrowser, container, false);
		this._webBrowserView = (WebBrowserView) this._thisView.findViewById(R.id.webBrowserView);
		this._webBrowserView.setActivity(this);
		this._webBrowserView.setURL(this._url);
		this._webBrowserView.setLogo(this._logo);
		this._webBrowserView.setTitleVisible(this._titleVisible);
		this._webBrowserView.setTitle(this._title, this._titleFontSize, this._titleFontColor, this._titleAlign, this._titlePic);
		this._webBrowserView.setTitleBackgroundColor(this._titleBackgroundColor);
		this._webBrowserView.setBackgroundColor(this._backgroundColor);
		this._webBrowserView.setChangeTitleWithHTML(this._changeTitleWithHTML);
		return this._thisView;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
	}
	@Override
	public Context getContext()
	{
		return this.getActivity();
	}
	@Override
	public void finish()
	{
		
	}
	@Override
	public MainActivity getMainActivity()
	{
		return (MainActivity)this.getActivity();
	}
	
	
	private int _showTimes = 0;//第几次显示
	@Override
	public void onResume()
	{
		super.onResume();
		
		if(this._thisView != null)
		{
			this._showTimes ++;
		}
		
		if(this.getUserVisibleHint())
		{
			if(this._showTimes > 1)
			{
				this._webBrowserView.refreshPage();
			}
		}
	}
	
	private void iniUIHandler()
	{		
		this._uiHandler = new Handler(this.getContext().getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);				
				Bundle bundle = msg.getData();
				String arg = bundle.getString("arg");
				
				StringHashMap shm=new StringHashMap();
				shm=JSONHelper.parseObject(arg, StringHashMap.class);				
				String func=shm.get("func");
				switch(func.toLowerCase())
				{
					case "setbadge"://callJavaScript
						int num=Integer.parseInt(shm.get("num"));
						setNumText(num);
						break;
				}
			}
		};
	}
	
	/*
	 * 跨线程调用
	 */
	public void UpdUI(String arg)
	{
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("arg", arg);	
		msg.setData(bundle);
		this._uiHandler.sendMessage(msg);
	}
	
	
}
