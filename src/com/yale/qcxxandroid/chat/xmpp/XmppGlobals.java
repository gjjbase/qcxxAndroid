package com.yale.qcxxandroid.chat.xmpp;



public class XmppGlobals {
	
	/**
	 * 当前登录的状态 
	 */
	public static final int LOGIN_STATUS = -1;
	/**
	 * 接收消息的监听（全局）
	 */
	public static final String MessageAction = "RECEIVE_MessageReciver";
	/**
	 * 转发消息到界面的监听（（只有注册的地方收到）
	 */
	public static final String MESSAGE_ACTION = "messagAction";	
	
	/**
	 * 花名册状态监听（（全局）
	 */
	public static final String PRESENCE_CHANGED = "presenceChanged";
	/**
	 * 转发花名册状态监听（只有注册的地方收到）
	 */
	public static final String MODE_ACTION = "modeAction";
}
