package com.yale.qcxxandroid.chat.xmpp;

import java.io.IOException;
import java.sql.SQLException;


import com.j256.ormlite.dao.Dao;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.bean.MessageBean;
import com.yale.qcxxandroid.util.DataHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;
import android.util.Log;

public class MessageReciver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action.equals(XmppGlobals.MessageAction)){
			Log.e("MessageReciver", "获得消息，转发消息");
			String from = intent.getStringExtra("from");
			String body = intent.getStringExtra("body");
			String time = intent.getStringExtra("time");
			Intent msgIntent = new Intent();
			msgIntent.putExtra("from", from);
			msgIntent.putExtra("body", body);
			msgIntent.putExtra("time", time);
			msgIntent.setAction(XmppGlobals.MESSAGE_ACTION);
			StartAudio(context);
			startVIBRATE(context);
			try {
				//发送的消息存到数据库
				Dao<MessageBean, Integer> messageDao = DataHelper.getInstance(context).getMessageDAO(); 
				MessageBean bean = new MessageBean();
				bean.setMsgtype(1);		//消息类别      发送的消息1
				bean.setType(0);		//消息类型   0代表正常的消息
				bean.setMsgContent(body);
				bean.setMsgTime(time);
				bean.setReciver("myself");
				String sender = from.substring(0, from.indexOf("@"));
				bean.setReaded(true);	//是否已读
				bean.setSender(sender);
				messageDao.create(bean);
				msgIntent.putExtra("bean", bean);
			}catch (SQLException e) {
				e.printStackTrace();
			}
			context.getApplicationContext().sendBroadcast(msgIntent);
		}else if(action.equals(XmppGlobals.PRESENCE_CHANGED)){
			String fromName = intent.getStringExtra("fromName");
			String mode = intent.getStringExtra("mode");
			presenceChanged(context,fromName, mode);
		}
	}
	
	/**
	 * 联系人状态变化
	 * @param fromName
	 * @param mode
	 */
	public void presenceChanged(Context context,String fromName,String mode) {
		Intent intent = new Intent();
		intent.putExtra("from", fromName);
		intent.putExtra("mode", mode);
		intent.setAction(XmppGlobals.MODE_ACTION);
		context.getApplicationContext().sendBroadcast(intent);
	}
	
	

	/**
	 * 上一次的播放铃声的时间
	 */
	private static long lastAudioTime = 0;
	private volatile boolean openFlag = true;
	private MediaPlayer mp ;
	/**
	 * 开始播放铃声 3秒钟内来多个消息的时候铃声只提醒一次
	 * @param context
	 */
	public void StartAudio(Context context){
		if(!openFlag){
			return;
		}
		openFlag = false;
		if(lastAudioTime != 0){
			long currentTime = System.currentTimeMillis();
			long timeCup = currentTime-lastAudioTime;
			lastAudioTime = System.currentTimeMillis();
			if(timeCup<3000){
				return;
			}
		}else{
			lastAudioTime = System.currentTimeMillis();
		}
		if(mp == null){
			mp = MediaPlayer.create(context, R.raw.gl);
		}else{
			if(mp.isPlaying())
				return;
		}
		try {
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer media) {
					media.stop();
					media.release();
					openFlag = true;
				}
			});
			mp.prepare();
			mp.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 振动提醒
	 */
	public void startVIBRATE(Context mContext){
		Vibrator vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
		long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启 
		vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1 
//		vibrator.cancel();
	}

}
