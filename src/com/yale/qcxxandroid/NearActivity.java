package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.yale.qcxxandroid.base.BaseActivity;

public class NearActivity extends BaseActivity {
	private ListView list;
	private List<String> datalist;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activtiy_near);
		list = (ListView) findViewById(R.id.list);
		datalist = new ArrayList<String>();
		BaseAdapter base = new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.itemnearactivity, null);
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
				return datalist.size();
			}
		};
		list.setAdapter(base);
	}

	public void back(View v) {
		finish();
	}
}
