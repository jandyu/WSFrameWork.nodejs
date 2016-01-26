package apk.bll.param;

import java.util.ArrayList;
import java.util.List;

import apk.common.JSONHelper;


public class ParamProcedure extends ParamBase
{
	private KeyValueSet _strparam;
	private List<KeyValueSet> _procList;
	
	public ParamProcedure()
	{
		super();
		
		//this._postData = new JSONObject();
		
		//this._postData.put("fmtid", "update");
		//this._postData.put("dStyle", "json");
		
		this._fmtid = "update";
		this._dStyle = "json";
		
		
		this._procList = new ArrayList<KeyValueSet>();
		
		this._strparam = new KeyValueSet();
		this._strparam.put("mode", "parameter");
		this._strparam.put("proc", this._procList);
	}
	/**
	 * 
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
	 * 添加一个过程
	 * @param name 过程名称
	 * @param param 参数
	 */
	public void addProcedure(String name, KeyValueSet param)
	{
		KeyValueSet procObject = new KeyValueSet();
		procObject.put("name", name);
		procObject.put("param", param);
		
		this._procList.add(procObject);
	}
	
	/**
	 * 将POST内容序列化
	 */
	@Override
	public String toPOSTString()
	{
		//this._postData.put("strparam", this._operData.toString());
		//return this._postData.toString();
		//return "defid=" + this._defid + "&fmtid=" + this._fmtid + "&dStyle=" + this._dStyle + "&strparam=" + this.escape(this._strparam.toString());
		
		return this.toPOSTString(JSONHelper.toJSON(this._strparam));
	}
	

}
