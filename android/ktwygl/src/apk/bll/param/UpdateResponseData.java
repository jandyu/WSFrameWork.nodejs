package apk.bll.param;

public class UpdateResponseData
{
	private boolean isSuccess;
	private String message;
	
	public boolean getIsSuccess()
	{
		return this.isSuccess;
	}
	public String getMessage()
	{
		return this.message;
	}
	public UpdateResponseData(String jsonString)
	{
		if(jsonString == null)
		{
			this.isSuccess = false;
			this.message = "";
			return;
		}
		
		String succBegin = "<succ msg='";
		String errorBegin = "<error msg='";
		
		
		
		if(jsonString.startsWith(succBegin))
		{
			String succEnd = "'/>";
			this.isSuccess = true;
			this.message = jsonString.substring(succBegin.length(), jsonString.length() - succEnd.length());
		}
		else if(jsonString.startsWith(errorBegin))
		{
			String errorEnd = "'/>";
			this.isSuccess = false;
			this.message = jsonString.substring(errorBegin.length(), jsonString.length() - errorEnd.length());
		}
	}
}
