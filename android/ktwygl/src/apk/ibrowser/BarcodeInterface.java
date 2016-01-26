package apk.ibrowser;

import android.app.Activity;
import android.content.Intent;
import apk.barcode.CaptureActivity;
import apk.customerview.WebBrowser;

public class BarcodeInterface extends BaseInterface
{
	private Activity _activity;
	public BarcodeInterface(Activity activity, WebBrowser webView)
	{
		super(activity, webView);
		this._activity = activity;
	}
	
	public void openBarcode()
	{
		Intent intent = new Intent(this._activity, CaptureActivity.class);
		

		
		this._activity.startActivityForResult(intent, 10);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == 10)
		{
			String barcode = data.getStringExtra("value");
			
			callJavaScript("responseBarcode('" + barcode + "')");
		}
	}
	

	
}
