package com.yale.qcxxandroid.chat.xmpp.smack;

import java.util.Collection;
import java.util.Date;
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
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.carbons.Carbon;
import org.jivesoftware.smackx.forward.Forwarded;
import org.jivesoftware.smackx.ping.provider.PingProvider;
import org.jivesoftware.smackx.provider.DelayInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;

import com.yale.qcxxandroid.chat.xmpp.XmppGlobals;
import com.yale.qcxxandroid.chat.xmpp.XmppService;
import android.content.Intent;
import android.util.Log;

public class SmackImpl implements ISmack{
	// 客户端名称和类型。主要是向服务器登记，有点类似QQ显示iphone或者Android手机在线的功能
	public static final String XMPP_IDENTITY_NAME = "android";// 客户端名称
	public static final String XMPP_IDENTITY_TYPE = "phone";// 客户端类型
	public static final int PACKET_TIMEOUT = 30000;	//连接超时时间
	public static final String HOST = "202.103.1.21";//服务器地址
	public static final int PORT = 5222;//服务器端口号
	public static final String SERVER = "soon";//服务器域名（@后面的部分,需要和服务器设置保持一致）
	
	public static SmackImpl smackImpl;
	private XmppService service;
	private XMPPConnection mXMPPConnection = null;
	private ConnectionListener mConnectionListener= null;
	private PacketListener mPacketListener;
	private Roster mRoster;
	private RosterListener mRosterListener;
	@SuppressWarnings("unused")
	private PacketListener mPongListener;// ping pong服务器动态监听
	
	/*******************************************************
	 *1. 静态代码块 初始化一些参数设置 开始
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
	    // add delayed delivery notifications  			//延迟发送通知
	    pm.addExtensionProvider("delay", "urn:xmpp:delay",  
	            new DelayInfoProvider());  
	    pm.addExtensionProvider("x", "jabber:x:delay", new DelayInfoProvider());  
	    // add carbons and forwarding  
	    pm.addExtensionProvider("forwarded", Forwarded.NAMESPACE,  	//消息转发
	            new Forwarded.Provider());  
	    pm.addExtensionProvider("sent", Carbon.NAMESPACE, new Carbon.Provider());  
	    pm.addExtensionProvider("received", Carbon.NAMESPACE,  
	            new Carbon.Provider());  
	    // add delivery receipts  					//收到消息交付凭证   消息回执
	    pm.addExtensionProvider(DeliveryReceipt.ELEMENT,  
	            DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());  
	    pm.addExtensionProvider(DeliveryReceiptRequest.ELEMENT,  
	            DeliveryReceipt.NAMESPACE,  
	            new DeliveryReceiptRequest.Provider());  
	    // add XMPP Ping (XEP-0199)
	    pm.addIQProvider("ping", "urn:xmpp:ping", new PingProvider());  	//XMPP心跳
	  
	    ServiceDiscoveryManager.setIdentityName(XMPP_IDENTITY_NAME);  
	    ServiceDiscoveryManager.setIdentityType(XMPP_IDENTITY_TYPE);  
	}  
	/*******************************************************
	 *1. 静态代码块 初始化一些参数设置 结束
	 *******************************************************/
	
	/**
	 * 私有构造函数 表示不允许直接实例化  这个配合单例模式目的是为了获取该类的实例为同一个
	 * @param service 启动XMPP的service
	 */
	private SmackImpl(XmppService service){
		ConnectionConfig();
		this.service = service;
	}
	
	//获取实例
	public static SmackImpl getInstance(XmppService xmppservice){
		if(smackImpl== null){
			smackImpl = new SmackImpl(xmppservice);
		}
		return smackImpl;
	}
	
	/*******************************************************
	 *2. 初始化连接参数        开始
	 *******************************************************/
	
	/**
	 * 连接参数设置
	 */
	ConnectionConfiguration mXMPPConfig;
	private void ConnectionConfig() {
		mXMPPConfig = new ConnectionConfiguration(HOST, PORT,SERVER);
		mXMPPConfig.setReconnectionAllowed(false);
		mXMPPConfig.setSendPresence(false);
		mXMPPConfig.setCompressionEnabled(false); // disable for now
		mXMPPConfig.setDebuggerEnabled(true);	// 是否需要smack debug 不启用
		mXMPPConfig.setReconnectionAllowed(true); 
//		mXMPPConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);       
		mXMPPConfig.setSASLAuthenticationEnabled(true);      
		mXMPPConfig .setTruststorePath("/system/etc/security/cacerts.bks");       
		mXMPPConfig.setTruststorePassword("changeit");       
		mXMPPConfig.setTruststoreType("bks"); 
		
		//是否需要ssl安全配置 不启用
		mXMPPConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		
	}
	/*******************************************************
	 *2. 初始化连接参数       结束
	 *******************************************************/
	

	/**
	 * 当前是否登陆成功！
	 */
	@Override
	public boolean isAuthenticated() {
		if(null != mXMPPConnection && mXMPPConnection.isConnected()){//连接为断开
			return mXMPPConnection.isAuthenticated();		//是否为登录态
		}
		return false;
	}
	
	
	/**
	 *打开连接并使用用户名、密码登陆
	 */
	@Override
	public boolean login(String account, String password) {
		password="123456";
		try {
			if(mXMPPConnection == null){
				ConnectionConfig();
				mXMPPConnection = new XMPPConnection(mXMPPConfig);
			}
			if (mXMPPConnection.isConnected()) {// 首先判断是否还连接着服务器，需要先断开
				try {
					mXMPPConnection.disconnect();
				} catch (Exception e) {
					Log.e("LoginError","conn.disconnect() failed: " + e.getMessage());
				}
			}
			SmackConfiguration.setPacketReplyTimeout(PACKET_TIMEOUT);// 设置连接登陆的超时时间
			SmackConfiguration.setKeepAliveInterval(-1);
			SmackConfiguration.setDefaultPingInterval(0);
			mXMPPConnection.connect();	//这里会抛出xmppException 如果抛出该异常，说明连接服务器的时候出现异常
			if(!mXMPPConnection.isConnected()){	//连接服务器失败
				throw new Exception("conneted failed!");	//如果出现连接不上服务器，直接抛出异常，不用网下走了！
			}
			mXMPPConnection.addConnectionListener(new ConnectionListener() {
				
				public void connectionClosedOnError(Exception e) {
					service.postConnectionFailed(e.getMessage());// 连接关闭时，动态反馈给服务
					Log.e("connectionClosedOnError", e.getMessage());
				}
				
				public void connectionClosed() {
					//连接关闭
					Log.e("connectionClosed", "连接关闭");
				}

				public void reconnectingIn(int seconds) {
					//开始重新连接！
					Log.e("reconnectingIn", "seconds:"+seconds);
				}

				public void reconnectionFailed(Exception e) {
					//重连失败
					Log.e("reconnectionFailed", e.getMessage());
				}

				public void reconnectionSuccessful() {
					//重连成功
					Log.e("reconnectionSuccessful","重连成功");
				}
			});
			//到此为止是连接服务器成功！开始登陆
			if(!mXMPPConnection.isAuthenticated()){
				mXMPPConnection.login(account+"@"+SERVER, password);
			}
			// 更新在线状态
			refrashState();
		} catch (XMPPException e) {
			e.printStackTrace();
			if(mXMPPConnection.isConnected()){
				//这里就是登陆失败，用户名或密码错误
			}else{
				//连接服务器失败！
			}
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			//连接服务器失败！
			return false;
		}
		//开始注册各种监听
		registeListener();
		return mXMPPConnection.isAuthenticated();
	}
	
	
	
	/**
	 * 注册监听
	 */
	private void registeListener() {
		if (isAuthenticated()) {	//已经登陆才注册各种监听
			registerMessageListener();// 注册新消息监听
			registeRosterListener();	//注册花名册监听
			registeConnectListener();	//连接监听
		}
	}
	
	/**
	 * 注册连接监听
	 */
	private void registeConnectListener() {
		if (mConnectionListener != null){
			mXMPPConnection.removeConnectionListener(mConnectionListener);
		}
		if(mXMPPConfig == null){
			ConnectionConfig();
		}
		mConnectionListener = new ConnectionListener() {
			
			@Override
			public void reconnectionSuccessful() {	//重新连接成功！
				// TODO Auto-generated method stub
				System.out.println("重新连接成功！");
				Log.e("connectionListener:", "进入reconnectionSuccessful");
			}
			
			@Override
			public void reconnectionFailed(Exception arg0) {	//重新连接失败!
				// TODO Auto-generated method stub
				System.out.println("重新连接失败!");
				Log.e("connectionListener:", "进入reconnectionFailed");
			}
			
			@Override
			public void reconnectingIn(int arg0) {	//正在重连
				// TODO Auto-generated method stub
				System.out.println("正在重连");
				Log.e("connectionListener:", "进入reconnectingIn");
			}
			
			@Override
			public void connectionClosedOnError(Exception arg0) {	//未知错误导致连接关闭
				System.out.println("未知错误导致连接关闭");
				Log.e("connectionListener:", "进入connectionClosedOnError");
				
			}
			
			@Override
			public void connectionClosed() {	//连接正常关闭
				System.out.println("连接关闭");
				Log.e("connectionListener:", "进入connectionClosed");
			}
		};
	}
	
	/**
	 * 注册消息监听
	 */
	private void registerMessageListener() {
		if (mPacketListener != null)
			mXMPPConnection.removePacketListener(mPacketListener);
		//消息过滤器
		PacketTypeFilter filter = new PacketTypeFilter(Message.class);
		mPacketListener = new PacketListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void processPacket(Packet packet) {
				if (packet instanceof Message) {// 如果是消息类型
					Message message = (Message) packet;
					//发现消息到消息中心（MessageReciver）
					Intent intent = new Intent(XmppGlobals.MessageAction);
					intent.putExtra("from", message.getFrom());
					intent.putExtra("body", message.getBody());
					intent.putExtra("time", new Date(System.currentTimeMillis()).toLocaleString());
					service.sendBroadcast(intent);
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
				if(presence.isAvailable()){
					try{
						values = presence.getMode().name();
					}catch(Exception ex){
						ex.printStackTrace();
						values = "online";	//空指针的时候为在线状态
					}
					if(values == null){
						values = "online";
					}
					Log.e("presenceChanged", fromName);
				}else{
					values = "outline";	//下线
					Log.e("presenceChanged", fromName);
				}
//				service.presenceChanged(fromName,values);
				Intent intent = new Intent(XmppGlobals.PRESENCE_CHANGED);
				intent.putExtra("fromName", fromName);
				intent.putExtra("mode", values);
				service.sendBroadcast(intent);
				 
			}
			
			@Override
			public void entriesUpdated(Collection<String> entries) {	//好友更新名片的时候
				// 更新数据库，第一次登陆
				Log.e("entriesUpdated", entries.toString());
			}
			
			@Override
			public void entriesDeleted(Collection<String> entries) {// 有好友删除时，
				Log.e("entriesDeleted", entries.toString());
				
			}
			
			@Override
			public void entriesAdded(Collection<String> entries) {
				// 有人添加好友时 
			}
		};
		mRoster.addRosterListener(mRosterListener);
	}
	
	//发送消息
	@Override
	public boolean sendMessage(String toJID, String message) {
		final Message newMessage = new Message(toJID+"@"+SERVER, Message.Type.chat);
		newMessage.setBody(message);
		newMessage.addExtension(new DeliveryReceiptRequest());	//发送成功后的回执
		if (mXMPPConnection.isAuthenticated()){
			mXMPPConnection.sendPacket(newMessage);
			return true;
		}else{
			//当前已断开连接
			return false;
		}
	}
	
	
	//添加好友
	@Override
	public boolean addRosterItem(String user, String alias, String group) {
		boolean flag = false;
		mRoster = mXMPPConnection.getRoster();
		try {
			mRoster.createEntry(user, alias, new String[]{group});
			flag = true;
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	//删除好友
	@Override
	public void removeRosterItem(String user) {
		// TODO Auto-generated method stub
		
	}
	
	//修改好友昵称
	@Override
	public void renameRosterItem(String user, String newName) {
		// TODO Auto-generated method stub
		
	}
	//获取好友列表
	@Override
	public List<RosterEntry> getEntries(String jid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//设置为在线状态
	@Override
	public void setStatusFromConfig() {
		// TODO Auto-generated method stub
		
	}
	//设置当前自己的状态
	public void refrashState() {
		Presence presence = new Presence(Presence.Type.available);
		presence.setMode(Mode.available);
		presence.setStatus("在线");
		presence.setPriority(0);
		mXMPPConnection.sendPacket(presence);
	}
	 //根据JID获取好友名称
	@Override
	public String getNameForJID(String jid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
