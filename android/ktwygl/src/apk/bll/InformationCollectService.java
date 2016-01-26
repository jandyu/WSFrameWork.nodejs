package apk.bll;

import java.util.List;

import android.content.Context;
import apk.bll.PhotoService.OnUploadFileListener;
import apk.bll.param.KeyValueSet;
import apk.bll.param.ParamUpdate;
import apk.bll.param.UpdateResponseData;
import apk.common.DateHelper;
import apk.common.StringHelper;
import apk.model.PicData;
import apk.model.framework.JsonData;
import apk.net.BaseAsyncNet;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;

public class InformationCollectService extends BaseService
{
	
	public interface OnSavePicturesFinishListner
	{
		public void OnSavePictureFinish(boolean isSuccess);
	}
	
	private OnSavePicturesFinishListner _onSavePicturesFinishListner;
	public void setOnSavePicturesFinishListner(OnSavePicturesFinishListner onSavePicturesFinishListner)
	{
		this._onSavePicturesFinishListner = onSavePicturesFinishListner;
	}
	
	
	private Context _context;
	public InformationCollectService(Context context)
	{
		this._context = context;
	}
	public void upload(String info, String rtype, String relationInfo, List<PicData> picDataList)
	{
		this.beginSaveText(info, rtype, relationInfo, picDataList);
	}
	
	private void beginSaveText(String info, String rtype, String relationInfo, final List<PicData> picDataList)
	{
		ParamUpdate update = new ParamUpdate();
		update.setDefid("db_app.app_resource");
		
		KeyValueSet item = new KeyValueSet();
		item.put("title", "");//标题
		item.put("info", info);//内容
		//item.put("ver", "");
		//item.put("auth", UserDAL.getUserIId());//当前用户名：即app_user表的iid
		item.put("auth", CurrUserInfoService.getUserId(this._context));//当前用户名：即app_user表的iid
		item.put("dtm", DateHelper.GetNowYYYYMMDDHHMMSS());//发启时间
		item.put("rtype", rtype);//所在的页面的标识，即由哪个页面发启的消息。取网页的给定值
		item.put("relation_info", relationInfo);//如果是回复的消息，保存被回复的消息的rid
		if(picDataList != null && picDataList.size() > 0)
			item.put("status", "0");//图片没有传完
		else
			item.put("status", 1);//没有图片
		
		update.addInsertRow("db_app.app_resource", item);
		
		String jsonStringResource = update.toPOSTString();
		
		//Log.d("json", jsonString);
		
		final PhotoService photoService = new PhotoService();
		photoService.setOnUploadFileListener(new OnUploadFileListener()
		{
			@Override
			public void OnUploadFileFinish(long resourceId, String fileName, boolean isSuccess)
			{
				updateSuccess(resourceId, picDataList);
			}
		});
		
		
		//上传信息内容
		BaseAsyncNet post = new BaseAsyncNet(this._context, "");
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				String json = jsonData.getJson();
				UpdateResponseData updateResponseData = new UpdateResponseData(json);
				boolean isLoginSuccess = updateResponseData.getIsSuccess();
				if (isLoginSuccess)
				{
					//final String rid = updateResponseData.getMessage();
					
					final long rid = StringHelper.parseLong(updateResponseData.getMessage(), -1);
					
					//上传图片
					if(picDataList != null && picDataList.size() > 0)
					{
						for(int i=0,j=picDataList.size();i<j;i++)
						{
							final PicData picData = picDataList.get(i);
							
							
//							//上传图片文件
							photoService.uploadFile(picData, rid, null);
							
//							UploadFile uploadFile = new UploadFile();
//							uploadFile.setOnUploadFileFinishListener(new OnUploadFileFinishListener()
//							{
//								@Override
//								public void OnUploadFileFinish(String responseText)
//								{
//									
//									StringHashMap resHash = JSONHelper.parseObject(responseText, StringHashMap.class);
//									if(resHash != null && resHash.containsKey("state") && "SUCCESS".equalsIgnoreCase(resHash.get("state")))//上传照片成功
//									{
//										String fullPath = resHash.get("url");
//										String urlPath = Util.getFileName(fullPath);
//										String imgId = getImageId(urlPath);
//										
//										
//										ParamUpdate update = new ParamUpdate();
//										update.setDefid("db_app.app_img");
//										
//										KeyValueSet item = new KeyValueSet();
//										item.put("imgid", imgId);//上传成功后 url 后面的文件名.jpg不要,即829c4f72-1bba-4a09-bbef-9301852ab69d
//										item.put("fullpath", fullPath);//  /upload1/2014-04-19/829c4f72-1bba-4a09-bbef-9301852ab69d.jpg
//										item.put("urlpath", urlPath);//829c4f72-1bba-4a09-bbef-9301852ab69d.jpg
//										item.put("info", "");//针对此图片的文字
//										item.put("auth", UserDAL.getUserIId());//当前用户名：即app_user表的iid
//										item.put("dtm", DateHelper.GetNowYYYYMMDDHHMMSS());//发启时间
//										item.put("rtype", "");//类型。图片类型：模块ID
//										item.put("pid", rid);//所属的 app_resource.rid
//										item.put("relation_info", "");
//										
//										update.addInsertRow("db_app.app_img", item);
//										
//										String jsonStringImg = update.toPOSTString();
//										//写图片信息到表db_app.app_img
//										BaseAsyncNet post = new BaseAsyncNet(_context, "");
//										post.setPostBackEvent(new HttpAsyncEvent()
//										{
//											@Override
//											public void postBackEvent(JsonData jsonData)
//											{
//												String json = jsonData.getJson();
//												UpdateResponseData updateResponseData = new UpdateResponseData(json);
//												boolean isLoginSuccess = updateResponseData.getIsSuccess();
//												if (isLoginSuccess)
//												{
//													picData.setUploadState(true);
//													
//													updateSuccess(rid, picDataList);
//												}
//											}
//										});
//										post.Post(jsonStringImg);
//										
//									}
//								}
//							});
//							
//							Map<String, String> fileMap = new StringHashMap();
//							fileMap.put("upfile", picData.getUpfile());
//							uploadFile.beginFormUpload(ConfigDAL.getUploadPictureUrl(), null, fileMap);
							
						}
					}
					else//没有图片要上传，调用完成
					{
						if(_onSavePicturesFinishListner != null)
						{
							_onSavePicturesFinishListner.OnSavePictureFinish(true);
						}
					}
				}
			}
		});
		
		post.Post(jsonStringResource);
	}
	
	
	private void updateSuccess(long rid, final List<PicData> picDataList)
	{
		//只要有一个图片没有上传成功，则不更新 app_resource.status
		for(int i=0,j=picDataList.size();i<j;i++)
		{
			PicData picData = picDataList.get(i);
			if(!picData.isUploadState())
			{
				return;
			}
		}
		
		
		//所有图片都上传成功，则更新 app_resource.status 为1
		ParamUpdate update = new ParamUpdate();
		update.setDefid("db_app.app_resource");
		
		KeyValueSet item = new KeyValueSet();
		item.put("status", "1");//图片上传完成
		item.put("key_rid", rid);
		
		
		update.addUpdateRow("db_app.app_resource", item);
		
		String jsonString = update.toPOSTString();
		BaseAsyncNet post = new BaseAsyncNet(this._context, "");
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				String json = jsonData.getJson();
				UpdateResponseData updateResponseData = new UpdateResponseData(json);
				boolean isSuccess = updateResponseData.getIsSuccess();
				
				if(_onSavePicturesFinishListner != null)
				{
					_onSavePicturesFinishListner.OnSavePictureFinish(isSuccess);
				}
			}
		});
		post.Post(jsonString);
	}
}
