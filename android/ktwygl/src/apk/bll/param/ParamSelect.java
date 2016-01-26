package apk.bll.param;

import java.util.ArrayList;
import java.util.List;

import apk.common.JSONHelper;

public class ParamSelect extends ParamBase
{
	
	private KeyValueSet _strparam;
	public enum AndOr
	{
		AND,
		OR,
		NULL
	}
	/**
	 * 条件
	 */
	public class Where
	{
		private List<KeyValueSet> _whereList;
		public Where()
		{
			this._whereList = new ArrayList<KeyValueSet>();
		}
		/**
		 * 添加条件
		 * @param leftBracket 左括号
		 * @param fieldName 字段名称，可以指定表 如：tbl1.fieldName
		 * @param logic 逻辑条件 如：like, =,<,<=,>,>= 等 
		 * @param value 查询的值
		 * @param rightBracket 右括号
		 * @param andor and或or或空
		 */
		public void addCondition(boolean leftBracket, String fieldName, String logic, String value,boolean rightBracket, AndOr andor)
		{
			this._whereList.add(newCondition(leftBracket, fieldName, logic, value, rightBracket, andor));
		}
		/**
		 * 添加条件
		 * @param fieldName 字段名称，可以指定表 如：tbl1.fieldName
		 * @param logic 逻辑条件 如：like, =,<,<=,>,>= 等 
		 * @param value 查询的值
		 * @param andor and或or
		 */
		public void addCondition(String fieldName, String logic, String value, AndOr andor)
		{
			this._whereList.add(newCondition(false, fieldName, logic, value, false, andor));
		}
		public List<KeyValueSet> getJSONObject()
		{
			return this._whereList;
		}
	}
	
	public Where newWhere()
	{
		return new Where();
	}

	/**
	 * Having
	 */
	public class Having
	{
		private List<KeyValueSet> _whereList;
		public Having()
		{
			this._whereList = new ArrayList<KeyValueSet>();
		}
		/**
		 * 添加条件
		 * @param leftBracket 左括号
		 * @param fieldName 字段名称，可以指定表 如：tbl1.fieldName
		 * @param logic 逻辑条件 如：like, =,<,<=,>,>= 等 
		 * @param value 查询的值
		 * @param rightBracket 右括号
		 * @param andor and或or或空
		 */
		public void addCondition(boolean leftBracket, String fieldName, String logic, String value,boolean rightBracket, AndOr andor)
		{
			this._whereList.add(newCondition(leftBracket, fieldName, logic, value, rightBracket, andor));
		}
		/**
		 * 添加条件
		 * @param fieldName 字段名称，可以指定表 如：tbl1.fieldName
		 * @param logic 逻辑条件 如：like, =,<,<=,>,>= 等 
		 * @param value 查询的值
		 * @param andor and或or或空
		 */
		public void addCondition(String fieldName, String logic, String value, AndOr andor)
		{
			this._whereList.add(newCondition(false, fieldName, logic, value, false, andor));
		}
		public List<KeyValueSet> getJSONObject()
		{
			return this._whereList;
		}
	}
	public Having newHaving()
	{
		return new Having();
	}
	/**
	 * 排序
	 */
	public class Order
	{
		private List<KeyValueSet> _orderList;
		public Order()
		{
			this._orderList = new ArrayList<KeyValueSet>();
		}
		public void addOrder(String fieldName, String sort)
		{
			KeyValueSet w = new KeyValueSet();
			w.put("col", fieldName);
			w.put("sort", sort);
			this._orderList.add(w);
		}
		public List<KeyValueSet> getJSONObject()
		{
			return this._orderList;
		}
	}
	public Order newOrder()
	{
		return new Order();
	}
	
	private KeyValueSet newCondition(boolean leftBracket, String fieldName, String logic, String value, boolean rightBracket, AndOr andor)
	{
		KeyValueSet w = new KeyValueSet();
		if(leftBracket)
			w.put("col", "(" + fieldName);
		else
			w.put("col", fieldName);
		w.put("logic", logic);
		w.put("val", value);
		switch(andor)
		{
			case AND:
				if(rightBracket)
					w.put("andor", ") and ");
				else
					w.put("andor", " and ");
				break;
			case OR:
				if(rightBracket)
					w.put("andor", ") or ");
				else
					w.put("andor", " or ");
				break;
			case NULL:
				if(rightBracket)
					w.put("andor", ")");	
				else
					w.put("andor", "");
				break;
		}
		return w;
	}
	
	public ParamSelect()
	{
		super();
		
		this._strparam = new KeyValueSet();
		//this._postData.put("dStyle", "json");
		this._dStyle = "json";
	}
	/**
	 * 设置通用约束
	 * @param where 条件
	 * @param currentPage 页序号
	 * @param pageSize 每页大小
	 */
	public void setCommonSelect(Where where, int currentPage, int pageSize)
	{
		KeyValueSet sqlObject = new KeyValueSet();
		sqlObject.put("where", where.getJSONObject());
		if(currentPage > 0)
			sqlObject.put("currpage", currentPage);
		else
			sqlObject.put("currpage", 1);
		
		if(pageSize > 0)
			sqlObject.put("pagesize", pageSize);
		else
			sqlObject.put("pagesize", Integer.MAX_VALUE);
		this._strparam.put("common", sqlObject);
	}
	/**
	 * 设置通用约束
	 * @param where 条件
	 */
	public void setCommonSelect(Where where)
	{
		this.setCommonSelect(where, -1, -1);
	}
	/**
	 * 添加一个查询
	 * @param defid 
	 * @param where 条件
	 * @param having having
	 * @param order 排序
	 * @param currentPage 页序号
	 * @param pageSize 每页大小
	 */
	public void addSelect(String defid, Where where, Having having, Order order, int currentPage, int pageSize)
	{
		KeyValueSet sqlObject = new KeyValueSet();
		sqlObject.put("where", where.getJSONObject());
		sqlObject.put("having", having.getJSONObject());
		sqlObject.put("order", order.getJSONObject());
		if(currentPage > 0)
			sqlObject.put("currpage", currentPage);
		if(pageSize > 0)
			sqlObject.put("pagesize", pageSize);
		this._strparam.put(defid, sqlObject);
	}
	/**
	 * 添加一个查询
	 * @param defid
	 * @param where 条件
	 * @param having having
	 * @param order 排序
	 */
	public void addSelect(String defid, Where where, Having having, Order order)
	{
		this.addSelect(defid, where, having, order, -1, -1);
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
	 * 格式化xslt文件
	 * @param fmtid
	 */
	public void setFormatId(String fmtid)
	{
		//this._postData.put("fmtid", fmtid);
		this._fmtid = fmtid;
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
	 * 将POST内容序列化
	 */
	@Override
	public String toPOSTString()
	{
//		this._postData.put("strparam", this._strparam.toString() );
//		return this._postData.toString();
		//return "defid=" + this._defid + "&fmtid=" + this._fmtid + "&dStyle=" + this._dStyle + "&strparam=" + this.escape(this._strparam.toString());
		return this.toPOSTString(JSONHelper.toJSON(this._strparam));
	}
	
	

	
	
}
