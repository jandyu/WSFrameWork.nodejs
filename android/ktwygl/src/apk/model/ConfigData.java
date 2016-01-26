package apk.model;

import java.util.Date;

/**
 * @author Administrator
 *
 */
public class ConfigData
{
	private int id;
	private String valueString;
	private int valueInt;
	private Date valueDate;
	private String description;
	/**
	 * 
	 */
	public ConfigData()
	{
		
	}
	/**
	 * @return id
	 */
	public int getId()
	{
		return id;
	}
	/**
	 * @param id
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	/**
	 * @return valueString
	 */
	public String getValueString()
	{
		return valueString;
	}
	/**
	 * @param valueString
	 */
	public void setValueString(String valueString)
	{
		this.valueString = valueString;
	}
	/**
	 * @return valueInt
	 */
	public int getValueInt()
	{
		return valueInt;
	}
	/**
	 * @param valueInt
	 */
	public void setValueInt(int valueInt)
	{
		this.valueInt = valueInt;
	}
	/**
	 * @return valueDate
	 */
	public Date getValueDate()
	{
		return valueDate;
	}
	/**
	 * @param valueDate
	 */
	public void setValueDate(Date valueDate)
	{
		this.valueDate = valueDate;
	}
	/**
	 * @return description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
}
