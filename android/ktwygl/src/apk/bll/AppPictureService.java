package apk.bll;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import apk.common.Util;
import apk.net.DownLoadFile;

public class AppPictureService
{
	public static final String getPicturePath()
	{
		String picturePath = Util.getAbsolutePath() + "/pics/";
		//return "/mnt/sdcard/Pictures/";
		return picturePath;
	}
	public static boolean downLoadPicture()
	{
		List<String> pictureList = new ArrayList<String>();// AppConfigDAL.getPictureList();
		
//		List<MainMenuData> mainMenuDataList = ConfigDAL.getMainMenuDataList();
//		for(MainMenuData mainMenuData : mainMenuDataList)
//		{
//			if(mainMenuData.getTitleType() == TitleType.picture)//如果标题为图片
//			{
//				pictureList.add(mainMenuData.getTitle(ServerOrLocal.server));
//			}
//			
//			pictureList.add(mainMenuData.getLogo(ServerOrLocal.server));
//			pictureList.add(mainMenuData.getMenuIconNormal(ServerOrLocal.server));
//			pictureList.add(mainMenuData.getMenuIconDown(ServerOrLocal.server));
////			pictureList.add(mainMenuData.getMenuBackgroundNormal(ServerOrLocal.server));
////			pictureList.add(mainMenuData.getMenuBackgroundDown(ServerOrLocal.server));
//		}
		
		
		List<String> pictureDistinctList = new ArrayList<String>();
		for(String picURLName : pictureList)
		{
			if(!pictureDistinctList.contains(picURLName))
			{
				pictureDistinctList.add(picURLName);
			}
		}
		
		String picturePath = getPicturePath();
//		File path = new File(picturePath);
//		if(!path.exists())
//		{
//			try
//			{
//				path.mkdirs();
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace();
//				return false;
//			}
//		}
		
		for(String picURLName : pictureDistinctList)
		{
			if(picURLName != null && picURLName.length() > 0)
			{
				String picName = Util.getFileName(picURLName);
				String saveFileName = picturePath + picName;
				File file = new File(saveFileName);
				if(!file.exists())//文件不存在
				{
					if(!DownLoadFile.downLoadFile(picURLName, saveFileName))
					{
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
