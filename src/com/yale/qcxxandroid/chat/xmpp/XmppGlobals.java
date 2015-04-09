package com.yale.qcxxandroid.chat.xmpp;

public class XmppGlobals {

	/**
	 * 当前登录的状态
	 */
	public static final int LOGIN_STATUS = -1;
	/**
	 * 消息中心的监听（全局）
	 */
	public static final String MessageAction = "RECEIVE_MessageReciver";

	/**
	 * 消息的监听:接收到来自好友消息的标志
	 */
	public static final String RECEIVE_MSGFLAG = "RECEIVE_MSGFLAG";
	/**
	 * 消息的监听:花名册状态监听标志
	 */
	public static final String PRESENCE_CHANGED = "PRESENCE_CHANGED";

	/**
	 * 消息的监听:发送的消息是否成功标志！
	 */
	public static final String SENDMSG_ISUCCESSED = "SENDMSG_ISUCCESSED";

	/**
	 * 转发消息到界面的监听（（只有注册的地方收到）
	 */
	public static final String MESSAGE_ACTION = "messagAction";

	/**
	 * 转发花名册状态监听（只有注册的地方收到）
	 */
	public static final String MODE_ACTION = "modeAction";

	public static final String ACTION_RECORDER_SEND = "ACTION_RECORDER_SEND";

	public class MessageType {
		public static final String text = "1"; // 文字
		public static final String sound = "2"; // 声音
		public static final String picture = "3"; // 图片
		public static final String Big_face = "4"; // 大表情
	}

}
