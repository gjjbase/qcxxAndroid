package com.yale.qcxxandroid.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yale.qcxxandroid.MyActivityManager;
import com.yale.qcxxandroid.R;

public class Exit extends BaseActivity {
	private LinearLayout layout;
	private Button exitBtn0, exitBtn1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_dialog);
		MyActivityManager.getInstance().addActivity(Exit.this);
		layout = (LinearLayout) findViewById(R.id.exit_layout);
		exitBtn0 = (Button) findViewById(R.id.exitBtn0);
		exitBtn0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		exitBtn1 = (Button) findViewById(R.id.exitBtn1);
		exitBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				System.exit(0);
			}
		});
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// finish();
	// return true;
	// }

	// public void exitbutton1(View v) {
	// this.finish();
	// }
	//
	// public void exitbutton0(View v) {
	// // this.finish();
	// // MainActivity.instance.finish();
	// System.exit(0);
	// }

}
