package com.yale.qcxxandroid.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "xmppMsgBean")
public class XmppMsgBean implements Serializable {

	public XmppMsgBean() {
		super();
	}

	public XmppMsgBean(int msgtype, String chatTopic, String type,
			String timeSend, String content, String fileSize, String toUserId,
			String fromUserId, String timeLen, boolean isReaded) {
		this.msgtype = msgtype;
		this.chatTopic = chatTopic;
		this.type = type;
		this.timeSend = timeSend;
		this.content = content;
		this.fileSize = fileSize;
		this.toUserId = toUserId;
		this.fromUserId = fromUserId;
		this.timeLen = timeLen;
		this.isReaded = isReaded;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6302853267103986711L;
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(columnName = "msgtype", canBeNull = false)
	private int msgtype; // 类型 （发送的消息消息 (0) 还是接收到的消息(1)）
	@DatabaseField(columnName = "chatTopic", canBeNull = false)
	private String chatTopic; // 一对一聊天的唯一标识 消费者topic 群聊天使用群ID
	@DatabaseField(columnName = "type", canBeNull = false)
	private String type; // 消息类型
	@DatabaseField(columnName = "timeSend", canBeNull = false)
	private String timeSend; // 发送的时间
	@DatabaseField(columnName = "content", canBeNull = false)
	private String content; // 消息主体
	@DatabaseField(columnName = "fileSize", canBeNull = false)
	private String fileSize; // 文件大小
	@DatabaseField(columnName = "toUserId", canBeNull = false)
	private String toUserId; // 接受者
	@DatabaseField(columnName = "fromUserId", canBeNull = false)
	private String fromUserId; // 发送者
	@DatabaseField(columnName = "timeLen", canBeNull = false)
	private String timeLen; //
	@DatabaseField(columnName = "isReaded", canBeNull = false)
	private boolean isReaded;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(int msgtype) {
		this.msgtype = msgtype;
	}

	public String getChatTopic() {
		return chatTopic;
	}

	public void setChatTopic(String chatTopic) {
		this.chatTopic = chatTopic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTimeSend() {
		return timeSend;
	}

	public void setTimeSend(String timeSend) {
		this.timeSend = timeSend;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getTimeLen() {
		return timeLen;
	}

	public void setTimeLen(String timeLen) {
		this.timeLen = timeLen;
	}

	public boolean isReaded() {
		return isReaded;
	}

	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}
}
