package apk.common;

import android.content.Context;

public class AppProjectType
{
	public final static int IData95 = 1;
	public final static int LoveGreen = 2;
	public final static int WestSoft = 3;
	
	
	/*
	 * 门前小店
	 */
	public final static int MenQianXiaoDian_mobi = 4;
	
	/**
	 * 我的小店
	 */
	public final static int WoDeXiaoDian_boss = 5;
	/*
	 * 人众资本
	 */
	public final static int RenZhongZiBen = 6;
	
	/*
	 * 科騰物業
	 */
	public final static int KTWY = 7;
	
	
	
	
	
	private static int _currentProjectType = -1;
	
	public static int getCurrentProjectType()
	{
		return AppProjectType._currentProjectType;
	}
	
	public static void init(Context context)
	{
		String packageName = StringHelper.toLowerCase(context.getPackageName());
		
		//AppProjectType._currentProjectType = AppProjectType.WoDeXiaoDian;//我的小店
		//packageName = "";
		
		
		if("idata95.platform.xrdz.com".equals(packageName))
		{
			AppProjectType._currentProjectType = AppProjectType.IData95;
		}
		else if("apk.platform.youjish.com".equals(packageName)
				|| "mobi.apk.platform.xrdz.com".equals(packageName) //Jpush中包名搞错了。将来等Jpush改过来后，这句话需要删掉
				)
		{
			AppProjectType._currentProjectType = AppProjectType.LoveGreen;//和著民
		}
		else if("apk.platform.xrdz.com".equals(packageName))
		{
			AppProjectType._currentProjectType = AppProjectType.WestSoft;
		}
		else if("mqxd.apk.platform.xrdz.com".equals(packageName))
		{
			AppProjectType._currentProjectType = AppProjectType.MenQianXiaoDian_mobi;//门前小店
		}
		else if("boss.apk.platform.xrdz.com".equals(packageName))
		{
			AppProjectType._currentProjectType = AppProjectType.WoDeXiaoDian_boss;//我的小店
		}
		else if("com.rzzb.rzcrm".equals(packageName))
		{
			AppProjectType._currentProjectType = AppProjectType.RenZhongZiBen;//人众资本
		}
		else if("cn.zjy8.wyapp".equals(packageName))
		{
			AppProjectType._currentProjectType = AppProjectType.KTWY;//科騰物業
		}
	}
}
