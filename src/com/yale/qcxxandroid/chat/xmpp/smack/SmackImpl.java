package com.yale.qcxxandroid.chat.xmpp.smack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.carbons.Carbon;
import org.jivesoftware.smackx.forward.Forwarded;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.ping.provider.PingProvider;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.json.JSONException;
import org.json.JSONObject;

import com.yale.qcxxandroid.bean.XmppMsgBean;
import com.yale.qcxxandroid.chat.xmpp.XmppGlobals;
import com.yale.qcxxandroid.chat.xmpp.XmppService;

import android.content.Intent;
import android.util.Log;

public class SmackImpl implements ISmack {
	// 客户端名称和类型。主要是向服务器登记，有点类似QQ显示iphone或者Android手机在线的功能
	public static final String XMPP_IDENTITY_NAME = "android";// 客户端名称
	public static final String XMPP_IDENTITY_TYPE = "phone";// 客户端类型
	public static final int PACKET_TIMEOUT = 30000; // 连接超时时间
	// public static final String HOST = "202.103.1.21";//服务器地址
	public static final String HOST = "121.41.111.107";
	public static final int PORT = 5222;// 服务器端口号
	public static final String SERVER = "iZ239bbzdlrZ";// 服务器域名（@后面的部分,需要和服务器设置保持一致）

	public static SmackImpl smackImpl;
	private XmppService service;
	private XMPPConnection mXMPPConnection = null;
	private ConnectionListener mConnectionListener = null;
	private PacketListener mPacketListener;
	private Roster mRoster;
	private RosterListener mRosterListener;
	@SuppressWarnings("unused")
	private PacketListener mPongListener;// ping pong服务器动态监听

	/*******************************************************
	 * 1. 静态代码块 初始化一些参数设置 开始
	 *******************************************************/
	static {
		registerSmackProviders();
	}

	// 做一些基本的配置
	static void registerSmackProviders() {
		ProviderManager pm = ProviderManager.getInstance();
		// add IQ handling
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// add delayed delivery notifications //延迟发送通知
		pm.addExtensionProvider("delay", "urn:xmpp:delay",
				new DelayInfoProvider());
		pm.addExtensionProvider("x", "jabber:x:delay", new DelayInfoProvider());
		// add carbons and forwarding
		pm.addExtensionProvider("forwarded", Forwarded.NAMESPACE, // 消息转发
				new Forwarded.Provider());
		pm.addExtensionProvider("sent", Carbon.NAMESPACE, new Carbon.Provider());
		pm.addExtensionProvider("received", Carbon.NAMESPACE,
				new Carbon.Provider());
		// add delivery receipts //收到消息交付凭证 消息回执
		pm.addExtensionProvider(DeliveryReceipt.ELEMENT,
				DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
		pm.addExtensionProvider(DeliveryReceiptRequest.ELEMENT,
				DeliveryReceipt.NAMESPACE,
				new DeliveryReceiptRequest.Provider());
		// add XMPP Ping (XEP-0199)
		pm.addIQProvider("ping", "urn:xmpp:ping", new PingProvider()); // XMPP心跳

		ServiceDiscoveryManager.setIdentityName(XMPP_IDENTITY_NAME);
		ServiceDiscoveryManager.setIdentityType(XMPP_IDENTITY_TYPE);
	}

	/*******************************************************
	 * 1. 静态代码块 初始化一些参数设置 结束
	 *******************************************************/

	/**
	 * 私有构造函数 表示不允许直接实例化 这个配合单例模式目的是为了获取该类的实例为同一个
	 * 
	 * @param service
	 *            启动XMPP的service
	 */
	private SmackImpl(XmppService service) {
		ConnectionConfig();
		this.service = service;
	}

	// 获取实例
	public static SmackImpl getInstance(XmppService xmppservice) {
		if (smackImpl == null) {
			smackImpl = new SmackImpl(xmppservice);
		}
		return smackImpl;
	}

	/*******************************************************
	 * 2. 初始化连接参数 开始
	 *******************************************************/

	/**
	 * 连接参数设置
	 */
	ConnectionConfiguration mXMPPConfig;

	private void ConnectionConfig() {
		mXMPPConfig = new ConnectionConfiguration(HOST, PORT, SERVER);
		// mXMPPConfig = new ConnectionConfiguration(HOST, PORT);
		mXMPPConfig.setReconnectionAllowed(false);
		mXMPPConfig.setSendPresence(false);
		mXMPPConfig.setCompressionEnabled(false); // disable for now
		mXMPPConfig.setDebuggerEnabled(false); // 是否需要smack debug 不启用
		mXMPPConfig.setReconnectionAllowed(true);
		// mXMPPConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
		mXMPPConfig.setSASLAuthenticationEnabled(false);
		mXMPPConfig.setTruststorePath("/system/etc/security/cacerts.bks");
		mXMPPConfig.setTruststorePassword("changeit");
		mXMPPConfig.setTruststoreType("bks");
		// 是否需要ssl安全配置 不启用
		mXMPPConfig
				.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
	}

	/*******************************************************
	 * 2. 初始化连接参数 结束
	 *******************************************************/

	/**
	 * 当前是否登陆成功！
	 */
	@Override
	public boolean isAuthenticated() {
		if (null != mXMPPConnection && mXMPPConnection.isConnected()) {// 连接为断开
			return mXMPPConnection.isAuthenticated(); // 是否为登录态
		}
		return false;
	}

	/**
	 * 打开连接并使用用户名、密码登陆
	 */
	@Override
	public boolean login(String account, String password) {
		password = "123456";
		try {
			if (mXMPPConnection == null) {
				ConnectionConfig();
				mXMPPConnection = new XMPPConnection(mXMPPConfig);
			}
			if (mXMPPConnection.isConnected()) {// 首先判断是否还连接着服务器，需要先断开
				try {
					mXMPPConnection.disconnect();
				} catch (Exception e) {
					Log.e("LoginError",
							"conn.disconnect() failed: " + e.getMessage());
				}
			}
			SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);// 设置连接登陆的超时时间
			SmackConfiguration.setKeepAliveInterval(-1);
			SmackConfiguration.setDefaultPingInterval(0);
			mXMPPConnection.connect(); // 这里会抛出xmppException
										// 如果抛出该异常，说明连接服务器的时候出现异常
			String serviceName = mXMPPConnection.getServiceName();
			System.out.println("serviceName:" + serviceName);
			if (!mXMPPConnection.isConnected()) { // 连接服务器失败
				throw new Exception("conneted failed!"); // 如果出现连接不上服务器，直接抛出异常，不用网下走了！
			}
			mXMPPConnection.addConnectionListener(new ConnectionListener() {

				public void connectionClosedOnError(Exception e) {
					service.postConnectionFailed(e.getMessage());// 连接关闭时，动态反馈给服务
					Log.e("connectionClosedOnError", e.getMessage());
				}

				public void connectionClosed() {
					// 连接关闭
					Log.e("connectionClosed", "连接关闭");
				}

				public void reconnectingIn(int seconds) {
					// 开始重新连接！
					Log.e("reconnectingIn", "seconds:" + seconds);
				}

				public void reconnectionFailed(Exception e) {
					// 重连失败
					Log.e("reconnectionFailed", e.getMessage());
				}

				public void reconnectionSuccessful() {
					// 重连成功
					Log.e("reconnectionSuccessful", "重连成功");
				}
			});
			// 到此为止是连接服务器成功！开始登陆
			if (!mXMPPConnection.isAuthenticated()) {
				mXMPPConnection.login(account, password);
			}
			// 更新在线状态
			refrashState();
		} catch (XMPPException e) {
			e.printStackTrace();
			if (mXMPPConnection.isConnected()) {
				// 这里就是登陆失败，用户名或密码错误
			} else {
				// 连接服务器失败！
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			// 连接服务器失败！
			return false;
		}
		// 开始注册各种监听
		registeListener();
		return mXMPPConnection.isAuthenticated();
	}

	/**
	 * 注册监听
	 */
	private void registeListener() {
		if (isAuthenticated()) { // 已经登陆才注册各种监听
			registerMessageListener();// 注册新消息监听
			registeRosterListener(); // 注册花名册监听
			registeConnectListener(); // 连接监听
		}
	}

	/**
	 * 注册连接监听
	 */
	private void registeConnectListener() {
		if (mConnectionListener != null) {
			mXMPPConnection.removeConnectionListener(mConnectionListener);
		}
		if (mXMPPConfig == null) {
			ConnectionConfig();
		}
		mConnectionListener = new ConnectionListener() {

			@Override
			public void reconnectionSuccessful() { // 重新连接成功！
				// TODO Auto-generated method stub
				System.out.println("重新连接成功！");
				Log.e("connectionListener:", "进入reconnectionSuccessful");
			}

			@Override
			public void reconnectionFailed(Exception arg0) { // 重新连接失败!
				// TODO Auto-generated method stub
				System.out.println("重新连接失败!");
				Log.e("connectionListener:", "进入reconnectionFailed");
			}

			@Override
			public void reconnectingIn(int arg0) { // 正在重连
				// TODO Auto-generated method stub
				System.out.println("正在重连");
				Log.e("connectionListener:", "进入reconnectingIn");
			}

			@Override
			public void connectionClosedOnError(Exception arg0) { // 未知错误导致连接关闭
				System.out.println("未知错误导致连接关闭");
				Log.e("connectionListener:", "进入connectionClosedOnError");
				// service.stopSelf();

			}

			@Override
			public void connectionClosed() { // 连接正常关闭
				System.out.println("连接关闭");
				Log.e("connectionListener:", "进入connectionClosed");
				service.stopSelf();
			}
		};
	}

	/**
	 * 注册消息监听
	 */
	private void registerMessageListener() {
		if (mPacketListener != null)
			mXMPPConnection.removePacketListener(mPacketListener);
		// 消息过滤器
		PacketTypeFilter filter = new PacketTypeFilter(Message.class);
		mPacketListener = new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				if (packet instanceof Message) {// 如果是消息类型
					Message message = (Message) packet;
					if (message.getType().equals(Type.chat)) { // 单人消息
						// 发现消息到消息中心（MessageReciver）
						Intent intent = new Intent(XmppGlobals.MessageAction);
						intent.putExtra("Action", XmppGlobals.RECEIVE_MSGFLAG);
						intent.putExtra("from", message.getFrom());
						intent.putExtra("body", message.getBody());
						service.sendBroadcast(intent);
					} else if (message.getType().equals(Type.groupchat)) { // 多人消息

					}
				}
			}
		};
		mXMPPConnection.addPacketListener(mPacketListener, filter);
	}

	/**
	 * 注册好友花名册监听
	 */
	private void registeRosterListener() {
		mRoster = mXMPPConnection.getRoster();
		mRosterListener = new RosterListener() {
			@Override
			public void presenceChanged(Presence presence) {
				// 联系人状态改变，比如在线或离开、隐身之类
				String from = presence.getFrom();
				String fromName = from.substring(0, from.indexOf("/"));
				String values = null;
				if (presence.isAvailable()) {
					try {
						values = presence.getMode().name();
					} catch (Exception ex) {
						ex.printStackTrace();
						values = "online"; // 空指针的时候为在线状态
					}
					if (values == null) {
						values = "online";
					}
					Log.e("presenceChanged", fromName);
				} else {
					values = "outline"; // 下线
					Log.e("presenceChanged", fromName);
				}
				// service.presenceChanged(fromName,values);
				Intent intent = new Intent(XmppGlobals.MessageAction);
				intent.putExtra("Action", XmppGlobals.PRESENCE_CHANGED);
				intent.putExtra("fromName", fromName);
				intent.putExtra("mode", values);
				service.sendBroadcast(intent);

			}

			@Override
			public void entriesUpdated(Collection<String> entries) { // 好友更新名片的时候
				// 更新数据库，第一次登陆
				Log.e("entriesUpdated", entries.toString());
				// 好友更新名片的时候
				System.out.println("好友更新名片的时候");
			}

			@Override
			public void entriesDeleted(Collection<String> entries) {// 有好友删除时，
				Log.e("entriesDeleted", entries.toString());
				System.out.println("有好友删除时！");
			}

			@Override
			public void entriesAdded(Collection<String> entries) {
				// 有人添加好友时
				System.out.println("有人添加好友！");
			}
		};
		mRoster.addRosterListener(mRosterListener);
	}

	// 发送消息
	@Override
	public boolean sendMessage(XmppMsgBean bean) {
		final Message newMessage = new Message(bean.getToUserId() + "@"
				+ SERVER, Message.Type.chat);
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", bean.getType());
			obj.put("timeSend", bean.getTimeSend());
			obj.put("content", bean.getContent());
			obj.put("fileSize", bean.getFileSize());
			obj.put("toUserId", bean.getToUserId());
			obj.put("fromUserId", bean.getFromUserId());
			obj.put("timeLen", bean.getTimeLen());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		newMessage.setBody(obj.toString());
		newMessage.addExtension(new DeliveryReceiptRequest()); // 发送成功后的回执
		boolean flag = false; // 消息发送成功还是失败！
		if (mXMPPConnection.isAuthenticated()) {
			mXMPPConnection.sendPacket(newMessage);
			// return true;
			flag = true;
		} else {
			// 当前已断开连接
			flag = false; // 发送失败
		}
		bean.setReaded(flag);
		// 发现消息到消息中心（MessageReciver）
		Intent intent = new Intent(XmppGlobals.MessageAction);
		intent.putExtra("Action", XmppGlobals.SENDMSG_ISUCCESSED);
		intent.putExtra("XmppMsgBean", bean);
		service.sendBroadcast(intent);
		return true;
	}

	// 添加好友
	@Override
	public boolean addRosterItem(String user, String alias, String group) {
		boolean flag = false;
		mRoster = mXMPPConnection.getRoster();
		try {
			mRoster.createEntry(user, alias, new String[] { group });
			flag = true;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	// 删除好友
	@Override
	public void removeRosterItem(String user) {

	}

	// 修改好友昵称
	@Override
	public void renameRosterItem(String user, String newName) {
		// TODO Auto-generated method stub

	}

	// 获取好友列表
	@Override
	public List<RosterEntry> getEntries(String jid) {
		// TODO Auto-generated method stub
		return null;
	}

	// 设置为在线状态
	@Override
	public void setStatusFromConfig() {
		// TODO Auto-generated method stub

	}

	// 设置当前自己的状态
	public void refrashState() {
		Presence presence = new Presence(Presence.Type.available);
		presence.setMode(Mode.available);
		presence.setStatus("在线");
		presence.setPriority(0);
		mXMPPConnection.sendPacket(presence);
	}

	// 根据JID获取好友名称
	@Override
	public String getNameForJID(String jid) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 创建房间
	 * 
	 * @param user
	 * @param roomName
	 *            房间名称
	 * @param password
	 *            房间密码
	 */
	public MultiUserChat createRoom(String user, String roomName,
			String password) {
		if (mXMPPConnection == null || !mXMPPConnection.isAuthenticated())
			return null;
		MultiUserChat muc = null;
		String serviceName = mXMPPConnection.getServiceName();
		try {
			// 创建一个MultiUserChat
			muc = new MultiUserChat(mXMPPConnection, roomName + "@conference."
					+ serviceName);
			// 创建聊天室
			muc.create(roomName);
			// 获得聊天室的配置表单
			Form form = muc.getConfigurationForm();
			// 根据原始表单创建一个要提交的新表单。
			Form submitForm = form.createAnswerForm();
			// 向要提交的表单添加默认答复
			for (Iterator<FormField> fields = form.getFields(); fields
					.hasNext();) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					// 设置默认值作为答复
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}
			// 设置聊天室的新拥有者
			List<String> owners = new ArrayList<String>();
			owners.add(mXMPPConnection.getUser());// 用户JID
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			// 设置聊天室是持久聊天室，即将要被保存下来
			submitForm.setAnswer("muc#roomconfig_persistentroom", true);
			// 房间仅对成员开放
			submitForm.setAnswer("muc#roomconfig_membersonly", true);
			// 允许占有者邀请其他人
			submitForm.setAnswer("muc#roomconfig_allowinvites", true);
			if (!password.equals("")) { // 如何传入的密码为空，则不需要密码
				// 进入是否需要密码
				submitForm.setAnswer("muc#roomconfig_passwordprotectedroom",
						true);
				// 设置进入密码
				submitForm.setAnswer("muc#roomconfig_roomsecret", password);
			}
			// 能够发现占有者真实 JID 的角色
			// submitForm.setAnswer("muc#roomconfig_whois", "anyone");
			// 登录房间对话
			submitForm.setAnswer("muc#roomconfig_enablelogging", true);
			// 仅允许注册的昵称登录
			submitForm.setAnswer("x-muc#roomconfig_reservednick", true);
			// 允许使用者修改昵称
			submitForm.setAnswer("x-muc#roomconfig_canchangenick", false);
			// 允许用户注册房间
			submitForm.setAnswer("x-muc#roomconfig_registration", false);
			// 发送已完成的表单（有默认值）到服务器来配置聊天室
			muc.sendConfigurationForm(submitForm);
		} catch (XMPPException e) {
			e.printStackTrace();
			return null;
		}
		return muc;
	}

	/**
	 * 加入会议室
	 * 
	 * @param user
	 *            昵称
	 * @param roomsName
	 *            会议室名
	 * @param password
	 *            会议室密码
	 */
	public MultiUserChat joinMultiUserChat(String user, String roomsName,
			String password) {
		if (mXMPPConnection == null)
			return null;
		try {
			// 使用XMPPConnection创建一个MultiUserChat窗口
			MultiUserChat muc = new MultiUserChat(mXMPPConnection, roomsName
					+ "@conference." + mXMPPConnection.getServiceName());
			// 聊天室服务将会决定要接受的历史记录数量
			DiscussionHistory history = new DiscussionHistory();
			history.setMaxChars(0);
			// history.setSince(new Date());
			// 用户加入聊天室
			muc.join(user, password, history,
					SmackConfiguration.getPacketReplyTimeout());
			Log.i("MultiUserChat", "会议室【" + roomsName + "】加入成功........");
			return muc;
		} catch (XMPPException e) {
			e.printStackTrace();
			Log.i("MultiUserChat", "会议室【" + roomsName + "】加入失败........");
			return null;
		}
	}

}
