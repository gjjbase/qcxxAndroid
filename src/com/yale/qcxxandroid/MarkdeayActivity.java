package com.yale.qcxxandroid;

import android.os.Bundle;
import android.view.View;

import com.yale.qcxxandroid.base.BaseActivity;

public class MarkdeayActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_markdeay);
	}

	public void backClick(View v) {
		finish();
	}
}
