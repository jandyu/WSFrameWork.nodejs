package apk.customerview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import apk.R;

public class DialogInputBox extends Dialog
{
	public interface OnDialogInputBoxOkListener
	{
		public void OnOk(String value);
	}
	
	private OnDialogInputBoxOkListener _onDialogInputBoxOkListener;
	public void setOnDialogInputBoxOkListener(OnDialogInputBoxOkListener onDialogInputBoxOkListener)
	{
		this._onDialogInputBoxOkListener = onDialogInputBoxOkListener;
	}
	
	
	private Button _btnOK;
	private EditText _txtText;
	public DialogInputBox(Context context, String title)
	{
		super(context);
		
		this.setContentView(R.layout.dialog_input_box);
		
		this.setTitle(title);
		
		
		this._txtText = (EditText) this.findViewById(R.id.txtText);
		this._btnOK = (Button) this.findViewById(R.id.btnOK);
		
		this._btnOK.setOnClickListener(new android.view.View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(_onDialogInputBoxOkListener != null)
				{
					_onDialogInputBoxOkListener.OnOk(_txtText.getText().toString());
				}
				dismiss();
			}
		});
	}
	
	public void setInputType(int inputType)
	{
		this._txtText.setInputType(inputType);
	}
	
	public void setText(String text)
	{
		this._txtText.setText(text);
	}
}
