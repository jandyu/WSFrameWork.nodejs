package apk.model;

import android.graphics.Color;
import apk.bll.AppPictureService;
import apk.common.Util;

public class MainMenuData
{
	public enum TitleType
	{
		/**
		 * 图片
		 */
		picture,
		/**
		 * 文字
		 */
		string,
		/**
		 * 不显示标题栏
		 */
		notitle,
	}
	
	public enum TitleAlign
	{
		left,
		center,
		right
	}
	
	public enum ServerOrLocal
	{
		server,
		local
	}
	
	private String configId;
	private String url;
	private String logo;
	private String titleType;
	private String title;
	private String titleSize;
	private String titleAlign;
	private String titleColor;
	private String titleBackgroundColor;
	private String backgroundColor;
	private String menuTitle;
	private String menuTitleSize;
	private String menuTitleColor;
	private String menuIconNormal;
	private String menuIconDown;
//	private String menuBackgroundNormal;
//	private String menuBackgroundDown;
	
	
	public String getConfigId()
	{
		return configId;
	}
	public void setConfigId(String configId)
	{
		this.configId = configId;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getLogo(ServerOrLocal serverOrLocal)
	{
		if(serverOrLocal == ServerOrLocal.server)
			return logo;
		else
			return this.getPicName(logo);
	}
	public void setLogo(String logo)
	{
		this.logo = logo;
	}
	public TitleType getTitleType()
	{
		if("picture".equalsIgnoreCase(titleType))
			return TitleType.picture;
		else if("string".equalsIgnoreCase(titleType))
			return TitleType.string;
		else
			return TitleType.notitle;
	}
	public void setTitleType(String titleType)
	{
		this.titleType = titleType;
	}
	public String getTitle(ServerOrLocal serverOrLocal)
	{
		if(this.getTitleType() == TitleType.picture)
		{
			if(serverOrLocal == ServerOrLocal.server)
				return this.title;
			else
				return this.getPicName(title);
		}
		else
			return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public float getTitleSize()
	{
		if(titleSize == null || titleSize.length() < 1)
			return 24;
		try
		{
			return Float.parseFloat(titleSize);
		}
		catch(Exception e)
		{
			return 24;
		}
	}
	public void setTitleSize(String titleSize)
	{
		this.titleSize = titleSize;
	}
	public TitleAlign getTitleAlign()
	{
		if("left".equalsIgnoreCase(titleAlign))
			return TitleAlign.left;
		else if("center".equalsIgnoreCase(titleAlign))
			return TitleAlign.center;
		else if("right".equalsIgnoreCase(titleAlign))
			return TitleAlign.right;
		else
			return TitleAlign.center;
	}
	public void setTitleAlign(String titleAlign)
	{
		this.titleAlign = titleAlign;
	}
	

	public int getTitleColor()
	{
		try
		{
			return Color.parseColor(titleColor);
		}
		catch(Exception e)
		{
			return Color.BLACK;
		}
	}

	public void setTitleColor(String titleColor)
	{
		this.titleColor = titleColor;
	}
	public int getTitleBackgroundColor()
	{
		try
		{
			return Color.parseColor(titleBackgroundColor);
		}
		catch(Exception e)
		{
			return Color.BLACK;
		}
	}
	public void setTitleBackgroundColor(String titleBackgroundColor)
	{
		this.titleBackgroundColor = titleBackgroundColor;
	}
	public int getBackgroundColor()
	{
		try
		{
			return Color.parseColor(backgroundColor);
		}
		catch(Exception e)
		{
			return Color.BLACK;
		}
	}
	public void setBackgroundColor(String backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	public String getMenuTitle()
	{
		return menuTitle;
	}
	public void setMenuTitle(String menuTitle)
	{
		this.menuTitle = menuTitle;
	}
	
	public float getMenuTitleSize()
	{
		if(menuTitleSize == null || menuTitleSize.length() < 1)
			return 24;
		try
		{
			return Float.parseFloat(menuTitleSize);
		}
		catch(Exception e)
		{
			return 24;
		}
	}
	public void setMenuTitleSize(String menuTitleSize)
	{
		this.menuTitleSize = menuTitleSize;
	}
	
	public int getMenuTitleColor()
	{
		try
		{
			return Color.parseColor(this.menuTitleColor);
		}
		catch(Exception e)
		{
			return Color.BLACK;
		}
	}
	public void setMenuTitleColor(String menuTitleColor)
	{
		this.menuTitleColor = menuTitleColor;
	}

	
	
	
	
	
	
	
	
	
	
	public String getMenuIconNormal(ServerOrLocal serverOrLocal)
	{
		if(serverOrLocal == ServerOrLocal.server)
			return this.menuIconNormal;
		else
			return this.getPicName(menuIconNormal);
	}
	public void setMenuIconNormal(String menuIconNormal)
	{
		this.menuIconNormal = menuIconNormal;
	}
	public String getMenuIconDown(ServerOrLocal serverOrLocal)
	{
		if(serverOrLocal == ServerOrLocal.server)
			return this.menuIconDown;
		else
			return this.getPicName(menuIconDown);
	}
	public void setMenuIconDown(String menuIconDown)
	{
		this.menuIconDown = menuIconDown;
	}
	
	
	
	
	
//	public String getMenuBackgroundNormal(ServerOrLocal serverOrLocal)
//	{
//
//		if(serverOrLocal == ServerOrLocal.server)
//			return this.menuBackgroundNormal;
//		else
//			return this.getPicName(menuBackgroundNormal);
//		
//	}
//	public void setMenuBackgroundNormal(String menuBackgroundNormal)
//	{
//		this.menuBackgroundNormal = menuBackgroundNormal;
//	}
//	
//	public String getMenuBackgroundDown(ServerOrLocal serverOrLocal)
//	{
//		
//		if(serverOrLocal == ServerOrLocal.server)
//			return this.menuBackgroundDown;
//		else
//			return this.getPicName(menuBackgroundDown);
//		
//	}
//	public void setMenuBackgroundDown(String menuBackgroundDown)
//	{
//		this.menuBackgroundDown = menuBackgroundDown;
//	}
	
	private String getPicName(String urlFileName)
	{
		String fileName = Util.getFileName(urlFileName);
		return AppPictureService.getPicturePath() + fileName;
	}
}
