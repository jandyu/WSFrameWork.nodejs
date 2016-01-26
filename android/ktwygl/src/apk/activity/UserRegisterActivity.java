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

public class UserRegisterActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private RelativeLayout _mainFrame;
	private EditText _txtUserName;
	private EditText _txtPhone;
	private EditText _txtPasswd1;
	private EditText _txtPasswd2;
	private TextView _labMessage;
	private TextView _txtUnit;
	private Button _btnRegister;
	private Button _btnToLoginPage;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_user_register);
		
		this._mainFrame = (RelativeLayout)this.findViewById(R.id.mainFrame);
		this._txtUserName = (EditText) this.findViewById(R.id.txtUserName);
		this._txtPhone = (EditText) this.findViewById(R.id.txtPhone);
		this._txtPasswd1 = (EditText) this.findViewById(R.id.txtPasswd1);
		this._txtPasswd2 = (EditText) this.findViewById(R.id.txtPasswd2);
		this._labMessage = (TextView) this.findViewById(R.id.labMessage);
		this._txtUnit = (TextView) this.findViewById(R.id.txtUnit);
		this._btnRegister = (Button) this.findViewById(R.id.btnRegister);
		this._btnToLoginPage = (Button) this.findViewById(R.id.btnToLoginPage);
		
		
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
		
		//点击选择小区
		this._txtUnit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(UserRegisterActivity.this, UnitSelectActivity.class);
				UserRegisterActivity.this.startActivityForResult(intent, 10);
				//UserRegisterActivity.this.finish();
			}
		});
		
		//跳至登录页面
		this._btnToLoginPage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
				UserRegisterActivity.this.startActivity(intent);
				UserRegisterActivity.this.finish();
				Util.gc();
			}
		});
		
		this._btnRegister.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(check())
				{
					UserService userService = new UserService(UserRegisterActivity.this);
					userService.setOnRegisterUser(new OnRegisterUser()
					{
						@Override
						public void OnRegisterUserFinsh(boolean success, String message)
						{
							if(!success)
							{
								if(message != null && message.indexOf("数据操作失败") > -1)
								{
									message = message.split("!")[1];
								}
								_labMessage.setText(message);
							}
							else
							{
								Intent intent = new Intent(UserRegisterActivity.this, UserEditActivity.class);
								UserRegisterActivity.this.startActivity(intent);
								UserRegisterActivity.this.finish();
								Util.gc();
							}
						}
					});
					
					userService.beginRegister(getUserData());
				}
			}
		});
	}
	
	private boolean check()
	{
		if(this._txtUserName.getText().toString().length() < 4)
		{
			this._labMessage.setText("请输入真实的业主姓名");
			this._txtUserName.requestFocus();
			return false;
		}
		
		
		
		if(Util.isNull(this._txtUnit.getTag(), -1) == -1)
		{
			this._labMessage.setText("请选择居住的小区房号");
			return false;
		}
		
		if(this._txtPhone.getText().toString().length() < 8)
		{
			this._labMessage.setText("请输入有效的联系电话");
			this._txtPhone.requestFocus();
			return false;
		}
		
		if(this._txtPasswd1.getText().toString().length() < 4)
		{
			this._labMessage.setText("密码至少需要4位，请重新输入");
			this._txtPasswd1.requestFocus();
			return false;
		}
		if(!this._txtPasswd1.getText().toString().equals(this._txtPasswd2.getText().toString()))
		{
			this._labMessage.setText("两次输入的密码不一致，请重新输入");
			this._txtPasswd2.requestFocus();
			return false;
		}
		return true;
	}
	
	private UserData getUserData()
	{
		UserData userData = new UserData();
		userData.set_name(this._txtUserName.getText().toString());
		userData.set_passwd(this._txtPasswd1.getText().toString());
		userData.set_phone_no(this._txtPhone.getText().toString());
		userData.set_unit_id(Util.isNull(this._txtUnit.getTag(), ""));
		userData.set_device_id(Util.getDeviceId());
		return userData;
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode)
		{
			case 10:
				
				if(resultCode == RESULT_OK)
				{
					long iid = data.getLongExtra("id", -1);
					String title = data.getStringExtra("text");
					
					this._txtUnit.setTag(iid);
					this._txtUnit.setText(title);
				}

				break;
		}
	}
}
