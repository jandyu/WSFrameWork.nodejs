package apk.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class BaseActivity extends Activity
{
	private ProgressDialog _progressDialog;
	protected void showWaite(String message)
	{
		this._progressDialog.setMax(100);
		this._progressDialog.setMessage(message);
		this._progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this._progressDialog.setCancelable(false);
		this._progressDialog.show();
	}

	protected void hideWaite()
	{
		this._progressDialog.dismiss();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//禁止横屏
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this._progressDialog = new ProgressDialog(this);
	}

	@Override
	protected void onDestroy()
	{
		this._progressDialog.dismiss();
		super.onDestroy();
	}
}
