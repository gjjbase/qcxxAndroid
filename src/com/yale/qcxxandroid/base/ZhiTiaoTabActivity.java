package com.yale.qcxxandroid.base;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.yale.qcxxandroid.MyZhiTiaoActivity;
import com.yale.qcxxandroid.R;

@SuppressWarnings("deprecation")
public class ZhiTiaoTabActivity extends TabActivity {
	private TabHost tabHost;
	private Intent intent;
	private Bundle bundle;
	private TextView btn1, btn2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhi_tiao_tab_activity);
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
		intent = new Intent().setClass(this, MyZhiTiaoActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("我收到的").setIndicator("我收到的")
				.setContent(intent);
		tabHost.addTab(spec);
		bundle = new Bundle();
		bundle.putInt("tabHostValue", 1);
		intent = new Intent().setClass(this, MyZhiTiaoActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("我递过的").setIndicator("我递过的")
				.setContent(intent);
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