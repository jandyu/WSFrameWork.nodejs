package apk.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import apk.R;
import apk.adapter.AdapterUnitSelect;
import apk.bll.UnitService;
import apk.bll.UnitService.OnGetUnitDataListByPid;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.dal.ConfigDAL;
import apk.model.MainMenuData;
import apk.model.WyUnitData;

public class UnitSelectActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private RelativeLayout _mainFrame;
	private ListView _listView0;
	private Button _btnBackUnit;
	private long _currParentId = 0;//父节点的父节点编号
	private long _currPid = 0;//父节点编号
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_unit_select);
		
		this._mainFrame = (RelativeLayout)this.findViewById(R.id.mainFrame);
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
		this._btnBackUnit = (Button) this.findViewById(R.id.btnBackUnit);
		this._btnBackUnit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(_currPid == 0)
				{
					Intent resultValue = new Intent();
					
					setResult(RESULT_CANCELED, resultValue);
					
					UnitSelectActivity.this.finish();
				}
				else
				{
					fillListData(_currParentId);
				}
			}
		});
		
		this._listView0 = (ListView) this.findViewById(R.id.listView0);
		
		this.fillListData(0);
	}
	
	
	public void fillListData(final long parentId)
	{
		UnitService unitServer = new UnitService(this);
		unitServer.setOnGetUnitDataListByPid(new OnGetUnitDataListByPid()
		{
			@Override
			public void OnGetUnitDataListByPidSuccess(List<WyUnitData> unitDataList)
			{
				AdapterUnitSelect adapter0 = new AdapterUnitSelect(UnitSelectActivity.this, unitDataList);
				UnitSelectActivity.this._listView0.setAdapter(adapter0);
				
				if(unitDataList.size() > 0)
				{
					WyUnitData unitData = unitDataList.get(0);
					_currParentId = unitData.getParentId();
					_currPid = unitData.getPid();
					_btnBackUnit.setText("上一步 " + unitData.getParentTitle());
				}
				else
				{
					//_currParentId = 0;
				}
				
				if(parentId == 0)
				{
					_btnBackUnit.setText("返回 小区房号选择");
				}
			}
		});
		unitServer.beginGetUnitDataListByPid(this, parentId);
		
		this._listView0.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3)
			{
				WyUnitData unitData = (WyUnitData)adapter.getItemAtPosition(position);
				if(unitData.getChildCount() > 0)
				{
					//有子节点，打开本节点的子节点列表
					fillListData(unitData.getIid());
				}
				else
				{
					//选中子节点，并返回页面
					
					Intent resultValue = new Intent();
					resultValue.putExtra("id", unitData.getIid());
					resultValue.putExtra("text", unitData.getUnitTitle());
					
					setResult(RESULT_OK, resultValue);
					
					UnitSelectActivity.this.finish();
				}
			}
		});
	}
}
