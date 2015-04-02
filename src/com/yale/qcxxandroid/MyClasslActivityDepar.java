package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.AllListView;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

@SuppressWarnings("unused")
public class MyClasslActivityDepar extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private ThreadUtil thread;
	private JSONArray jsoo;
	private AllListView mylist, list;
	private List<String> str;
	private Adapter adpter;

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void backs(View v) {
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myclassdepar);
		list = (AllListView) findViewById(R.id.list);
		mylist = (AllListView) findViewById(R.id.mylist);
		str = new ArrayList<String>();
		str.add("Z1201");
		str.add("Z1202");
		str.add("Z1203");
		adpter = new Adapter(getApplicationContext(), str);
		list.setAdapter(adpter);
		str = new ArrayList<String>();
		str.add("Z1208");
		str.add("Z1289");
		adpter = new Adapter(getApplicationContext(), str);
		mylist.setAdapter(adpter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "跳到班级", 3000).show();
			}
		});
		mylist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "跳到班级", 3000).show();
			}
		});
	}

	class Adapter extends BaseAdapter {
		Context context;
		List<String> list;

		public Adapter(Context context, List<String> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(context).inflate(
					R.layout.deparitemactivity, null);
			TextView txt = (TextView) convertView.findViewById(R.id.txt);
			txt.setText(list.get(position));
			return convertView;
		}
	}
}
