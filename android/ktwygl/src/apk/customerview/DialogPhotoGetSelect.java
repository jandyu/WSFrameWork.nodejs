package apk.customerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import apk.R;

public class DialogPhotoGetSelect extends PopupWindow
{
	
	public static final int TAKEPHOTO = 1;
	public static final int SELECTPHOTO = 2;
	
	public interface SelectedListener
	{
		public void onSelected(int value);
	}
	private SelectedListener _selectedListener;
	public void setSelectedListener(SelectedListener selectedListener)
	{
		this._selectedListener = selectedListener;
	}
	
	private Button _btnTakePhoto;
	private Button _btnSelectPhoto;
	private Button _btnCancel;
	private View mMenuView;
	@SuppressLint("InflateParams")
	public DialogPhotoGetSelect(Context context)
	{
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mMenuView = inflater.inflate(R.layout.dialog_select_getphoto, null);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		this.mMenuView.setOnTouchListener(new OnTouchListener()
		{
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event)
			{
				int height = mMenuView.findViewById(R.id.lineLine).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					if (y < height)
					{
						dismiss();
					}
				}
				return true;
			}
		});

		this._btnTakePhoto = (Button) this.mMenuView.findViewById(R.id.btnTakePhoto);
		this._btnTakePhoto.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(_selectedListener != null)
				{
					_selectedListener.onSelected(TAKEPHOTO);
				}
				dismiss();
			}
		});

		this._btnSelectPhoto = (Button) this.mMenuView.findViewById(R.id.btnSelectPhoto);
		this._btnSelectPhoto.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(_selectedListener != null)
				{
					_selectedListener.onSelected(SELECTPHOTO);
				}
				dismiss();
			}
		});

		this._btnCancel = (Button) this.mMenuView.findViewById(R.id.btnCancel);
		this._btnCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});

	}
}
