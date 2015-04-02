package com.yale.qcxxandroid.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.yale.qcxxandroid.R;

public class MainTopMoreDialog extends Activity {
	//private MyDialog dialog;
	private LinearLayout layout;
	private Intent intent = new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_top_more_dialog);
		layout=(LinearLayout)findViewById(R.id.main_dialog_layout);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
	public void toNewShow(View view){
		intent = new Intent (MainTopMoreDialog.this,ShowMainTabActivity.class);			
		startActivity(intent);	
	}
	public void toTopShow(View view){
		intent = new Intent (MainTopMoreDialog.this,ShowMainTabActivity.class);			
		startActivity(intent);		
	}
	public void toCityShow(View view){
		intent = new Intent (MainTopMoreDialog.this,ShowMainTabActivity.class);			
		startActivity(intent);		
	}
	public void toSchoolShow(View view){
		intent = new Intent (MainTopMoreDialog.this,ShowMainTabActivity.class);			
		startActivity(intent);	
	}
}
