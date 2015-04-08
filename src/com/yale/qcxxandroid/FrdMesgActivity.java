package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

@SuppressWarnings("deprecation")
@SuppressLint("HandlerLeak")
public class FrdMesgActivity extends BaseActivity {
	public TextView topname, cancel;
	public ListView searchList;
	public LinearLayout hint;
	protected AsyncQueryHandler asyncQuery;
	protected static final String NAME = "name", NUMBER = "number",
			SORT_KEY = "sort_key";
	protected List<ContentValues> phonelist;
	private ThreadUtil thread;
	private List<String> list = new ArrayList<String>();
	private String ster;
	private AlertDialog alert;

	protected class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);

		}

		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				phonelist = new ArrayList<ContentValues>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					ContentValues cv = new ContentValues();
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String number = cursor.getString(2);
					String sortKey = cursor.getString(3);
					if (number.startsWith("+86")) {
						cv.put(NAME, name);
						cv.put(NUMBER, number.substring(3)); // 去掉+86
						cv.put(SORT_KEY, sortKey);
					} else {
						cv.put(NAME, name);
						cv.put(NUMBER, number);
						cv.put(SORT_KEY, sortKey);
					}
					phonelist.add(cv);
				}
			}
			for (int k = 0; k < phonelist.size(); k++) {
				list.add(phonelist.get(k).getAsString(NUMBER) + "/"
						+ phonelist.get(k).getAsString(NAME) + ",");
			}
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < list.size(); j++) {
				sb.append(list.get(j));
			}
			ster = (sb.substring(0, sb.length() - 1).toString()).replace(" ",
					"");
			init();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		Uri uri = Uri.parse("content://com.android.contacts/data/phones");
		String[] projection = { "_id", "display_name", "data1", "sort_key" };
		asyncQuery.startQuery(0, null, uri, projection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}

	// 邀请好友加入
	private void myfrd(String youid, int friendHas) {
		Handler handler = new Handler() {
			@SuppressWarnings("unused")
			public void handlerMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					String returnJson = (String) msg.getData().getString(
							"returnJson");
					try {
						if (returnJson.equals(Globals.RETURN_STR_TRUE)) {
							toast("邀请成功，等待好友回复", getApplicationContext());
						} else {
							toast("邀请失败，请检查网络", getApplicationContext());
						}
					} catch (Exception e) {
						// TODO: handle exception
						toast("邀请失败，请检查网络", getApplicationContext());
					}
					break;
				case 2:
					toast("邀请失败，请检查网络", getApplicationContext());
					break;

				}
			}
		};
		thread = new ThreadUtil(handler);
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'updateMyFriend'}]";

		String jsonParamStr = "[{'meId':" + sp.getString("userId", "")
				+ ",'yourId':" + youid + ",'columnValue':" + friendHas
				+ ",'columnName':" + "action_id" + "}]";
		thread.doStartWebServicerequestWebService(FrdMesgActivity.this,
				jsonParamStr, methodStr, true);
	}

	// 导入通讯录
	private void init() {
		thread = new ThreadUtil(myhandler);
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'saveUserInfoByTxl'}]";

		String jsonParamStr = "[{'telPhone':" + "'" + ster + "'" + ",'meId':"
				+ sp.getString("userId", "") + "}]";
		thread.doStartWebServicerequestWebService(FrdMesgActivity.this,
				jsonParamStr, methodStr, true);
	}

	// 发送短信
	private void msg(String meId, final String tel, final TextView txt,
			final TextView txt2) {
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:

					String returnJson = (String) msg.getData().getString(
							"returnJson");
					final String contet = "789744455";
					try {
						if (returnJson.equals(Globals.RETURN_STR_TRUE)) {
							// new
							// AlertDialog.Builder(FrdMesgActivity.this).setView(R.layout.)
							WindowManager manager = getWindowManager();
							Display display = manager.getDefaultDisplay();
							int width = display.getWidth();
							alert = new AlertDialog.Builder(
									FrdMesgActivity.this).create();
							alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
							alert.getWindow().setGravity(Gravity.CENTER);
							alert.show();
							alert.getWindow().setLayout(width * 8 / 9,
									LayoutParams.WRAP_CONTENT);
							alert.getWindow()
									.setContentView(R.layout.addalitem);
							final Button btn1 = (Button) alert
									.findViewById(R.id.btn1);
							final Button btn2 = (Button) alert
									.findViewById(R.id.btn2);
							final TextView photxt = (TextView) alert
									.findViewById(R.id.photxt);
							final EditText content = (EditText) alert
									.findViewById(R.id.content);
							photxt.setText(tel);
							content.setText(contet);
							btn1.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									SmsManager manger = SmsManager.getDefault();
									ArrayList<String> str = manger
											.divideMessage(contet);
									for (String s : str) {
										manger.sendTextMessage(tel, null, s,
												null, null);
									}
									alert.dismiss();
								}
							});
							btn2.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									alert.dismiss();
								}
							});

						} else {
							toast("发送失败，请检查网络", getApplicationContext());
						}
					} catch (Exception e) {
						// TODO: handle exception
						toast("发送失败，请检查网络", getApplicationContext());
					}
					break;
				case 2:
					toast("发送失败，请检查网络", getApplicationContext());
					break;
				}
			}
		};
		thread = new ThreadUtil(handler);
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'sendMsg'}]";

		String jsonParamStr = "[{'param':" + meId + ",'tel':" + tel + "}]";
		thread.doStartWebServicerequestWebService(FrdMesgActivity.this,
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
						if (jsoo.getJSONObject(i).getInt("actionId") == Globals.FRIEND_INIT) {
							jsarry.put(jsoo.getJSONObject(i));
						}
					}
					if (jsarry.length() != 0) {
						BaseAdapter base = new BaseAdapter() {

							@Override
							public View getView(final int position,
									View convertView, ViewGroup parent) {
								// TODO Auto-generated method stub
								convertView = LayoutInflater.from(
										FrdMesgActivity.this).inflate(
										R.layout.newfditem, null);
								TextView name = (TextView) convertView
										.findViewById(R.id.name);
								final TextView msg = (TextView) convertView
										.findViewById(R.id.msg);
								final TextView txt = (TextView) convertView
										.findViewById(R.id.txt);
								try {
									name.setText(jsarry.getJSONObject(position)
											.getString("actionMemo"));
									msg.setText("您可以邀请该同学加入青春秀秀");
									txt.setText("邀请");
									txt.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method
											// stub
											try {
												msg(jsarry.getJSONObject(
														position).getString(
														"meId"),
														jsarry.getJSONObject(
																position)
																.getString(
																		"yourId"),
														msg, txt);
												myfrd(jsarry.getJSONObject(
														position).getString(
														"yourId"),
														Globals.FRIEND_REQUEST_APP);
												init();
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}
									});

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								return convertView;
							}

							@Override
							public long getItemId(int position) {
								// TODO Auto-generated method stub
								return position;
							}

							@Override
							public Object getItem(int position) {
								// TODO Auto-generated method stub
								return position;
							}

							@Override
							public int getCount() {
								// TODO Auto-generated method stub
								return jsarry.length();
							}
						};
						searchList.setAdapter(base);
					} else {
						toast("失败", getApplicationContext());
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				break;

			case 2:

				break;

			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewfrd);
		topname = (TextView) findViewById(R.id.topname);
		hint = (LinearLayout) findViewById(R.id.hint);
		hint.setVisibility(View.GONE);
		topname.setText("通讯录好友");
		cancel = (TextView) findViewById(R.id.cancel);
		searchList = (ListView) findViewById(R.id.list);
		topname.setText("加好友");
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		topname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void back(View v) {
		finish();
	}
}
