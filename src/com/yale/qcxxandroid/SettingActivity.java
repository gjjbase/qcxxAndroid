package com.yale.qcxxandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.Welcome;
import com.yale.qcxxandroid.base.ZhiTiaoTabActivity;

public class SettingActivity extends BaseActivity {
	Intent intent = new Intent();
	public TextView backer;
	Bundle bundle = new Bundle();
	public static Welcome instance = null;

	public void newMessage(View v) {
		intent.setClass(SettingActivity.this, ZhiTiaoTabActivity.class);
		startActivity(intent);
	}

	public void yinsi(View v) {
		Toast.makeText(getApplicationContext(), "隐私", 3000).show();
	}

	public void zhanghao(View v) {
		intent.setClass(SettingActivity.this, ZhanghaoActivity.class);
		startActivity(intent);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		MyActivityManager.getInstance().addActivity(SettingActivity.this);
		backer = (TextView) findViewById(R.id.backer);
		backer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingActivity.this);
				builder.setMessage("你确定退出吗？")
						.setCancelable(false)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										MyActivityManager.getInstance().exit();
										editor.clear();
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
		});
	}

	public void backClick(View v) {
		this.finish();
	}
}
