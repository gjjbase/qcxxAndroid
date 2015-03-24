package com.yale.qcxxandroid;

import android.annotation.SuppressLint;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

public class MychildeActivity extends BaseActivity {
	private TextView back, txt_child;
	private EditText edt;
	private Editor edit;
	private ThreadUtil thread;
	@SuppressLint("HandlerLeak")
	public Handler mhandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				if (returnJson.equals("true")) {
					Log.i("###########", "通过");
				} else {
					Log.i("###########", "不通过");
				}
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "网络连接失败", 3000).show();
				break;

			}
		}
	};

	public void init(String name) {
		thread = new ThreadUtil(mhandler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.member.impl.UserInfoSessionBean':'updateUserInfo'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "")
				+ ",'columnName':" + name + ",'columnValue':"
				+ edt.getText().toString() + "}]";
		thread.doStartWebServicerequestWebService(MychildeActivity.this,
				jsonParamStr, methodStr, true);
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mychilde);
		back = (TextView) findViewById(R.id.back);
		txt_child = (TextView) findViewById(R.id.txt_child);
		edt = (EditText) findViewById(R.id.edt);
		edit = sp.edit();
		edit.putString("back", edt.getText().toString());
		edit.commit();
		txt_child.setText(getIntent().getStringExtra("chile"));

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (getIntent().getStringExtra("chile").equals("昵称")) {
					init("nick_name");
				} else if (getIntent().getStringExtra("chile").equals("签名")) {
					init("sign");
				} else if (getIntent().getStringExtra("chile").equals("真实姓名")) {
					init("verity_name");
				} else if (getIntent().getStringExtra("chile").equals("联系方式")) {
					init("phone_num");
				}
				finish();
			}
		});
	}
}
