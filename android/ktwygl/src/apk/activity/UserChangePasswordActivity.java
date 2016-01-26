package apk.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import apk.R;
import apk.bll.UserService;
import apk.bll.UserService.OnGetUserFromServer;
import apk.bll.UserService.OnUpdateUser;
import apk.common.Util;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.dal.ConfigDAL;
import apk.model.MainMenuData;
import apk.model.UserData;

public class UserChangePasswordActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private RelativeLayout _mainFrame;
	private TextView _txtPasswd1;
	private TextView _txtPasswd2;
	private TextView _txtPasswd3;
	private TextView _labMessage;
	private Button _btnOk;
	private Button _btnBack1;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_user_change_password);
		
		this._mainFrame = (RelativeLayout)this.findViewById(R.id.mainFrame);
		this._txtPasswd1 = (TextView) this.findViewById(R.id.txtPasswd1);
		this._txtPasswd2 = (TextView) this.findViewById(R.id.txtPasswd2);
		this._txtPasswd3 = (TextView) this.findViewById(R.id.txtPasswd3);
		this._labMessage = (TextView) this.findViewById(R.id.labMessage); 
		this._btnOk = (Button) this.findViewById(R.id.btnOk);
		this._btnBack1 = (Button) this.findViewById(R.id.btnBack1);
		
		MainMenuData mainMenuData = ConfigDAL.getNormalMainMenuData();
		if(mainMenuData != null)
		{
			this._mainFrame.setBackgroundColor(mainMenuData.getBackgroundColor());
		}
		this._titleBar = (TitleBar) this.findViewById(R.id.titleBar);
		this._titleBar.showLogo(false);
		this._titleBar.showBackButton(false);
		this._titleBar.showOkButton(false);
		this._titleBar.setTextTitle(true, "个人中心");
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
		
		this._btnBack1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
				Util.gc();
			}
		});
		
		this._btnOk.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(_txtPasswd2.getText().toString().length() < 4)
				{
					_labMessage.setText("密码至少需要4位，请重新输入");
					_txtPasswd2.setText("");
					_txtPasswd3.setText("");
					_txtPasswd2.requestFocus();
					return;
				}
				if(!_txtPasswd3.getText().toString().equals(_txtPasswd2.getText().toString()))
				{
					_labMessage.setText("两次输入的密码不一致，请重新输入");
					_txtPasswd2.setText("");
					_txtPasswd3.setText("");
					_txtPasswd2.requestFocus();
					return;
				}
				
				
				UserService userService = new UserService(UserChangePasswordActivity.this);
				userService.setOnGetUserFromServer(new OnGetUserFromServer()
				{
					@Override
					public void OnGetUserFromServerFinish(UserData userData)
					{
						String passwd = userData.get_passwd();
						
						if(_txtPasswd1.getText().toString().equals(passwd))
						{
							UserService userService1 = new UserService(UserChangePasswordActivity.this);
							userService1.setOnUpdateUser(new OnUpdateUser()
							{
								@Override
								public void OnUpdateUserFinish(boolean success, String message)
								{
									finish();
									Util.gc();
								}
							});
							userService1.beginUpdateUser("passwd", _txtPasswd2.getText().toString());
						}
						else
						{
							_labMessage.setText("输入的原密码错误");
						}
					}
				});
				
				userService.beginGetUserFromServer(Util.getDeviceId());
			}
		});
	}
}
