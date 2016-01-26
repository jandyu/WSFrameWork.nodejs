package apk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import apk.R;
import apk.bll.UserService;
import apk.bll.UserService.OnRegisterUser;
import apk.common.Util;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.dal.ConfigDAL;
import apk.model.MainMenuData;
import apk.model.UserData;

public class UserLoginActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private RelativeLayout _mainFrame;
	private EditText _txtPhone;
	private EditText _txtPasswd1;
	private TextView _labMessage;
	private Button _btnLogin;
	private Button _btnToRegister;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_user_login);
		
		this._mainFrame = (RelativeLayout)this.findViewById(R.id.mainFrame);
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
		this._txtPhone = (EditText) this.findViewById(R.id.txtPhone);
		this._txtPasswd1 = (EditText) this.findViewById(R.id.txtPasswd1);
		this._labMessage = (TextView) this.findViewById(R.id.labMessage);
		
		this._btnLogin = (Button) this.findViewById(R.id.btnLogin);
		this._btnLogin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				UserData userData = new UserData();
				userData.set_device_id(Util.getDeviceId());
				userData.set_phone_no(_txtPhone.getText().toString());
				userData.set_passwd(_txtPasswd1.getText().toString());
				
				UserService userService = new UserService(UserLoginActivity.this);
				userService.setOnRegisterUser(new OnRegisterUser()
				{
					@Override
					public void OnRegisterUserFinsh(boolean success, String message)
					{
						if(success)
						{
							Intent intent = new Intent(UserLoginActivity.this, UserEditActivity.class);
							UserLoginActivity.this.startActivity(intent);
							UserLoginActivity.this.finish();
							Util.gc();
						}
						else
						{
							if(message != null && message.indexOf("数据操作失败") > -1)
							{
								message = message.split("!")[1];
							}
							_labMessage.setText(message);
						}
					}
				});
				userService.beginRegister(userData);
			}
		});
		
		this._btnToRegister = (Button) this.findViewById(R.id.btnToRegister);
		this._btnToRegister.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
				UserLoginActivity.this.startActivity(intent);
				UserLoginActivity.this.finish();
				Util.gc();
			}
		});
	}
}
