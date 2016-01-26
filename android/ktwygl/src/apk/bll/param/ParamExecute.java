package apk.bll.param;

import java.util.ArrayList;
import java.util.List;

import apk.common.JSONHelper;


public class ParamExecute extends ParamBase
{
	private List<KeyValueSet> _strparam;
	private KeyValueSet _stringParam;
	
	public ParamExecute()
	{
		super();
		
		//this._postData.put("dStyle", "execute");
		this._dStyle = "execute";
		
		this._stringParam = new KeyValueSet();
		
		this._strparam = new ArrayList<KeyValueSet>();
		this._strparam.add(this._stringParam);
	}
	
	/**
	 * 设置处理类名
	 * @param className 类名
	 */
	public void setClassName(String className)
	{
		this._stringParam.put("name", className);
	}
	/**
	 * 设置处理类的命名空间
	 * @param assembly 命名空间
	 */
	public void setAssembly(String assembly)
	{
		this._stringParam.put("assembly", assembly);
	}
	/**
	 * 设置参数
	 * @param param 参数
	 */
	public void setParam(KeyValueSet param)
	{
		this._stringParam.put("param", param);
	}

	/**
	 * 将POST内容序列化
	 */
	@Override
	public String toPOSTString()
	{
//		this._stringParamList.put(this._stringParam);
//		this._postData.put("strparam", this._stringParamList);
//		return this._postData.toString();
		
		//return "defid=" + this._defid + "&fmtid=" + this._fmtid + "&dStyle=" + this._dStyle + "&strparam=" + this.escape(this._strparam.toString());
		
		return this.toPOSTString(JSONHelper.toJSON(this._strparam));
		
	}
	

}
