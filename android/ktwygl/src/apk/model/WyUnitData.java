package apk.model;

public class WyUnitData
{
	private long _iid;
	private long _pid;
	private String _title;
	private String _unitTitle;
	private int _childCount;
	private long _parentId;
	private String _parentTitle;
	
	
	public long getIid()
	{
		return this._iid;
	}
	public void setIid(long iid)
	{
		this._iid = iid;
	}
	/**
	 * 获取父节点编号
	 * @return
	 */
	public long getPid()
	{
		return this._pid;
	}
	public void setPid(long pid)
	{
		this._pid = pid;
	}
	public String getTitle()
	{
		return this._title;
	}
	public void setTitle(String title)
	{
		this._title = title;
	}
	/**
	 * 获取全名
	 * @return
	 */
	public String getUnitTitle()
	{
		return this._unitTitle;
	}
	public void setUnitTitle(String unitTitle)
	{
		this._unitTitle = unitTitle;
	}
	
	public void setChildCount(int childCount)
	{
		this._childCount = childCount;
	}
	
	public long getChildCount()
	{
		return this._childCount;
	}
	
	public void setParentId(long parentId)
	{
		this._parentId = parentId;
	}
	/**
	 * 获取父节点的父节点编号
	 * @return
	 */
	public long getParentId()
	{
		return this._parentId;
	}
	
	public void setParentTitle(String title)
	{
		this._parentTitle = title;
	}
	public String getParentTitle()
	{
		return this._parentTitle;
	}
	
}
