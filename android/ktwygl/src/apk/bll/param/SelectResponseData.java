package apk.bll.param;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectResponseData
{
	
	public class Table
	{
		private String tableName;
		private int pagesize;
		private int totalrows;
		private int currpage;
		private int totalpages;
		private String orderby;
		
		private JSONArray dataList;
		private JSONObject s;
		
		
		public String getTableName()
		{
			return tableName;
		}
		public int getPagesize()
		{
			return pagesize;
		}
		public int getTotalrows()
		{
			return totalrows;
		}
		public int getCurrpage()
		{
			return currpage;
		}
		public int getTotalpages()
		{
			return totalpages;
		}
		public String getOrderby()
		{
			return orderby;
		}
		public JSONArray getDataList()
		{
			return dataList;
		}
		public JSONObject getS()
		{
			return s;
		}
	}
	
	private String _version;
	public String getVersion()
	{
		return _version;
	}



	public List<Table> getTableList()
	{
		return _data;
	}
	
	public JSONArray getFirstTableDataList()
	{
		List<Table> tableList = this.getTableList();
		if(tableList != null && tableList.size() > 0)
		{
			return tableList.get(0).getDataList();
		}
		
		return null;
	}
	
	public JSONObject getFirstTableFirstRow()
	{
		JSONArray dataList = this.getFirstTableDataList();
		if(dataList != null && dataList.length() > 0)
		{
			try
			{
				return (JSONObject)dataList.get(0);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}



	private List<Table> _data;
	
	private boolean isSuccess;
	public boolean getIsSuccess()
	{
		return this.isSuccess;
	}
	

	
	public SelectResponseData(String responseJson) throws JSONException
	{
		if(responseJson == null || responseJson.length() == 0 || responseJson.equals("{}"))
		{
			this.isSuccess = false;
			return;
		}
		
		int beginIndex = responseJson.indexOf("{");
		int endIndex = responseJson.lastIndexOf("}");
		
		if(beginIndex < 0 || endIndex < 0 || endIndex <= beginIndex)
		{
			this.isSuccess = false;
			return;
		}
		
		String json = responseJson.substring(beginIndex, endIndex + 1);
		
		if(json.length() == 0)
		{
			this.isSuccess = false;
			return;
		}

		JSONObject object = new JSONObject(json);
		this._version = object.get("ver").toString();
		this._data = new ArrayList<Table>();
		
		JSONArray dataList = object.getJSONArray("data");
		for(int i=0,j=dataList.length();i<j;i++)
		{
			Table table = new Table();
			
			JSONObject d = (JSONObject)dataList.get(i);
			
			@SuppressWarnings("unchecked")
			Iterator<String> keys = d.keys();
			while(keys.hasNext())
			{
				String key = keys.next();
				JSONObject value = d.getJSONObject(key);
				
				table.tableName = key;
				table.pagesize = Integer.parseInt(value.getString("pagesize"), 10);
				table.totalrows = Integer.parseInt(value.getString("totalrows"), 10);
				table.currpage = Integer.parseInt(value.getString("currpage"), 10);
				table.totalpages = Integer.parseInt(value.getString("totalpages"), 10);
				table.orderby = value.getString("orderby");
				
				if(value.has("d"))
				{
					table.dataList = value.getJSONArray("d");
				}
				table.s = value.getJSONObject("s");
			}
			this._data.add(table);
		}
		this.isSuccess = true;
	}
}
