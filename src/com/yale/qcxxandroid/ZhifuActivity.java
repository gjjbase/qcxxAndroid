package com.yale.qcxxandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;

public class ZhifuActivity extends BaseActivity {
	private TextView txt;
	String str = "点击保存按键，表示已经阅读并同意支付宝协议";

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activityzhifu);
		txt = (TextView) findViewById(R.id.txt);

		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new BackgroundColorSpan(Color.RED), 16, 21,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		txt.setText(style);
	}

	public void onback(View v) {
		finish();
	}
}
