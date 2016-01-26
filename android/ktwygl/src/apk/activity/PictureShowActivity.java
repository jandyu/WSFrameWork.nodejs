package apk.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
import apk.R;
import apk.common.DateHelper;
import apk.common.Util;
import apk.net.DownLoadFile;
import apk.net.DownLoadFile.OnDownLoadFileFinishListener;

public class PictureShowActivity extends BaseActivity
{
	private Handler _uiHanlder;
	private ImageView image_spacePage;
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.activity_picture_show);
		
		Intent intent = this.getIntent();
		String url = intent.getStringExtra("url");
		
		this._uiHanlder = new Handler(this.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				
				Bundle bundle = msg.getData();
				if (bundle.getBoolean("success"))
				{
					String fileName = bundle.getString("fileName");
					bitmap = BitmapFactory.decodeFile(fileName);
					image_spacePage.setImageBitmap(bitmap);
				}
				else
				{
					Toast.makeText(PictureShowActivity.this, "网络不络力，请稍后重试！", Toast.LENGTH_SHORT).show();
				}
				
				hideWaite();
			}
		};
		
		image_spacePage = (ImageView) findViewById(R.id.image_spacepage);
		image_spacePage.setOnTouchListener(new MulitPointTouchListener(image_spacePage));
		image_spacePage.setScaleType(ScaleType.CENTER_INSIDE);
		
		
		this.showWaite("正在加载...");
		
		final String fileName = Util.getAbsolutePath() + "/pics/" + "ShowPic_" + DateHelper.GetNowYYYYMMDDHHMMSS() + ".jpg";
		DownLoadFile downLoadFile = new DownLoadFile();
		downLoadFile.setOnDownLoadFileFinishListener(new OnDownLoadFileFinishListener()
		{
			@Override
			public void OnDownLoadFileFinish(boolean isSuccess)
			{
				Message msg = new Message();
				Bundle data = new Bundle();
				msg.setData(data);
				
				if (isSuccess)
				{
					data.putString("fileName", fileName);
					data.putBoolean("success", true);
				}
				else
				{
					data.putBoolean("success", false);
				}
				_uiHanlder.sendMessage(msg);
			}
		});
		downLoadFile.beginDownLoadFile(this, url, fileName);
	}
	
	public class MulitPointTouchListener implements OnTouchListener
	{
		
		Matrix matrix = new Matrix();
		Matrix savedMatrix = new Matrix();
		
		public ImageView image;
		static final int NONE = 0;
		static final int DRAG = 1;
		static final int ZOOM = 2;
		int mode = NONE;
		
		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist = 1f;
		
		public MulitPointTouchListener(ImageView image)
		{
			super();
			this.image = image;
		}
		
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			this.image.setScaleType(ScaleType.MATRIX);
			
			ImageView view = (ImageView) v;
			
			switch (event.getAction() & MotionEvent.ACTION_MASK)
			{
				case MotionEvent.ACTION_DOWN:
					
					Log.w("FLAG", "ACTION_DOWN");
					matrix.set(view.getImageMatrix());
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					mode = DRAG;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					Log.w("FLAG", "ACTION_POINTER_DOWN");
					oldDist = spacing(event);
					if (oldDist > 10f)
					{
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
					}
					break;
				case MotionEvent.ACTION_UP:
					Log.w("FLAG", "ACTION_UP");
				case MotionEvent.ACTION_POINTER_UP:
					Log.w("FLAG", "ACTION_POINTER_UP");
					mode = NONE;
					break;
				case MotionEvent.ACTION_MOVE:
					Log.w("FLAG", "ACTION_MOVE");
					if (mode == DRAG)
					{
						matrix.set(savedMatrix);
						matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
					}
					else if (mode == ZOOM)
					{
						float newDist = spacing(event);
						if (newDist > 10f)
						{
							matrix.set(savedMatrix);
							float scale = newDist / oldDist;
							matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}
					break;
			}
			
			view.setImageMatrix(matrix);
			return true;
		}
		
		private float spacing(MotionEvent event)
		{
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return (float)Math.sqrt(x * x + y * y);
		}
		
		private void midPoint(PointF point, MotionEvent event)
		{
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	}
	
	@Override
	protected void onStop()
	{
		if (bitmap != null)
		{
			if (!bitmap.isRecycled())
			{
				bitmap.recycle(); // 回收图片所占的内存
				bitmap = null;
				System.gc(); // 提醒系统及时回收
			}
		}
		super.onStop();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (bitmap != null)
		{
			if (!bitmap.isRecycled())
			{
				bitmap.recycle(); // 回收图片所占的内存
				bitmap = null;
				System.gc(); // 提醒系统及时回收
			}
		}
	}
}
