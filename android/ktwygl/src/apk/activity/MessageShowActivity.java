package apk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import apk.R;
import apk.bll.MessageServer;
import apk.common.DateHelper;
import apk.common.JSONHelper;
import apk.common.StringHelper;
import apk.customerview.MessageBox;
import apk.customerview.MessageBoxButtons;
import apk.customerview.MessageBoxIcon;
import apk.customerview.MessageBoxResult;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.dal.ConfigDAL;
import apk.dal.MessageDAL;
import apk.i.IActivity;
import apk.model.MainMenuData;
import apk.model.MessageData;
import apk.model.Protocol;
import apk.model.StringHashMap;

public class MessageShowActivity extends BaseActivity implements IActivity
{
	private TitleBar _titleBar;
	private TextView _labDate;
	private TextView _labText;
	private Button _btnDelete;
	private RelativeLayout _mainFrame;
	private String _url;
	private String _rid;
	private Handler _uiHandler;//跨线程修改UI
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_message_show);
		
		this._mainFrame = (RelativeLayout)this.findViewById(R.id.mainFrame);
		
		
		MainMenuData mainMenuData = ConfigDAL.getNormalMainMenuData();
		if(mainMenuData != null)
		{
			this._mainFrame.setBackgroundColor(mainMenuData.getBackgroundColor());
		}
		
		this._titleBar = (TitleBar) this.findViewById(R.id.titleBar);
		this._titleBar.showLogo(false);
		this._titleBar.showOkButton(false);
		this._titleBar.setTextTitle(true, "消息");
		this._titleBar.setOnTitleBarListener(new OnTitleBarListener()
		{
			@Override
			public void OnBackClick(View v)
			{
				finish();
			}

			@Override
			public void OnPopupMenuClick(View v)
			{
				
			}

			@Override
			public void OnOk(View v, String clickUrl)
			{
				if(!StringHelper.IsNullOrEmptyOrBlank(_url))
				{
					Protocol protocol = new Protocol();
					protocol.setActivity(MessageShowActivity.this);
					protocol.getProtocol(_url);
					
					//如果url非null，则此时更新为已读
					setRead(_rid);
				}
			}
		});
		
		this._labDate = (TextView) this.findViewById(R.id.labDate);
		this._labText = (TextView) this.findViewById(R.id.labText);
		
		this._btnDelete = (Button) this.findViewById(R.id.btnDelete);
		
		
		Intent intent = this.getIntent();
		String rid = intent.getStringExtra("rid");
		
		MessageData md = MessageDAL.getMessageByRid(rid);
		if(md != null)
		{
			this._titleBar.setTitle(md.getMessageTitle());
			this._labDate.setText(DateHelper.FormatDate( md.getMessageTime(), "yyyy-MM-dd HH:mm:ss"));
			this._labText.setText(md.getMessageText());
			this._url = md.getMessageData();
			this._rid = md.getRid();
			
			//如果url为null，则更新为已读
			if(StringHelper.IsNullOrEmptyOrBlank(_url))
			{
				this.setRead(this._rid);
			}
			
			if(!StringHelper.IsNullOrEmpty(this._url))
			{
				this._titleBar.showOkButton(true);
				this._titleBar.setOkButtonTitle("查看详情", this._url);
			}
			
			this._btnDelete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(MessageBox.Show(MessageShowActivity.this, "删除确认", "是否确认删除？", MessageBoxButtons.OKCancel, MessageBoxIcon.Question) == MessageBoxResult.OK )
					{
						MessageDAL.deleteMessage(_rid);
						finish();
					}
				}
			});
		}
		
		iniUIHandler();
	}
	
	
	private void setRead(String rid)
	{
		//更新已读状态
		MessageServer messageServer = new MessageServer();
		messageServer.setRead(this, rid);
	}


	@Override
	public void setNumText(int numValue)
	{
		this._titleBar.setNumText(numValue);
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
	@Override
	public void UpdUI(String arg)
	{
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("arg", arg);	
		msg.setData(bundle);
		this._uiHandler.sendMessage(msg);
	}
}
