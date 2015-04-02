package com.yale.qcxxandroid.chat.xmpp;

import java.sql.SQLException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


import com.j256.ormlite.dao.Dao;
import com.yale.qcxxandroid.bean.MessageBean;
import com.yale.qcxxandroid.chat.xmpp.smack.SmackImpl;
import com.yale.qcxxandroid.util.DataHelper;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

@SuppressLint("HandlerLeak")
public class XmppService extends Service {

	public static final int PONG_TIMEOUT = 10000;
	private String account;
	private String password;
	private boolean isFristConnect = true;	//是否是首次登陆！（登陆界面）
	private static SmackImpl smackImpl;		//自定义smack接口的实现类
	
	private IBinder mBinder = new XmppBinder();
	
	public SmackImpl getSmackImpl(){
		if(smackImpl == null){
			smackImpl = SmackImpl.getInstance(XmppService.this);
		}
		return smackImpl;
	}
	
	/**
	 * 创建service
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		isFristConnect = true;	//初始化参数
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return mBinder;
	}

	
	/**
	*取消bindservice
	**/
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
	/**
	 * 重新绑定
	 */
	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}
	
	
	/**
	 * 重启的时候
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getAction() != null){//开机自启动
			//从XML中获取用户名和密码
			if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password) && !isFristConnect)
				Login(account, password);	//如果不是第一次登陆的话，就直接重新登陆
			Log.e("onStartCommand", "Login");
		}
		account = XmppService.this.getSharedPreferences("qcxx", Context.MODE_PRIVATE).getString("phoneNum", "");
		password = XmppService.this.getSharedPreferences("qcxx", Context.MODE_PRIVATE).getString("passWord", "");
		if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password) && isFristConnect){
			Login(account, password);	//如果不是第一次登陆的话，就直接重新登陆
			isFristConnect = false;
		}
		return START_STICKY;
	}
	
	/**
	 * 登陆方法
	 * @param account
	 * @param password
	 */
	public void Login(final String account,final String password) {
		smackImpl = getSmackImpl();
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean flag = smackImpl.login(account, password);
				Log.e("onStartCommand", flag+"");
				if(flag)
					handler.sendEmptyMessage(0);
				else
					handler.sendEmptyMessage(1);
			}
		}).start();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				pingTimer = new Timer();
				pingTimer.schedule(task, 30, 300000);
			}
		};
	};
	
	Timer pingTimer ;
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			if(smackImpl != null)
				smackImpl.refrashState();
			else{
				smackImpl = SmackImpl.getInstance(XmppService.this);
				smackImpl.refrashState();
			}
		}
	};
	
	
	/**
	 * 发送消息
	 * @param toJid	接受者的jid
	 * @param msg 消息主体
	 */
	public void sendMsg(final String toJid,final String msg){
		new Thread(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				smackImpl = getSmackImpl();
				@SuppressWarnings("unused")
				boolean flag = smackImpl.sendMessage(toJid, msg);//+"@"+smackImpl.SERVER
				try {	
					//发送的消息存到数据库
					Dao<MessageBean, Integer> messageDao = DataHelper.getInstance(XmppService.this).getMessageDAO(); 
					MessageBean bean = new MessageBean();
					bean.setMsgtype(0);		//消息类别      发送的消息1
					bean.setType(0);		//消息类型   0代表正常的消息
					bean.setMsgContent(msg);
					bean.setMsgTime(new Date(System.currentTimeMillis()).toLocaleString());
					bean.setReaded(false);	//是否已读
					bean.setReciver(toJid);	//消息的接受者
					bean.setSender("myself");	//消息的发送者
					messageDao.create(bean);
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * Service句柄
	 * @author kindlion
	 *
	 */
	public class XmppBinder extends Binder {
		public XmppService getService() {
			return XmppService.this;
		}
	}
	
	
	
	
	/**
	 * 连接异常关闭的时候
	 * @param errorMsg
	 */
	public void postConnectionFailed(String errorMsg) {
		Log.e("连接异常关闭：", errorMsg);
	}
	
	
}
