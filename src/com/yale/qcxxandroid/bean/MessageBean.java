package com.yale.qcxxandroid.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 消息类
 * @author kindlion
 *
 */
@DatabaseTable(tableName="MessageBean")
public class MessageBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6402480634182840824L;
	
	@DatabaseField(generatedId = true)
	int id ;
	@DatabaseField(columnName = "msgtype",canBeNull = false)
	int msgtype;	//类型 （发送的消息消息 (1) 还是接收到的消息(0)）
	@DatabaseField(columnName = "type",canBeNull = false)
	int type;	//消息类型 （语音消息，图片消息，文本消息）
	@DatabaseField(columnName = "msgTime",canBeNull = false)
	String msgTime;	//消息发送的时间	
	@DatabaseField(columnName = "msgContent",canBeNull = false)
	String msgContent;	//消息内容
	@DatabaseField(columnName = "sender",canBeNull = false)
	String sender;	//消息发送者
	@DatabaseField(columnName = "reciver",canBeNull = false)
	String reciver;	//接收者
	@DatabaseField(columnName = "readed",canBeNull = false)
	boolean readed;//是否已读
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReciver() {
		return reciver;
	}
	public void setReciver(String reciver) {
		this.reciver = reciver;
	}
	public boolean isReaded() {
		return readed;
	}
	public void setReaded(boolean readed) {
		this.readed = readed;
	}
	
}
