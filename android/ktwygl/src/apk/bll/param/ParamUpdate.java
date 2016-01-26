package apk.bll.param;

import java.util.ArrayList;
import java.util.List;

import apk.common.JSONHelper;

public class ParamUpdate extends ParamBase
{
	private KeyValueSet _strparam;
	private List<String> _tableList;
	
	public ParamUpdate()
	{
		super();
		
		//this._postData = new JSONObject();
		//this._postData.put("fmtid", "update");
		//this._postData.put("dStyle", "json");
		
		this._fmtid = "update";
		this._dStyle = "json";
		
		this._strparam = new KeyValueSet();
		this._tableList = new ArrayList<String>();
		
		this._strparam.put("tblList", this._tableList);
	}
	

	private void addTable(String tableName)
	{
		boolean existsTableName = false;
		for(int i=0,j=this._tableList.size();i<j;i++)
		{
			Object o = this._tableList.get(i);
			if(tableName.equals(o))
			{
				existsTableName = true;
				break;
			}
		}
		if(!existsTableName)
		{
			this._tableList.add(tableName);
		}
		
		if(!this._strparam.containsKey(tableName))
		{
			KeyValueSet tableNode = new KeyValueSet();
			List<KeyValueSet> dataList = new ArrayList<KeyValueSet>();
			List<KeyValueSet> delList = new ArrayList<KeyValueSet>();
			
			tableNode.put("data", dataList);
			tableNode.put("delData", delList);
			
			this._strparam.put(tableName, tableNode);
		}
	}
	
	/**
	 * 设置
	 * @param defid
	 */
	public void setDefid(String defid)
	{
		//this._postData.put("defid", defid);
		this._defid = defid;
	}
	/**
	 * 设置返回类型
	 * @param dStyle
	 */
	public void setDStyle(String dStyle)
	{
		//this._postData.put("dStyle", dStyle);
		this._dStyle = dStyle;
	}
	/**
	 * 添加一行
	 * @param tableName 表名
	 * @param item 行数据
	 */
	public void addInsertRow(String tableName, KeyValueSet item)
	{
		KeyValueSet newItem = this.simpleClone("8", item);//浅克隆是因为这里要添加一个值
		
		this.addTable(tableName);
		
		KeyValueSet tableNode = (KeyValueSet)this._strparam.get(tableName);
		
		@SuppressWarnings("unchecked")
		List<KeyValueSet> dataList = (List<KeyValueSet>)tableNode.get("data");
		
		//newItem.put("c0", "8");//insert标识
		dataList.add(newItem);
	}
	/**
	 * 修改一行
	 * @param tableName 表名
	 * @param item 行数据
	 */
	public void addUpdateRow(String tableName, KeyValueSet item)
	{
		KeyValueSet newItem = this.simpleClone("9", item);//浅克隆是因为这里要添加一个值
		
		this.addTable(tableName);
		
		KeyValueSet tableNode = (KeyValueSet)this._strparam.get(tableName);
		
		@SuppressWarnings("unchecked")
		List<KeyValueSet> dataList = (List<KeyValueSet>)tableNode.get("data");
		
		//newItem.put("c0", "9");//update标识
		dataList.add(newItem);
	}
	/**
	 * 删除一行
	 * @param tableName 表名
	 * @param item 行数据
	 */
	public void addDeleteRow(String tableName, KeyValueSet item)
	{
		//JSONObject newItem = this.simpleClone(item);//浅克隆是因为这里要添加一个值
		
		this.addTable(tableName);
		
		KeyValueSet tableNode = (KeyValueSet)this._strparam.get(tableName);
		
		@SuppressWarnings("unchecked")
		List<KeyValueSet> dataList = (List<KeyValueSet>)tableNode.get("delData");
		
		dataList.add(item);
	}
	/**
	 * 根据表的iid字段删除一行
	 * @param tableName 表名
	 * @param iid iid字段值
	 */
	public void addDeleteRow(String tableName, Object iid)
	{
		//JSONObject newItem = this.simpleClone(item);//浅克隆是因为这里要添加一个值
		
		this.addTable(tableName);
		
		KeyValueSet tableNode = (KeyValueSet)this._strparam.get(tableName);
		
		@SuppressWarnings("unchecked")
		List<Object> dataList = (List<Object>)tableNode.get("delData");
		
		dataList.add(iid);
	}
	
	
	
	
	
	
	
	/**
	 * 将POST内容序列化
	 */
	@Override
	public String toPOSTString()
	{
		//this._postData.put("strparam", this._stringParam.toString());
		//return this._postData.toString();
		//return "defid=" + this._defid + "&fmtid=" + this._fmtid + "&dStyle=" + this._dStyle + "&strparam=" + this.escape(this._strparam.toString());
		
		String strparam = JSONHelper.toJSON(this._strparam);
		
		return this.toPOSTString(strparam);
	}
	

	
	
	
}
