package com.yale.qcxxandroid;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.PopupWindow;

import com.yale.qcxxandroid.base.AlertDialoger;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MainActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

public class StartActivity extends BaseActivity {
	private ThreadUtil threadUtil;
	private Intent intent;
	private Bundle bundle;
	private PopupWindow menuWindow;
	private boolean menu_display = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyActivityManager.getInstance().addActivity(StartActivity.this);
		try {
			if (!StringUtils.isEmpty(sp.getString("number", ""))
					&& !StringUtils.isEmpty(sp.getString("passWord", ""))) {
				threadUtil = new ThreadUtil(handler);
				String methodStr = "[{'com.yale.qcxx.sessionbean.member.impl.UserInfoSessionBean':'getUserInfo'}]";
				String jsonParamStr = "[{'number':"
						+ sp.getString("number", "") + ",'password':"
						+ sp.getString("passWord", "") + "}]";
				threadUtil.doStartWebServicerequestWebService(
						StartActivity.this, jsonParamStr, methodStr, true);
			}
		} catch (Exception e) {
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);
	}

	public void toLogin(View v) {
		Intent intent = new Intent(StartActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public void toRegdit(View v) {
		Intent intent = new Intent(StartActivity.this, RegditActivity.class);
		startActivity(intent);
		finish();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					JSONArray joA = new JSONArray(returnJson);
					JSONObject jo = joA.getJSONObject(0);
					if (!StringUtils.isEmpty(jo.get("userId").toString())) {
						intent = new Intent();
						intent.setClass(StartActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						if (menu_display) {
							menuWindow.dismiss();
							menu_display = false;
						} else {
							commDialog();
						}
					}
				} catch (JSONException e) {
					commDialog();
				}
				break;
			case 2:
				break;
			}
		}
	};

	public void commDialog() {
		intent = new Intent();
		bundle = new Bundle();
		bundle.putString("falg", "1");
		bundle.putString("message", "用户名或密码已经被修改，请重新登录");
		intent.putExtras(bundle);
		intent.setClass(StartActivity.this, AlertDialoger.class);
		startActivity(intent);
	}
}
