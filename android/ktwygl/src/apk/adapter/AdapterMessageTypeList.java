package apk.adapter;
//package apk.platform.youjish.com.adapter;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import mqxd.apk.R;
//import apk.platform.youjish.com.model.MessageTypeData;
//
//public class AdapterMessageTypeList extends BaseAdapter
//{
//	private Context _context;
//	private List<MessageTypeData> _messageTypeList;
//	private LayoutInflater _layoutInflater;
//	
//	public AdapterMessageTypeList(Context context, List<MessageTypeData> messageTypeList)
//	{
//		this._context = context;
//		this._messageTypeList = messageTypeList;
//		this._layoutInflater = LayoutInflater.from(this._context);
//	}
//
//	@Override
//	public int getCount()
//	{
//		return this._messageTypeList.size();
//	}
//
//	@Override
//	public Object getItem(int position)
//	{
//		return this._messageTypeList.get(position);
//	}
//
//	@Override
//	public long getItemId(int position)
//	{
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent)
//	{
//		if(convertView == null)
//		{
//			convertView = this._layoutInflater.inflate(R.layout.listviewitem_message, null);
//		}
//		
//		MessageTypeData messageType = this._messageTypeList.get(position);
//		
//		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
//		TextView labMessageTitle = (TextView) convertView.findViewById(R.id.labMessageTitle);
//		TextView labDateTime = (TextView) convertView.findViewById(R.id.labDateTime);
//		TextView labMessageText = (TextView) convertView.findViewById(R.id.labMessageText);
//		
//		imageView.setBackgroundResource(R.drawable.tab_weixin_pressed);
//		labMessageTitle.setText(messageType.getMessageTypeTitle());
//		labDateTime.setText(messageType.getLastMessageTimeString());
//		labMessageText.setText(messageType.getLastMessageText());
//		
//		return convertView;
//	}
//
//}
