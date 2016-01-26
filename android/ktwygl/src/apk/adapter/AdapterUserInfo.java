package apk.adapter;
//package apk.platform.youjish.com.adapter;
//
//import java.util.Calendar;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import android.app.Activity;
//import android.app.DatePickerDialog;
//import android.app.DatePickerDialog.OnDateSetListener;
//import android.content.Intent;
//import android.text.InputType;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.DatePicker;
//import android.widget.TextView;
//import mqxd.apk.R;
//import apk.platform.youjish.com.activity.UserInfoActivity;
//import apk.platform.youjish.com.activity.UserInfoEditActivity;
//import apk.platform.youjish.com.activity.UserInfoUnitEditActivity;
//import apk.platform.youjish.com.common.DateHelper;
//import apk.platform.youjish.com.dal.ConfigDAL;
//import apk.platform.youjish.com.dal.WyUnitDAL;
//import apk.platform.youjish.com.model.UserInfoData;
//
//public class AdapterUserInfo extends BaseAdapter
//{
//	private UserInfoData _userInfo;
//	private Activity _activity;
//	private ViewHolder _currentEditViewHolder;
//	private JSONArray _userExtendInfoDefine;
//	private LayoutInflater _layoutInflater;
//	private int _userBaseInfoLength = 8;
//	private float _baseFontSize = 24;
//	
//	
//	public AdapterUserInfo(Activity context, UserInfoData userInfo)
//	{
//		this._activity = context;
//		this._userInfo = userInfo;
//		this._layoutInflater = LayoutInflater.from(this._activity);
//		this._userExtendInfoDefine = ConfigDAL.getUserExtendInfoDefine();
//		this._baseFontSize = ConfigDAL.getBaseFontSize();
//	}
//
//	@Override
//	public int getCount()
//	{
//		int keyLen = (this._userExtendInfoDefine != null ? this._userExtendInfoDefine.length() : 0);
//		return this._userBaseInfoLength + keyLen + 2;//2个空行6，9
//	}
//
//	@Override
//	public Object getItem(int position)
//	{
//		return null;
//	}
//
//	@Override
//	public long getItemId(int position)
//	{
//		return position;
//	}
//	
//	@Override  
//    public boolean isEnabled(int position) 
//	{  
//		switch(position)
//		{
//			case 6:
//			case 9:
//				return false;
//		}
//        return super.isEnabled(position);  
//    }  
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent)
//	{
//		
//		switch(position)
//		{
//			case 6:
//			case 9:
//				if(convertView == null)
//				{
//					TextView t1 = new TextView(this._activity);
//					t1.setHeight(30);
//					return t1;
//				}
//				return convertView;
//		}
//		
//		
//		
//		ViewHolder holder = null;
//		if(convertView == null)
//		{
//			holder = new ViewHolder();
//			convertView = this._layoutInflater.inflate(R.layout.listviewitem_userinfo, null);
//			
//			TextView labTitle = (TextView) convertView.findViewById(R.id.labTitle);
//			TextView labText = (TextView) convertView.findViewById(R.id.labText);
//			//TextView labToRight = (TextView) convertView.findViewById(R.id.labToRight);
//			
//			labTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, this._baseFontSize);
//			labText.setTextSize(TypedValue.COMPLEX_UNIT_SP, this._baseFontSize);
//			//labToRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, this._baseFontSize);
//			
//			holder.labTitle = labTitle;
//			holder.labText = labText;
//			holder.position = position;
//			
//			convertView.setTag(holder);
//		}
//		else
//		{
//			holder = (ViewHolder) convertView.getTag();
//			//holder.labText.setTag(position);
//		}
//		
//		switch(position)
//		{
//			case 0://姓名
//				holder.labTitle.setText("姓名:");
//				holder.labText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//				holder.labText.setText(this._userInfo.getName());
//				break;
//			case 1://昵称
//				holder.labTitle.setText("昵称:");
//				holder.labText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//				holder.labText.setText(this._userInfo.getSign_name());
//				break;
//			case 2://签名
//				holder.labTitle.setText("签名:");
//				holder.labText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
//				holder.labText.setText(this._userInfo.getNick_name());
//				break;
//			case 3://生日
//				holder.labTitle.setText("生日:");
//				holder.labText.setInputType(InputType.TYPE_CLASS_DATETIME);
//				holder.labText.setText(this._userInfo.getBirthday());
//				break;
//			case 4://手机
//				holder.labTitle.setText("手机:");
//				holder.labText.setInputType(InputType.TYPE_CLASS_NUMBER);
//				holder.labText.setText(this._userInfo.getPhone_no());
//				break;
//			case 5://密码
//				holder.labTitle.setText("密码:");
//				holder.labText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//				holder.labText.setText(this._userInfo.getPasswd());
//				break;
//				
//				
//			case 7://小区
//				holder.labTitle.setText("小区");
//				holder.labText.setInputType(InputType.TYPE_CLASS_TEXT);
//				holder.value = String.valueOf(this._userInfo.getUnit_id());
//				holder.labText.setText(WyUnitDAL.getTitle(this._userInfo.getUnit_id()));
//				break;
//			case 8://楼层
//				holder.labTitle.setText("楼层");
//				holder.labText.setInputType(InputType.TYPE_CLASS_TEXT);
//				holder.labText.setText(this._userInfo.getFloor());
//				break;
//				
//				
//			default://扩展信息
//				try
//				{
//					JSONObject userExtendInfoDefine = this._userExtendInfoDefine.getJSONObject(position - this._userBaseInfoLength - 2);//2个空行：6，9
//					String key = userExtendInfoDefine.getString("key");
//					String title = userExtendInfoDefine.getString("title");
//					String type = userExtendInfoDefine.getString("type");
//					holder.labTitle.setText(title + ":");
//					
//					if("number".equals(type))
//						holder.labText.setInputType(InputType.TYPE_CLASS_NUMBER);
//					else
//						holder.labText.setInputType(InputType.TYPE_CLASS_TEXT);
//					
//					//String value = this._userExtendInfo.get(key);
//					
//					String value = this._userInfo.getMemo(key);
//					
//					
//					holder.labText.setText(value);
//				}
//				catch (JSONException e)
//				{
//					e.printStackTrace();
//				}
//				break;
//		}
//		
//		convertView.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				_currentEditViewHolder = (ViewHolder) v.getTag();
//
//				Intent intent;
//				switch(_currentEditViewHolder.position)
//				{
//					case 3://生日
//						Calendar cal = Calendar.getInstance();
//						DatePickerDialog datePickerDialog = new DatePickerDialog(AdapterUserInfo.this._activity, new OnDateSetListener()
//						{
//							@Override
//							public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
//							{
//								Calendar cal1 = Calendar.getInstance();
//								cal1.set(Calendar.YEAR, year);
//								cal1.set(Calendar.MONTH, monthOfYear);
//								cal1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//								
//								String birthday = DateHelper.FormatDate(cal1.getTime(), "yyyy-MM-dd");
//								
//								_currentEditViewHolder.labText.setText(birthday);
//								cacheData(birthday, _currentEditViewHolder.position);
//							}
//						}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//						datePickerDialog.show();
//						break;
//					case 7://小区
//						intent = new Intent(AdapterUserInfo.this._activity, UserInfoUnitEditActivity.class);
//						
//						intent.putExtra("title", _currentEditViewHolder.labTitle.getText().toString());
//						intent.putExtra("value", _currentEditViewHolder.value);
//						intent.putExtra("text", _currentEditViewHolder.labText.getText().toString());
//						intent.putExtra("inputType", _currentEditViewHolder.labText.getInputType());
//						
//						AdapterUserInfo.this._activity.startActivityForResult(intent, UserInfoActivity.RESULT_USERINFO_AREA);
//						
//						AdapterUserInfo.this._activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out); 
//						break;
//					default:
//						intent = new Intent(AdapterUserInfo.this._activity, UserInfoEditActivity.class);
//						
//						intent.putExtra("title", _currentEditViewHolder.labTitle.getText().toString());
//						//intent.putExtra("value", _currentEditViewHolder.value);
//						intent.putExtra("text", _currentEditViewHolder.labText.getText().toString());
//						intent.putExtra("inputType", _currentEditViewHolder.labText.getInputType());
//						
//						AdapterUserInfo.this._activity.startActivityForResult(intent, UserInfoActivity.RESULT_USERINFO_DEFAULT);
//						
//						AdapterUserInfo.this._activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out); 
//						break;
//				}
//			}
//		});
//		
//		return convertView;
//	}
//	
//	public void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		if(requestCode == UserInfoActivity.RESULT_USERINFO_DEFAULT)
//		{
//			if(resultCode == Activity.RESULT_OK)
//			{
//				String value = data.getStringExtra("value");
//				
//				_currentEditViewHolder.labText.setText(value);
//				cacheData(value, _currentEditViewHolder.position);
//			}
//		}
//		else if(requestCode == UserInfoActivity.RESULT_USERINFO_AREA)
//		{
//			if(resultCode == Activity.RESULT_OK)
//			{
//				long value = data.getLongExtra("value", -1);
//				
//				_currentEditViewHolder.labText.setText(WyUnitDAL.getTitle(value));
//				cacheData(String.valueOf(value), _currentEditViewHolder.position);
//			}
//		}
//	}
//	
//	void cacheData(String str, int position)
//	{
//		switch(position)
//		{
//			case 0://姓名
//				this._userInfo.setName(str);
//				break;
//			case 1://昵称
//				this._userInfo.setSign_name(str);
//				break;
//			case 2://签名
//				this._userInfo.setNick_name(str);
//				break;
//			case 3://生日
//				this._userInfo.setBirthday(str);
//				break;
//			case 4://手机
//				this._userInfo.setPhone_no(str);
//				break;
//			case 5://密码
//				this._userInfo.setPasswd(str);
//				break;
//			case 7://小区
//				this._userInfo.setUnit_id(Integer.parseInt(str, 10));
//				break;
//			case 8://楼层
//				this._userInfo.setFloor(str);
//				break;
//			default://扩展信息
//				try
//				{
//					JSONObject userExtendInfoDefine = this._userExtendInfoDefine.getJSONObject(position - this._userBaseInfoLength - 2);//2个空行：6，9
//					String key = userExtendInfoDefine.getString("key");
//					//this._userExtendInfo.put(key, str);
//					this._userInfo.setMemo(key, str);
//				}
//				catch(Exception e)
//				{
//					e.printStackTrace();
//				}
//				break;
//		}
//	}
//	
//	
//	private final class ViewHolder
//	{
//		private TextView labTitle;
//		private TextView labText;
//		private String value;
//		private int position;
//	}
//	
//
//
//	
//	
//}
