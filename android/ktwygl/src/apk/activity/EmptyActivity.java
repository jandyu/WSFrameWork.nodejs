package apk.activity;

import android.os.Bundle;
import apk.R;

public class EmptyActivity extends BaseActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_webview);
	}
}
