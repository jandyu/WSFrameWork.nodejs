package apk.customerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import apk.R;
import apk.bll.AppPictureService;
import apk.common.MD5;
import apk.common.Util;
import apk.dal.ConfigDAL;
import apk.model.MainMenuData;
import apk.model.MainMenuData.TitleAlign;
import apk.net.DownLoadFile;
import apk.net.DownLoadFile.OnDownLoadFileFinishListener;

public class TitleBar extends RelativeLayout
{
	private Button _btnBack;
	private Button _btnOK;
	private Button _btnMenu;
	private ImageView _btnOkImage;
	private ImageView _imgLogo;
	private ImageView _imgTitlePic;
	private TextView _txtNumText;
//	private ImageButton _btnMenu;
	private TextView _labTitle;
	private RelativeLayout _titlaMainFrame;
	
	private String _clickUrl;
	
	private MainMenuData _mainMenuData;
	
	public interface OnTitleBarListener
	{
		public void OnBackClick(View v);
		public void OnPopupMenuClick(View v);
		public void OnOk(View v, String url);
	}
	
	private OnTitleBarListener _onTitleBarListener;
	public void setOnTitleBarListener(OnTitleBarListener onTitleBarListener)
	{
		this._onTitleBarListener = onTitleBarListener;
	}
	
	public TitleBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.title_bar, this);
		
		
		
		
		
		this._uiHanlder = new Handler(context.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				
				Bundle bundle = msg.getData();
				String imageFileName = bundle.getString("imageFileName");
				
				Bitmap bitmap = BitmapFactory.decodeFile(imageFileName);
				_btnOkImage.setImageBitmap(bitmap);
			}
		};
		
		
		
		
		
		
		
		
		this._titlaMainFrame = (RelativeLayout) this.findViewById(R.id.titlaMainFrame);
		
		this._mainMenuData = ConfigDAL.getNormalMainMenuData();
		
		this._titlaMainFrame.setBackgroundColor(this._mainMenuData.getBackgroundColor());
		
		
		this._labTitle = (TextView)this.findViewById(R.id.labTitle);
		this._btnBack = (Button) this.findViewById(R.id.btnBack);
		this._btnOK = (Button) this.findViewById(R.id.btnOK);
		this._btnMenu = (Button) this.findViewById(R.id.btnMenu); 
		this._btnOkImage = (ImageView) this.findViewById(R.id.btnOkImage);
		this._imgLogo = (ImageView) this.findViewById(R.id.imgLogo);
		this._imgTitlePic = (ImageView) this.findViewById(R.id.imgTitlePic);
//		this._btnMenu = (ImageButton) this.findViewById(R.id.btnMenu);
		
		this._txtNumText = (TextView) this.findViewById(R.id.txtNumText);
		
		this._btnBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(TitleBar.this._onTitleBarListener != null)
				{
					TitleBar.this._onTitleBarListener.OnBackClick(v);
				}
			}
		});
		
		this._btnMenu.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(TitleBar.this._onTitleBarListener != null)
				{
					TitleBar.this._onTitleBarListener.OnPopupMenuClick(v);
				}
			}
		});
		
		OnClickListener btnOkLintener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(TitleBar.this._onTitleBarListener != null)
				{
					TitleBar.this._onTitleBarListener.OnOk(v, _clickUrl);
				}
			}
		};
		
		this._btnOK.setOnClickListener(btnOkLintener);
		this._btnOkImage.setOnClickListener(btnOkLintener);
	}
	
	public void showLogo(boolean isShowLogo)
	{
		if(isShowLogo)
		{
			this._btnBack.setVisibility(INVISIBLE);
			this._imgLogo.setVisibility(VISIBLE);
		}
		else
		{
			this._btnBack.setVisibility(VISIBLE);
			this._imgLogo.setVisibility(INVISIBLE);
		}
	}
	
	public void showBackButton(boolean isShowBackButton)
	{
		if(isShowBackButton)
		{
			this._btnBack.setVisibility(VISIBLE);
		}
		else
		{
			this._btnBack.setVisibility(INVISIBLE);
		}
	}
	
	public void showOkButton(boolean isShowOkButton)
	{
		if(isShowOkButton)
		{
			this._btnOK.setVisibility(VISIBLE);
		}
		else
		{
			this._btnOK.setVisibility(INVISIBLE);
		}
	}
	public void showMenuButton(boolean isShowMenuButton)
	{
		if(isShowMenuButton)
		{
			this._btnMenu.setVisibility(VISIBLE);
		}
		else
		{
			this._btnMenu.setVisibility(INVISIBLE);
		}
	}
	public void setMenuButtonTitle(String title)
	{
		this._btnMenu.setText(title);
	}
	
	public void setOkButtonTitle(String title, String clickUrl)
	{
		this._btnOK.setText(title);
		this._clickUrl = clickUrl;
	}
	
	public void showOkButtonImage(boolean isShowOkButtonImage)
	{
		if(isShowOkButtonImage)
		{
			this._btnOkImage.setVisibility(VISIBLE);
		}
		else
		{
			this._btnOkImage.setVisibility(INVISIBLE);
		}
	}
	
	public void setNumText(int value)
	{
		if(value > 0)
		{
			this._txtNumText.setVisibility(View.VISIBLE);
			this._txtNumText.setText(String.valueOf(value));
		}
		else
		{
			this._txtNumText.setVisibility(View.INVISIBLE);
			this._txtNumText.setText("0");
		}
	}
	
	
	
	
	
	private Handler _uiHanlder;
	
	public void setOkButtonImage(String imageUrl, String clickUrl)
	{
		try
		{
			Bitmap bitmap = null;
			final String saveFileName = AppPictureService.getPicturePath() + MD5.getMD5(imageUrl);
			if(!Util.isFileExists(saveFileName))//如果不在本地，则下载
			{
				DownLoadFile dFile = new DownLoadFile();
				dFile.setOnDownLoadFileFinishListener(new OnDownLoadFileFinishListener()
				{
					@Override
					public void OnDownLoadFileFinish(boolean isSuccess)
					{
						if(isSuccess)
						{
							Message msg = new Message();
							Bundle data = new Bundle();
							
							data.putString("imageFileName", saveFileName);
							
							msg.setData(data);
							_uiHanlder.sendMessage(msg);
						}
					}
				});
				dFile.beginDownLoadFile(this.getContext(), imageUrl, saveFileName);
			}
			else
			{
				bitmap = BitmapFactory.decodeFile(saveFileName);
				this._btnOkImage.setImageBitmap(bitmap);
			}
		}
		catch(Exception e)
		{
			Log.d("", e.getMessage());
		}
		this._clickUrl = clickUrl;
	}
	
	
	public void setLogo(String picName)
	{
		Bitmap bitmap = BitmapFactory.decodeFile(picName);
		if(bitmap != null)
		{
			this._imgLogo.setImageBitmap(bitmap);
		}
	}
	
	public void setBackgroundColor(int color)
	{
		this._titlaMainFrame.setBackgroundColor(color);
	}
	
	
	public void setTextTitle(boolean isShowTextTitle, String title)
	{
		if(isShowTextTitle)
		{
			this._labTitle.setVisibility(VISIBLE);
			this._labTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, this._mainMenuData.getTitleSize());
			this._labTitle.setText(title);
			this._labTitle.setTextColor(this._mainMenuData.getTitleColor());
			
			switch(this._mainMenuData.getTitleAlign())
			{
				case left:
					this._labTitle.setGravity(Gravity.START);
					break;
				case center:
					this._labTitle.setGravity(Gravity.CENTER);
					break;
				case right:
					this._labTitle.setGravity(Gravity.END);
					break;
			}
		}
		else
		{
			this._labTitle.setVisibility(INVISIBLE);
		}
	}
	
	public void setPicTitle(boolean isShowPicTitle, String titlePic)
	{
		if(isShowPicTitle)
		{
			Bitmap bitmap = BitmapFactory.decodeFile(titlePic);
			this._imgTitlePic.setImageBitmap(bitmap);
			this._imgTitlePic.setVisibility(VISIBLE);
			
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)this._imgTitlePic.getLayoutParams();
			switch(this._mainMenuData.getTitleAlign())
			{
				case left:
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					break;
				case center:
					lp.addRule(RelativeLayout.CENTER_IN_PARENT);
					break;
				case right:
					lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					break;
			}
			this._imgTitlePic.setLayoutParams(lp);
		}
		else
		{
			this._imgTitlePic.setVisibility(INVISIBLE);
		}
	}
	
	
	public void setTitle(String title, float fontSize, int fontColor, TitleAlign align, String titlePic)
	{
		if(titlePic == null || titlePic.length() < 1)
		{
			this._labTitle.setText(title);
			this._labTitle.setTextColor(fontColor);
			this._labTitle.setVisibility(VISIBLE);
			this._labTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
			this._imgTitlePic.setVisibility(INVISIBLE);
		}
		else
		{
			Bitmap bitmap = BitmapFactory.decodeFile(titlePic);
			this._imgTitlePic.setImageBitmap(bitmap);
			this._labTitle.setVisibility(INVISIBLE);
			this._imgTitlePic.setVisibility(VISIBLE);
		}
		
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)this._imgTitlePic.getLayoutParams();
		
		switch(align)
		{
			case left:
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				this._labTitle.setGravity(Gravity.START);
				break;
			case center:
				lp.addRule(RelativeLayout.CENTER_IN_PARENT);
				this._labTitle.setGravity(Gravity.CENTER);
				break;
			case right:
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				this._labTitle.setGravity(Gravity.END);
				break;
		}
		this._imgTitlePic.setLayoutParams(lp);
	}
	
	public void setTitle(String title)
	{
		this._labTitle.setText(title);
	}
}
