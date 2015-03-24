package com.yale.qcxxandroid;

import org.apache.cordova.DroidGap;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

public class ScorePlazaActivity extends DroidGap {
	Intent intent = new Intent();
	Bundle bundle = new Bundle();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init();
		this.appView.setBackgroundResource(R.drawable.demo5);
		// // super.setIntegerProperty("splashscreen",
		// // R.layout.common_flipping_loading_diloag);
		// super.new
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		// 判断是否联网 如果联网加载远程地址 如果不联网加载本地地址
		if (info != null && info.isAvailable()) {
			super.loadUrl("file:///android_asset/www/plaza-index.html", 3000);
			appView.addJavascriptInterface(this, "jsOnclick");
		} else {
			super.loadUrl("file:///android_asset/www/error.html", 3000);
		}

	}

	/****
	 * 反跳html到Activity
	 */
	public void back() {
		intent.setClass(ScorePlazaActivity.this, MyMessageActivity.class);
		startActivity(intent);
	}
}
