package apk.customerview;

import android.app.ProgressDialog;
import android.content.Context;

public class WaiteBox
{
	private ProgressDialog _progressDialog;
	
	public WaiteBox(Context context, String message)
	{
		this._progressDialog = new ProgressDialog(context);
		this._progressDialog.setMax(100);
		this._progressDialog.setMessage(message);
		this._progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this._progressDialog.setCancelable(false);
	}
	
	//开始等待
	public void show()
	{
		
		this._progressDialog.show();
	}
	//结束等待
	public void dismiss()
	{
		this._progressDialog.dismiss();
	}
}
