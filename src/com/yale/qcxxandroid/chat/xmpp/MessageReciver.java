package com.yale.qcxxandroid.chat.xmpp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.bean.XmppMsgBean;
import com.yale.qcxxandroid.util.DataHelper;
import com.yale.qcxxandroid.util.SoundPlay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;

public class MessageReciver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String intentAction = intent.getAction();
		if (intentAction != XmppGlobals.MessageAction) {
			return;
		}
		String action = intent.getStringExtra("Action");
		if (action.equals(XmppGlobals.RECEIVE_MSGFLAG)) {
			Log.e("MessageReciver", "获得消息，转发消息");
			String body = intent.getStringExtra("body");
			// 消息提示
			StartAudio(context);

			// startVIBRATE(context);
			// 解析XMPP消息
			XmppMsgBean xmppBean = getJsonBean(body, context);
			// 发送广播
			Intent msgIntent = new Intent();
			msgIntent.putExtra("body", xmppBean);
			msgIntent.setAction(XmppGlobals.MESSAGE_ACTION);
			context.getApplicationContext().sendBroadcast(msgIntent);
		} else if (action.equals(XmppGlobals.PRESENCE_CHANGED)) { // 好友花名册状态监听
			String fromName = intent.getStringExtra("fromName");
			String mode = intent.getStringExtra("mode");
			presenceChanged(context, fromName, mode);
		} else if (action.equals(XmppGlobals.SENDMSG_ISUCCESSED)) { // 发送消息状态监听
			XmppMsgBean bean = (XmppMsgBean) intent
					.getSerializableExtra("XmppMsgBean");
			// 保存数据库
			try {
				if (bean.getType().equals(XmppGlobals.MessageType.sound)) {
					bean.setContent(bean.getFileSize());
				}
				Dao<XmppMsgBean, Integer> mesgDao = DataHelper.getInstance(
						context).getXmppMsgDAO();
				mesgDao.create(bean);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Intent msgIntent = new Intent();
			msgIntent.putExtra("IsSuccess", bean.isReaded());
			msgIntent.setAction(XmppGlobals.SENDMSG_ISUCCESSED);
			context.getApplicationContext().sendBroadcast(msgIntent);
		}
	}

	/**
	 * 联系人状态变化
	 * 
	 * @param fromName
	 * @param mode
	 */
	public void presenceChanged(Context context, String fromName, String mode) {
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
	private MediaPlayer mp;
	SoundPlay play;

	/**
	 * 开始播放铃声 3秒钟内来多个消息的时候铃声只提醒一次
	 * 
	 * @param context
	 */
	public void StartAudio(Context context) {
		if (!openFlag) {
			return;
		}
		openFlag = false;
		if (lastAudioTime != 0) {
			long currentTime = System.currentTimeMillis();
			long timeCup = currentTime - lastAudioTime;
			lastAudioTime = System.currentTimeMillis();
			if (timeCup < 3000) {
				return;
			}
		} else {
			lastAudioTime = System.currentTimeMillis();
		}
		MediaPlayer.create(context, R.raw.gl).start();
	}

	/**
	 * 振动提醒
	 */
	public void startVIBRATE(Context mContext) {
		Vibrator vibrator = (Vibrator) mContext
				.getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1
		// vibrator.cancel();
	}

	/**
	 * 解析json对象为Java对象，并存储到数据库
	 * 
	 * @param jsonstr
	 * @param context
	 * @return
	 */
	public XmppMsgBean getJsonBean(String jsonstr, Context context) {
		XmppMsgBean bean = null;
		try {
			JSONObject json = new JSONObject(jsonstr);
			int msgtype = 1;
			String chatTopic = json.getString("fromUserId");
			String fromUserId = json.getString("fromUserId");
			String toUserId = json.getString("toUserId");
			String type = json.getString("type");
			String timeSend = json.getString("timeSend");
			String content = json.getString("content");
			String fileSize = json.getString("fileSize");
			String timeLen = json.getString("timeLen");
			// 存储到数据库
			if (type.equals(XmppGlobals.MessageType.sound)) { // 语音消息处理
			// content = fileSize;
				//
				byte[] bytes = Base64.decode(content, Base64.DEFAULT);
				String filePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath().toString()
						+ fileSize.substring(0, fileSize.lastIndexOf("/") + 1);
				String fileName = fileSize.substring(
						fileSize.lastIndexOf("/") + 1, fileSize.length());
				boolean flag = byte2File(bytes, filePath, fileName);
				content = fileSize;
			}
			bean = new XmppMsgBean(msgtype, chatTopic, type, timeSend, content,
					fileSize, toUserId, fromUserId, timeLen, false);
			Dao<XmppMsgBean, Integer> messageDao = DataHelper.getInstance(
					context).getXmppMsgDAO();
			messageDao.create(bean);
			Log.e("成功", "存储数据成功！");

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public static boolean byte2File(byte[] buf, String filePath, String fileName) {
		boolean flag = true;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {
				boolean flags = dir.mkdirs();
				System.out.println(flags);
			}
			file = new File(filePath + fileName);
			file.createNewFile();
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

}
