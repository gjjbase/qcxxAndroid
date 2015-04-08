package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

public class AddnewFrdACtivity extends BaseActivity {
	public ListView list;
	public List<Map<String, Object>> mylist = new ArrayList<Map<String, Object>>();
	public Intent intent = new Intent();
	public ThreadUtil thread;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewfrd);
		list = (ListView) findViewById(R.id.list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "手机通讯录");
		map.put("msg", "添加或邀请手机通讯录好友");
		map.put("img", R.drawable.img_near);
		mylist.add(map);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("name", "附近的同学");
		map1.put("msg", "添加附近的同学");
		map.put("img", R.drawable.img_zhitiao);
		mylist.add(map1);
		BaseAdapter base = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.newfditem, null);
				TextView txt = (TextView) convertView.findViewById(R.id.txt);
				TextView name = (TextView) convertView.findViewById(R.id.name);
				TextView msg = (TextView) convertView.findViewById(R.id.msg);
				ImageView img = (ImageView) convertView.findViewById(R.id.img);
				switch (position) {
				case 0:
					img.setImageResource(R.drawable.img_near);
					break;
				case 1:
					img.setImageResource(R.drawable.img_zhitiao);
					break;

				}
				name.setText((CharSequence) mylist.get(position).get("name"));
				msg.setText((CharSequence) mylist.get(position).get("msg"));
				txt.setVisibility(View.GONE);
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
				return mylist.size();
			}
		};
		list.setAdapter(base);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					intent.setClass(getApplicationContext(),
							FrdMesgActivity.class);
					startActivity(intent);
					break;
				case 1:

					break;

				}
			}
		});
	}

	public void back(View v) {
		finish();
	}
}
