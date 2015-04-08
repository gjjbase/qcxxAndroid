package com.yale.qcxxandroid.chat.xmpp.smack;

import java.util.List;

import org.jivesoftware.smack.RosterEntry;

import com.yale.qcxxandroid.bean.XmppMsgBean;


/**
 * 自定义需要的aSmack方法接口
 * 
 * @author kindlion
 * 
 */
public interface ISmack {

	/**
	 * 连接是否可用
	 * 
	 * @return
	 */
	public boolean isAuthenticated();

	/**
	 * 登陆
	 * 
	 * @param account
	 * @param password
	 * @return 结果代号 int
	 */
	public boolean login(String account, String password);

	/**
	 * 发送消息
	 * 
	 * @param toJID
	 *            要发送的人的JID
	 * @param message
	 *            消息主题（消息封装之后的String）
	 * @return 是否断开登陆
	 */
	public boolean sendMessage(XmppMsgBean bean);

	/**
	 * 添加好友
	 * 
	 * @param user
	 *            好友id
	 * @param alias
	 *            昵称
	 * @param group
	 *            所在的分组
	 * @throws XXException
	 */
	public boolean addRosterItem(String user, String alias, String group);

	/**
	 * 删除好友
	 * 
	 * @param user
	 *            好友id
	 * @throws XXException
	 */
	public void removeRosterItem(String user);

	/**
	 * 修改好友昵称
	 * 
	 * @param user
	 *            好友id
	 * @param newName
	 *            新昵称
	 * @throws XXException
	 */
	public void renameRosterItem(String user, String newName);

	/**
	 * 获取好友列表
	 * 
	 * @param jid
	 * @return
	 */
	public List<RosterEntry> getEntries(String jid);

	/**
	 * 设置当前在线状态
	 */
	public void setStatusFromConfig();

	/**
	 * 从jid中获取好友名
	 * 
	 * @param jid
	 * @return
	 */
	public String getNameForJID(String jid);

}
