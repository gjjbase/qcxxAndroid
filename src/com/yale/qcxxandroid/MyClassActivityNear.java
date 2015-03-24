package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.AlertDialoger;
import com.yale.qcxxandroid.base.BaseLocationActivity;
import com.yale.qcxxandroid.base.MyBaseListView;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
import com.yale.qcxxandroid.util.RedisThreadUtil;

@SuppressLint("HandlerLeak")
public class MyClassActivityNear extends BaseLocationActivity {
	private MyBaseListView near_list;
	private Adapter adpter;
	private List<JSONObject> jsonPersons = new ArrayList<JSONObject>();
	private TextView back;
	private RedisThreadUtil redisThreadUtil;
	private Intent intent;
	private Bundle bundle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mynear);
		near_list = (MyBaseListView) findViewById(R.id.near_list);
		back = (TextView) findViewById(R.id.txt_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		redisThreadUtil = new RedisThreadUtil(handler);
		redisThreadUtil.redisRequestService(0.0, 0.0, sp, 1,
				MyClassActivityNear.this);
		near_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				Toast.makeText(MyClassActivityNear.this, "刷新",
						Toast.LENGTH_SHORT).show();
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						near_list.onRefreshComplete();
					}
				}.execute();
			}

		});
	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				jsonPersons = (List<JSONObject>) msg.getData().getSerializable(
						"returnJsonPersons");
				adpter = new Adapter(MyClassActivityNear.this);
				near_list.setAdapter(adpter);
				break;
			case 2:
				break;
			}
		}
	};

	public void commDialog() {
		intent = new Intent();
		bundle = new Bundle();
		bundle.putString("message", "数据异常");
		bundle.putString("falg", "1");
		intent.putExtras(bundle);
		intent.setClass(MyClassActivityNear.this, AlertDialoger.class);
		startActivity(intent);
	}

	class Adapter extends BaseAdapter {
		private Context context;
		private ViewHolder viewholder;

		private Adapter(Context context) {
			this.context = context;
		}

		public int getCount() {
			try {
				return jsonPersons.size();
			} catch (Exception e) {
				return 0;
			}
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
		public View getView(int position, View convertView, ViewGroup parent) {
			viewholder = new ViewHolder();
			JSONObject jsonPerson = jsonPersons.get(position);
			if (convertView == null) {

				convertView = LayoutInflater.from(context).inflate(
						R.layout.myclassnear_item, null);
			}
			viewholder.near_id = (TextView) convertView
					.findViewById(R.id.near_id);
			viewholder.txt_dis = (TextView) convertView
					.findViewById(R.id.txt_dis);
			try {
				viewholder.txt_dis.setText(jsonPerson.getString("distance"));
				viewholder.near_id.setText(jsonPerson.getString("name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}

		class ViewHolder {
			private TextView near_id, txt_dis;
		}
	}
}
