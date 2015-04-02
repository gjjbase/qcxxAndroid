package com.yale.qcxxandroid.base;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;

import com.yale.qcxxandroid.ClassActivity;
import com.yale.qcxxandroid.MyActivity;
import com.yale.qcxxandroid.MyActivityManager;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.ShowActivity;
import com.yale.qcxxandroid.YouthActivity;
import com.yale.qcxxandroid.chat.xmpp.XmppService;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	private TabHost tabHost;
	private Intent intent;
	private Bundle bundle;
	private MoveTab mMoveTab;
	private Button[] button = new Button[4];
	int tabHostValue = 0;
	static MainActivity instance = null;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		MyActivityManager.getInstance().addActivity(MainActivity.this);
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		instance = this;
		bundle = new Bundle();
		intent = new Intent().setClass(this, YouthActivity.class).putExtras(
				bundle);
		spec = tabHost.newTabSpec("青春").setIndicator("青春").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ShowActivity.class).putExtras(
				bundle);
		spec = tabHost.newTabSpec("秀秀").setIndicator("秀秀").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ClassActivity.class).putExtras(
				bundle);
		spec = tabHost.newTabSpec("班级").setIndicator("班级").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, MyActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("我的").setIndicator("我的").setContent(intent);
		tabHost.addTab(spec);
		mMoveTab = (MoveTab) findViewById(R.id.main_tab_group);
		tabHost.setCurrentTab(0);
		for (int i = 0; i < button.length; i++) {
			button[i] = (Button) mMoveTab.findViewWithTag("main_button" + i);
			button[i].setOnClickListener(new MainOnclickListener(i));
			try {
				tabHostValue = (Integer) getIntent().getSerializableExtra(
						"tabHostValue");
			} catch (Exception e) {

			}
		}
		button[0]
				.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_qingchun),
						null, null);
		Intent intent = new Intent(MainActivity.this, XmppService.class);
		startService(intent);
	}

	class MainOnclickListener implements OnClickListener {
		private int i = 0;

		public MainOnclickListener(int i) {
			this.i = i;
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
		@Override
		public void onClick(View v) {
			tabHost.setCurrentTab(i);
			mMoveTab.selectTab(v);
			switch (i) {
			case 0:
				button[0].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_qingchun),
						null, null);
				button[1].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_xiuxiuer),
						null, null);
				button[2].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_banjier),
						null, null);
				button[3].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_wodeer),
						null, null);
				break;
			case 1:
				button[0].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_qingchuner),
						null, null);
				button[1].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_xiuxiu),
						null, null);
				button[2].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_banjier),
						null, null);
				button[3].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_wodeer),
						null, null);
				break;
			case 2:
				button[0].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_qingchuner),
						null, null);
				button[1].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_xiuxiuer),
						null, null);
				button[2].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_banji), null,
						null);
				button[3].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_wodeer),
						null, null);
				break;
			case 3:
				button[0].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_qingchuner),
						null, null);
				button[1].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_xiuxiuer),
						null, null);
				button[2].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_banjier),
						null, null);
				button[3].setCompoundDrawablesRelativeWithIntrinsicBounds(null,
						getResources().getDrawable(R.drawable.img_wode), null,
						null);
				break;
			}
		}
	}
}