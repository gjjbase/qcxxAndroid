package com.yale.qcxxandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yale.qcxxandroid.base.BaseActivity;

public class MoneyActivity extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_money);
	}

	public void zhifuonclick(View v) {
		intent.setClass(getApplicationContext(), ZhifuActivity.class);
		startActivity(intent);
	}
}
