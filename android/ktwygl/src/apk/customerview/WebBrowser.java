package apk.customerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import apk.bll.PhotoService;
import apk.bll.PhotoService.OnUploadFileListener;
import apk.common.StringHelper;
import apk.i.IActivity;
import apk.ibrowser.NativeInterface;
import apk.model.PicData;
import apk.model.Protocol;
import apk.model.Size;

public class WebBrowser extends android.webkit.WebView implements apk.pullorpush.Pullable
{
	private Context _context;
	
	
	public interface OnWebViewListener
	{
		/**
		 * 得到标题时
		 * @param title 标题
		 */
		public void OnReceivedTitle(String title);
		/**
		 * 页面加载完成
		 */
		public void OnLoaded();
	}
	
	private OnWebViewListener _onOnWebViewListener;
	
	/*
	 * 设置WebView侦听事件，侦听页面加载完毕和
	 */
	public void setOnWebViewListener(OnWebViewListener onOnWebViewListener)
	{
		this._onOnWebViewListener = onOnWebViewListener;
	}
	
	private Protocol _protocol = new Protocol();
	public Protocol getProtocol()
	{
		return this._protocol;
	}
	
	public WebBrowser(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this._context = context;
		this.init();
		
		this._protocol.setCurrentWebView(this);
	}
	
	//private String rid;//写回HTML页面的对象id
	
	private NativeInterface _nativeInterface;
	public void setNativeInterface(NativeInterface nativeInterface)
	{
		this._nativeInterface = nativeInterface;
	}
	
	public NativeInterface getNativeInterface()
	{
		return this._nativeInterface;
	}
	
	

	public void setActivity(IActivity activity)
	{
		this._protocol.setActivity(activity);
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void init()
	{
		WebSettings webSettings = this.getSettings();
		
		webSettings.setJavaScriptEnabled(true);// 令WebView可以执行JavaScript
		webSettings.setDomStorageEnabled(true);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 80);
		webSettings.setAppCachePath(this._context.getDir("cache", Context.MODE_PRIVATE).getPath());
		webSettings.setAllowFileAccess(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		
		
		this._progressDialog = new ProgressDialog(this._context);
		
		this.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				return true;
			}
		});
		
		this.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				
				if (WebBrowser.this._onOnWebViewListener != null)
				{
					WebBrowser.this._onOnWebViewListener.OnLoaded();
				}
				
				hideWaite();
			}
			
			

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
				showWaite("正在加载...");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				String lowerUrl = StringHelper.toLowerCase(url); 
				if(lowerUrl.startsWith("tel:") || lowerUrl.startsWith("mailto:") || lowerUrl.startsWith("geo:"))
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					view.getContext(). startActivity(intent);
					return true;
				}
				
//				rid = "";
				
				boolean isStopLoad = _protocol.getProtocol(url);
				
//				rid = _protocol.getReturnObjectId();
				
				return isStopLoad;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				super.onReceivedError(view, errorCode, description, failingUrl);
				//view.loadUrl("file:///android_asset/html/net_error.html");//无网络时，显示
				//Toast.makeText(_context, "网络不给力！", Toast.LENGTH_SHORT).show();
				//view.stopLoading();
				
				//view.getc
			}
		});
		
		this.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result)
			{
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public void onReachedMaxAppCacheSize(long requiredStorage, long quota, QuotaUpdater quotaUpdater)
			{
				quotaUpdater.updateQuota(requiredStorage * 2);
			}
		});
		
		// this.setBackgroundColor(Color.parseColor("#B0C4DE"));
		
		this.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				super.onReceivedTitle(view, title);
				
				if (WebBrowser.this._onOnWebViewListener != null)
				{
					WebBrowser.this._onOnWebViewListener.OnReceivedTitle(title);
				}
			}
		});
	}
	
	private ProgressDialog _progressDialog;
	private void showWaite(String message)
	{
		this._progressDialog.setMax(100);
		this._progressDialog.setMessage(message);
		this._progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this._progressDialog.setCancelable(false);
		this._progressDialog.show();
	}

	
	private void hideWaite()
	{
		this._progressDialog.dismiss();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode)
		{
			case Protocol.Protocol_Result_Scan:
				
				if(data != null)
				{
					String barcode = data.getStringExtra("value");
					
					
					String callback = _protocol.getParamHashMap().get("callback");
					if(!StringHelper.IsNullOrEmptyOrBlank(callback))
					{
						//_nativeInterface.jsCall(callback.replace("#_val_#", fileName));
						_nativeInterface.jsCall(callback+"('"+barcode+"')");
					}
					
					//this.nativeInterface.beginPageTo(barcode);
					
					
					//this._protocol = new Protocol();
					//this._protocol.setContext(_context);
					//this._protocol.setFragment(_fragment);
					//this._protocol.setCurrentWebView(this);
					
					
//					boolean isStopLoad = this._protocol.getProtocol(barcode);
					
					
					//if(!this._protocol.getProtocol(barcode))
					//{
					//	this.loadUrl(barcode);
					//}
					
//					rid = this._protocol.getReturnObjectId();
					
	//				if(this.rid != null && this.rid.length() > 0)
	//				{
	//					this.nativeInterface.beginNativeSet(this.rid, barcode);//扫描后，回填HTML页
	//				}
	//				else
	//				{
	//					this.loadUrl(barcode);
	//				}
				}
				break;
			case Protocol.Protocol_Result_Photo_Load_Image:
			case Protocol.Protocol_Result_Photo_Take_Photo:
				
				final WaiteBox _waiteBox = new WaiteBox(this._context, "正在处理，请稍候...");
				
				
				PicData picData = null;
				PhotoService photoService = new PhotoService();
				photoService.setOnUploadFileListener(new OnUploadFileListener()
				{
					@Override
					public void OnUploadFileFinish(long resourceId, String fileName, boolean isSuccess)
					{
						String callback = _protocol.getParamHashMap().get("callback");
						if(!StringHelper.IsNullOrEmptyOrBlank(callback))
						{
							//_nativeInterface.jsCall(callback.replace("#_val_#", fileName));
							_nativeInterface.jsCall(callback+"('"+fileName+"')");
						}
						
						_waiteBox.dismiss();
					}
				});
				if (requestCode == Protocol.Protocol_Result_Photo_Load_Image && resultCode == Activity.RESULT_OK && data != null)
				{
					String pictureName = this.getPictureName(data, requestCode);
					picData = new PicData();
					picData.setUpfile(pictureName);
				}
				else if (requestCode == Protocol.Protocol_Result_Photo_Take_Photo && resultCode == Activity.RESULT_OK)// && data != null)
				{
					String pictureName = this.getPictureName(data, requestCode);
					picData = new PicData();
					picData.setUpfile(pictureName);
				}
				if(picData != null)
				{
					_waiteBox.show();
					String size = _protocol.getParamHashMap().get("size");
					if(!StringHelper.IsNullOrEmptyOrBlank(size))
					{
						int width = StringHelper.parseInt(size, 0);
						Size picMaxSize = new Size(width, width);
						photoService.uploadFile(picData, 0, picMaxSize);
					}
					else
					{
						photoService.uploadFile(picData, 0, null);
					}
				}
				break;
				
				
				
//				String pictureName = null;
//				
//				
//				if (requestCode == Protocol.Protocol_Result_Photo_Load_Image && resultCode == Activity.RESULT_OK && data != null)
//				{
//					pictureName = this.getPictureName(data, requestCode);
//
//				}
//				else if (requestCode == Protocol.Protocol_Result_Photo_Take_Photo && resultCode == Activity.RESULT_OK)// && data != null)
//				{
//					pictureName = this.getPictureName(data, requestCode);
//
//				}
//				if(pictureName != null)
//				{
//					String callback = _protocol.getParamHashMap().get("callback");
//					_nativeInterface.jsCall(callback.replace("#_val_#", "file:///android_asset/css/start.jpg"));
//					
//					
//					Log.d("", pictureName);
//				}
//				break;
		}
	}
	
	private String getPictureName(Intent data, int requestCode)
	{
		if(requestCode == Protocol.Protocol_Result_Photo_Take_Photo)
		{
			String p = Environment.getExternalStorageDirectory() + "/" + Protocol.TakePhotoFileName;
			return p;
		}
		else if(requestCode == Protocol.Protocol_Result_Photo_Load_Image)
		{
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Cursor cursor = this._context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String pictureName = cursor.getString(columnIndex);
			cursor.close();

			return pictureName;
		}
		
		return "";
	}

	@Override
	public boolean canPullDown()
	{
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getScrollY() >= getContentHeight() * getScale()
				- getMeasuredHeight())
			return true;
		else
			return false;
	}
}
