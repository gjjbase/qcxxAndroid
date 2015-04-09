package com.yale.qcxxandroid;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.yale.qcxxandroid.base.AlertDialoger;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MainActivity;
import com.yale.qcxxandroid.base.Welcome;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class LoginActivity extends BaseActivity {
	private Intent intent;
	private Bundle bundle;
	private ThreadUtil threadUtil;
	private EditText userName, passWord;
	private Button login, forgetPasswd;
	private boolean menu_display = false;
	private String uName;
	private String pWord;
	private PopupWindow menuWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		userName = (EditText) this.findViewById(R.id.userName);
		MyActivityManager.getInstance().addActivity(LoginActivity.this);
		userName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				uName = userName.getText().toString();
				pWord = passWord.getText().toString();
				if (!StringUtils.isEmpty(uName) && !StringUtils.isEmpty(pWord)) {
					login.setEnabled(true);
					login.refreshDrawableState();
				} else {
					login.setEnabled(false);
					login.refreshDrawableState();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				uName = userName.getText().toString();
				pWord = passWord.getText().toString();
				if (!StringUtils.isEmpty(uName) && !StringUtils.isEmpty(pWord)) {
					login.setEnabled(true);
					login.refreshDrawableState();
				} else {
					login.setEnabled(false);
					login.refreshDrawableState();
				}
			}
		});
		passWord = (EditText) this.findViewById(R.id.passWord);
		passWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				uName = userName.getText().toString();
				pWord = passWord.getText().toString();
				if (!StringUtils.isEmpty(uName) && !StringUtils.isEmpty(pWord)) {
					login.setEnabled(true);
					login.refreshDrawableState();
				} else {
					login.setEnabled(false);
					login.refreshDrawableState();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				uName = userName.getText().toString();
				pWord = passWord.getText().toString();
				if (!StringUtils.isEmpty(uName) && !StringUtils.isEmpty(pWord)) {
					login.setEnabled(true);
					login.refreshDrawableState();
				} else {
					login.setEnabled(false);
					login.refreshDrawableState();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				uName = userName.getText().toString();
				pWord = passWord.getText().toString();
				if (!StringUtils.isEmpty(uName) && !StringUtils.isEmpty(pWord)) {
					login.setEnabled(true);
					login.refreshDrawableState();
				} else {
					login.setEnabled(false);
					login.refreshDrawableState();
				}
			}
		});
		login = (Button) this.findViewById(R.id.login);
		forgetPasswd = (Button) this.findViewById(R.id.forgetPasswd);
		// if (getIntent().getExtras().getBoolean("true", false) == true) {
		// userName.setText(getIntent().getExtras().getString("name"));
		// passWord.setText(getIntent().getExtras().getString("psw"));
		// }
	}

	public void loginBackClick(View v) { // 标题栏 返回按钮
		this.finish();
	}

	public void loginClick(View view) {// 登录
		uName = userName.getText().toString();
		pWord = passWord.getText().toString();
		threadUtil = new ThreadUtil(handler);
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'getUserInfo'}]";
		String jsonParamStr = "[{'number':" + uName + ",'password':" + pWord
				+ "}]";
		threadUtil.doStartWebServicerequestWebService(LoginActivity.this,
				jsonParamStr, methodStr, true);
	}

	public void forgetPasswdClick(View v) { // 忘记密码
		Uri uri = Uri.parse("#");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
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

					if (returnJson == null || returnJson.equals("")) {
						Toast.makeText(LoginActivity.this, "登录失败！", 3000)
								.show();
						return;
					}
					JSONArray joA = new JSONArray(returnJson);
					JSONObject jo = joA.getJSONObject(0);
					if (!StringUtils.isEmpty(jo.get("userId").toString())) {
						editor.putString("number", uName);
						editor.putString("userId", jo.get("userId").toString());
						editor.putString("passWord", jo.get("passWord")
								.toString());
						editor.putString("pWord", pWord);
						editor.putString("xxNum", jo.get("xxNum").toString());
						editor.putString("phoneNum", jo.get("phoneNum")
								.toString());
						editor.putString("nickName", jo.get("nickName")
								.toString());
						JSONArray jsonArray = jo.getJSONArray("userClasses");
						StringBuffer classes = new StringBuffer();
						StringBuffer inSchool = new StringBuffer();
						StringBuffer prof = new StringBuffer();
						StringBuffer collage = new StringBuffer();
						StringBuffer school = new StringBuffer();
						StringBuffer schoolCity = new StringBuffer();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jobj = jsonArray.getJSONObject(i);
							classes.append(jobj.getString("classes") + ",");
							inSchool.append(jobj.getString("inSchool") + ",");
							prof.append(jobj.getString("prof") + ",");
							collage.append(jobj.getString("collage") + ",");
							school.append(jobj.getString("school") + ",");
							schoolCity.append(jobj.getString("prof") + ",");
						}
						editor.putString(
								"schoolCity",
								"'"
										+ schoolCity.substring(0,
												schoolCity.length() - 1) + "'");
						editor.putString("school",
								"'" + school.substring(0, school.length() - 1)
										+ "'");
						editor.putString(
								"collage",
								"'"
										+ collage.substring(0,
												collage.length() - 1) + "'");
						editor.putString(
								"inSchool",
								"'"
										+ inSchool.substring(0,
												inSchool.length() - 1) + "'");
						editor.putString("prof",
								"'" + prof.substring(0, prof.length() - 1)
										+ "'");
						editor.putString(
								"classes",
								"'"
										+ classes.substring(0,
												classes.length() - 1) + "'");
						editor.commit();
						intent = new Intent();
						bundle = new Bundle();
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
						if (sp.getBoolean("true", false) == true) {
							intent.setClass(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
						} else {
							intent.setClass(LoginActivity.this, Welcome.class);
							startActivity(intent);
							editor.putBoolean("true", true);
							editor.commit();
						}

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
		bundle.putString("message", "用户名或密码错误");
		bundle.putString("falg", "1");
		intent.putExtras(bundle);
		intent.setClass(LoginActivity.this, AlertDialoger.class);
		startActivity(intent);
	}
}