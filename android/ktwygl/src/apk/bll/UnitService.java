package apk.bll;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import apk.bll.param.ParamSelect;
import apk.bll.param.SelectResponseData;
import apk.bll.param.ParamSelect.AndOr;
import apk.bll.param.ParamSelect.Where;
import apk.common.JSONReader;
import apk.model.WyUnitData;
import apk.model.framework.JsonData;
import apk.net.BaseAsyncNet;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;

public class UnitService extends BaseService
{
	
	public interface OnGetUnitDataListByPid
	{
		public void OnGetUnitDataListByPidSuccess(List<WyUnitData> unitDataList);
	}
	
	private OnGetUnitDataListByPid _onGetUnitDataListByPid;
	public void setOnGetUnitDataListByPid(OnGetUnitDataListByPid onGetUnitDataListByPid)
	{
		this._onGetUnitDataListByPid = onGetUnitDataListByPid;
	}
	
	
	
	public UnitService(Context context)
	{
		
	}
	
	public void beginGetUnitDataListByPid(Context context, long pid)
	{
		ParamSelect pp = new ParamSelect();
		pp.setDefid("app_unit_list");
		pp.setFormatId("json");
		pp.setDStyle("json");
		
		Where where = pp.newWhere();
		where.addCondition("pid", "=", String.valueOf(pid), AndOr.NULL);
		
		
		pp.setCommonSelect(where);
		
		BaseAsyncNet post = new BaseAsyncNet(context, "callback=rtn");
		
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				List<WyUnitData> unitDataList = null;
				try
				{
					SelectResponseData rd = new SelectResponseData(jsonData.getJson());
					if(rd.getIsSuccess())
					{
						JSONArray rows = rd.getFirstTableDataList();
						unitDataList = new ArrayList<WyUnitData>();
						for(int i=0;i<rows.length();i++)
						{
							JSONReader jsonReader = new JSONReader(rows.getJSONObject(i));
							
							WyUnitData unitData = new WyUnitData();
							
							unitData.setIid(jsonReader.getLong("iid", -1));
							unitData.setPid(jsonReader.getLong("pid", -1));
							unitData.setTitle(jsonReader.getString("title", ""));
							unitData.setUnitTitle(jsonReader.getString("unit_title", ""));
							unitData.setChildCount(jsonReader.getInt("child", 0));
							unitData.setParentId(jsonReader.getLong("parentid", -1));
							unitData.setParentTitle(jsonReader.getString("p_title", ""));
							unitDataList.add(unitData);
						}
					}
				}
				catch(Exception e)
				{
					unitDataList = null;
				}
				
				if(UnitService.this._onGetUnitDataListByPid != null)
				{
					UnitService.this._onGetUnitDataListByPid.OnGetUnitDataListByPidSuccess(unitDataList);
				}
			}
		});
		
		post.Post(pp.toPOSTString());
	}
}
