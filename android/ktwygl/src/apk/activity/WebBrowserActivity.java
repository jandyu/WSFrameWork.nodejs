package apk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import apk.R;
import apk.common.JSONHelper;
import apk.customerview.WebBrowserView;
import apk.dal.ConfigDAL;
import apk.i.IActivity;
import apk.model.MainMenuData;
import apk.model.StringHashMap;

public class WebBrowserActivity extends BaseActivity implements IActivity
{
	private WebBrowserView _webBrowserView;
	private Handler _uiHandler;//跨线程修改UI
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_webview);
		
		this._webBrowserView = (WebBrowserView) this.findViewById(R.id.webBrowserView);
		this._webBrowserView.setActivity(this);
		this._webBrowserView.showLogo(false);
		
		MainMenuData mainMenuData = ConfigDAL.getNormalMainMenuData();
		if(mainMenuData != null)
		{
			this._webBrowserView.setTitleBackgroundColor(mainMenuData.getTitleBackgroundColor());
		}
		this._webBrowserView.setTitle("", mainMenuData.getTitleSize(), mainMenuData.getTitleColor(), mainMenuData.getTitleAlign(), null);
		
		Intent intent = this.getIntent();
		String url = intent.getStringExtra("url");
		
		this._webBrowserView.setURL(url);
		iniUIHandler();
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
					case "setbadge"://
						int num=Integer.parseInt(shm.get("num"));
						setNumText(num);
						break;
					case "setrightbutton"://SetRightButton
						String jstr=shm.get("jstr");
						_webBrowserView.setOkButton(jstr);
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
	
	//此方法用于网页中请求打开的native页面返回值时调用。
	//本来应该写到具体的Interface类里的，但是Interface里无法注册页面返回方法。
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		this._webBrowserView.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void setNumText(int numValue)
	{
		this._webBrowserView.setNumText(numValue);
	}

	@Override
	public Context getContext()
	{
		return this;
	}

	@Override
	public MainActivity getMainActivity()
	{
		return null;
	}
	
	private int _showTimes = 0;//第几次显示
	@Override
	public void onResume()
	{
		super.onResume();
		
		this._showTimes ++;
		
		if(this._showTimes > 1)
		{
			this._webBrowserView.refreshPage();
		}
	}
}
