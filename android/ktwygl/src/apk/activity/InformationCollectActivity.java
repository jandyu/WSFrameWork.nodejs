package apk.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import apk.R;
import apk.bll.InformationCollectService;
import apk.bll.InformationCollectService.OnSavePicturesFinishListner;
import apk.common.ImageHelper;
import apk.customerview.DialogSelect;
import apk.customerview.DialogSelect.SelectedListener;
import apk.customerview.MessageBox;
import apk.customerview.MessageBoxButtons;
import apk.customerview.MessageBoxIcon;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;
import apk.customerview.WaiteBox;
import apk.dal.ConfigDAL;
import apk.model.MainMenuData;
import apk.model.PicData;
import apk.model.Protocol;

public class InformationCollectActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private RelativeLayout _mainFrame;
	private EditText _editContent;
	private Button _btnAddPhoto;
	private GridView _gridPicture;
	private List<PicData> _pictureList = new ArrayList<PicData>();
	private DialogSelect _dialogPhotoGetSelect;
	private WaiteBox _waiteBox;

	private static int RESULT_LOAD_IMAGE = 101;
	private static int RESULT_TAKE_PHOTO = 102;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_information_collect);
		
		

		this._gridPicture = (GridView) this.findViewById(R.id.gridPicture);

		this._mainFrame = (RelativeLayout) this.findViewById(R.id.mainFrame);

		MainMenuData mainMenuData = ConfigDAL.getNormalMainMenuData();
		if (mainMenuData != null)
		{
			this._mainFrame.setBackgroundColor(mainMenuData.getBackgroundColor());
		}

		this._titleBar = (TitleBar) this.findViewById(R.id.titleBar);
		this._titleBar.showLogo(false);
		this._titleBar.showOkButton(true);
		this._titleBar.setTextTitle(true, "信息采集");
		this._titleBar.setOkButtonTitle("提交", "");

		this._titleBar.setOnTitleBarListener(new OnTitleBarListener()
		{
			@Override
			public void OnBackClick(View v)
			{
				finish();
			}

			@Override
			public void OnPopupMenuClick(View v)
			{

			}

			@Override
			public void OnOk(View v, String clickUrl)
			{
				_waiteBox = new WaiteBox(InformationCollectActivity.this, "正在处理，请稍候...");
				_waiteBox.show();
				
				InformationCollectService informationCollectService = new InformationCollectService(InformationCollectActivity.this);
				informationCollectService.setOnSavePicturesFinishListner(new OnSavePicturesFinishListner()
				{
					@Override
					public void OnSavePictureFinish(boolean isSuccess)
					{
						_waiteBox.dismiss();
						if(isSuccess)
						{
							finish();
						}
						else
						{
							MessageBox.Show(InformationCollectActivity.this, "提示", "网络不给力，请重试！", MessageBoxButtons.OK, MessageBoxIcon.Error);
						}
					}
				});
				
				informationCollectService.upload(_editContent.getText().toString(), "", "", _pictureList);
				
			}
		});

		this._editContent = (EditText) this.findViewById(R.id.editContent);

		this._gridPicture = (GridView) this.findViewById(R.id.gridPicture);
		this._gridPicture.setAdapter(new BaseAdapter()
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				ImageView imageView;
				if (convertView == null)
				{
					imageView = new ImageView(InformationCollectActivity.this);
					imageView.setLayoutParams(new GridView.LayoutParams(_btnAddPhoto.getWidth(), _btnAddPhoto.getHeight()));
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				}
				else
				{
					imageView = (ImageView) convertView;
				}

				PicData pictureObj = (PicData) getItem(position);
				imageView.setImageBitmap(ImageHelper.zoomImage(pictureObj.getUpfile(), _btnAddPhoto.getWidth(), _btnAddPhoto.getHeight()));
				imageView.setTag(pictureObj);
				return imageView;
			}

			@Override
			public long getItemId(int position)
			{
				return position;
			}

			@Override
			public Object getItem(int position)
			{
				return _pictureList.get(position);
			}

			@Override
			public int getCount()
			{
				return _pictureList.size();
			}
		});

		this._gridPicture.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
			{
				DialogSelect dialogDelete = new DialogSelect(InformationCollectActivity.this, new int[] { 0 }, new String[] { "删除" });
				dialogDelete.setSelectedListener(new SelectedListener()
				{
					@Override
					public void onSelected(int value)
					{
						if (value == 0)
						{
							_pictureList.remove(position);
							((BaseAdapter) _gridPicture.getAdapter()).notifyDataSetChanged();
							setGridViewHeight();
						}
					}
				});
				dialogDelete.showAtLocation(InformationCollectActivity.this.findViewById(R.id.labBottom), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});

		this._btnAddPhoto = (Button) this.findViewById(R.id.btnAddPhoto);
		this._btnAddPhoto.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				hideSoftInput();
				_dialogPhotoGetSelect = new DialogSelect(InformationCollectActivity.this, new int[] { 0, 1 }, new String[] { "拍照", "从相册选择" });
				_dialogPhotoGetSelect.setSelectedListener(new SelectedListener()
				{
					@Override
					public void onSelected(int value)
					{
						Intent intent;
						switch (value)
						{
							case 0:
								intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Protocol.TakePhotoFileName)));
								startActivityForResult(intent, Protocol.Protocol_Result_Photo_Take_Photo);
								break;
							case 1:
								intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(intent, RESULT_LOAD_IMAGE);
								break;
						}
					}
				});
				_dialogPhotoGetSelect.showAtLocation(InformationCollectActivity.this.findViewById(R.id.labBottom), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				// _dialogPhotoGetSelect.showAsDropDown(anchor);
			}
		});
	}
	
	private void setGridViewHeight()
	{
		if(_pictureList.size() > 9)
		{
			this._gridPicture.getLayoutParams().height = this._btnAddPhoto.getHeight() * 3;
		}
		else
		{
			this._gridPicture.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		this.hideSoftInput();
		return super.onTouchEvent(event);
	}
	
	private void hideSoftInput()
	{
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(this._editContent.getWindowToken(), 0);
	}
	
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		PicData picData;
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null)
		{
			String pictureName = this.getPictureName(data);
			picData = new PicData();
			picData.setUpfile(pictureName);
		}
		else if (requestCode == RESULT_TAKE_PHOTO && resultCode == Activity.RESULT_OK)
		{
			String pictureName = Environment.getExternalStorageDirectory() + "/" + Protocol.TakePhotoFileName;

			picData = new PicData();
			picData.setUpfile(pictureName);
		}
		else
		{
			return;
		}

		this._pictureList.add(picData);
		((BaseAdapter) this._gridPicture.getAdapter()).notifyDataSetChanged();
		this.setGridViewHeight();
	}

	private String getPictureName(Intent data)
	{
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String pictureName = cursor.getString(columnIndex);
		cursor.close();

		return pictureName;
	}
}
