package apk.model;

import java.util.Date;

public class MessageData
{
	//private String messageId;
	private String rid;
//	private String messageTypeId;
	private String messageTitle;
	private String messageImage;
	private String messageText;
	private String messageData;
	private Date messageTime;
	private boolean isRead;
//	public String getMessageId()
//	{
//		return messageId;
//	}
//	public void setMessageId(String messageId)
//	{
//		this.messageId = messageId;
//	}
	public String getRid()
	{
		return rid;
	}
	public void setRid(String rid)
	{
		this.rid = rid;
	}

	
//	public String getMessageTypeId()
//	{
//		return messageTypeId;
//	}
//	public void setMessageTypeId(String messageTypeId)
//	{
//		this.messageTypeId = messageTypeId;
//	}
	public String getMessageTitle()
	{
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle)
	{
		this.messageTitle = messageTitle;
	}
	public String getMessageImage()
	{
		return messageImage;
	}
	public void setMessageImage(String messageImage)
	{
		this.messageImage = messageImage;
	}
	public String getMessageText()
	{
		return messageText;
	}
	public void setMessageText(String messageText)
	{
		this.messageText = messageText;
	}
	
	/**
	 * @return the messageData
	 */
	public String getMessageData()
	{
		return messageData;
	}
	/**
	 * @param messageData the messageData to set
	 */
	public void setMessageData(String messageData)
	{
		this.messageData = messageData;
	}
	public Date getMessageTime()
	{
		return messageTime;
	}
	public void setMessageTime(Date messageTime)
	{
		this.messageTime = messageTime;
	}
	public boolean isRead()
	{
		return isRead;
	}
	public void setRead(boolean isRead)
	{
		this.isRead = isRead;
	}
	
	
}
