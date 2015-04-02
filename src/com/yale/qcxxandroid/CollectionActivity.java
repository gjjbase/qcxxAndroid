package com.yale.qcxxandroid;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class CollectionActivity extends TabActivity {
	private TabHost tabHost;
	private Intent intent;
	private Bundle bundle;
	private TextView btn1, btn2;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activitycollection);

		btn1 = (TextView) findViewById(R.id.btn1);
		btn2 = (TextView) findViewById(R.id.btn2);
		btn1.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn1.setTextColor(getResources().getColor(R.color.white));

				btn1.setBackgroundResource(R.drawable.mbtn);
				btn2.setTextColor(getResources().getColor(R.color.greener));
				btn2.setBackgroundResource(R.drawable.copyofmbtnr);
				tabHost.setCurrentTab(0);
			}
		});
		btn2.setOnClickListener(new OnClickListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				btn2.setTextColor(getResources().getColor(R.color.white));
				btn2.setBackgroundResource(R.drawable.mbtnr);
				btn1.setTextColor(getResources().getColor(R.color.greener));
				btn1.setBackgroundResource(R.drawable.copyofmbtn);
				tabHost.setCurrentTab(1);
			}
		});
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		bundle = new Bundle();
		bundle.putInt("tabHostValue", 0);
		intent = new Intent().setClass(this, MyCollectionActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("收藏").setIndicator("收藏").setContent(intent);
		tabHost.addTab(spec);
		bundle = new Bundle();
		bundle.putInt("tabHostValue", 1);
		intent = new Intent().setClass(this, MyCollectionActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("关注").setIndicator("关注").setContent(intent);
		tabHost.addTab(spec);
		// for (int i = 0; i < button.length; i++) {
		// button[i] = (Button) mMoveTab.findViewWithTag("main_button" + i);
		// button[i].setOnClickListener(new MainOnclickListener(i));
		// }
	}

	// class MainOnclickListener implements OnClickListener {
	// private int i = 0;
	//
	// public MainOnclickListener(int i) {
	// this.i = i;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// tabHost.setCurrentTab(i);
	// mMoveTab.selectTab(v);
	// }
	// }
	//
	public void backClick(View view) {
		this.finish();
	}
}
