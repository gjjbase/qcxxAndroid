package com.yale.qcxxandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;

public class DeteShooleActivity extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private TextView sf_name, sc_name, xy_name;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detaschool);
		sf_name = (TextView) findViewById(R.id.sf_name);
		sc_name = (TextView) findViewById(R.id.sc_name);
		xy_name = (TextView) findViewById(R.id.xy_name);
	}

	public void regBackClick(View v) {
		finish();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		sf_name.setText(sp.getString("sf_name", ""));
		sc_name.setText(sp.getString("sc_name", ""));
		xy_name.setText(sp.getString("xy_name", ""));
	}

	// 省份
	public void onclik1(View v) {
		bundle.putString("typ", "1");
		intent.setClass(getApplicationContext(), DetaActivity.class).putExtras(
				bundle);
		sc_name.setText("未选择");
		xy_name.setText("未选择");
		startActivity(intent);
	}

	// 学校
	public void onclik2(View v) {
		if (sp.getString("pasf_name", "").equals("")) {
			Toast.makeText(getApplicationContext(), "请先输入省份", 3000).show();
		} else {
			bundle.putString("typ", "2");
			intent.setClass(getApplicationContext(), DetaActivity.class)
					.putExtras(bundle);
			xy_name.setText("未选择");
			startActivity(intent);
		}

	}

	// 院系
	public void onclik3(View v) {
		if (sp.getString("pasc_name", "").equals("")) {
			Toast.makeText(getApplicationContext(), "请先输入学校", 3000).show();
		} else {
			bundle.putString("typ", "3");
			intent.setClass(getApplicationContext(), DetaActivity.class)
					.putExtras(bundle);
			startActivity(intent);
		}
	}
}
