package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

public class MyClassActionActivity extends BaseActivity {
	private ListView myclass_list;
	private Actionadapter adapter;
	private TextView txt_back;
	private ImageView img_add;
	private Bundle bunlde;
	private ThreadUtil thread;

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					JSONArray jsoo = new JSONArray(returnJson);
					adapter = new Actionadapter(MyClassActionActivity.this,
							jsoo);
					myclass_list.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case 2:
				Toast.makeText(getApplicationContext(), "网络连接失败", 3000).show();
				break;

			}
		}
	};

	public void init() {
		thread = new ThreadUtil(handler);
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
			params.put("flag", 1);
			jsonArr = new JSONArray().put(params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		thread.doStartWebServicerequestWebService(MyClassActionActivity.this,
				jsonArr.toString(), methodStr, true);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myclassact);
		myclass_list = (ListView) findViewById(R.id.myclass_list);
		txt_back = (TextView) findViewById(R.id.txt_back);
		img_add = (ImageView) findViewById(R.id.img_add);
		init();
		img_add.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "点击", 3000).show();
			}
		});
		txt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	class Actionadapter extends BaseAdapter {
		private Context context;
		private ViewHolder viewholder;
		private JSONArray jso;

		private Actionadapter(Context context, JSONArray jso) {
			this.context = context;
			this.jso = jso;
		}

		public int getCount() {
			return jso.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.myclass_other, null);
			viewholder.item_name = (TextView) convertView
					.findViewById(R.id.item_name);
			viewholder.myclass_itemgrid = (GridView) convertView
					.findViewById(R.id.myclass_itemgrid);
			BaseAdapter adapter = new BaseAdapter() {

				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {
					viewholder = new ViewHolder();
					arg1 = LayoutInflater.from(context).inflate(
							R.layout.myclassgrid_item, null);
					viewholder.class_num = (TextView) arg1
							.findViewById(R.id.class_num);
					viewholder.class_img = (ImageView) arg1
							.findViewById(R.id.class_img);
					// viewholder.class_img.setImageResource();
					try {
						viewholder.class_num.setText(jso
								.getJSONObject(position)
								.getJSONArray("classesList")
								.getJSONObject(arg0).getString("cdMc"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					viewholder.class_img.setImageResource(R.drawable.demo5);
					return arg1;
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
					try {
						return jso.getJSONObject(position)
								.getJSONArray("classesList").length();
					} catch (JSONException e) {
						e.printStackTrace();
						return 0;
					}
				}
			};
			viewholder.myclass_itemgrid.setAdapter(adapter);
			try {
				viewholder.item_name
						.setText(jso.getJSONObject(position).getString(
								"schoolName")
								+ jso.getJSONObject(position).getString(
										"collageName")
								+ jso.getJSONObject(position).getString(
										"profName")
								+ jso.getJSONObject(position).getString(
										"inSchoolName"));
				for (int i = 0; i < jso.length(); i++) {
					for (int j = 0; j < jso.getJSONObject(i)
							.getJSONArray("classesList").length(); j++) {
						System.out.println(jso.getJSONObject(i)
								.getJSONArray("classesList").getJSONObject(j)
								.getJSONObject("cdMc"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			viewholder.myclass_itemgrid
					.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
								View view, int arg0, long id) {
							bunlde = new Bundle();
							try {
								bunlde.putString(
										"data",
										jso.getJSONObject(position)
												.getJSONArray("classesList")
												.getJSONObject(arg0)
												.getString("cdMc"));
								bunlde.putString(
										"cdId",
										jso.getJSONObject(position)
												.getJSONArray("classesList")
												.getJSONObject(arg0)
												.getString("cdId"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							Intent intent = new Intent(context,
									MyClassActivityitem.class)
									.putExtras(bunlde);
							startActivity(intent);
						}
					});
			return convertView;
		}

		class ViewHolder {
			private TextView item_name, class_num;
			private ImageView class_img;
			private GridView myclass_itemgrid;
		}
	}
}
