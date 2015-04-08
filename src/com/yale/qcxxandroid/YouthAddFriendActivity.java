package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

public class YouthAddFriendActivity extends BaseActivity {
	@SuppressWarnings("unused")
	private ListView searchList, bomlist;
	private TextView search;
	private TextView bomcancel, topname, cancel;
	private EditText bomsearch;
	private LinearLayout toplin, bomlin;
	private ThreadUtil thread, threadutil;

	@SuppressLint("HandlerLeak")
	private void myfrd(String youid, int friendHas, final int tag,
			final TextView view) {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:

					String returnjs = (String) msg.getData().getString(
							"returnJson");
					if (returnjs.equals(Globals.RETURN_STR_TRUE)) {
						switch (tag) {
						case 1:
							break;
						case 2:
							// 别人正在处理
							view.setText("处理中");
							view.setEnabled(false);
							view.setTextColor(getResources().getColor(
									R.color.gray));
							view.setText("您已向该同学发出好友申请");
							break;

						}
					} else {
						toast("添加失败,请检查网络", getApplicationContext());
					}
					init();
					break;

				case 2:
					toast("添加失败,请检查网络", getApplicationContext());
					break;

				}
			}
		};
		threadutil = new ThreadUtil(handler);
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'updateMyFriend'}]";

		String jsonParamStr = "[{'meId':" + sp.getString("userId", "")
				+ ",'yourId':" + youid + ",'columnValue':" + friendHas
				+ ",'columnName':" + "'" + "action_id" + "'" + "}]";
		threadutil.doStartWebServicerequestWebService(
				YouthAddFriendActivity.this, jsonParamStr, methodStr, true);
	}

	// 申请中，处理中，拒绝，被拒绝，通过
	// 我申请对方加我好友，我处理对方加我好友的请求
	@SuppressLint("HandlerLeak")
	private void init() {
		thread = new ThreadUtil(myhandler);
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'myFriendList'}]";

		String jsonParamStr = "[{'userId':" + sp.getString("userId", "")
				+ ",'actionId':+-100+}]";
		thread.doStartWebServicerequestWebService(YouthAddFriendActivity.this,
				jsonParamStr, methodStr, true);

	}

	@SuppressLint("HandlerLeak")
	Handler myhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					final JSONArray jsoo = new JSONArray(returnJson);
					final JSONArray jsarry = new JSONArray();
					for (int i = 0; i < jsoo.length(); i++) {
						if (jsoo.getJSONObject(i).getInt("actionId") != Globals.FRIEND_INIT
								&& jsoo.getJSONObject(i).getInt("actionId") != Globals.FRIEND_HAS) {
							jsarry.put(jsoo.getJSONObject(i));
						}
					}
					final BaseAdapter base = new BaseAdapter() {

						@Override
						public View getView(final int position,
								View convertView, ViewGroup parent) {
							convertView = LayoutInflater.from(
									getApplicationContext()).inflate(
									R.layout.newfditem, null);
							final TextView txt = (TextView) convertView
									.findViewById(R.id.txt);
							final TextView name = (TextView) convertView
									.findViewById(R.id.name);
							final TextView msg = (TextView) convertView
									.findViewById(R.id.msg);
							try {
								name.setText(jsarry.getJSONObject(position)
										.getString("actionMemo"));
								switch (jsarry.getJSONObject(position).getInt(
										"actionId")) {
								// 接受邀请
								case Globals.FRIEND_REQUEST_APP:
									msg.setText("该同学正在接受您的邀请");
									txt.setText("邀请中");
									txt.setEnabled(false);
									txt.setTextColor(getResources().getColor(
											R.color.gray));
									break;
								// 正在请求添加
								case Globals.FRIEND_REQUEST:
									msg.setText("该同学正在请求您的添加");
									txt.setText("请求中");
									txt.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											new AlertDialog.Builder(
													YouthAddFriendActivity.this)
													.setTitle("好友操作")
													.setMessage("是否同意")
													.setPositiveButton(
															"确认",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	try {
																		myfrd(jsarry
																				.getJSONObject(
																						position)
																				.getString(
																						"yourId"),
																				Globals.FRIEND_HAS,
																				1,
																				null);
																	} catch (JSONException e) {
																		e.printStackTrace();
																	}

																}
															})
													.setNegativeButton(
															"不同意",
															new DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	msg.setText("该同学正在请求您的添加");
																	dialog.dismiss();
																}
															})
													.setNeutralButton(
															"取消",
															new DialogInterface.OnClickListener() {

																@Override
																public void onClick(
																		DialogInterface dialog,
																		int which) {
																	msg.setText("该同学正在请求您的添加");
																	dialog.dismiss();
																}
															}).create().show();
										}
									});

									break;
								// 别人正在处理你的添加
								case Globals.FRIEND_WAIT:
									txt.setText("等待添加");
									txt.setEnabled(false);
									txt.setTextColor(getResources().getColor(
											R.color.gray));
									msg.setText("该同学正在处理您的申请");
									break;
								// 拒绝你的添加
								case Globals.FRIEND_REF:
									txt.setText("已拒绝");
									txt.setEnabled(false);
									txt.setTextColor(getResources().getColor(
											R.color.gray));
									msg.setText("您已拒绝该同学的申请");
									break;
								// 系统注册了，可以添加
								case Globals.FRIEND_IN_SYS:
									txt.setText("添加");
									msg.setText("您可以添加该同学为好友");
									txt.setEnabled(true);
									txt.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method
											// stub
											try {
												myfrd(jsarry.getJSONObject(
														position).getString(
														"yourId"),
														Globals.FRIEND_WAIT, 2,
														txt);

											} catch (JSONException e) {
												e.printStackTrace();

											}
										}
									});
									break;
								// 已经拉黑
								case Globals.FRIEND_BLACK:
									txt.setText("已拉黑");
									txt.setEnabled(false);
									txt.setTextColor(getResources().getColor(
											R.color.gray));
									break;
								// 已经删除
								case Globals.FRIEND_DELETE:
									txt.setText("已删除");
									txt.setEnabled(false);
									txt.setTextColor(getResources().getColor(
											R.color.gray));
									break;
								}

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return convertView;
						}

						@Override
						public long getItemId(int position) {
							return position;
						}

						@Override
						public Object getItem(int position) {
							return position;
						}

						@Override
						public int getCount() {
							return jsarry.length();
						}
					};
					searchList.setAdapter(base);
				} catch (Exception e) {
					// TODO: handle exception
					toast("获取通讯录失败", getApplicationContext());
				}
				break;
			case 2:
				toast("获取通讯录失败", getApplicationContext());
				break;

			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youth_addfriend_activity);
		search = (TextView) findViewById(R.id.search);
		bomcancel = (TextView) findViewById(R.id.bomcancel);
		topname = (TextView) findViewById(R.id.topname);
		cancel = (TextView) findViewById(R.id.cancel);
		bomsearch = (EditText) findViewById(R.id.bomsearch);
		searchList = (ListView) findViewById(R.id.searchList);
		bomlist = (ListView) findViewById(R.id.bomlist);
		toplin = (LinearLayout) findViewById(R.id.toplin);
		bomlin = (LinearLayout) findViewById(R.id.bomlin);
		cancel.setText("通讯录");
		topname.setText("新的朋友");
		init();
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toplin.setVisibility(View.GONE);
				bomlin.setVisibility(View.VISIBLE);
				bomsearch.setFocusable(true);
				bomsearch.setFocusableInTouchMode(true);
				bomsearch.requestFocus();
				bomsearch.findFocus();
				bomsearch.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(searchList, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
						InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		});
		bomcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toplin.setVisibility(View.VISIBLE);
				bomlin.setVisibility(View.GONE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(bomsearch.getWindowToken(), 0);
			}
		});
	}

	public void backer(View v) {
		finish();
	}
}
