package apk.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import apk.R;
import apk.adapter.AdapterFragmentPager;
import apk.bll.MessageServer;
import apk.bll.UpdateService;
import apk.bll.UpdateService.OnCheckUpdate;
import apk.bll.UpdateService.OnDownLoadAPK;
import apk.common.AppProjectType;
import apk.common.JSONHelper;
import apk.common.Util;
import apk.customerview.MainViewPager;
import apk.customerview.MessageBox;
import apk.customerview.MessageBoxButtons;
import apk.customerview.MessageBoxIcon;
import apk.customerview.MessageBoxResult;
import apk.customerview.TabWebBrowser;
import apk.dal.ConfigDAL;
import apk.i.IActivity;
import apk.model.MainMenuData;
import apk.model.MainMenuData.ServerOrLocal;
import apk.model.MainMenuData.TitleAlign;
import apk.model.MessageData;
import apk.model.Protocol;
import apk.model.StringHashMap;
import cn.jpush.android.api.JPushInterface;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements IActivity
{
	// private RelativeLayout _mainFrame;
	// private RelativeLayout _mainMenu;
	private MainViewPager _viewPager;
	private AdapterFragmentPager _viewPagerAdapter;
	private RelativeLayout _bottomButtons;
	private RadioGroup _tabRgMenu;

	private RelativeLayout _bottomNumbers;
	private TextView[] _txtNumTextList;	

	private ArrayList<Fragment> _fragments = new ArrayList<Fragment>();
	
	private Handler _uiHandler;//跨线程修改UI
	// private LinearLayout.LayoutParams _mainFrameParams;

	// /**
	// * mainFrame最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
	// */
	// private int _mainFrameRightEdge = 0;
	// /**
	// * mainFrame最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
	// */
	// private int _mainFrameLeftEdge;
	// /**
	// * mainFrame滑出时，最小显示宽度。
	// */
	// private int _mainFrameMinShowWidth;
	// private int _screenWidth;

	private static MainActivity _intance;

	public static MainActivity getIntance()
	{
		return _intance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//禁止横屏
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		_intance = this;

		this._progressDialog = new ProgressDialog(this);

		// this._screenWidth = Util.getScreenWidth(this);
		// this._mainFrameMinShowWidth = (int) (this._screenWidth * 0.25);

		this.setContentView(R.layout.activity_main);
		
		this._bottomButtons = (RelativeLayout) this.findViewById(R.id.bottomButtons);
		this._bottomNumbers = (RelativeLayout) this.findViewById(R.id.bottomNumbers);
		
		this.initView();
		iniUIHandler();
		JPushInterface.init(getApplicationContext());
		// this.registerMessageReceiver();
		
		this.checkUpdate();
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
						int idx= Integer.parseInt(shm.get("tabIndex"));
						int num=Integer.parseInt(shm.get("num"));
						setTabNumValue(idx, num);
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
		// String k = JPushInterface.getRegistrationID(this);
		// String k = JPushInterface.getUdid(this);
		this.doNotice();
	}

	public void doNotice()
	{
		// 当由通知打开本程序时
		MessageData messageData = Util.getNotice();
		if (messageData != null)
		{
			Util.setNotice(null);

			Protocol protocol = new Protocol();
			protocol.setActivity(this);
			protocol.getProtocol(messageData.getMessageData());

			// 更新已读状态
			MessageServer messageServer = new MessageServer();
			messageServer.setRead(this, messageData.getRid());
		}
	}

	private void checkUpdate()
	{
		final UpdateService updateService = new UpdateService();
		updateService.setOnCheckUpdate(new OnCheckUpdate()
		{
			@Override
			public void OnCheckUpdateFinish(boolean success, boolean haveNewVersion)
			{
				if (success && haveNewVersion)// 有升级包
				{
					if (MessageBox.Show(MainActivity.this, "检测到有新版本，是否下载更新？", "更新提示", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == MessageBoxResult.Yes)
					{
						showWaite("正在加载...");
						updateService.setOnDownLoadAPK(new OnDownLoadAPK()
						{
							@Override
							public void OnDownLoadAPKFinish(boolean success, String fileName)
							{
								if (success)
								{
									Intent intent = new Intent(Intent.ACTION_VIEW);
									intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
									startActivity(intent);
								}
								else
								{
									Toast.makeText(MainActivity.this, "网络不络力，更新包下载失败！", Toast.LENGTH_SHORT).show();
								}

								hideWaite();
							}
						});
						updateService.beginDownLoadAPK(MainActivity.this);
					}
				}
			}
		});
		updateService.beginCheckUpdate(this);
	}

	private RadioButton newRadioButton(MainMenuData mainMenuData)
	{
		Resources res = this.getResources();
		RadioButton rb = new RadioButton(this);

		rb.setButtonDrawable(android.R.color.transparent);// 去掉圆点，相当于：<item
															// >@null</item>
		// rb.setTextColor(res.getColor(R.color.tab_selector_tv_color));
		// rb.setTextColor(mainMenuData.getMenuTitleColor());
		// rb.setTextSize(TypedValue.COMPLEX_UNIT_SP,
		// mainMenuData.getMenuTitleSize());
		// rb.setText(menuTitle);

		// Drawable drawableTop =
		// res.getDrawable(R.drawable.tab_selector_weixing);
		// drawableTop.setBounds(0, 0, 60, 60);//这里还要处理一下
		// rb.setCompoundDrawables(null, drawableTop, null, null);

		LayoutParams ly = new LayoutParams(0, LayoutParams.MATCH_PARENT);
		ly.weight = 1;
		ly.gravity = Gravity.BOTTOM;
		rb.setLayoutParams(ly);

		rb.setGravity(Gravity.CENTER);

		// ------------------------下面是处理background
		// Bitmap backgroundBitmapNormal =
		// BitmapFactory.decodeFile(mainMenuData.getMenuBackgroundNormal(ServerOrLocal.local));
		// Bitmap backgroundBitmapDown =
		// BitmapFactory.decodeFile(mainMenuData.getMenuBackgroundDown(ServerOrLocal.local));
		// if(backgroundBitmapDown == null || backgroundBitmapNormal == null)
		// {
		// BitmapDrawable backgroundDrawableNormal = new BitmapDrawable(res
		// ,backgroundBitmapNormal);
		// BitmapDrawable backgroundDrawableDown = new BitmapDrawable(res,
		// backgroundBitmapDown);
		//
		// StateListDrawable sdBackground = new StateListDrawable();
		// sdBackground.addState(new int[]{-android.R.attr.state_checked},
		// backgroundDrawableNormal);
		// sdBackground.addState(new int[]{android.R.attr.state_checked},
		// backgroundDrawableDown);
		//
		// //sdBackground.setBounds(0, 0, width, height);//这里还要处理一下
		// //rb.setCompoundDrawables(null, sdBackground, null, null);
		//
		// //rb.setBackgroundResource(R.drawable.tab_selector_checked_bg);
		// rb.setBackgroundDrawable(sdBackground);
		// }

		// ------------------------下面是处理icon
		// Bitmap iconBitmapNormal =
		// BitmapFactory.decodeFile(mainMenuData.getMenuIconNormal(ServerOrLocal.local));
		// Bitmap iconBitmapDown =
		// BitmapFactory.decodeFile(mainMenuData.getMenuIconDown(ServerOrLocal.local));

		int intBitmapNormal = res.getIdentifier(this.getPackageName() + ":drawable/" + mainMenuData.getMenuIconNormal(ServerOrLocal.server), null, null);
		int intBitmapDown = res.getIdentifier(this.getPackageName() + ":drawable/" + mainMenuData.getMenuIconDown(ServerOrLocal.server), null, null);

		Bitmap iconBitmapNormal = BitmapFactory.decodeResource(res, intBitmapNormal);
		Bitmap iconBitmapDown = BitmapFactory.decodeResource(res, intBitmapDown);

		BitmapDrawable iconDrawableNormal = new BitmapDrawable(res, iconBitmapNormal);
		BitmapDrawable iconDrawableDown = new BitmapDrawable(res, iconBitmapDown);

		StateListDrawable sdIcon = new StateListDrawable();
		sdIcon.addState(new int[] { -android.R.attr.state_checked }, iconDrawableNormal);
		sdIcon.addState(new int[] { android.R.attr.state_checked }, iconDrawableDown);

		// int width = iconDrawableNormal.getMinimumWidth();
		// int height= iconDrawableDown.getMinimumHeight();

		// sdIcon.setBounds(0, 0, width, height);//这里还要处理一下
		// rb.setCompoundDrawables(null, sdIcon, null, null);

		rb.setBackgroundDrawable(sdIcon);

		// String icon = AppConfigDAL.getBottomMenuIcon(index);
		//
		// Bitmap bitmap = BitmapFactory.decodeFile(icon);
		//
		// BitmapDrawable bd = new BitmapDrawable(bitmap);
		// Drawable drawableTop = bd;
		// drawableTop.setBounds(0, 0, bitmap.getWidth(),
		// bitmap.getHeight());//这里还要处理一下
		// rb.setCompoundDrawables(null, drawableTop, null, null);

		return rb;
	}

	private void initNumTexts()
	{
		int screenWidth = Util.getScreenWidth(this);
		float pWidth = screenWidth * 1.0f / this._txtNumTextList.length;

		for (int i = 0; i < this._txtNumTextList.length; i++)
		{
			TextView numText = new TextView(this);

			LayoutParams ly = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			ly.weight = 1;
			ly.gravity = Gravity.BOTTOM;
			ly.width = LayoutParams.WRAP_CONTENT;
			ly.height = LayoutParams.WRAP_CONTENT;
			numText.setLayoutParams(ly);

			numText.setGravity(Gravity.CENTER);

			numText.setText("0");
			numText.setTextColor(Color.WHITE);

			float x = pWidth * i;

			numText.setX(x);
			numText.setY(0);
			numText.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tab_unread_bg));
			numText.setVisibility(View.INVISIBLE);

			this._bottomNumbers.addView(numText);

			this._txtNumTextList[i] = numText;
		}
	}

	public void setTabNumValue(int tabIndex, int value)
	{
		if (this._txtNumTextList.length > tabIndex && tabIndex > -1)
		{
			TextView txtView = this._txtNumTextList[tabIndex];
			if (value > 0)
			{
				txtView.setVisibility(View.VISIBLE);
				txtView.setText(String.valueOf(value));
			}
			else
			{
				txtView.setVisibility(View.INVISIBLE);
				txtView.setText("0");
			}
		}
	}

	private ProgressDialog _progressDialog;

	private void showWaite(String message)
	{
		this._progressDialog.setMax(100);
		this._progressDialog.setMessage(message);
		this._progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this._progressDialog.setCancelable(false);
		this._progressDialog.show();
	}

	private void hideWaite()
	{
		this._progressDialog.dismiss();
	}

	private void initView()
	{
		this._tabRgMenu = (RadioGroup) findViewById(R.id.tab_rg_menu);

		// this._mainFrame = (RelativeLayout) this.findViewById(R.id.mainFrame);
		// this._mainMenu = (RelativeLayout) this.findViewById(R.id.mainMenu);

		this._viewPager = (MainViewPager) this.findViewById(R.id.viewPager);

		List<MainMenuData> mainMenuDataList = ConfigDAL.getMainMenuDataList();

		int titleBackGroundColor = -1;
		
		
		//当底部菜单只有一个按钮时，不显示底部的按钮
		if(mainMenuDataList.size() <= 1)
		{
			this._bottomButtons.setVisibility(View.INVISIBLE);
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)this._viewPager.getLayoutParams();
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		}
		
		for (int i = 0, j = mainMenuDataList.size(); i < j; i++)
		{
			MainMenuData mainMenuData = mainMenuDataList.get(i);

			if (titleBackGroundColor == -1)
				titleBackGroundColor = mainMenuData.getBackgroundColor();
			String configId = mainMenuData.getConfigId();
			String logo = mainMenuData.getLogo(ServerOrLocal.local);
			String title = "", titlePic = "";
			float titleFontSize = 24;
			int titleFontColor = mainMenuData.getTitleColor();
			TitleAlign titleAlign = mainMenuData.getTitleAlign();
			boolean titleVisible = true;
			switch (mainMenuData.getTitleType())
			{
				case picture:
					titlePic = mainMenuData.getTitle(ServerOrLocal.local);
					break;
				
				case string:
					title = mainMenuData.getTitle(ServerOrLocal.local);
					titleFontSize = mainMenuData.getTitleSize();
					break;
					
				case notitle:
					titleVisible = false;
					break;
			}

			if (!configId.endsWith("_more"))
			{
				RadioButton rb = newRadioButton(mainMenuData);
				this._tabRgMenu.addView(rb);
				this._tabRgMenu.setBackgroundColor(titleBackGroundColor);
				if (i == 0)
				{
					rb.setChecked(true);
				}
				

				TabWebBrowser tabWebBrowser = new TabWebBrowser();

				String url = mainMenuData.getUrl();

				tabWebBrowser.setURL(url);
				tabWebBrowser.setLogo(logo);
				tabWebBrowser.setChangeTitleWithHTML(false);
				tabWebBrowser.setTitleVisible(titleVisible);
				tabWebBrowser.setTitle(title, titleFontSize, titleFontColor, titleAlign, titlePic);
				tabWebBrowser.setTitleBackgroundColor(mainMenuData.getTitleBackgroundColor());
				tabWebBrowser.setBackgroundColor(mainMenuData.getBackgroundColor());
				this._fragments.add(tabWebBrowser);
			}
			// else// more
			// {
			// RadioButton tb_rd_more = newRadioButton(mainMenuData);
			// this._tabRg.addView(tb_rd_more);
			//
			// TabMore tabMore = new TabMore();
			// tabMore.setLogo(logo);
			// tabMore.setTitle(title, titleFontSize,titleFontColor, titleAlign,
			// titlePic);
			// tabMore.setTitleBackgroundColor(mainMenuData.getTitleBackgroundColor());
			// tabMore.setBackgroundColor(mainMenuData.getBackgroundColor());
			//
			// this._fragments.add(tabMore);
			// }
		}

		this._txtNumTextList = new TextView[this._fragments.size()];
		this.initNumTexts();

		// this._mainFrameParams = (LinearLayout.LayoutParams)
		// this._mainMenu.getLayoutParams();// .getLayoutParams();;
		//
		// this._mainFrameParams.width = this._screenWidth;//
		// 将menu的宽度设置为屏幕宽度减去menuPadding
		//
		// this._mainFrameLeftEdge = this._mainFrameMinShowWidth -
		// this._mainFrameParams.width;// 左边缘的值赋值为最小可见宽度加上宽度的负数
		//
		// this._mainFrame.getLayoutParams().width = this._screenWidth;

		this._viewPagerAdapter = new AdapterFragmentPager(this.getSupportFragmentManager(), this._fragments, this._viewPager, this._tabRgMenu);
		this._viewPager.setAdapter(this._viewPagerAdapter);
		this._viewPagerAdapter.setCurrentTab(0);

		// 向左滑，出现菜单
		// this._viewPager.setOnTouchListener(new OnTouchListener()
		// {
		// /**
		// * 滚动显示和隐藏mainFrame时，手指滑动需要达到的速度。
		// */
		// public static final int SNAP_VELOCITY = 200;
		// private float xDown;
		// private float xMove;
		// private float xUp;
		//
		// private boolean isMainFrameFullScreen = true;// 当前mainFrame时否全屏
		// private VelocityTracker moveVelocity = null;
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event)
		// {
		// if (_viewPagerAdapter.isLastPage())
		// {
		// createVelocityTracker(event);
		// switch (event.getAction())
		// {
		// case MotionEvent.ACTION_DOWN:
		// this.xDown = event.getRawX();
		// break;
		// case MotionEvent.ACTION_MOVE:
		// // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整mainFrame的leftMargin值
		// this.xMove = event.getRawX();
		// int distanceX = (int) (this.xMove - this.xDown);
		// if (this.isMainFrameFullScreen)
		// {
		// _mainFrameParams.leftMargin = distanceX;
		// }
		// else
		// {
		// _mainFrameParams.leftMargin = _mainFrameLeftEdge + distanceX;
		// }
		// if (_mainFrameParams.leftMargin < _mainFrameLeftEdge)
		// {
		// _mainFrameParams.leftMargin = _mainFrameLeftEdge;
		// }
		// else
		// {
		// if (_mainFrameParams.leftMargin > _mainFrameRightEdge)
		// {
		// _mainFrameParams.leftMargin = _mainFrameRightEdge;
		// }
		// }
		// _mainFrame.setLayoutParams(_mainFrameParams);
		// if (!this.isMainFrameFullScreen)// 当mainFrame非全屏时，viewpager里的内容不要动
		// return true;
		// else if (distanceX < 0)// 左滑时，viewpager里的内容不要动
		// return true;
		// break;
		// case MotionEvent.ACTION_UP:
		// this.xUp = event.getRawX();// 手指抬起时，进行判断当前手势的意图，从而决定是滚动到mainFrame界面
		// if (this.wantToShowMainFrame())
		// {
		// if (this.shouldScrollToMainFrame())
		// {
		// this.scrollToMainFrame();
		// }
		// else
		// {
		// this.scrollToMenu();
		// }
		// }
		// else
		// {
		// if (this.wantToShowMemu())
		// {
		// if (this.shouldScrollToMenu())
		// {
		// this.scrollToMenu();
		// }
		// else
		// {
		// this.scrollToMainFrame();
		// }
		// }
		// }
		// recycleVelocityTracker();
		// break;
		// }
		// }
		// return false;
		// }
		//
		// private boolean wantToShowMainFrame()
		// {
		// return this.xUp > this.xDown && !this.isMainFrameFullScreen;
		// }
		//
		// private boolean shouldScrollToMainFrame()
		// {
		// return this.xUp - this.xDown > _screenWidth / 2 ||
		// getScrollVelocity() > SNAP_VELOCITY;
		// }
		//
		// /**
		// * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
		// *
		// * @param event
		// * content界面的滑动事件
		// */
		// private void createVelocityTracker(MotionEvent event)
		// {
		// if (moveVelocity == null)
		// {
		// moveVelocity = VelocityTracker.obtain();
		// }
		// moveVelocity.addMovement(event);
		// }
		//
		// /**
		// * 获取手指在content界面滑动的速度。
		// *
		// * @return 滑动速度，以每秒钟移动了多少像素值为单位。
		// */
		// private int getScrollVelocity()
		// {
		// moveVelocity.computeCurrentVelocity(1000);
		// int velocity = (int) moveVelocity.getXVelocity();
		// return Math.abs(velocity);
		// }
		//
		// /**
		// * 将屏幕滚动到mainFrame界面，滚动速度设定为30.
		// */
		// private void scrollToMainFrame()
		// {
		// new ScrollTask().execute(30);
		// }
		//
		// /**
		// * 将屏幕滚动到menu界面，滚动速度设定为-30.
		// */
		// private void scrollToMenu()
		// {
		// new ScrollTask().execute(-30);
		// }
		//
		// /**
		// * 判断当前手势的意图是不是想显示menu。如果手指移动的距离是负数，且当前mainFrame是可见的，
		// * 则认为当前手势是想要显示menu。
		// *
		// * @return 当前手势想显示menu返回true，否则返回false。
		// */
		// private boolean wantToShowMemu()
		// {
		// return xUp - xDown < 0 && this.isMainFrameFullScreen;
		// }
		//
		// /**
		// * 判断是否应该滚动将menu展示出来。如果手指移动距离加上_mainFrameMinShowWidth大于屏幕的1/2，
		// * 或者手指移动速度大于SNAP_VELOCITY， 就认为应该滚动将menu展示出来。
		// *
		// * @return 如果应该滚动将menu展示出来返回true，否则返回false。
		// */
		// private boolean shouldScrollToMenu()
		// {
		// return this.xDown - this.xUp + _mainFrameMinShowWidth > _screenWidth
		// / 2 || getScrollVelocity() > SNAP_VELOCITY;
		// }
		//
		// /**
		// * 回收VelocityTracker对象。
		// */
		// private void recycleVelocityTracker()
		// {
		// moveVelocity.recycle();
		// moveVelocity = null;
		// }
		//
		// class ScrollTask extends AsyncTask<Integer, Integer, Integer>
		// {
		// @Override
		// protected Integer doInBackground(Integer... speed)
		// {
		// int leftMargin = _mainFrameParams.leftMargin;
		// // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
		// while (true)
		// {
		// leftMargin = leftMargin + speed[0];
		// if (leftMargin > _mainFrameRightEdge)
		// {
		// leftMargin = _mainFrameRightEdge;
		// break;
		// }
		// if (leftMargin < _mainFrameLeftEdge)
		// {
		// leftMargin = _mainFrameLeftEdge;
		// break;
		// }
		// publishProgress(leftMargin);
		// // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
		// sleep(20);
		// }
		// if (speed[0] > 0)
		// {
		// isMainFrameFullScreen = true;
		// }
		// else
		// {
		// isMainFrameFullScreen = false;
		// }
		// return leftMargin;
		// }
		//
		// @Override
		// protected void onProgressUpdate(Integer... leftMargin)
		// {
		// _mainFrameParams.leftMargin = leftMargin[0];
		// _mainMenu.setLayoutParams(_mainFrameParams);
		// }
		//
		// @Override
		// protected void onPostExecute(Integer leftMargin)
		// {
		// _mainFrameParams.leftMargin = leftMargin;
		// _mainMenu.setLayoutParams(_mainFrameParams);
		// }
		//
		// /**
		// * 使当前线程睡眠指定的毫秒数。
		// *
		// * @param millis
		// * 指定当前线程睡眠多久，以毫秒为单位
		// */
		// private void sleep(long millis)
		// {
		// try
		// {
		// Thread.sleep(millis);
		// }
		// catch (InterruptedException e)
		// {
		// e.printStackTrace();
		// }
		// }
		// }
		// });
	}

	// private MessageReceiver mMessageReceiver;
	// public static final String MESSAGE_RECEIVED_ACTION =
	// "apk.platform.youjish.com.MESSAGE_RECEIVED_ACTION";
	// public static final String KEY_TITLE = "title";
	// public static final String KEY_MESSAGE = "message";
	// public static final String KEY_EXTRAS = "extras";
	//
	// public void registerMessageReceiver()
	// {
	// mMessageReceiver = new MessageReceiver();
	// IntentFilter filter = new IntentFilter();
	// filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
	// filter.addAction(MESSAGE_RECEIVED_ACTION);
	// this.registerReceiver(mMessageReceiver, filter);
	// }
	//
	// public class MessageReceiver extends BroadcastReceiver
	// {
	// @Override
	// public void onReceive(Context context, Intent intent)
	// {
	// if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction()))
	// {
	// String messge = intent.getStringExtra(KEY_MESSAGE);
	// String extras = intent.getStringExtra(KEY_EXTRAS);
	//
	// String url = "";
	// try
	// {
	// JSONObject jobject = new JSONObject(extras);
	// url = jobject.getString("url");
	// }
	// catch(Exception e)
	// {}
	//
	//
	//
	// MessageTypeData mtd = new MessageTypeData();
	// mtd.setMessageTypeId(MessageTypeData.PUSH);
	// mtd.setMessageTypeTitle("推送消息");
	//
	// MessageData md = new MessageData();
	// md.setMessageTypeId(mtd.getMessageTypeId());
	// md.setMessageTitle("消息");
	// md.setMessageText(messge);
	// md.setMessageData(url);
	// md.setMessageTime(new Date());
	//
	// MessageDAL.addMessageData(mtd, md);
	//
	// // MessageTypeData messageType = new MessageTypeData();
	// // messageType.setLastMessageText(messge);
	// // messageType.setMessageTypeId("3");
	// // messageType.setMessageTypeTitle("推送消息");
	// // messageType.setLastMessageTime(new Date());
	// // messageTypeList.add(messageType);
	// //
	// //
	// // MessageListActivity.this._adapterMessageList.notifyDataSetChanged();
	//
	// }
	// }
	// }

	private long _exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if ((System.currentTimeMillis() - this._exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				this._exitTime = System.currentTimeMillis();
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

	// 键盘事件
	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
		if(AppProjectType.getCurrentProjectType() == AppProjectType.IData95)
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN)
			{
				switch (event.getKeyCode())
				{
					case KeyEvent.KEYCODE_F7:// F1
						this._viewPagerAdapter.setCurrentTab(0);
						return true;
					case KeyEvent.KEYCODE_F8:// F2
						this._viewPagerAdapter.setCurrentTab(1);
						return true;
					case KeyEvent.KEYCODE_F5:// F3
						this._viewPagerAdapter.setCurrentTab(2);
						return true;
					case KeyEvent.KEYCODE_F6:// F4
						this._viewPagerAdapter.setCurrentTab(3);
						return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void setNumText(int numValue)
	{

	}

	@Override
	public Context getContext()
	{
		return this;
	}

	@Override
	public MainActivity getMainActivity()
	{
		return this;
	}
}
