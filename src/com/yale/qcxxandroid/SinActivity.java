package com.yale.qcxxandroid;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;

public class SinActivity extends BaseActivity {
	private TextView txt_back;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sin);
		txt_back = (TextView) findViewById(R.id.txt_back);
		txt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public void xuanyan(View v) {
		toast("宣言", getApplicationContext());
	}

	public void caiyi(View v) {
		toast("才艺", getApplicationContext());
	}

	public void zhuanye(View v) {
		toast("专业", getApplicationContext());
	}

	public void aihao(View v) {
		toast("爱好", getApplicationContext());
	}
}
