package apk.ibrowser;

public class StartPageInterface
{
	public interface OnInterMainPageListener
	{
		public void OnInterMainPage();
	}
	
	private OnInterMainPageListener _onInterMainPageListener;
	public void setOnInterListener(OnInterMainPageListener onInterMainPageListener)
	{
		this._onInterMainPageListener = onInterMainPageListener;
	}
	
	
	public void interMainPage()
	{
		if(this._onInterMainPageListener != null)
		{
			this._onInterMainPageListener.OnInterMainPage();
		}
	}
	
	//private Context context;
	public StartPageInterface()
	{
		//this.context = context;
	}
	
//	public String getUserInfo()
//	{
////		Context context = this._webBrowserView.getContext();
////		String userInfo = HybridPageHelper.getHybridPageString(context);
////		
////		Resources res = context.getResources();
////		String userInfoKey = res.getString(R.string.user_info_key);
////		
////		String userInfoString = null;
////		try
////		{
////			userInfoString = userInfo + MD5.getMD5(userInfo + userInfoKey);
////		}
////		catch (NoSuchAlgorithmException e)
////		{
////			e.printStackTrace();
////		}
////		
////		return userInfoString;
//		
//		return UserService.getUserInfo();
//	}
	
	
	
}
