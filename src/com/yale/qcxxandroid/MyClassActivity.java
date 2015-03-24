package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MyClassotherGridview;
import com.yale.qcxxandroid.util.ThreadUtil;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MyClassActivity extends BaseActivity {
	private TextView back;
	private TextView img_add;
	private Bundle bundle;
	private ArrayList<HashMap<String, Object>> lister;
	@SuppressWarnings("unused")
	private SimpleAdapter adaptera;
	private ThreadUtil thread;
	private com.yale.qcxxandroid.base.MyClassotherGridview depear_grid;

	private int falg = 0;
	private TextView txt_enter, txt_back;
	private JSONArray jsoo;
	private int[] img = { R.drawable.xiuxiu6, R.drawable.xiuxiu7 };

	private void init() {

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:

					String returnJson = (String) msg.getData().getString(
							"returnJson");
					try {
						jsoo = new JSONArray(returnJson);
						BaseAdapter base = new BaseAdapter() {

							@Override
							public View getView(final int arg0, View arg1,
									ViewGroup arg2) {
								// TODO Auto-generated method stub
								arg1 = LayoutInflater.from(
										getApplicationContext()).inflate(
										R.layout.myclassgrid_item, null);
								CheckBox ck_item = (CheckBox) arg1
										.findViewById(R.id.ck_item);
								ImageView class_img = (ImageView) arg1
										.findViewById(R.id.class_img);
								TextView class_num = (TextView) arg1
										.findViewById(R.id.class_num);
								ck_item.setVisibility(View.VISIBLE);
								class_img.setImageResource(img[arg0]);
								try {
									class_num.setText(jsoo.getJSONObject(0)
											.getJSONArray("userClassList")
											.getJSONObject(arg0)
											.getJSONObject("userInfo")
											.getString("verityName"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// class_img
								// .setOnClickListener(new OnClickListener() {
								//
								// @Override
								// public void onClick(View v) {
								// // TODO Auto-generated method
								// // stub
								// bundle = new Bundle();
								// bundle.putString("userId", "");
								// bundle.putString(
								// "name",
								// (String) lister.get(
								// arg0).get(
								// "class_num"));
								// bundle.putString("sign", "");
								// Intent intent = new Intent(
								// getApplicationContext(),
								// MyDetailActivity.class)
								// .putExtras(bundle);
								// startActivity(intent);
								// }
								// });
								return arg1;
							}

							@Override
							public long getItemId(int arg0) {
								// TODO Auto-generated method stub
								return arg0;
							}

							@Override
							public Object getItem(int arg0) {
								// TODO Auto-generated method stub
								return arg0;
							}

							@Override
							public int getCount() {
								// TODO Auto-generated method stub
								try {
									return jsoo.getJSONObject(0)
											.getJSONArray("userClassList")
											.length();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									return 0;
								}

							}
						};
						depear_grid.setAdapter(base);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 2:

					break;

				}
			}
		};
		thread = new ThreadUtil(handler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'myClassDetail'}]";

		String jsonParamStr = "[{'signType':" + 1 + ",'commentType':" + 1
				+ ",'primaryId':" + 5 + ",'pubType':" + 0 + ",'orderBy':" + 4
				+ "}]";// primaryid 班级ID
		// String jsonParamStr = "[{'signType':" + 1 + ",'commentType':" + 1
		// + ",'primaryId':" + bundle.getString("cdId") + ",'pubType':" + 0 +
		// ",'orderBy':" + 4
		// + "}]";// primaryid 班级ID
		thread.doStartWebServicerequestWebService(MyClassActivity.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressLint("CutPasteId")
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myclass_activity);
		back = (TextView) findViewById(R.id.txt_back);
		txt_enter = (TextView) findViewById(R.id.txt_enter);
		depear_grid = (MyClassotherGridview) findViewById(R.id.depear_grid);

		txt_back = (TextView) findViewById(R.id.txt_back);
		txt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// hin_lin.setVisibility(View.VISIBLE);
		depear_grid.setVisibility(View.VISIBLE);
		init();
		txt_enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < depear_grid.getChildCount(); i++) {
					LinearLayout layou = (LinearLayout) depear_grid
							.getChildAt(i);
					CheckBox ck = (CheckBox) layou.findViewById(R.id.ck_item);
					if (ck.isChecked() == true) {
						Log.i("++++++++++", String.valueOf(i));

						list.add(String.valueOf(i + 1));
					}
				}
				StringBuffer sb = new StringBuffer();
				for (String str : list) {
					sb.append(str + ",");
				}
				Toast.makeText(getApplicationContext(),
						"您当前选中了:" + sb.toString(), 3000).show();
			}
		});
		img_add = (TextView) findViewById(R.id.img_add);
		img_add.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				falg++;
				if (falg % 2 == 0) {
					img_add.setText("全选");
					check(false);
					Toast.makeText(getApplicationContext(), "全不选", 3000).show();
				} else {
					img_add.setText("全不选");
					check(true);
					Toast.makeText(getApplicationContext(), "全选", 3000).show();
				}

			}
		});

		back.setText("同学");
	}

	public void check(boolean ischeck) {
		for (int i = 0; i < depear_grid.getChildCount(); i++) {
			LinearLayout layou = (LinearLayout) depear_grid.getChildAt(i);
			CheckBox ck = (CheckBox) layou.findViewById(R.id.ck_item);
			ck.setChecked(ischeck);
		}
		;
	}
}
