package com.yale.qcxxandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.yale.qcxxandroid.base.BaseActivity;

public class ClassActivity extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ClassActivity.this);
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

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.class_activity);
	}

	@SuppressLint("HandlerLeak")
	public void myClass(View v) {

		bundle.putInt("data", 0);
		intent.setClass(ClassActivity.this, MyClassActivityitem.class)
				.putExtras(bundle);
		startActivity(intent);
	}
	//通讯录
	public void adressbook(View v) {
		intent.setClass(ClassActivity.this, AdrebookActivity.class);
		startActivity(intent);
	}
	//隔壁班级
	public void nextClass(View v) {

		bundle.putInt("data", 1);
		intent.setClass(ClassActivity.this, NearActivity.class)
				.putExtras(bundle);
		startActivity(intent);
	}
	//摇摇
	public void onShake(View v) {

		intent.setClass(ClassActivity.this, ShakeActivity.class);
		startActivity(intent);
	}
	//院系
	public void deparClass(View v) {

		bundle.putInt("falg", 1);
		intent.setClass(ClassActivity.this, MyClasslActivityDepar.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	public void interClass(View v) {

		bundle.putString("name", "实习秀秀");
		intent.setClass(ClassActivity.this, MyShowListActivity.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	public void NearClass(View v) {

		intent.setClass(ClassActivity.this, MyClassActivityNear.class);
		startActivity(intent);
	}

}
