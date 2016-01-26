package apk.customerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebStorage.QuotaUpdater;


public class WebBrowserStartPage extends android.webkit.WebView
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
	
	public void setOnWebViewListener(OnWebViewListener onOnWebViewListener)
	{
		this._onOnWebViewListener = onOnWebViewListener;
	}
	
	public WebBrowserStartPage(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this._context = context;
		this.init();
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
				
				if (WebBrowserStartPage.this._onOnWebViewListener != null)
				{
					WebBrowserStartPage.this._onOnWebViewListener.OnLoaded();
				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return super.shouldOverrideUrlLoading(view, url);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
			{
				//super.onReceivedError(view, errorCode, description, failingUrl);
				
				view.loadUrl("file:///android_asset/html/net_error.html");
			}
		});
		
		this.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result)
			{
				return super.onJsAlert(view, url, message, result);
			}
		});
		
		this.setWebChromeClient(new WebChromeClient()
		{
			@Override
			public void onReceivedTitle(WebView view, String title)
			{
				super.onReceivedTitle(view, title);
			}
			
			@Override
			public void onReachedMaxAppCacheSize(long requiredStorage, long quota, QuotaUpdater quotaUpdater)
			{
				quotaUpdater.updateQuota(requiredStorage * 2);
			}
		});
	}
}
