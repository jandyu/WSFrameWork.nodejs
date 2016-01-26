package apk.bll;

import java.util.LinkedHashSet;

import cn.jpush.android.api.JPushInterface;
import android.content.Context;
import apk.bll.param.KeyValueSet;
import apk.bll.param.ParamProcedure;
import apk.bll.param.ParamSelect;
import apk.bll.param.ParamUpdate;
import apk.bll.param.SelectResponseData;
import apk.bll.param.UpdateResponseData;
import apk.bll.param.ParamSelect.AndOr;
import apk.bll.param.ParamSelect.Where;
import apk.common.JSONReader;
import apk.common.Util;
import apk.model.UserData;
import apk.model.framework.JsonData;
import apk.net.BaseAsyncNet;
import apk.net.HttpAsyncHelper.HttpAsyncEvent;

public class UserService extends BaseService
{
	private Context _context;
//	private Handler _uiHanlder;
	
	public UserService(Context context)
	{
		this._context = context;
		
		
//		this._uiHanlder = new Handler(context.getMainLooper())
//		{
//			@Override
//			public void handleMessage(Message msg)
//			{
//				super.handleMessage(msg);
//				
//				Bundle bundle = msg.getData();
//				switch (bundle.getInt("type"))
//				{
//					case 1://login
//						boolean isLoginSuccess = bundle.getBoolean("isLoginSuccess");
//						String loginMessage = bundle.getString("loginMessage");
//						if (_loginUserFinishListener != null)
//						{
//							_loginUserFinishListener.onLoginUserFinish(isLoginSuccess, loginMessage);
//						}
//						break;
//				}
//			}
//		};
	}
	
	public interface OnRegisterUser
	{
		public void OnRegisterUserFinsh(boolean success, String message);
	}
	
	private OnRegisterUser _onRegisterUser;
	public void setOnRegisterUser(OnRegisterUser onRegisterUser)
	{
		this._onRegisterUser = onRegisterUser;
	}
	
	/**
	 * 注册
	 * @param context
	 * @param userData
	 * @return
	 */
	public void beginRegister(UserData userData)
	{
		ParamProcedure procedure = new ParamProcedure();
		procedure.setDefid("db_app");
		procedure.setDStyle("xml");
		
		KeyValueSet param = new KeyValueSet();
		param.put("name", Util.isNull(userData.get_name(), ""));
		param.put("unit", Util.isNull(userData.get_unit_id(), ""));
		param.put("mobi", Util.isNull(userData.get_phone_no(), ""));
		param.put("pwd", Util.isNull(userData.get_passwd(), ""));
		param.put("udid", Util.isNull(userData.get_device_id(), ""));
		
		procedure.addProcedure("usp_hy_regowner", param);
		
		String json = procedure.toPOSTString();
		
		BaseAsyncNet post = new BaseAsyncNet(this._context, "");
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				String json = jsonData.getJson();
				
				UpdateResponseData updateResponseData = new UpdateResponseData(json);
				boolean success = updateResponseData.getIsSuccess();
				
				if(success)
				{
					CurrUserInfoService.beginInitUserInfo(_context);
				}
				
				if (_onRegisterUser != null)
				{
					
					_onRegisterUser.OnRegisterUserFinsh(success, updateResponseData.getMessage());
				}
			}
		});
		post.Post(json);
	}
	
	public interface OnLogoff
	{
		public void OnLogoffFinish(boolean success, String message);
	}
	private OnLogoff _onLogoff;
	public void setOnLogoff(OnLogoff onLogoff)
	{
		this._onLogoff = onLogoff;
	}
	
	public void beginLogoff()
	{
		UserService userService = new UserService(this._context);
		userService.setOnUpdateUser(new OnUpdateUser()
		{
			@Override
			public void OnUpdateUserFinish(boolean success, String message)
			{
				if(success)
				{
					
					//注销推送
					JPushInterface.setAliasAndTags(_context, null, new LinkedHashSet<String>(), null);//Tag 组
					JPushInterface.setAliasAndTags(_context, "", null, null);//设备别名
					
					if(_onLogoff != null)
					{
						_onLogoff.OnLogoffFinish(success, message);
					}
				}
			}
		}); 
		userService.beginUpdateUser("device_id", "");
	}
	
	public interface OnGetUserFromServer
	{
		public void OnGetUserFromServerFinish(UserData userData);
	}
	private OnGetUserFromServer _onGetUserFromServer;
	public void setOnGetUserFromServer(OnGetUserFromServer onGetUserFromServer)
	{
		this._onGetUserFromServer = onGetUserFromServer;
	}
	
	public void beginGetUserFromServer(final String deviceId)
	{
		ParamSelect pp = new ParamSelect();
		pp.setDefid("app_user");
		pp.setFormatId("json");
		pp.setDStyle("json");
		
		Where where = pp.newWhere();
		where.addCondition("device_id", "=", deviceId, AndOr.NULL);
		
		pp.setCommonSelect(where);
		
		String json = pp.toPOSTString();
		
		BaseAsyncNet post = new BaseAsyncNet(this._context, "callback=rtn");
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				try
				{
					SelectResponseData selectResponseData = new SelectResponseData(jsonData.getJson());
					
					if(_onGetUserFromServer != null)
					{
						UserData userData = new UserData();
						
						JSONReader jsonReader = new JSONReader(selectResponseData.getFirstTableFirstRow());
						
						userData.set_device_id(deviceId);
						userData.set_head_pic(jsonReader.getString("head_pic", ""));
						userData.set_name(jsonReader.getString("name", ""));
						userData.set_nick_name(jsonReader.getString("nick_name", ""));
						userData.set_phone_no(jsonReader.getString("phone_no", ""));
						userData.set_uid(jsonReader.getLong("uid", 0));
						userData.set_unit_id(jsonReader.getString("unit_id", ""));
						userData.set_unit_title(jsonReader.getString("unit_title", ""));
						userData.set_passwd(jsonReader.getString("passwd", ""));
						
						_onGetUserFromServer.OnGetUserFromServerFinish(userData);
					}
				}
				catch(Exception ex)
				{
					_onGetUserFromServer.OnGetUserFromServerFinish(null);
				}
			}
		});
		
		post.Post(json);
	}
	
	public interface OnUpdateUser
	{
		public void OnUpdateUserFinish(boolean success, String message);
	}
	private OnUpdateUser _onUpdateUser;
	public void setOnUpdateUser(OnUpdateUser onUpdateUser)
	{
		this._onUpdateUser = onUpdateUser;
	}
	
	public void beginUpdateUser(final String key, final String value)
	{
		ParamUpdate update = new ParamUpdate();
		update.setDefid("app_user");
		
		KeyValueSet item = new KeyValueSet();
		item.set("key_device_id", Util.getDeviceId());
		item.set(key, value);
		
		update.addUpdateRow("app_user", item);
		
		BaseAsyncNet post = new BaseAsyncNet(this._context, "callback=rtn");
		post.setPostBackEvent(new HttpAsyncEvent()
		{
			@Override
			public void postBackEvent(JsonData jsonData)
			{
				try
				{
					UpdateResponseData updateResponseData = new UpdateResponseData(jsonData.getJson());
					
					if(_onUpdateUser != null)
					{
						_onUpdateUser.OnUpdateUserFinish(updateResponseData.getIsSuccess(), updateResponseData.getMessage());
					}
				}
				catch(Exception ex)
				{
					_onUpdateUser.OnUpdateUserFinish(false, "更新失败");
				}
			}
		});
		
		post.Post(update.toPOSTString());
	}
	
//	public interface SaveUserFinishListener
//	{
//		public void onSaveUserFinish(boolean success);
//	}
//	
//	private SaveUserFinishListener _saveUserFinishListener;
//	
//	public void setSaveUserFinishListener(SaveUserFinishListener saveUserFinishListener)
//	{
//		this._saveUserFinishListener = saveUserFinishListener;
//	}
//	
//	public interface LoginUserFinishListener
//	{
//		public void onLoginUserFinish(boolean success, String loginMessage);
//	}
//	
//	private LoginUserFinishListener _loginUserFinishListener;
//	
//	public void setLoginUserFinishListener(LoginUserFinishListener loginUserFinishListener)
//	{
//		this._loginUserFinishListener = loginUserFinishListener;
//	}
//	
//	public interface LogOffUserFinishListener
//	{
//		public void onLogOffUserFinish(boolean success);
//	}
//	
//	private LogOffUserFinishListener _logOffUserFinishListener;
//	
//	public void setLogOffUserFinishListener(LogOffUserFinishListener logOffUserFinishListener)
//	{
//		this._logOffUserFinishListener = logOffUserFinishListener;
//	}
	
//	public void registerUser(final UserInfoData userInfoData)
//	{
//		ParamProcedure procedure = new ParamProcedure();
//		procedure.setDefid("db_app");
//		procedure.setDStyle("xml");
//		
//		KeyValueSet param = new KeyValueSet();
//		param.put("phone_no", userInfoData.getPhone_no());
//		param.put("passwd", userInfoData.getPasswd());
//		param.put("nick_name", userInfoData.getNick_name());
//		param.put("sign_name", userInfoData.getSign_name());
//		param.put("head_pic", userInfoData.getHead_pic());
//		
//		procedure.addProcedure("P_registerUser", param);
//		
//		String json = procedure.toPOSTString();
//		
//		BaseAsyncNet post = new BaseAsyncNet(this._context, "");
//		post.setPostBackEvent(new HttpAsyncEvent()
//		{
//			@Override
//			public void postBackEvent(JsonData jsonData)
//			{
//				String json = jsonData.getJson();
//				// AppUserDAL.save(userInfoData);
//				if (_registerUserFinishListener != null)
//				{
//					UpdateResponseData updateResponseData = new UpdateResponseData(json);
//					_registerUserFinishListener.onRegisterUserFinish(updateResponseData.getIsSuccess());
//				}
//			}
//		});
//		post.Post(json);
//	}
	
//	private String getUserExtendInfoXML(HashMap<String, String> userExtendInfo)
//	{
//		try
//		{
//			DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document document = xmlDocumentBuilder.newDocument();
//			
//			Element root = document.createElement("userInfo");
//			
//			Iterator<String> iterator = userExtendInfo.keySet().iterator();
//			while (iterator.hasNext())
//			{
//				Element item = document.createElement("item");
//				
//				String key = iterator.next();
//				item.setAttribute("property", key);
//				item.setAttribute("val", userExtendInfo.get(key));
//				root.appendChild(item);
//			}
//			
//			document.appendChild(root);
//			
//			StringWriter writer = new StringWriter();
//			Transformer transformer = TransformerFactory.newInstance().newTransformer();
//			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
//			transformer.transform(new DOMSource(document), new StreamResult(writer));
//			return writer.toString();
//		}
//		catch (Exception e)
//		{
//			return "";
//		}
//	}
	
//	public void saveUserServer(final UserInfoData userInfoData)
//	{
//		ParamProcedure procedure = new ParamProcedure();
//		procedure.setDefid("db_app");
//		procedure.setDStyle("xml");
//		
//		KeyValueSet param = new KeyValueSet();
//		param.put("device_id", userInfoData.getDevice_id());
//		param.put("phone_no", userInfoData.getPhone_no());
//		param.put("passwd", userInfoData.getPasswd());
//		param.put("name", userInfoData.getName());
//		param.put("nick_name", userInfoData.getNick_name());
//		param.put("sign_name", userInfoData.getSign_name());
//		param.put("birthday", userInfoData.getBirthday());
//		param.put("head_pic", userInfoData.getHead_pic());
//		param.put("unit_id", userInfoData.getUnit_id());
//		param.put("units", userInfoData.getUnits());
//		param.put("oid", userInfoData.getOid());
//		param.put("status", userInfoData.getStatus());
//		param.set("memo0", userInfoData.getMemo0());
//		param.set("memo1", userInfoData.getMemo1());
//		param.set("memo2", userInfoData.getMemo2());
//		param.set("memo3", userInfoData.getMemo3());
//		param.set("memo4", userInfoData.getMemo4());
//		param.set("memo5", userInfoData.getMemo5());
//		param.set("memo6", userInfoData.getMemo6());
//		param.set("memo7", userInfoData.getMemo7());
//		param.set("memo8", userInfoData.getMemo8());
//		param.set("memo9", userInfoData.getMemo9());
//		//param.put("user_extend", StringHelper.StringToBase64String(this.getUserExtendInfoXML(userExtendInfo)));
//		//param.put("user_extend", this.getUserExtendInfoXML(userExtendInfo));
//		
//		procedure.addProcedure("P_user_ui", param);
//		
//		String json = procedure.toPOSTString();
//		
//		BaseAsyncNet post = new BaseAsyncNet(this._context, "");
//		post.setPostBackEvent(new HttpAsyncEvent()
//		{
//			@Override
//			public void postBackEvent(JsonData jsonData)
//			{
//				String json = jsonData.getJson();
//				UpdateResponseData updateResponseData = new UpdateResponseData(json);
//				if (updateResponseData.getIsSuccess())
//				{
//					UserDAL.save(userInfoData);
//					//AppUserExtendDAL.save(userExtendInfo);
//				}
//				if (_saveUserFinishListener != null)
//				{
//					_saveUserFinishListener.onSaveUserFinish(updateResponseData.getIsSuccess());
//				}
//			}
//		});
//		post.Post(json);
//	}
//	
//	public UserInfoData getUserInfoFromServer(String deviceId)
//	{
//		ParamSelect pp = new ParamSelect();
//		pp.setDefid("app_user");
//		pp.setFormatId("json");
//		pp.setDStyle("json");
//		
//		Where where = pp.newWhere();
//		where.addCondition("device_id", "=", deviceId, AndOr.NULL);
//		//where.addCondition("status", "=", "1", AndOr.NULL);
//		
//		//where.addCondition("phone_no", "=", mobile, AndOr.AND);
//		//where.addCondition("passwd", "=", passwd, AndOr.NULL);
//		
//		pp.setCommonSelect(where);
//		
//		String json = pp.toPOSTString();
//		
//		BaseSyncNet post = new BaseSyncNet("callback=rtn");
//		JsonData jsonData = post.Post(json);
//		
//		
//		String jsonString = jsonData.getJson();
//		
//		try
//		{
//			SelectResponseData rd = new SelectResponseData(jsonString);
//			if (rd.getIsSuccess())
//			{
//				JSONObject jsonObject = rd.getFirstTableFirstRow();
//				
//				UserInfoData userInfoData = new UserInfoData();
//				userInfoData.setIid(jsonObject.getLong("iid"));
//				userInfoData.setDevice_id(jsonObject.getString("device_id"));
//				userInfoData.setPhone_no(jsonObject.getString("phone_no"));
//				userInfoData.setPasswd(jsonObject.getString("passwd"));
//				userInfoData.setHead_pic(jsonObject.getString("head_pic"));
//				userInfoData.setName(jsonObject.getString("name"));
//				userInfoData.setNick_name(jsonObject.getString("nick_name"));
//				userInfoData.setSign_name(jsonObject.getString("sign_name"));
//				userInfoData.setBirthday(jsonObject.getString("birthday"));
////				userInfoData.setUser_group_id(jsonObject.getString("user_group_id"));
//				userInfoData.setUnit_id(StringHelper.parseInt(jsonObject.getString("unit_id"), -1));
//				userInfoData.setUnits(jsonObject.getString("unit_ids"));
//				userInfoData.setOid(StringHelper.parseLong(jsonObject.getString("oid"), -1));
//				userInfoData.setStatus(jsonObject.getString("status"));
//				userInfoData.setMemo0(jsonObject.getString("memo0"));
//				userInfoData.setMemo1(jsonObject.getString("memo1"));
//				userInfoData.setMemo2(jsonObject.getString("memo2"));
//				userInfoData.setMemo3(jsonObject.getString("memo3"));
//				userInfoData.setMemo4(jsonObject.getString("memo4"));
//				userInfoData.setMemo5(jsonObject.getString("memo5"));
//				userInfoData.setMemo6(jsonObject.getString("memo6"));
//				userInfoData.setMemo7(jsonObject.getString("memo7"));
//				userInfoData.setMemo8(jsonObject.getString("memo8"));
//				userInfoData.setMemo9(jsonObject.getString("memo9"));
//				
//				if(UserDAL.save(userInfoData))
//				{
//					String localHeadPic = userInfoData.getHead_pic(ServerOrLocal.local);
//					File filePic = new File(localHeadPic);
//					if(!filePic.exists())
//					{
//						//下载头像
//						DownLoadFile.downLoadFile(userInfoData.getHead_pic(ServerOrLocal.server), localHeadPic);
//					}
//				}
//				
//				return userInfoData;
//			}
//			else
//			{
//				return null;
//			}
//		}
//		catch (Exception e)
//		{
//			Log.d("login", e.getMessage());
//			return null;
//		}
//	}
	
//	public void getUserExtendInfoFromServer(String mobile)
//	{
//		ParamSelect pp = new ParamSelect();
//		pp.setDefid("db_app.app_user_info");
//		pp.setFormatId("json");
//		pp.setDStyle("json");
//		
//		Where where = pp.newWhere();
//		// where.addCondition("device_id", "=", deviceId, AndOr.AND);
//		where.addCondition("phone_no", "=", mobile, AndOr.NULL);
//		
//		pp.setCommonSelect(where);
//		
//		String json = pp.toPOSTString();
//		
//		BaseSyncNet post = new BaseSyncNet("callback=rtn");
//		JsonData jsonData = post.Post(json);
//		
//		String jsonString = jsonData.getJson();
//		
//		try
//		{
//			SelectResponseData rd = new SelectResponseData(jsonString);
//			if (rd.getIsSuccess())
//			{
//				JSONArray jsonList = rd.getFirstTableDataList();
//				HashMap<String, String> userExtendData = new HashMap<String, String>();
//				//AppUserExtendDAL.delete();
//				for(int i=0,j=jsonList.length();i<j;i++)
//				{
//					JSONObject jsonObject = jsonList.getJSONObject(i);
//					
//					String key = jsonObject.getString("property");
//					String val = jsonObject.getString("val");
//					
//					if(key != null)
//						userExtendData.put(key, val);
//				}
//				
//				AppUserExtendDAL.save(userExtendData);
//			}
//		}
//		catch (Exception e)
//		{
//			Log.d("login", e.getMessage());
//		}
//	}
	
//	public void checkLogin(String deviceId)
//	{
//		this.getUserInfoFromServer(deviceId);
//		
////		UserInfoData userInfoData = this.getUserInfoFromServer(deviceId);
////		if(userInfoData != null)
////		{
////			this.getUserExtendInfoFromServer(userInfoData.getPhone_no());
////		}
//	}
	
//	public void login(final String deviceId, final String mobile, final String passwd)
//	{
//		ParamProcedure procedure = new ParamProcedure();
//		procedure.setDefid("db_app");
//		procedure.setDStyle("xml");
//		
//		KeyValueSet param = new KeyValueSet();
//		param.put("device_id", deviceId);
//		param.put("phone_no", mobile);
//		param.put("passwd", passwd);
//		
//		procedure.addProcedure("P_deviceIdBind", param);
//		
//		final String json = procedure.toPOSTString();
//		
//		Thread thread = new Thread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				BaseSyncNet post = new BaseSyncNet("");
//				
//				JsonData jsonData = post.Post(json);
//				
//				String json = jsonData.getJson();
//				// AppUserDAL.save(userInfoData);
//				UpdateResponseData updateResponseData = new UpdateResponseData(json);
//				
//				String loginMessage = "";
//				boolean isLoginSuccess = false;
//				
//				if(updateResponseData.getIsSuccess())
//				{
//					if("1".equals(updateResponseData.getMessage()))//登录成功
//					{
//						getUserInfoFromServer(deviceId);
//						//getUserExtendInfoFromServer(mobile);
//						isLoginSuccess = true;
//						loginMessage = "";
//					}
//					else if("2".equals(updateResponseData.getMessage()))//未审核
//					{
//						isLoginSuccess = false;
//						loginMessage = "用户尚未审核，请稍候再试！";
//					}
//					else if("0".equals(updateResponseData.getMessage()))//用户名或密码不正确！
//					{
//						isLoginSuccess = false;
//						loginMessage = "用户名或密码不正确！";
//					}
//				}
//				else
//				{
//					isLoginSuccess = false;
//					loginMessage = "用户名或密码不正确！";
//				}
//				
//				Message msg = new Message();
//				Bundle data = new Bundle();
//				data.putInt("type", 1);
//				data.putBoolean("isLoginSuccess", isLoginSuccess);
//				data.putString("loginMessage", loginMessage);
//				msg.setData(data);
//				_uiHanlder.sendMessage(msg);
//			}
//		});
//		thread.start();
//	}
//	
//	public void logOff()
//	{
//		ParamProcedure procedure = new ParamProcedure();
//		procedure.setDefid("db_app");
//		procedure.setDStyle("xml");
//		
//		KeyValueSet param = new KeyValueSet();
//		param.put("device_id", Util.getDeviceId());
//		
//		procedure.addProcedure("P_deviceIdUnBind", param);
//		
//		String json = procedure.toPOSTString();
//		
//		BaseAsyncNet post = new BaseAsyncNet(this._context, "");
//		post.setPostBackEvent(new HttpAsyncEvent()
//		{
//			@Override
//			public void postBackEvent(JsonData jsonData)
//			{
//				String json = jsonData.getJson();
//				// AppUserDAL.save(userInfoData);
//				UpdateResponseData updateResponseData = new UpdateResponseData(json);
//				
//				if (_logOffUserFinishListener != null)
//				{
//					_logOffUserFinishListener.onLogOffUserFinish(updateResponseData.getIsSuccess());
//				}
//			}
//		});
//		post.Post(json);
//	}
}
