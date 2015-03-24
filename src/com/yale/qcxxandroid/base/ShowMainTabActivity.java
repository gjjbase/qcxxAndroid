package com.yale.qcxxandroid.base;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import com.yale.qcxxandroid.ShowActivity;
import com.yale.qcxxandroid.ShowListActivity;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.ShowPubActivity;

@SuppressWarnings("deprecation")
public class ShowMainTabActivity extends TabActivity {
	private TabHost tabHost;
	private Intent intent;
	private Bundle bundle;
	private MoveTab mMoveTab;
	private Button[] button = new Button[4];
	int tabHostValue = 0;
	Button chose;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_main_tab_activity);
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		bundle = new Bundle();
		intent = new Intent().setClass(this, ShowListActivity.class).putExtras(
				bundle);
		spec = tabHost.newTabSpec("最新").setIndicator("最新").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ShowListActivity.class).putExtras(
				bundle);
		spec = tabHost.newTabSpec("我校").setIndicator("我校").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ShowListActivity.class).putExtras(
				bundle);
		spec = tabHost.newTabSpec("同城").setIndicator("同城").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ShowListActivity.class).putExtras(
				bundle);
		spec = tabHost.newTabSpec("总榜").setIndicator("总榜").setContent(intent);
		tabHost.addTab(spec);
		mMoveTab = (MoveTab) findViewById(R.id.main_tab_group);
		try {
			tabHostValue = (Integer) getIntent().getSerializableExtra(
					"tabHostValue");
		} catch (Exception e) {
		}
		for (int i = 0; i < button.length; i++) {
			button[i] = (Button) mMoveTab.findViewWithTag("main_button" + i);
			button[i].setOnClickListener(new MainOnclickListener(i));
		}
		chose = (Button) findViewById(R.id.chose);
		chose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			}
		});
		button[0].setTextColor(getResources().getColor(R.color.white));
		button[0].setBackground(getResources().getDrawable(R.color.greener));
	}

	class MainOnclickListener implements OnClickListener {
		private int i = 0;

		public MainOnclickListener(int i) {
			this.i = i;
		}

		@Override
		public void onClick(View v) {
			tabHost.setCurrentTab(i);
			mMoveTab.selectTab(v);
			switch (i) {
			case 0:
				button[0].setTextColor(getResources().getColor(R.color.white));
				button[1]
						.setTextColor(getResources().getColor(R.color.greener));
				button[2]
						.setTextColor(getResources().getColor(R.color.greener));
				button[3]
						.setTextColor(getResources().getColor(R.color.greener));
				button[0].setBackground(getResources().getDrawable(
						R.color.greener));

				button[1].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[2].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[3].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				break;
			case 1:
				button[0].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));

				button[1].setBackground(getResources().getDrawable(
						R.color.greener));
				button[2].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[3].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[0]
						.setTextColor(getResources().getColor(R.color.greener));
				button[1].setTextColor(getResources().getColor(R.color.white));
				button[2]
						.setTextColor(getResources().getColor(R.color.greener));
				button[3]
						.setTextColor(getResources().getColor(R.color.greener));
				break;
			case 2:
				button[0].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));

				button[1].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[2].setBackground(getResources().getDrawable(
						R.color.greener));
				button[3].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[0]
						.setTextColor(getResources().getColor(R.color.greener));
				button[1]
						.setTextColor(getResources().getColor(R.color.greener));
				button[2].setTextColor(getResources().getColor(R.color.white));
				button[3]
						.setTextColor(getResources().getColor(R.color.greener));
				break;
			case 3:
				button[0].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));

				button[1].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[2].setBackground(getResources().getDrawable(
						R.drawable.btn_bg_xml));
				button[3].setBackground(getResources().getDrawable(
						R.color.greener));
				button[0]
						.setTextColor(getResources().getColor(R.color.greener));
				button[1]
						.setTextColor(getResources().getColor(R.color.greener));
				button[2]
						.setTextColor(getResources().getColor(R.color.greener));
				button[3].setTextColor(getResources().getColor(R.color.white));
				break;

			}
		}
	}

	public void pubShow(View view) {
		intent.setClass(ShowMainTabActivity.this, ShowPubActivity.class);
		startActivity(intent);
	}
}