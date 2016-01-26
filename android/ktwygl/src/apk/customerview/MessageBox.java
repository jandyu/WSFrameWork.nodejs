package apk.customerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import apk.R;




/**
 * <p>类名:MessageBox</p>
 * @author DongJiande
 * @version 1.0 2013年8月31日
 */
public class MessageBox
{
	private static AlertDialog.Builder _alert;
	private static MessageBoxResult _result;
	private static Handler _handler;
	
	
	/**
	 * 弹出消息框
	 * @param context 父窗体
	 * @param title 标题
	 * @param message 消息体
	 * @param buttons 按钮
	 * @param icon 图标
	 * @return 点击按钮
	 */
	public static MessageBoxResult Show(Context context, String title, String message, MessageBoxButtons buttons, MessageBoxIcon icon)
	{
		_alert = new AlertDialog.Builder(context);
		_alert.setTitle(title);
		_alert.setMessage(message);
		_alert.setCancelable(false);
		
		switch(icon)
		{
			case Error:
				_alert.setIcon(R.drawable.messagebox_error);
				break;
			case Information:
				_alert.setIcon(R.drawable.messagebox_information);
				break;
			case Question:
				_alert.setIcon(R.drawable.messagebox_question);
				break;
			case Warning:
				_alert.setIcon(R.drawable.messagebox_warning);
				break;
			default:
				break;
		}
		
		if(buttons == MessageBoxButtons.OK || buttons == MessageBoxButtons.OKCancel)
		{
			//Positive 确定的
			//Neutral 中立的
			//Negative 消极的
			_alert.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					_result = MessageBoxResult.OK;
					_handler.sendEmptyMessage(0);
				}
			});
		}
		else if(buttons == MessageBoxButtons.YesNo || buttons == MessageBoxButtons.YesNoCancel)
		{
			_alert.setPositiveButton("是", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					_result = MessageBoxResult.Yes;
					_handler.sendEmptyMessage(0);
				}
			});
			_alert.setNegativeButton("否", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					_result = MessageBoxResult.No;
					_handler.sendEmptyMessage(0);
				}
			});
		}
		
		if(buttons == MessageBoxButtons.OKCancel)
		{
			_alert.setNeutralButton("取消", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					_result = MessageBoxResult.Cancel;
					_handler.sendEmptyMessage(0);
				}
			});
		}
		
		if(buttons == MessageBoxButtons.YesNoCancel)
		{
			_alert.setNeutralButton("取消", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					_result = MessageBoxResult.Cancel;
					_handler.sendEmptyMessage(0);
				}
			});
		}
		
		
		_alert.show();
		
		_handler = new Handler(context.getMainLooper())
		{
			@Override
			public void handleMessage(Message msg)
			{
				throw new RuntimeException();//抛出异常，令Looper.loop()结束
			}
		};
		
		try
		{
			//Looper.prepare();//主线程已经与Looper绑定。所以，这里不需要此语句
			Looper.loop();//此会处理消息，直到调用quit()方法。但主线程不能调用quit()方法，所以，在消息里抛出RuntimeException异常
		}
		catch(RuntimeException ex)
		{
			
		}
		
		return _result;
	}
}


