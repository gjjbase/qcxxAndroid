package com.yale.qcxxandroid.util;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ImageThread extends Thread {

	private List<String> imgs;
	private Handler handler;

	public ImageThread(List<String> imgs, Handler handler) {
		this.imgs = imgs;
		this.handler = handler;
	}

	public void run() {
		for (int i = 0; i < imgs.size(); i++) {
			try {
				GlobalUtil.getBitmapFromNet(imgs.get(i));
				Message msg = new Message();
				Bundle b = new Bundle();// 存放数据
				b.putInt("cl", 0);
				msg.setData(b);
				handler.sendMessage(msg); // 向Handler发送消息,更新UI
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
