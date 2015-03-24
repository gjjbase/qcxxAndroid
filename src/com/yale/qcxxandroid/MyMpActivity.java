package com.yale.qcxxandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.yale.qcxxandroid.base.BaseActivity;
public class MyMpActivity extends BaseActivity {
	Intent intent = new Intent();
	Bundle bundle = new Bundle();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_mp_activity);
	}
	public void backClick(View v) {                        
		this.finish();
	}
	public void saveClick(View v) {                        
		this.finish();
	}	
	public void viewSchoolOnclick(View ivew){
    	intent.setClass(MyMpActivity.this,MySchoolActivity.class);
    	startActivity(intent);
	}
}
