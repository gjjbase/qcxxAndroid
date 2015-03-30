package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

public class MyClasslActivityDepar extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private ThreadUtil thread;
	private ListView list;
	private JSONArray jsoo;
	private BaseAdapter adapter;
	private TextView txt_back;

	private void nexta() {
		List<String> mylist = new ArrayList<String>();
		for (int i = 0; i < list.getChildCount(); i++) {
			LinearLayout layou = (LinearLayout) list.getChildAt(i);
			CheckBox ck = (CheckBox) layou.findViewById(R.id.list_check);

			if (ck.isChecked() == true) {
				Log.i("++++++++++", String.valueOf(i));

				mylist.add(String.valueOf(i + 1));
				// TODO: handle exception
			}
		}
		StringBuffer sb = new StringBuffer();
		for (String str : mylist) {
			sb.append(str + ",");
		}

		if (sb.toString().equals("")) {
			Toast.makeText(getApplicationContext(), "您当前选中了:" + sb.toString(),
					3000).show();
		}

	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unused")
	public void init() {

		thread = new ThreadUtil(mhandler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'listOfClass'}]";

		JSONArray jsonArr = new JSONArray();
		String classes = sp.getString("classes", "");// 班级'5,13'

		String inSchool = sp.getString("inSchool", "");
		try {
			JSONObject params = new JSONObject();
			params.put(
					"inSchool",
					inSchool.substring(inSchool.indexOf("'") + 1,
							inSchool.lastIndexOf("'")));
			params.put(
					"classes",
					classes.substring(classes.indexOf("'") + 1,
							classes.lastIndexOf("'")));
			params.put("flag", getIntent().getExtras().getInt("data"));
			jsonArr = new JSONArray().put(params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		thread.doStartWebServicerequestWebService(MyClasslActivityDepar.this,
				jsonArr.toString(), methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unused")
	CompoundButton chebtn = null;
	Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					jsoo = new JSONArray(returnJson);
					adapter = new BaseAdapter() {

						@Override
						public View getView(int arg0, View arg1, ViewGroup arg2) {
							// TODO Auto-generated method stub
							final ViewHolder viewholder = new ViewHolder();
							arg1 = LayoutInflater.from(getApplicationContext())
									.inflate(R.layout.member_listview, null);
							viewholder.list_txt = (TextView) arg1
									.findViewById(R.id.list_txt);
							viewholder.hint = (TextView) arg1
									.findViewById(R.id.hint);
							viewholder.lin = (LinearLayout) arg1
									.findViewById(R.id.lin);
							viewholder.list_check = (CheckBox) arg1
									.findViewById(R.id.list_check);
							viewholder.list_txt.setText("武汉大学计算机学院");
							viewholder.list_check
									.setOnCheckedChangeListener(new OnCheckedChangeListener() {

										@Override
										public void onCheckedChanged(
												CompoundButton arg0,
												boolean arg1) {
											if (arg1) { // 设置为选择
												if (chebtn != null)
													chebtn.setChecked(false);// 否则的话
												chebtn = arg0;
											} else { // 设置取消
												chebtn = null;
											}
										}
									});
							viewholder.lin
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											/*
											 * fag++; if (fag % 2 == 0) {
											 * viewholder
											 * .list_check.setChecked(false); }
											 * else {
											 * viewholder.list_check.setChecked
											 * (true); }
											 */
											boolean flag = viewholder.list_check
													.isChecked();
											viewholder.list_check
													.setChecked(!flag);
										}
									});
							// try {
							// viewholder.hint.setText(jsoo
							// .getJSONObject(arg0)
							// .getJSONArray("classesList")
							// .getJSONObject(arg0).getString("cdMc"));

							// viewholder.list_txt.setText(jsoo.getJSONObject(
							// arg0).getString("schoolName")
							// + jsoo.getJSONObject(arg0).getString(
							// "collageName")
							// + jsoo.getJSONObject(arg0).getString(
							// "profName")
							// + jsoo.getJSONObject(arg0).getString(
							// "inSchoolName"));
							// } catch (JSONException e) {
							// e.printStackTrace();
							// }
							return arg1;
						}

						public long getItemId(int arg0) {
							return arg0;
						}

						@Override
						public Object getItem(int arg0) {
							return arg0;
						}

						public int getCount() {
							return 4;
							// return jsoo.length();

						}

						class ViewHolder {
							TextView list_txt, hint;
							CheckBox list_check;
							LinearLayout lin;
						}
					};
					list.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 2:

				break;

			}
		}
	};

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myclassdepar);
		list = (ListView) findViewById(R.id.list);
		txt_back = (TextView) findViewById(R.id.txt_back);
		txt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		init();
	}
}
