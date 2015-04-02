package com.yale.qcxxandroid;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import com.yale.qcxxandroid.base.MarqueTextView;
import com.yale.qcxxandroid.base.MoveTab;
import com.yale.qcxxandroid.base.ZhiTiaoTabActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

@SuppressWarnings("deprecation")
public class ShowActivity extends TabActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private ThreadUtil thread;
	private TabHost tabHost;
	private Button[] button = new Button[4];
	int tabHostValue = 0;
	private MoveTab mMoveTab;
	private MarqueTextView mMarque;
	private TextView realShow;
	Timer timer = new Timer();
	TimerTask task;
	int index = 0;
	private TextView txt_myshow, txt_msg, txt_msger;
	protected SharedPreferences sp = null;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ShowActivity.this);
			builder.setMessage("你确定退出吗？")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
									MyActivityManager.getInstance().exit();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void xiuxiu(View v) {
		bundle.putString("name", txt_myshow.getText().toString());
		intent.setClass(ShowActivity.this, MyShowListActivity.class).putExtras(
				bundle);
		startActivity(intent);
	}

	@SuppressLint("HandlerLeak")
	private void ini() {
		thread = new ThreadUtil(myhandler);// com.yale.qcxx.sessionbean.member.impl
		String methodStr = "[{'com.yale.qcxx.sessionbean.show.impl.ShowsSessionBean':'showshowMain'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "") + "}]";
		thread.doStartWebServicerequestWebService(ShowActivity.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	Handler myhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					JSONArray joA = new JSONArray(returnJson);
					JSONObject jo = joA.getJSONObject(0);
					txt_msg.setText(jo.getInt("countZitiao") + "");
					txt_msger.setText(jo.getInt("countMyShow") + "");

				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 2:

				break;

			}

		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_activity);
		sp = getSharedPreferences("qcxx", Context.MODE_PRIVATE);
		mMarque = (MarqueTextView) findViewById(R.id.tv_marque);
		txt_myshow = (TextView) findViewById(R.id.txt_myshow);
		txt_msger = (TextView) findViewById(R.id.txt_msger);
		txt_msg = (TextView) findViewById(R.id.txt_msg);
		ini();
		mMarque.setText("2015新年秀，火热征集中...                  2015新年秀，火热征集中...               2015新年秀，火热征集中...");
		realShow = (TextView) findViewById(R.id.realShow);
		task = new TimerTask() {
			@Override
			public void run() {
				Message message = handler.obtainMessage();// 构造消息
				Bundle b = new Bundle();
				if (index == 0) {
					index = 1;
					b.putString("msg", "王玮发布了一个新年秀");
				} else {
					index = 0;
					b.putString("msg", "邱老师发布了视频秀");
				}
				message.setData(b);
				handler.sendMessage(message);
			}
		};
		timer.scheduleAtFixedRate(task, 0, 3000);
		tabHost = this.getTabHost();
		TabHost.TabSpec spec;
		bundle = new Bundle();
		intent = new Intent().setClass(this, ShowPageListActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("最新").setIndicator("最新").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ShowPageListActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("我校").setIndicator("我校").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ShowPageListActivity.class)
				.putExtras(bundle);
		spec = tabHost.newTabSpec("同城").setIndicator("同城").setContent(intent);
		tabHost.addTab(spec);
		intent = new Intent().setClass(this, ShowPageListActivity.class)
				.putExtras(bundle);
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
		button[0].setTextColor(getResources().getColor(R.color.white));
		button[0].setBackground(getResources().getDrawable(R.color.greener));
	}

	class MainOnclickListener implements OnClickListener {
		private int i = 0;

		public MainOnclickListener(int i) {
			this.i = i;
		}

		@SuppressLint("NewApi")
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
		bundle.putInt("tager", 2);
		intent.setClass(ShowActivity.this, ShowPubActivity.class).putExtras(
				bundle);
		startActivity(intent);
	}

	public void zhitiao(View view) {
		intent.setClass(ShowActivity.this, ZhiTiaoTabActivity.class);
		startActivity(intent);
	}

	public void gebi(View v) {
		bundle.putInt("data", 1);
		intent.setClass(ShowActivity.this, MyClassActivityitem.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			realShow.setText(msg.getData().getString("msg"));
		}
	};

}
