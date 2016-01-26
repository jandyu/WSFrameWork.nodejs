package apk.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import apk.R;
import apk.adapter.AdapterMessageList;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.dal.ConfigDAL;
import apk.dal.MessageDAL;
import apk.model.MainMenuData;
import apk.model.MessageData;


public class MessageListActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private ListView _listView;
	private RelativeLayout _mainFrame;
	private AdapterMessageList _adapterMessageList;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_message_list);
		
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
				
			}
		});
		
		this._listView = (ListView) this.findViewById(R.id.listView); 
		
		this.dataFill();
	}
	
	private List<MessageData> _messageList;
	
	private void dataFill()
	{
//		messageTypeList = new ArrayList<MessageTypeData>();
//		MessageTypeData messageType = new MessageTypeData();
//		messageType.setLastMessageText("您好！您有快递");
//		messageType.setMessageTypeId("1");
//		messageType.setMessageTypeTitle("物业消息");
//		messageType.setLastMessageTime(new Date());
//		messageTypeList.add(messageType);
//		
//		messageType = new MessageTypeData();
//		messageType.setLastMessageText("您好！您订的餐已做好，请尽快过来！");
//		messageType.setMessageTypeId("2");
//		messageType.setMessageTypeTitle("火锅店消息");
//		messageType.setLastMessageTime(new Date());
//		messageTypeList.add(messageType);
		
//		Intent intent = this.getIntent();
//		String messageTypeId = intent.getStringExtra("messageTypeId");
		
//		messageList = MessageDAL.getMessageDataList(messageTypeId);
		this._messageList = MessageDAL.getMessageDataList();
		
		this._adapterMessageList = new AdapterMessageList(this);
		this._adapterMessageList.setDataList(this._messageList);
		this._listView.setAdapter(this._adapterMessageList);
		
		this._listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				MessageData mtd = _messageList.get(arg2);
				
				Intent intent = new Intent(MessageListActivity.this, MessageShowActivity.class);
				
				intent.putExtra("rid", mtd.getRid());
				MessageListActivity.this.startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1)
		{
			this._messageList = MessageDAL.getMessageDataList();
			this._adapterMessageList.setDataList(this._messageList);
			this._adapterMessageList.notifyDataSetChanged();
		}
	}
}
