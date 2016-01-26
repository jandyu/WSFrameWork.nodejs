package apk.customerview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import apk.R;

public class TabBarcode extends Fragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		
		//LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//inflater.inflate(R.layout.barcode_tab, this);
		
		View parentView = inflater.inflate(R.layout.activity_information_collect, container, false);
		
		
		
		

	    //TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.InformationCollectTab);
	    //String title = a.getString(R.styleable.InformationCollectTab_title);
	    
	    //this._titleBar.setTitle(title);
	    

	    //a.recycle();
	    
	    
	    
	    
	   return parentView;
		
	}
	
	
}
