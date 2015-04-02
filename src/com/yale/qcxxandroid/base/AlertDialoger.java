package com.yale.qcxxandroid.base;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yale.qcxxandroid.R;

public class AlertDialoger extends BaseActivity {
	private TextView message;
	private EditText edt_put;
	private String falg;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		edt_put = (EditText) findViewById(R.id.edt_put);
		message = (TextView) findViewById(R.id.mess);
		falg = getIntent().getExtras().getString("falg");
		if (falg.equals("1")) {
			edt_put.setVisibility(View.GONE);
			message.setText(getIntent().getExtras().getString("message"));
		} else if (falg.equals("2")) {
			message.setVisibility(View.GONE);

		}
		// System.out.println(getIntent().getExtras().getString("message"));

	}

	public void noButton(View v) {
		this.finish();
	}

	public void exitButton(View v) {
		//
		// Toast.makeText(getApplicationContext(), edt_put.getText().toString(),
		// 3000).show();
		finish();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
