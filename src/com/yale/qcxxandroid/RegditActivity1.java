package com.yale.qcxxandroid;

import java.util.Timer;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MainActivity;

public class RegditActivity1 extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private TextView sexValue, loveValue;
	private EditText code, nc, passWord, valPassWord;
	private String cd, ncheng, pw, valpw, sex, love;
	private Button regdit;
	Timer timer = new Timer();
	AlertDialog alert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regdit1_activity);
		regdit = (Button) this.findViewById(R.id.regdit);
		cd = code.getText().toString();
		ncheng = nc.getText().toString();
		pw = passWord.getText().toString();
		valpw = valPassWord.getText().toString();
		sex = sexValue.getText().toString();
		love = loveValue.getText().toString();
		regdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(cd) && !StringUtils.isEmpty(ncheng)
						&& !StringUtils.isEmpty(pw)
						&& !StringUtils.isEmpty(valpw)
						&& !StringUtils.isEmpty(sex)
						&& !StringUtils.isEmpty(love)) {
					// regdit.setEnabled(true);
					// regdit.refreshDrawableState();
					if (StringUtils.equals(valpw, pw)) {
						Toast.makeText(getApplicationContext(), "两次输入的密码不一致",
								3000).show();
					} else {

					}
				} else {
					// regdit.setEnabled(false);
					// regdit.refreshDrawableState();
					Toast.makeText(getApplicationContext(), "请填写完整的信息", 3000)
							.show();
				}

			}
		});
		nc = (EditText) this.findViewById(R.id.nc);
		nc.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});

		passWord = (EditText) this.findViewById(R.id.passWord);
		passWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});

		valPassWord = (EditText) this.findViewById(R.id.valPassWord);
		valPassWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});

		sexValue = (TextView) this.findViewById(R.id.sexValue);
		sexValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});
	}

	public void regPreClick(View view) {
		this.finish();
	}

	public void regditSub(View view) {
		intent = new Intent();
		bundle = new Bundle();
		intent.setClass(RegditActivity1.this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	public void loveChose(View view) {

	}

	private void txt_arr(TextView[] view, int falg) {
		for (int i = 0; i < view.length; i++) {
			view[i].setTextColor(getResources().getColor(R.color.textcol));
		}
		view[falg].setTextColor(Color.RED);
	}

	public void alert() {
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		alert = new AlertDialog.Builder(this).create();
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.getWindow().setGravity(Gravity.BOTTOM);
		alert.show();
		alert.getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
		alert.getWindow().setContentView(R.layout.popwind);
		// LinearLayout view = (LinearLayout) getLayoutInflater().inflate(
		// R.layout.popwind, null);
		final TextView txt_exit = (TextView) alert.findViewById(R.id.txt_exit);
		final TextView txt1 = (TextView) alert.findViewById(R.id.txt1);
		final TextView txt2 = (TextView) alert.findViewById(R.id.txt2);
		final TextView[] txt = { txt1, txt2, txt_exit };

		txt_exit.setText("单身中");
		txt1.setText("恋爱中");
		txt2.setText("保密");
		txt_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_arr(txt, 2);
				sexValue.setText(txt_exit.getText().toString());
			}
		});

		txt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt_arr(txt, 0);
				sexValue.setText(txt1.getText().toString());
			}
		});

		txt2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				txt_arr(txt, 1);
				sexValue.setText(txt2.getText().toString());
			}
		});

	}

	public void sexChose(View view) {
		alert();
	}

	public void commValid() {
		cd = code.getText().toString();
		ncheng = nc.getText().toString();
		pw = passWord.getText().toString();
		valpw = valPassWord.getText().toString();
		sex = sexValue.getText().toString();
		love = loveValue.getText().toString();
		if (!StringUtils.isEmpty(cd) && !StringUtils.isEmpty(ncheng)
				&& !StringUtils.isEmpty(pw) && !StringUtils.isEmpty(valpw)
				&& !StringUtils.isEmpty(sex) && !StringUtils.isEmpty(love)) {
			regdit.setEnabled(true);
			regdit.refreshDrawableState();
		} else {
			regdit.setEnabled(false);
			regdit.refreshDrawableState();
		}
	}
}
