package apk.activity;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import apk.R;
import apk.bll.UpdateService;
import apk.bll.UpdateService.OnCheckUpdate;
import apk.bll.UpdateService.OnDownLoadAPK;
import apk.common.Util;
import apk.customerview.TitleBar;
import apk.customerview.TitleBar.OnTitleBarListener;

public class AboutActivity extends BaseActivity
{
	private TitleBar _titleBar;
	private TextView _labVersion;
	private Button _btnUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_about);

		this._titleBar = (TitleBar) this.findViewById(R.id.titleBar);
		this._titleBar.showLogo(false);
		this._titleBar.showBackButton(false);
		this._titleBar.showOkButton(false);
		this._titleBar.setTextTitle(true, "关于");
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

			}
		});

		this._labVersion = (TextView) this.findViewById(R.id.labVersion);
		this._labVersion.setText("当前版本：V" + Util.getVersionName());

		this._btnUpdate = (Button) this.findViewById(R.id.btnUpdate);
		this._btnUpdate.setVisibility(View.INVISIBLE);

		final UpdateService updateService = new UpdateService();
		updateService.setOnCheckUpdate(new OnCheckUpdate()
		{
			@Override
			public void OnCheckUpdateFinish(boolean success,
					boolean haveNewVersion)
			{
				if (success && haveNewVersion)// 有升级包
				{
					_btnUpdate.setText("有新版本已发布，立即升级");
					_btnUpdate.setVisibility(View.VISIBLE);
				}
			}
		});
		updateService.beginCheckUpdate(this);

		this._btnUpdate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showWaite("正在加载...");

				UpdateService updateService = new UpdateService();
				updateService.setOnDownLoadAPK(new OnDownLoadAPK()
				{
					@Override
					public void OnDownLoadAPKFinish(boolean success,
							String fileName)
					{
						if (success)
						{
							// String fileName = getSDPath()
							// +"/download/"+AppConfig.APKNAME;
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(
									Uri.fromFile(new File(fileName)),
									"application/vnd.android.package-archive");
							startActivity(intent);
						}
						else
						{
							Toast.makeText(AboutActivity.this,
									"网络不络力，更新包下载失败！", Toast.LENGTH_SHORT)
									.show();
						}
						hideWaite();
					}
				});
				updateService.beginDownLoadAPK(AboutActivity.this);
			}
		});
	}
}
