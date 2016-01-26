package apk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import apk.R;
import apk.bll.CurrUserInfoService;
import apk.bll.FlashPageService;
import apk.common.AppLog;
import apk.common.AppProjectType;
import apk.common.Util;
import apk.customerview.MessageBox;
import apk.customerview.MessageBoxButtons;
import apk.customerview.MessageBoxIcon;
import apk.customerview.MessageBoxResult;
import apk.dal.ConfigDAL;
import apk.dal.DataBaseHelper;
import cn.jpush.android.api.JPushInterface;


public class FlashNativeActivity extends BaseActivity
{
	private ImageView _imgView;
	private boolean _haveGoneNextPage = false;// 是否已跳往下一页
	private boolean _haveLoadConfigDataFromServer = false;// 是否已从服务端加载数据

	private void openThreadToStartNextPage()
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(ConfigDAL.getStartPageWaiteTime());
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				startNextPage();
			}
		});
		thread.start();
	}

	private synchronized void startNextPage()
	{
		if (this._haveGoneNextPage)
			return;
		this._haveGoneNextPage = true;

		Handler uiHanlder = new Handler(FlashNativeActivity.this.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);

				Toast.makeText(FlashNativeActivity.this, "正在加载...", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(FlashNativeActivity.this, MainActivity.class);
				FlashNativeActivity.this.startActivity(intent);
				FlashNativeActivity.this.finish();

				// Intent intent = new Intent(FlashActivity.this,
				// UserRegisterActivity.class);
				// Intent intent = new Intent(FlashActivity.this,
				// AboutActivity.class);
				// FlashActivity.this.startActivity(intent);
				// FlashActivity.this.finish();

				Util.gc();
			}
		};
		uiHanlder.sendMessage(new Message());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		JPushInterface.init(getApplicationContext());

		setContentView(R.layout.activity_flash_native);
		
		this._imgView = (ImageView) this.findViewById(R.id.imgView);
		
		AppProjectType.init(this);
		
		FlashPageService.initAppConfigFromXML(this);

		AppLog.Init(this);
		DataBaseHelper.InitInstance(this);

		this._haveLoadConfigDataFromServer = true;
		this.openThreadToStartNextPage();
	}

	private boolean checkNetConnect()
	{
		// 如果网络是关的，则打开网络配置页面
		if (!Util.isNetConnect(this))
		{
			if (MessageBox.Show(this, "提示", "系统需要访问网络，是否立即打开网络配置？", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == MessageBoxResult.Yes)
			{
				Intent intent;
				if (android.os.Build.VERSION.SDK_INT > 10)
				{
					intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
				}
				else
				{
					intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				}
				this.startActivity(intent);
				return false;
			}
			else
			{
				// System.exit(0);
				Toast.makeText(this, "网络没有打开！", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		else
		{
			return true;
		}
	}

	private void init()
	{
		Util.init(this);
		
		CurrUserInfoService.beginInitUserInfo(this);//从服务端获取用户信息（异步）

		this._haveGoneNextPage = false;
		
		switch(AppProjectType.getCurrentProjectType())
		{
			case AppProjectType.IData95:
				this._imgView.setImageResource(R.drawable.wodexiaodian_start);
				break;
			case AppProjectType.LoveGreen:
				this._imgView.setImageResource(R.drawable.lovegreen_start);
				break;
			case AppProjectType.WestSoft:
				this._imgView.setImageResource(R.drawable.wodexiaodian_start);
				break;
			case AppProjectType.MenQianXiaoDian_mobi:
				this._imgView.setImageResource(R.drawable.menqianxiaodian_start);
				break;
			case AppProjectType.WoDeXiaoDian_boss:
				this._imgView.setImageResource(R.drawable.wodexiaodian_start);
				break;
			case AppProjectType.RenZhongZiBen:
				this._imgView.setImageResource(R.drawable.renzhongziben_start);
				break;
			case AppProjectType.KTWY:
				this._imgView.setImageResource(R.drawable.ktwy_start);
				break;
		}
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if ((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else
			{
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (_haveLoadConfigDataFromServer)
		{
			startNextPage();
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		JPushInterface.onResume(this);

		if (this.checkNetConnect())
		{
			this.init();
		}
		else
		{
			Toast.makeText(this, "网络没有打开！", Toast.LENGTH_LONG).show();
			this.init();
		}
	}
}
