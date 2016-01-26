package apk.dal;

import java.util.List;

import apk.common.DateHelper;
import apk.model.MessageData;

public class MessageDAL
{
	static
	{
//		MessageDAL.createMessageTypeTable();
		MessageDAL.createMessageTable();
	}
	
//	public static boolean dropMessageTypeTable()
//	{
//		String dropTableSQL = "drop table if exists MessageType";
//		return DataBaseHelper.ExecuteSQL(dropTableSQL);
//	}
	
	private static boolean dropMessageTable()
	{
		String dropTableSQL = "drop table if exists Message";
		return DataBaseHelper.ExecuteSQL(dropTableSQL);
	}
	
//	public static boolean createMessageTypeTable()
//	{
//		//dropMessageTypeTable();
//		String createTable = "create table if not exists MessageType(" +
//				"messageTypeId varchar(20) primary key, " +
//				"messageTypeTitle varchar(20), " +
//				"messageTypeImage int" +
//				")";
//		return DataBaseHelper.ExecuteSQL(createTable);
//	}
	
	public static boolean createMessageTable()
	{
		//dropMessageTable();
		String createTable = "create table if not exists Message(" +
				"rid long primary key," +//关联服务端表 app_resource.rid
//				"messageTypeId varchar(20), " +
				"messageTitle varchar(20), " +
				"messageImage int," +
				"messageText varchar(20)," +
				"messageData varchar(1000)," +
				"messageTime datetime," +
				"read bit" + //是否已读，0:未读，1:已读
				")";
		return DataBaseHelper.ExecuteSQL(createTable);
	}
	
//	private static MessageData getLastMessageDataByType(String messageTypeId)
//	{
//		String sql = "select * from Message where messageTypeId=? order by messageTime desc";
//		return DataBaseHelper.SelectTop1(MessageData.class	, sql, new String[]{messageTypeId});
//	}
	
//	public static List<MessageTypeData> getMessageTypeList()
//	{
//		String sql = "select messageTypeId,messageTypeTitle,messageTypeImage from MessageType";
//		
//		List<MessageTypeData> list = DataBaseHelper.Select(MessageTypeData.class, sql);
//		for(MessageTypeData mtd : list)
//		{
//			MessageData md = getLastMessageDataByType(mtd.getMessageTypeId());
//			if(md != null)
//			{
//				mtd.setLastMessageText(md.getMessageText());
//				mtd.setLastMessageTime(md.getMessageTime());
//			}
//		}
//		return list;
//	}
	
//	public static List<MessageData> getMessageDataList(String messageTypeId)
//	{
//		String sql = "select * from Message where messageTypeId=?";
//		return DataBaseHelper.Select(MessageData.class	, sql, new String[]{messageTypeId});
//	}
	
	public static List<MessageData> getMessageDataList()
	{
		String sql = "select * from Message order by messageTime desc";
		return DataBaseHelper.Select(MessageData.class	, sql);
	}
	
//	private static MessageTypeData getMessageTypeById(String messageTypeId)
//	{
//		String sql = "select * from MessageType where messageTypeId=?";
//		
//		return DataBaseHelper.SelectTop1(MessageTypeData.class, sql, new String[]{messageTypeId});
//	}
	
	public static MessageData getMessageByRid(String rid)
	{
		String sql = "select * from Message where rid = ?";
		
		return DataBaseHelper.SelectTop1(MessageData.class, sql, new String[]{rid});
	}
	
//	public static boolean addMessageTypeData(MessageTypeData mtd)
//	{
//		String sql = "insert into MessageType(messageTypeId,messageTypeTitle,messageTypeImage) values(?,?,?)";
//		return DataBaseHelper.ExecuteSQL(sql, new String[]{mtd.getMessageTypeId(), mtd.getMessageTypeTitle(), mtd.getMessageTypeId()});
//	}
	
	public static boolean setRead(String rid)
	{
		String sql = "update Message set read = 1 where rid = ?";
		return DataBaseHelper.ExecuteSQL(sql,new String[]{ rid });
	}
	
	public static boolean addMessageData(MessageData md)
	{
//		if(getMessageTypeById(mtd.getMessageTypeId()) == null)
//		{
//			addMessageTypeData(mtd);
//		}
		
//		String sql = "insert into Message(messageTypeId,rid,messageTitle,messageImage,messageText,messageData,messageTime,read) values(?,?,?,?,?,?,?,?) ";
		String sql = "insert into Message(rid,messageTitle,messageImage,messageText,messageData,messageTime,read) values(?,?,?,?,?,?,?) ";
		return DataBaseHelper.ExecuteSQL(sql,new String[]{
//				md.getMessageTypeId(), 
				md.getRid(),
				md.getMessageTitle(), 
				md.getMessageImage(), 
				md.getMessageText(),
				md.getMessageData(),
				DateHelper.FormatDate(md.getMessageTime(), "yyyy-MM-dd HH:mm:ss"),
				"0"});
	}
	
	public static boolean deleteMessage(String rid)
	{
		String sql = "delete from Message where rid=?";
		return DataBaseHelper.ExecuteSQL(sql,new String[]{ rid });
	}
	
	public static boolean empty()
	{
		MessageDAL.dropMessageTable();
		MessageDAL.createMessageTable();
		
		return true;
	}
}
