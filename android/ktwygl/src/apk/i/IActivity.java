package apk.i;

import android.content.Context;
import android.content.Intent;
import apk.activity.MainActivity;

public interface IActivity
{
	public void startActivityForResult(Intent intent, int requestCode);
	
	public void setNumText(int numValue);
	
	public Context getContext();
	
	public MainActivity getMainActivity();
	
	public void finish();
	
	public void UpdUI(String arg);
}
