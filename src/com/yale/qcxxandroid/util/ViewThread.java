package com.yale.qcxxandroid.util;

import android.content.Context;
import android.view.LayoutInflater;

public class ViewThread implements Runnable {
	Context context;
	private int view;

	public ViewThread(Context context, int view) {
		super();
		this.context = context;
		this.view = view;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!Thread.currentThread().isInterrupted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			// 使用postInvalidate可以直接在线程中更新界面
			LayoutInflater.from(context).inflate(view, null).postInvalidate();
		}
	}

}
