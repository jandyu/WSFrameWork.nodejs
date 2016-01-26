package apk.bll;

import java.util.Map;

import apk.common.JSONHelper;
import apk.dal.ConfigDAL;
import apk.model.PicData;
import apk.model.Size;
import apk.model.StringHashMap;
import apk.net.UploadFile;
import apk.net.UploadFile.OnUploadFileFinishListener;

public class PhotoService extends BaseService
{
	
	public interface OnUploadFileListener
	{
		public void OnUploadFileFinish(long resourceId, String fullPath, boolean isSuccess);
	}
	
	private OnUploadFileListener _onUploadFileListener;
	public void setOnUploadFileListener(OnUploadFileListener onUploadFileListener)
	{
		this._onUploadFileListener = onUploadFileListener;
	}
	
	
	public void uploadFile(final PicData picData, final long resourceId, final Size picMaxSize)
	{
		//上传图片文件
		UploadFile uploadFile = new UploadFile();
		uploadFile.setOnUploadFileFinishListener(new OnUploadFileFinishListener()
		{
			@Override
			public void OnUploadFileFinish(String responseText)
			{
				StringHashMap resHash = JSONHelper.parseObject(responseText, StringHashMap.class);
				if(resHash != null && resHash.containsKey("state") && "SUCCESS".equalsIgnoreCase(resHash.get("state")))//上传照片成功
				{
					final String fullPath = resHash.get("url");
						
					if(_onUploadFileListener != null)
					{
						_onUploadFileListener.OnUploadFileFinish(resourceId, fullPath, true);
					}
					
						
					
					
					
//					String urlPath = Util.getFileName(fullPath);
//					String imgId = getImageId(urlPath);
//					
//					
//					ParamUpdate update = new ParamUpdate();
//					update.setDefid("db_app.app_img");
//					
//					KeyValueSet item = new KeyValueSet();
//					item.put("imgid", imgId);//上传成功后 url 后面的文件名.jpg不要,即829c4f72-1bba-4a09-bbef-9301852ab69d
//					item.put("fullpath", fullPath);//  /upload1/2014-04-19/829c4f72-1bba-4a09-bbef-9301852ab69d.jpg
//					item.put("urlpath", urlPath);//829c4f72-1bba-4a09-bbef-9301852ab69d.jpg
//					item.put("info", "");//针对此图片的文字
//					//item.put("auth", UserDAL.getUserIId());//当前用户名：即app_user表的iid
//					item.put("auth", UserService.getUserId());//当前用户名：即app_user表的iid
//					item.put("dtm", DateHelper.GetNowYYYYMMDDHHMMSS());//发启时间
//					item.put("rtype", "");//类型。图片类型：模块ID
//					item.put("pid", resourceId);//所属的 app_resource.rid
//					item.put("relation_info", "");
//					
//					update.addInsertRow("db_app.app_img", item);
//					
//					String jsonStringImg = update.toPOSTString();
//					//写图片信息到表db_app.app_img
//					BaseAsyncNet post = new BaseAsyncNet(context, "");
//					post.setPostBackEvent(new HttpAsyncEvent()
//					{
//						@Override
//						public void postBackEvent(JsonData jsonData)
//						{
//							String json = jsonData.getJson();
//							UpdateResponseData updateResponseData = new UpdateResponseData(json);
//							boolean isLoginSuccess = updateResponseData.getIsSuccess();
//							if (isLoginSuccess)
//							{
//								picData.setUploadState(true);
//								
//								//updateSuccess(rid, picDataList);
//								
//								if(_onUploadFileListener != null)
//								{
//									_onUploadFileListener.OnUploadFileFinish(resourceId, fullPath, true);
//								}
//							}
//							else
//							{
//								if(_onUploadFileListener != null)
//								{
//									_onUploadFileListener.OnUploadFileFinish(resourceId, "", false);
//								}
//							}
//						}
//					});
//					post.Post(jsonStringImg);
				}
				else
				{
					if(_onUploadFileListener != null)
					{
						_onUploadFileListener.OnUploadFileFinish(resourceId, "", false);
					}
				}
			}
		});
		
		Map<String, String> fileMap = new StringHashMap();
		fileMap.put("upfile", picData.getUpfile());
		uploadFile.beginFormUpload(ConfigDAL.getUploadPictureUrl(), null, fileMap, picMaxSize);
	}
	
//	private String getImageId(String fileName)
//	{
//		if(fileName != null)
//		{
//			String[] fs = fileName.split("\\.");
//			if(fs.length > 0)
//			{
//				return fs[0];
//			}
//		}
//		return null;
//	}
}
