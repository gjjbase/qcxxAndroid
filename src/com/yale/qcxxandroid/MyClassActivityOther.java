package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class MyClassActivityOther extends BaseActivity {
	private ListView myclass_list;
	private Listadapter adapter;
	private TextView txt_back;
	private ImageView img_add;
	private ThreadUtil threadutil;
	private Bundle bundle;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myclassother);
		myclass_list = (ListView) findViewById(R.id.myclass_list);
		txt_back = (TextView) findViewById(R.id.txt_back);
		img_add = (ImageView) findViewById(R.id.img_add);
		sp = getSharedPreferences("qcxx", Context.MODE_PRIVATE);

		// editor.putString("number",uName);
		// editor.putString("userId", jo.get("userId").toString());
		// editor.putString("passWord", jo.get("passWord").toString());
		// editor.putString("xxNum",jo.get("xxNum").toString());
		// editor.putString("phoneNum",jo.get("phoneNum").toString());
		// editor.putString("nickName",jo.get("nickName").toString());
		// String[] schoolCity = sp.getString("schoolCity", "").split(",");//
		// ['3,
		// // 11']
		// // 专业名称
		// String[] school = sp.getString("school", "").split(",");// 学校
		// ['1, 9']
		// String[] collage = sp.getString("collage", "").split(",");//
		// String[] inSchool = sp.getString("inSchool", "").split(",");//
		// String[] prof = sp.getString("prof", "").split(",");//
		// String classes = sp.getString("classes", "");// 班级'5,13'
		// String cal = classes.substring(classes.indexOf("'") + 1,
		// classes.lastIndexOf("'"));
		// ['1, 9']
		init();
		img_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "增加", 3000).show();
			}
		});
		txt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void init() {
		threadutil = new ThreadUtil(mhandler);
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
			params.put("flag", 0);
			jsonArr = new JSONArray().put(params);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		threadutil.doStartWebServicerequestWebService(
				MyClassActivityOther.this, jsonArr.toString(), methodStr, true);
	}

	Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// [{"classesList":[{"cdId":"16","cdMc":"1班","cdMemo":"",
				// "cdParentId":"4","cdType":5,"deleteFlag":0,"orderList":1,
				// "prepare1":0,"prepare2":"","prepare3":""},{"cdId":"17",
				// "cdMc":"2班","cdMemo":"","cdParentId":"4","cdType":5,
				// "deleteFlag":0,"orderList":2,"prepare1":0,"prepare2":""
				// ,"prepare3":""}],"collageName":"计算机学院",
				// "inSchoolName":"2002级","profName":"计算机科学",
				// "schoolName":"华中科技大学"},{"classesList":
				// [{"cdId":"14","cdMc":"2班","cdMemo":"",
				// "cdParentId":"12","cdType":5,"deleteFlag"
				// :0,"orderList":2,"prepare1":0,"prepare2":"","prepare3":""},
				// {"cdId":"15","cdMc":"3班","cdMemo":"","cdParentId":"12",
				// "cdType":5,"deleteFlag":0,"orderList":3,"prepare1":0,
				// "prepare2":"","prepare3":""}],"collageName":
				// "软件学院","inSchoolName":
				// "2006级","profName":"软件工程","schoolName":"武汉大学"}]
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					JSONArray joa = new JSONArray(returnJson);
					adapter = new Listadapter(MyClassActivityOther.this, joa);
					myclass_list.setAdapter(adapter);
					adapter.notifyDataSetChanged();

				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "网络连接失败", 3000).show();
				break;
			}
		}
	};

	class Listadapter extends BaseAdapter {
		private Context context;
		private ViewHolder viewholder;
		private JSONArray jso;

		private Listadapter(Context context, JSONArray jso) {
			this.context = context;
			this.jso = jso;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jso.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.myclass_other, null);
			viewholder.item_name = (TextView) convertView
					.findViewById(R.id.item_name);
			viewholder.myclass_itemgrid = (GridView) convertView
					.findViewById(R.id.myclass_itemgrid);
			BaseAdapter base = new BaseAdapter() {

				public View getView(int arg0, View arg1, ViewGroup arg2) {
					// TODO Auto-generated method stub
					viewholder = new ViewHolder();
					arg1 = LayoutInflater.from(context).inflate(
							R.layout.myclassgrid_item, null);
					viewholder.class_num = (TextView) arg1
							.findViewById(R.id.class_num);
					viewholder.class_img = (ImageView) arg1
							.findViewById(R.id.class_img);
					try {
						viewholder.class_num.setText(jso
								.getJSONObject(position)
								.getJSONArray("classesList")
								.getJSONObject(arg0).getString("cdMc"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					viewholder.class_img.setImageResource(R.drawable.demo5);
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
						return jso.getJSONObject(position)
								.getJSONArray("classesList").length();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return 0;
					}
				}

			};
			viewholder.myclass_itemgrid.setAdapter(base);
			viewholder.myclass_itemgrid
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							bundle = new Bundle();
							try {
								bundle.putString(
										"data",
										jso.getJSONObject(position)
												.getJSONArray("classesList")
												.getJSONObject(arg2)
												.getString("cdMc"));//cdid 班级id/
								bundle.putString("cdid", jso.getJSONObject(position)
												.getJSONArray("classesList")
												.getJSONObject(arg2)
												.getString("cdId"));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							Intent intent = new Intent(context,
									MyClassActivityitem.class)
									.putExtras(bundle);
							startActivity(intent);
						}
					});
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}

		class ViewHolder {
			private TextView item_name, class_num;
			private ImageView class_img;
			private GridView myclass_itemgrid;
		}
	}
}
