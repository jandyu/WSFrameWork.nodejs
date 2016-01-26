package apk.model;
//package apk.platform.youjish.com.model;
//
//import java.util.HashMap;
//
//import android.content.Context;
//import apk.platform.youjish.com.bll.AppPictureService;
//import apk.platform.youjish.com.common.Util;
//import apk.platform.youjish.com.dal.ConfigDAL;
//import apk.platform.youjish.com.model.MainMenuData.ServerOrLocal;
//
//public class UserInfoData
//{
//	private long iid;
//	private String phone_no;
//	private String passwd;
//	private String name;
//	private String nick_name;
//	private String sign_name;
//	private String birthday;
//	private String head_pic;
//	private String device_id;
////	private String user_group_id;
//	private long unit_id;
//	private String units;
//	private long oid;
//	private String status;
//	private String memo0;
//	private String memo1;
//	private String memo2;
//	private String memo3;
//	private String memo4;
//	private String memo5;
//	private String memo6;
//	private String memo7;
//	private String memo8;
//	private String memo9;
//	
//	
//	
//	private HashMap<String, String> _extendInfo = new HashMap<String, String>();
//	
//	
//	public UserInfoData()
//	{
//		this.iid = -1;
//		this.phone_no = "";
//		this.passwd = "";
//		this.name = "";
//		this.nick_name = "";
//		this.sign_name = "";
//		this.head_pic = "";
//		this.device_id = "";
//		this.birthday = "";
////		this.user_group_id = "";
//		this.unit_id = -1;
//		this.units = "";
//		this.oid = -1;
//		this.status = "0";
//		this.memo0 = "";
//		this.memo1 = "";
//		this.memo2 = "";
//		this.memo3 = "";
//		this.memo4 = "";
//		this.memo5 = "";
//		this.memo6 = "";
//		this.memo7 = "";
//		this.memo8 = "";
//		this.memo9 = "";
//		
//	}
//	
//	public long getIid()
//	{
//		return iid;
//	}
//	public void setIid(long iid)
//	{
//		this.iid = iid;
//	}
//	public String getPhone_no()
//	{
//		return phone_no;
//	}
//	public void setPhone_no(String phone_no)
//	{
//		this.phone_no = phone_no;
//	}
//	public String getPasswd()
//	{
//		return passwd;
//	}
//	public void setPasswd(String passwd)
//	{
//		this.passwd = passwd;
//	}
//	
//
//	public String getName()
//	{
//		return name;
//	}
//
//	public void setName(String name)
//	{
//		this.name = name;
//	}
//	public String getNick_name()
//	{
//		return nick_name;
//	}
//	public void setNick_name(String nick_name)
//	{
//		this.nick_name = nick_name;
//	}
//	public String getSign_name()
//	{
//		return sign_name;
//	}
//	public void setSign_name(String sign_name)
//	{
//		this.sign_name = sign_name;
//	}
//	
//
//	public String getBirthday()
//	{
//		return birthday;
//	}
//
//
//	public void setBirthday(String birthday)
//	{
//		this.birthday = birthday;
//	}
//
//	public String getHead_pic()
//	{
//		return head_pic;
//	}
//	public String getHead_pic(ServerOrLocal serverOrLocal)
//	{
//		if(serverOrLocal == ServerOrLocal.server)
//			return ConfigDAL.getServerMainUrl() + this.head_pic;
//		else
//			return this.getPicName(this.head_pic);
//	}
//	public void setHead_pic(String head_pic)
//	{
//		this.head_pic = head_pic;
//	}
//	
//	public String getDevice_id()
//	{
//		return device_id;
//	}
//	public void setDevice_id(String device_id)
//	{
//		this.device_id = device_id;
//	}
//	
//	
//	
//
////	public String getUser_group_id()
////	{
////		return user_group_id;
////	}
////
////
////	public void setUser_group_id(String user_group_id)
////	{
////		this.user_group_id = user_group_id;
////	}
//
//	public String getExtendInfo(String key)
//	{
//		if(this._extendInfo.containsKey(key))
//		{
//			return this._extendInfo.get(key);
//		}
//		else
//		{
//			return null;
//		}
//	}
//	
//	public void setExtendInfo(String key, String value)
//	{
//		this._extendInfo.put(key, value);
//	}
//
//	
//	
//	
//	public long getUnit_id()
//	{
//		return unit_id;
//	}
//
//	public void setUnit_id(long unit_id)
//	{
//		this.unit_id = unit_id;
//	}
//
//	
//	
//	
//	
//
//	public String getUnits()
//	{
//		return units;
//	}
//
//	public void setUnits(String units)
//	{
//		this.units = units;
//	}
//
//	public long getOid()
//	{
//		return oid;
//	}
//
//	public void setOid(long oid)
//	{
//		this.oid = oid;
//	}
//
//	public String getStatus()
//	{
//		return status;
//	}
//
//	public void setStatus(String status)
//	{
//		this.status = status;
//	}
//
//	public String getMemo0()
//	{
//		return memo0;
//	}
//
//	
//	public void setMemo0(String memo0)
//	{
//		this.memo0 = memo0;
//	}
//
//	
//	public String getMemo1()
//	{
//		return memo1;
//	}
//
//	
//	public void setMemo1(String memo1)
//	{
//		this.memo1 = memo1;
//	}
//
//	
//	public String getMemo2()
//	{
//		return memo2;
//	}
//
//	
//	public void setMemo2(String memo2)
//	{
//		this.memo2 = memo2;
//	}
//
//	
//	public String getMemo3()
//	{
//		return memo3;
//	}
//
//	
//	public void setMemo3(String memo3)
//	{
//		this.memo3 = memo3;
//	}
//
//	
//	public String getMemo4()
//	{
//		return memo4;
//	}
//
//
//	public void setMemo4(String memo4)
//	{
//		this.memo4 = memo4;
//	}
//
//
//	public String getMemo5()
//	{
//		return memo5;
//	}
//
//
//	public void setMemo5(String memo5)
//	{
//		this.memo5 = memo5;
//	}
//
//
//	public String getMemo6()
//	{
//		return memo6;
//	}
//
//
//	public void setMemo6(String memo6)
//	{
//		this.memo6 = memo6;
//	}
//
//
//	public String getMemo7()
//	{
//		return memo7;
//	}
//
//
//	public void setMemo7(String memo7)
//	{
//		this.memo7 = memo7;
//	}
//
//
//	public String getMemo8()
//	{
//		return memo8;
//	}
//
//
//	public void setMemo8(String memo8)
//	{
//		this.memo8 = memo8;
//	}
//
//
//	public String getMemo9()
//	{
//		return memo9;
//	}
//
//
//	public void setMemo9(String memo9)
//	{
//		this.memo9 = memo9;
//	}
//	
////	public String getMemo(String key)
////	{
////		try
////		{
////			Class<? extends Object> objClazz = this.getClass();
////			Method[] methods = objClazz.getDeclaredMethods();
////			for(Method method : methods)
////			{
////				try
////				{
////					String methodName = method.getName();
////					String fieldName;
////					if(methodName.startsWith("get"))
////					{
////						fieldName = StringHelper.StringWithFirstLetterLower(methodName.substring(3));
////						
////						if(fieldName != null && method.getParameterTypes().length == 0)
////						{
////							Object returnValue = method.invoke(this, new Object[]{});
////							if(returnValue == null)
////								return null;
////							else
////								return returnValue.toString();
////						}
////						else
////						{
////							return null;
////						}
////					}
////					else
////					{
////						return null;
////					}
////				}
////				catch (Exception e)
////				{
////					continue;
////				}
////			}
////			return null;
////		}
////		catch (Exception e)
////		{
////			//e.printStackTrace();
////			return null;
////		}
////	}
//	
//	public String getMemo(String key)
//	{
//		if(key.equalsIgnoreCase("memo0"))
//			return this.getMemo0();
//		else if(key.equalsIgnoreCase("memo1"))
//			return this.getMemo1();
//		else if(key.equalsIgnoreCase("memo2"))
//			return this.getMemo2();
//		else if(key.equalsIgnoreCase("memo3"))
//			return this.getMemo3();
//		else if(key.equalsIgnoreCase("memo4"))
//			return this.getMemo4();
//		else if(key.equalsIgnoreCase("memo5"))
//			return this.getMemo5();
//		else if(key.equalsIgnoreCase("memo6"))
//			return this.getMemo6();
//		else if(key.equalsIgnoreCase("memo7"))
//			return this.getMemo7();
//		else if(key.equalsIgnoreCase("memo8"))
//			return this.getMemo8();
//		else if(key.equalsIgnoreCase("memo9"))
//			return this.getMemo9();
//		else
//			return "";
//	}
//	
//	public void setMemo(String key, String value)
//	{
//		if(key.equalsIgnoreCase("memo0"))
//			this.setMemo0(value);
//		else if(key.equalsIgnoreCase("memo1"))
//			this.setMemo1(value);
//		else if(key.equalsIgnoreCase("memo2"))
//			this.setMemo2(value);
//		else if(key.equalsIgnoreCase("memo3"))
//			this.setMemo3(value);
//		else if(key.equalsIgnoreCase("memo4"))
//			this.setMemo4(value);
//		else if(key.equalsIgnoreCase("memo5"))
//			this.setMemo5(value);
//		else if(key.equalsIgnoreCase("memo6"))
//			this.setMemo6(value);
//		else if(key.equalsIgnoreCase("memo7"))
//			this.setMemo7(value);
//		else if(key.equalsIgnoreCase("memo8"))
//			this.setMemo8(value);
//		else if(key.equalsIgnoreCase("memo9"))
//			this.setMemo9(value);
//	}
//	
//	private String getPicName(String urlFileName)
//	{
//		String fileName = Util.getFileName(urlFileName);
//		return AppPictureService.getPicturePath() + fileName;
//	}
//	
//	
//	
//}
