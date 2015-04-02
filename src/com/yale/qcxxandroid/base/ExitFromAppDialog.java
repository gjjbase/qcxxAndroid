package com.yale.qcxxandroid.base;


import com.yale.qcxxandroid.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ExitFromAppDialog extends Activity {
	//private MyDialog dialog;
	private LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exit_dialog_app_dialog);
		//dialog=new MyDialog(this);
		layout=(LinearLayout)findViewById(R.id.exit_layout2);
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
	
	public void exitbutton1(View v) {  
    	this.finish();    	
      }  
	public void exitbutton0(View v) {  
		SharedPreferences sp = getSharedPreferences("qcxx",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("userId", "");
		editor.putString("passWord", "");
		editor.commit();		
    	this.finish();
    	MainActivity.instance.finish();
      }  
	
}
