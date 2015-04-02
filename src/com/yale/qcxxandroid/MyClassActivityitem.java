package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yale.qcxxandroid.base.AllListView;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("CutPasteId")
public class MyClassActivityitem extends BaseActivity {

	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private ThreadUtil thread;
	private AllListView list;
	private JSONArray jsoo;
	private BaseAdapter adapter;
	private TextView txt_back, txt_myclass;
	private List<String> mylist;
	private List<String> classlist;
	private String strclas;

	private void nexta() {
		mylist = new ArrayList<String>();
		for (int i = 0; i < list.getChildCount(); i++) {
			LinearLayout layou = (LinearLayout) list.getChildAt(i);
			CheckBox ck = (CheckBox) layou.findViewById(R.id.list_check);

			if (ck.isChecked() == true) {
				Log.i("++++++++++", String.valueOf(i));
				for (int j = 1; j < list.getChildCount(); j++) {
					LinearLayout layouer = (LinearLayout) list.getChildAt(j);
					CheckBox cker = (CheckBox) layouer
							.findViewById(R.id.list_check);
					cker.setChecked(false);
					LinearLayout layout = (LinearLayout) list.getChildAt(i);
					CheckBox ckd = (CheckBox) layout
							.findViewById(R.id.list_check);
					ckd.setChecked(true);
				}
				mylist.add(String.valueOf(i + 1));
			}
		}
		StringBuffer sb = new StringBuffer();
		for (String str : mylist) {
			sb.append(str + ",");
		}
		if (sb.toString().equals("")) {
			Toast.makeText(getApplicationContext(), "请选择班级", 3000).show();
			if (mylist.size() != 0) {
				Toast.makeText(getApplicationContext(), "只能选择一个班级", 3000)
						.show();
			}
		} else {
			bundle.putString("classlist", strclas);
			intent.setClass(MyClassActivityitem.this, MyClassActivityCont.class)
					.putExtras(bundle);
			startActivity(intent);
		}

	}

	@SuppressLint("HandlerLeak")
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
		thread.doStartWebServicerequestWebService(MyClassActivityitem.this,
				jsonArr.toString(), methodStr, true);
	}

	CompoundButton chebtn = null;
	@SuppressLint("HandlerLeak")
	Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				String returnJson = (String) msg.getData().getString(
						"returnJson");
				final List<String> mlist = new ArrayList<String>();
				classlist = new ArrayList<String>();
				try {
					jsoo = new JSONArray(returnJson);

					for (int i = 0; i < jsoo.length(); i++) {
						for (int j = 0; j < jsoo.getJSONObject(i)
								.getJSONArray("classesList").length(); j++) {
							mlist.add(jsoo.getJSONObject(i).getString(
									"schoolName")
									+ jsoo.getJSONObject(i).getString(
											"collageName")
									+ jsoo.getJSONObject(i).getString(
											"profName")
									+ jsoo.getJSONObject(i).getString(
											"collageName")
									+ jsoo.getJSONObject(i)
											.getJSONArray("classesList")
											.getJSONObject(j).getString("cdMc"));
							classlist.add(jsoo.getJSONObject(i)
									.getJSONArray("classesList")
									.getJSONObject(j).getString("cdId"));
						}
					}
					adapter = new BaseAdapter() {

						public View getView(final int arg0, View arg1,
								ViewGroup arg2) {
							// TODO Auto-generated method stub
							final ViewHolder viewholder = new ViewHolder();
							arg1 = LayoutInflater.from(getApplicationContext())
									.inflate(R.layout.member_listview, null);
							viewholder.list_txt = (TextView) arg1
									.findViewById(R.id.list_txt);
							viewholder.list_check = (CheckBox) arg1
									.findViewById(R.id.list_check);
							viewholder.lin = (LinearLayout) arg1
									.findViewById(R.id.lin);
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
											strclas = classlist.get(arg0);
											boolean flag = viewholder.list_check
													.isChecked();
											viewholder.list_check
													.setChecked(!flag);
										}
									});

							viewholder.list_txt.setText(mlist.get(arg0));
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
							return mlist.size();

						}

						class ViewHolder {
							TextView list_txt;
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

	public void img_add(View v) {
		mylist = new ArrayList<String>();
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
			Toast.makeText(getApplicationContext(), "请选择班级", 3000).show();
			if (mylist.size() != 0) {
				Toast.makeText(getApplicationContext(), "只能选择一个班级", 3000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "您当前选中了:" + sb.toString(),
					3000).show();
		}

	}

	public void img_add2(View v) {
		bundle.putInt("data", 1);
		nexta();
	}

	public void img_add3(View v) {
		bundle.putInt("data", 2);
		nexta();
	}

	public void img_add4(View v) {
		intent.setClass(MyClassActivityitem.this, MyClassActivity.class);
		startActivity(intent);
	}

	public void img_add5(View v) {
		bundle.putInt("data", 4);
		nexta();
	}

	public void addimger(View v) {
		intent.setClass(MyClassActivityitem.this, AddActivity.class);
		startActivity(intent);
	}

	public void addclass(View v) {
		intent.setClass(MyClassActivityitem.this, FabuActivity.class);
		startActivity(intent);
	}

	public void faxiuxiu(View v) {
		bundle.putInt("tager", 1);
		intent.setClass(MyClassActivityitem.this, ShowPubActivity.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	public void xieliuyan() {
		bundle.putInt("data", 4);
		nexta();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myclass_actiivtyitem);
		list = (AllListView) findViewById(R.id.list);
		txt_back = (TextView) findViewById(R.id.txt_back);
		txt_myclass = (TextView) findViewById(R.id.txt_myclass);
		switch (getIntent().getExtras().getInt("data")) {
		case 0:
			txt_back.setText("我的班级");
			txt_myclass.setText("我的班级");
			break;
		case 1:
			txt_back.setText("隔壁班级");
			txt_myclass.setText("隔壁班级");
			break;
		}
		txt_back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		// list.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		// CheckBox ck = (CheckBox) view.findViewById(R.id.list_check);
		// ck.setChecked(true);
		// editor.putString("classlist", classlist.get(position));
		// editor.commit();
		// }
		// });
		init();
	}

}