package apk.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import apk.R;
import apk.model.WyUnitData;

public class AdapterUnitSelect extends BaseAdapter
{
	
	private Activity _activity;
	private List<WyUnitData> _unitDataList;
	private LayoutInflater _layoutInflater;
	
	public AdapterUnitSelect(Activity context, List<WyUnitData> unitDataList)
	{
		this._activity = context;
		this._unitDataList = unitDataList;
		this._layoutInflater = LayoutInflater.from(this._activity);
	}
	
	
	@Override
	public int getCount()
	{
		return this._unitDataList.size();
	}
	
	@Override
	public Object getItem(int arg0)
	{
		return this._unitDataList.get(arg0);
	}
	
	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
		{
			convertView = this._layoutInflater.inflate(R.layout.listviewitem_unit, null);
		}
		
		TextView labText = (TextView) convertView.findViewById(R.id.labText);
		WyUnitData unitData = this._unitDataList.get(position);
		labText.setText(unitData.getTitle());
		
		ImageView imgUnit = (ImageView) convertView.findViewById(R.id.imgUnit);
		if(unitData.getChildCount() > 0)
		{
			imgUnit.setVisibility(View.VISIBLE);
		}
		else
		{
			imgUnit.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
}
