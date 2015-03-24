package com.yale.qcxxandroid;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

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
		Toast.makeText(getApplicationContext(), "宣言", 3000).show();
	}

	public void caiyi(View v) {
		Toast.makeText(getApplicationContext(), "才艺", 3000).show();
	}

	public void zhuanye(View v) {
		Toast.makeText(getApplicationContext(), "专业", 3000).show();
	}

	public void aihao(View v) {
		Toast.makeText(getApplicationContext(), "爱好", 3000).show();
	}
}
