package apk.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import apk.R;
import apk.common.DateHelper;
import apk.model.MessageData;

public class AdapterMessageList extends BaseAdapter
{
	private Context _context;
	private List<MessageData> _messageList;
	private LayoutInflater _layoutInflater;
	
	public AdapterMessageList(Context context)
	{
		this._context = context;
		this._layoutInflater = LayoutInflater.from(this._context);
	}
	
	public void setDataList(List<MessageData> messageList)
	{
		this._messageList = messageList;
	}

	@Override
	public int getCount()
	{
		return this._messageList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return this._messageList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
		{
			convertView = this._layoutInflater.inflate(R.layout.listviewitem_message, null);
			
//			convertView.setOnTouchListener(new OnTouchListener()
//			{
//				@Override
//				public boolean onTouch(View v, MotionEvent event)
//				{
//					return false;
//				}
//			});
		}
		
		MessageData message = this._messageList.get(position);
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
		TextView labMessageTitle = (TextView) convertView.findViewById(R.id.labMessageTitle);
		TextView labDateTime = (TextView) convertView.findViewById(R.id.labDateTime);
		TextView labMessageText = (TextView) convertView.findViewById(R.id.labMessageText);
		
		imageView.setBackgroundResource(R.drawable.tab_weixin_pressed);
		labMessageTitle.setText(message.getMessageTitle());
		labDateTime.setText(DateHelper.FormatDate(message.getMessageTime(), "yyyy-MM-dd HH:mm:ss"));
		labMessageText.setText(message.getMessageText());
		
		return convertView;
	}

}
