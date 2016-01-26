package apk.activity;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import apk.R;
import apk.bll.AppPictureService;
import apk.bll.PhotoService;
import apk.bll.PhotoService.OnUploadFileListener;
import apk.bll.UserService;
import apk.bll.UserService.OnGetUserFromServer;
import apk.bll.UserService.OnLogoff;
import apk.bll.UserService.OnUpdateUser;
import apk.common.StringHelper;
import apk.common.Util;
import apk.customerview.DialogSelect;
import apk.customerview.DialogSelect.SelectedListener;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.customerview.WaiteBox;
import apk.dal.ConfigDAL;
import apk.model.MainMenuData;
import apk.model.PicData;
import apk.model.Protocol;
import apk.model.Size;
import apk.model.UserData;
import apk.net.DownLoadFile;
import apk.net.DownLoadFile.OnDownLoadFileFinishListener;

public class UserEditActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private RelativeLayout _mainFrame;
	private TextView _txtUnit;
	private ImageView _imgUserHeadPhoto;
	private EditText _txtUserName;
	private EditText _txtUserNickName;
	private EditText _txtPhone;
	private TextView _txtPasswd1;
	private TextView _labMessage;
	private Button _btnLogOff;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_user_edit);
		
		this._mainFrame = (RelativeLayout)this.findViewById(R.id.mainFrame);
		this._txtUnit = (TextView) this.findViewById(R.id.txtUnit);
		this._imgUserHeadPhoto = (ImageView) this.findViewById(R.id.imgUserHeadPhoto);
		this._txtUserName = (EditText) this.findViewById(R.id.txtUserName);
		this._txtUserNickName = (EditText) this.findViewById(R.id.txtUserNickName);
		this._txtPhone = (EditText) this.findViewById(R.id.txtPhone);
		this._txtPasswd1 = (TextView) this.findViewById(R.id.txtPasswd1); 
		this._labMessage = (TextView) this.findViewById(R.id.labMessage);
		this._btnLogOff = (Button) this.findViewById(R.id.btnLogOff);
		
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
		
		this._txtPasswd1.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(UserEditActivity.this, UserChangePasswordActivity.class);
				UserEditActivity.this.startActivity(intent);
			}
		});
		
		
		UserService userService = new UserService(this);
		userService.setOnGetUserFromServer(new OnGetUserFromServer()
		{
			@Override
			public void OnGetUserFromServerFinish(UserData userData)
			{
				if(userData == null)
				{
					//如果没有注册
					Intent intent = new Intent(UserEditActivity.this, UserRegisterActivity.class);
					UserEditActivity.this.startActivity(intent);
					UserEditActivity.this.finish();
					Util.gc();
				}
				else
				{
					//如果已经注册
					String headPic = userData.get_head_pic();
					if(StringHelper.IsNullOrEmpty(headPic))
					{
						_imgUserHeadPhoto.setImageResource(R.drawable.no_head_photo);
					}
					else
					{
						String fileUrl = ConfigDAL.getServerMainUrl() + headPic;
						final String fileName = getPicName(fileUrl);
						File picFile = new File(fileName);
						if(!picFile.exists())
						{
							setHeadPhotoUrl(headPic);
						}
						else
						{
							Bitmap bitmap = BitmapFactory.decodeFile(fileName);
							_imgUserHeadPhoto.setImageBitmap(bitmap);
						}
					}
					
					_txtUnit.setText(userData.get_unit_title());
					_txtUserName.setText(userData.get_name());
					_txtUserNickName.setText(userData.get_nick_name());
					_txtPhone.setText(userData.get_phone_no());
				}
			}
		});
		
		userService.beginGetUserFromServer(Util.getDeviceId());
		
		this._imgUserHeadPhoto.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				DialogSelect _dialogPhotoGetSelect = new DialogSelect(UserEditActivity.this, new int[] { 0, 1 }, new String[] { "拍照", "从相册选择" });
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
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Protocol.TakePhotoFileName)));
								UserEditActivity.this.startActivityForResult(intent, Protocol.Protocol_Result_Photo_Take_Photo);
								break;
							case 1:
								intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								UserEditActivity.this.startActivityForResult(intent, Protocol.Protocol_Result_Photo_Load_Image);
								break;
						}
					}
				});
				_dialogPhotoGetSelect.showAtLocation(UserEditActivity.this._imgUserHeadPhoto.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				
				hideSoftInput(); 
			}
		});
		
		
		
		
		this._txtUserName.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				updateField(hasFocus, _txtUserName, "name", "请输入真实的业主姓名", "业主姓名");
			}
		});
		
		this._txtUserNickName.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				updateField(hasFocus, _txtUserNickName, "nick_name", "昵称太短，请重新输入", "昵称");
			}
		});
		
		this._txtPhone.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				updateField(hasFocus, _txtPhone, "phone_no", "请输入有效的电话号码", "联系手机");
			}
		});
		
		this._btnLogOff.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				UserService userService = new UserService(UserEditActivity.this);
				userService.setOnLogoff(new OnLogoff()
				{
					@Override
					public void OnLogoffFinish(boolean success, String message)
					{
						if(success)
						{
							Intent intent = new Intent(UserEditActivity.this, UserLoginActivity.class);
							UserEditActivity.this.startActivity(intent);
							UserEditActivity.this.finish();
							Util.gc();
						}
					}
				});
				userService.beginLogoff();
			}
		});
	}
	
	
	private void setHeadPhotoUrl(String headPic)
	{
		
		String fileUrl = ConfigDAL.getServerMainUrl() + headPic;
		final String fileName = getPicName(fileUrl);
		
		//如查图片文件本地不存在，则下载
		DownLoadFile downLoadFile = new DownLoadFile();
		downLoadFile.setOnDownLoadFileFinishListener(new OnDownLoadFileFinishListener()
		{
			@Override
			public void OnDownLoadFileFinish(boolean isSuccess)
			{
				Bitmap bitmap = BitmapFactory.decodeFile(fileName);
				_imgUserHeadPhoto.setImageBitmap(bitmap);
			}
		});
		downLoadFile.beginDownLoadFile(UserEditActivity.this, fileUrl, fileName);
	}
	
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode)
		{
			case Protocol.Protocol_Result_Photo_Load_Image:
			case Protocol.Protocol_Result_Photo_Take_Photo:
				
				final WaiteBox _waiteBox = new WaiteBox(this, "正在处理，请稍候...");
				
				
				PicData picData = null;
				PhotoService photoService = new PhotoService();
				photoService.setOnUploadFileListener(new OnUploadFileListener()
				{
					@Override
					public void OnUploadFileFinish(long resourceId, final String fileName, boolean isSuccess)
					{
						UserService userService = new UserService(UserEditActivity.this);
						userService.setOnUpdateUser(new OnUpdateUser()
						{
							@Override
							public void OnUpdateUserFinish(boolean success, String message)
							{
								if(success)
								{
									setHeadPhotoUrl(fileName);
								}
								else
								{
									
								}
							}
						});
						userService.beginUpdateUser("head_pic", fileName);
						_waiteBox.dismiss();
					}
				});
				if (requestCode == Protocol.Protocol_Result_Photo_Load_Image && resultCode == Activity.RESULT_OK && data != null)
				{
					String pictureName = this.getPictureName(data, requestCode);
					picData = new PicData();
					picData.setUpfile(pictureName);
				}
				else if (requestCode == Protocol.Protocol_Result_Photo_Take_Photo && resultCode == Activity.RESULT_OK)// && data != null)
				{
					String pictureName = this.getPictureName(data, requestCode);
					picData = new PicData();
					picData.setUpfile(pictureName);
				}
				if(picData != null)
				{
					_waiteBox.show();
					Size size = ConfigDAL.getUploadHeadPhotoMaxSize();
					
					photoService.uploadFile(picData, 0, size);
					
				}
				break;
		}
	}
	
	
	private String getPictureName(Intent data, int requestCode)
	{
		if(requestCode == Protocol.Protocol_Result_Photo_Take_Photo)
		{
			String p = Environment.getExternalStorageDirectory() + "/" + Protocol.TakePhotoFileName;
			return p;
		}
		else if(requestCode == Protocol.Protocol_Result_Photo_Load_Image)
		{
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String pictureName = cursor.getString(columnIndex);
			cursor.close();

			return pictureName;
		}
		
		return "";
	}
	
	
	
	
	private void hideSoftInput()
	{
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(this._txtUserName.getWindowToken(), 0);
	}
	
	private void updateField(boolean hasFocus, EditText txtBox, String key, String message, final String fieldName)
	{
		if(hasFocus)
		{
			txtBox.setTag(txtBox.getText().toString());
		}
		else
		{
			if(txtBox.getText().toString().length() < 4)
			{
				_labMessage.setText(message);
				txtBox.setText(Util.isNull(txtBox.getTag(), ""));
			}
			else
			{
				final String text = txtBox.getText().toString();
				final String oldText = Util.isNull(txtBox.getTag(), ""); 
				
				if(!text.equals(oldText))
				{
					//如果修改了，则更新
					UserService userService = new UserService(UserEditActivity.this);
					userService.setOnUpdateUser(new OnUpdateUser()
					{
						@Override
						public void OnUpdateUserFinish(boolean success, String message)
						{
							if(!success)
							{
								_labMessage.setText(fieldName + "更新失败");
							}
						}
					});
					userService.beginUpdateUser(key, text);
				}
			}
		}
	}
	
	private String getPicName(String urlFileName)
	{
		String fileName = Util.getFileName(urlFileName);
		return AppPictureService.getPicturePath() + fileName;
	}
}
