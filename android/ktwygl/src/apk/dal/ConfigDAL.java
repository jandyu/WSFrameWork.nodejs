package apk.dal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import apk.R;
import apk.common.AppProjectType;
import apk.common.StringHelper;
import apk.model.MainMenuData;
import apk.model.Size;

public class ConfigDAL
{
	private static List<MainMenuData> _mainMenuDataList;
	private static MainMenuData _normalMenuData;
	private static float _base_font_size;
	private static int _start_page_waite_time;
	private static String _app_update_url;
	private static String _upload_picture_url;
	private static String _server_main_url;
	private static String _upload_picture_max_size;
	private static String _upload_head_photo_max_size;
	private static String _server_url;
	private static String _update_defid;
	private static String _update_dstyle;
	private static String _url_protocol;
	private static String _app_update_version_key;
	private static int _timeout_http_normal_post;
	private static String _logpath;
	private static String _hybridPageString;
	
	public static boolean init(Context context)
	{
		
		Resources res = context.getResources();
		
		
		XmlResourceParser xmlResourceParser = null;
		
		switch(AppProjectType.getCurrentProjectType())
		{
			case AppProjectType.IData95:
				xmlResourceParser = res.getXml(R.xml.app_config_idata95);
				break;
			case AppProjectType.LoveGreen:
				xmlResourceParser = res.getXml(R.xml.app_config_lovegreen);
				break;
			case AppProjectType.WestSoft:
				xmlResourceParser = res.getXml(R.xml.app_config_westsoft);
				break;
			case AppProjectType.MenQianXiaoDian_mobi:
				xmlResourceParser = res.getXml(R.xml.app_config_menqianxiaodian_mobi);
				break;
			case AppProjectType.WoDeXiaoDian_boss:
				xmlResourceParser = res.getXml(R.xml.app_config_wodexiaodian_boss);
				break;
			case AppProjectType.RenZhongZiBen:
				xmlResourceParser = res.getXml(R.xml.app_config_renzhongjinrong);
				break;
			case AppProjectType.KTWY:
				xmlResourceParser = res.getXml(R.xml.app_config_ktwy);
				break;
		}
		
		_mainMenuDataList = new ArrayList<MainMenuData>();
		
		int event;
		try 
		{
			while((event = xmlResourceParser.next()) != XmlResourceParser.END_DOCUMENT)
			{
				switch(event)
				{
					case XmlResourceParser.START_DOCUMENT:
						break;
					case XmlResourceParser.START_TAG:
						if("app_config".equals(xmlResourceParser.getName()))
						{
							initAppConfig(xmlResourceParser);
						}
						break;
					case XmlResourceParser.END_TAG:
						break;
					case XmlResourceParser.END_DOCUMENT:
						break;
				}
			}
		} 
		catch (XmlPullParserException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		   
				
		return true;

	}
	
	private static void initAppConfig(XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException
	{
		int event;
		while((event = xmlResourceParser.next()) != XmlResourceParser.END_TAG && event != XmlResourceParser.END_DOCUMENT)
		{
			if("main_menu".equalsIgnoreCase(xmlResourceParser.getName()))
			{
				initMainMenu(xmlResourceParser);
			}
			else if("config".equalsIgnoreCase(xmlResourceParser.getName()))
			{
				initConfigValues(xmlResourceParser);
			}
		}
	}
	
	private static void initConfigValues(XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException
	{
		String name = xmlResourceParser.getAttributeValue(null, "name");
		if("base_font_size".equalsIgnoreCase(name))
		{
			_base_font_size = StringHelper.parseFloat(xmlResourceParser.getAttributeValue(null, "value"), 24);
			xmlResourceParser.next();
		}
		else if("start_page_waite_time".equalsIgnoreCase(name))
		{
			_start_page_waite_time = StringHelper.parseInt(xmlResourceParser.getAttributeValue(null, "value"), 10000);
			xmlResourceParser.next();
		}
		else if("app_update_url".equalsIgnoreCase(name))
		{
			_app_update_url = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("upload_picture_url".equalsIgnoreCase(name))
		{
			_upload_picture_url = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("server_main_url".equalsIgnoreCase(name))
		{
			_server_main_url = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("upload_picture_max_size".equalsIgnoreCase(name))
		{
			_upload_picture_max_size = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("upload_head_photo_max_size".equalsIgnoreCase(name))
		{
			_upload_head_photo_max_size = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("server_url".equalsIgnoreCase(name))
		{
			_server_url = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("update_defid".equalsIgnoreCase(name))
		{
			_update_defid = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("update_dstyle".equalsIgnoreCase(name))
		{
			_update_dstyle = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("url_protocol".equalsIgnoreCase(name))
		{
			_url_protocol = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("app_update_version_key".equalsIgnoreCase(name))
		{
			_app_update_version_key = xmlResourceParser.getAttributeValue(null, "value");
			xmlResourceParser.next();
		}
		else if("timeout_http_normal_post".equalsIgnoreCase(name))
		{
			_timeout_http_normal_post = StringHelper.parseInt(xmlResourceParser.getAttributeValue(null, "value"), 10) * 1000;
			xmlResourceParser.next();
		}
		else if("logpath".equalsIgnoreCase(name))
		{
			_logpath = xmlResourceParser.getAttributeValue(null, "logpath");
			if(_logpath != null)
			{
				_logpath = _logpath.replace("\\", "/");
				if(!_logpath.endsWith("/"))
					_logpath += "/";
			}
			xmlResourceParser.next();
		}
		else if("hybridPageString".equalsIgnoreCase(name))
		{
			_hybridPageString = xmlResourceParser.nextText();
		}
		else
		{
			xmlResourceParser.next();
		}
	}
	
	private static void initMainMenu(XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException
	{
		_normalMenuData = initMainMenuItem(xmlResourceParser);
		
		while(xmlResourceParser.next() != XmlResourceParser.END_TAG)
		{
			if("menu_item".equalsIgnoreCase(xmlResourceParser.getName()))
			{
				_mainMenuDataList.add(initMainMenuItem(xmlResourceParser));
				xmlResourceParser.next();
			}
		}
	}
	
	private static MainMenuData initMainMenuItem(XmlResourceParser xmlResourceParser)
	{
		MainMenuData mainMenuData = new MainMenuData();
		
		mainMenuData.setBackgroundColor(xmlResourceParser.getAttributeValue(null, "backgroundColor"));
		mainMenuData.setConfigId(xmlResourceParser.getAttributeValue(null, "name"));
		mainMenuData.setLogo(xmlResourceParser.getAttributeValue(null, "logo"));
		mainMenuData.setMenuIconDown(xmlResourceParser.getAttributeValue(null, "menuIconDown"));
		mainMenuData.setMenuIconNormal(xmlResourceParser.getAttributeValue(null, "menuIconNormal"));
		mainMenuData.setMenuTitle(xmlResourceParser.getAttributeValue(null, "menuTitle"));
		mainMenuData.setMenuTitleColor(xmlResourceParser.getAttributeValue(null, "menuTitleColor"));
		mainMenuData.setMenuTitleSize(xmlResourceParser.getAttributeValue(null, "menuTitleSize"));
		mainMenuData.setTitle(xmlResourceParser.getAttributeValue(null, "title"));
		mainMenuData.setTitleAlign(xmlResourceParser.getAttributeValue(null, "titleAlign"));
		mainMenuData.setTitleBackgroundColor(xmlResourceParser.getAttributeValue(null, "titleBackgroundColor"));
		mainMenuData.setTitleColor(xmlResourceParser.getAttributeValue(null, "titleColor"));
		mainMenuData.setTitleSize(xmlResourceParser.getAttributeValue(null, "titleSize"));
		mainMenuData.setTitleType(xmlResourceParser.getAttributeValue(null, "titleType"));
		mainMenuData.setUrl(xmlResourceParser.getAttributeValue(null, "url"));
		
		return mainMenuData;
	}
	

	
	
	public static List<MainMenuData> getMainMenuDataList()
	{
		return _mainMenuDataList;
	}
	
	

	
	
	public static String getServerURL()
	{
		return _server_url;
	}
	
	public static String getUpdateDefid()
	{
		return _update_defid;
	}
	
	public static String getUpdateDStyle()
	{
		return _update_dstyle;
	}
	
	public static String getUrlProtocol()
	{
		return _url_protocol;
	}
	
	/**
	 * 获取升级包版本号，对应服务端数据库表app_config的键名
	 * @return
	 */
	public static String getAppUpdateVersionKey()
	{
		if(StringHelper.IsNullOrEmpty(_app_update_version_key))
			return "app_update_version";
		
		return _app_update_version_key;
	}
	
	public static int getStartPageWaiteTime()
	{
		return _start_page_waite_time;
	}
	
	
	
	
	//-----------------------------------------------------------MainMenuData-------------------------
	public static MainMenuData getMainMenuData(int index)
	{
		return _mainMenuDataList.get(index);
	}
	
	public static MainMenuData getNormalMainMenuData()
	{
		return _normalMenuData;
	}
		

	
	public static float getBaseFontSize()
	{
		return _base_font_size;
	}
	

	
	public static String getAppUpdateUrl()
	{
		return _app_update_url;
	}
	
	public static String getUploadPictureUrl()
	{
		return _upload_picture_url;
	}
	
	public static String getServerMainUrl()
	{
		return _server_main_url;
	}
	
	public static Size getUploadPictureMaxSize()
	{
		String[] s = _upload_picture_max_size.split("\\*");
		
		Size size = new Size(200, 200);
		if(s.length > 1)
		{
			try
			{
				size.width = Float.parseFloat(s[0]);
				size.height= Float.parseFloat(s[1]);
			}
			catch(Exception e)
			{
				size.width = 200;
				size.height = 200;
			}
		}
		return size;
	}
	
	public static Size getUploadHeadPhotoMaxSize()
	{
		String[] s = _upload_head_photo_max_size.split("\\*");
		
		Size size = new Size(200, 200);
		if(s.length > 1)
		{
			try
			{
				size.width = Float.parseFloat(s[0]);
				size.height= Float.parseFloat(s[1]);
			}
			catch(Exception e)
			{
				size.width = 200;
				size.height = 200;
			}
		}
		return size;
	}
	
	public static int getTimeOutHttpNormalPost()
	{
		return _timeout_http_normal_post;
	}
	
	public static String getLogpath()
	{
		return _logpath;
	}
	
	public static String getHybridPageString()
	{
		return _hybridPageString;
	}
}
