package apk.customerview;
//package apk.platform.youjish.com.customerview;
//
//import java.util.ArrayList;
//import java.util.List;
//import mqxd.apk.R;
//import apk.platform.youjish.com.activity.InformationCollectActivity;
//import apk.platform.youjish.com.activity.LoginActivity;
//import apk.platform.youjish.com.activity.MessageTypeListActivity;
//import apk.platform.youjish.com.activity.UserInfoActivity;
//import apk.platform.youjish.com.barcode.CaptureActivity;
//import apk.platform.youjish.com.model.Protocol;
//import apk.platform.youjish.com.model.MainMenuData.TitleAlign;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//public class TabMore extends Fragment
//{
//	private Context _context;
//	private LayoutInflater _layoutInflater;
//	private View _thisView;
//	private TitleBar _titleBar;
//	private String _logo;
//	private String _title;
//	private float _titleFontSize;
//	private int _titleFontColor;
//	private TitleAlign _titleAlign;
//	private String _titlePic;
//	private int _titleBackgroundColor;
//	private int _backgroundColor;
//	private ListViewTabMore _listView;
//	
//	private List<MoreItem> _moreItemList;
//	
//	private class MoreItem
//	{
//		public int ImageId;
//		public MenuIdEnum ItemId;
//		public String ItemTitle;
//		public MoreItem(int imageId, MenuIdEnum itemId, String itemTitle)
//		{
//			this.ImageId = imageId;
//			this.ItemId = itemId;
//			this.ItemTitle = itemTitle;
//		}
//	}
//	
//	private enum MenuIdEnum
//	{
//		login,
//		userInfo,
//		scan,
//		messageList,
//		messageShare,
//		pay,
//		infoColl
//	}
//	
//	private class AdapterMore extends BaseAdapter
//	{
//		@Override
//		public int getCount()
//		{
//			return TabMore.this._moreItemList.size();
//		}
//
//		@Override
//		public Object getItem(int position)
//		{
//			return TabMore.this._moreItemList.get(position);
//		}
//
//		@Override
//		public long getItemId(int position)
//		{
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent)
//		{
//			if(convertView == null)
//			{
//				convertView = TabMore.this._layoutInflater.inflate(R.layout.listviewitem_menu_more, null);
//			}
//			
//			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
//			TextView labText = (TextView) convertView.findViewById(R.id.labText);
//			
//			
//			MoreItem moreItem = TabMore.this._moreItemList.get(position);
//			convertView.setTag(moreItem);
//			
//			imageView.setBackgroundResource(moreItem.ImageId);
//			labText.setText(moreItem.ItemTitle);
//			
//			
//			convertView.setOnClickListener(new OnClickListener()
//			{
//				@Override
//				public void onClick(View v)
//				{
//					MoreItem moreItem = (MoreItem) v.getTag();
//					Intent intent;
//					switch(moreItem.ItemId)
//					{
//						case login://登录
//							intent = new Intent(TabMore.this._context, LoginActivity.class);
//							TabMore.this.startActivity(intent);
//							break;
//						case userInfo://用户信息
//							intent = new Intent(TabMore.this._context, UserInfoActivity.class);
//							TabMore.this.startActivity(intent);
//							break;
//						case messageList://消息浏览
//							intent = new Intent(TabMore.this._context, MessageTypeListActivity.class);
//							TabMore.this.startActivity(intent);
//							break;
//						case infoColl://信息采集
//							intent = new Intent(TabMore.this._context, InformationCollectActivity.class);
//							TabMore.this.startActivity(intent);
//							break;
//						case messageShare://
//							break;
//						case scan://扫一扫
//							intent = new Intent(TabMore.this._context, CaptureActivity.class);
//							TabMore.this.startActivityForResult(intent, 11);
//							break;
//						case pay://支付
//							break;
//					}
//				}
//			});
//			
//			return convertView;
//		}
//	}
//	
//	//此方法用于网页中请求打开的native页面返回值时调用。
//	//本来应该写到具体的Interface类里的，但是Interface里无法注册页面返回方法。
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		Protocol protocol = new Protocol();
//		protocol.setContext(_context);
//		protocol.setFragment(this);
//		protocol.setCurrentWebView(null);
//		
//		
//		if(requestCode == 11)
//		{
//			if(data != null)
//			{
//				String url = data.getStringExtra("value");
//				protocol.getProtocol(url);
//			}
//		}
//	}
//
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		
//		this._context = this.getActivity();
//		
//		this._layoutInflater = LayoutInflater.from(this._context);
//		
//		this._moreItemList = new ArrayList<MoreItem>();
//		this._moreItemList.add(new MoreItem(R.drawable.tab_weixin_pressed, MenuIdEnum.login, "登录"));
//		this._moreItemList.add(new MoreItem(R.drawable.tab_weixin_pressed, MenuIdEnum.userInfo, "用户信息"));
//		this._moreItemList.add(new MoreItem(R.drawable.tab_weixin_pressed, MenuIdEnum.scan, "扫一扫"));
//		this._moreItemList.add(new MoreItem(R.drawable.tab_weixin_pressed, MenuIdEnum.messageList, "消息浏览"));
//		this._moreItemList.add(new MoreItem(R.drawable.tab_weixin_pressed, MenuIdEnum.infoColl, "消息采集"));
//		this._moreItemList.add(new MoreItem(R.drawable.tab_weixin_pressed, MenuIdEnum.messageShare, "信息分享"));
//		this._moreItemList.add(new MoreItem(R.drawable.tab_weixin_pressed, MenuIdEnum.pay, "在线支付"));
//	}
//	
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//	{
//		this._thisView = inflater.inflate(R.layout.tab_more, container, false);
//		this._titleBar = (TitleBar) this._thisView.findViewById(R.id.titleBar);
//		this._titleBar.setLogo(this._logo);
//		this._titleBar.setTitle(this._title, this._titleFontSize, this._titleFontColor, this._titleAlign, this._titlePic);
//		this._titleBar.setBackgroundColor(this._titleBackgroundColor);
//		
//		this._listView = (ListViewTabMore) this._thisView.findViewById(R.id.listView);
//		this._listView.setAdapter(new AdapterMore());
//		
//		this._thisView.setBackgroundColor(this._backgroundColor);
//		
//		return this._thisView;
//	}
//	
//	public void setLogo(String picName)
//	{
//		this._logo = picName;
//	}
//	public void setTitle(String title, float fontSize, int fontColor, TitleAlign align, String titlePic)
//	{
//		this._title = title;
//		this._titleFontSize = fontSize;
//		this._titleFontColor = fontColor;
//		this._titleAlign = align;
//		this._titlePic = titlePic;
//	}
//	
//	public void setTitleBackgroundColor(int color)
//	{
//		this._titleBackgroundColor = color;
//	}
//	
//	public void setBackgroundColor(int color)
//	{
//		this._backgroundColor = color;
//	}
//
//}
