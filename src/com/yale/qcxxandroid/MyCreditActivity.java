package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.BaseListView;

public class MyCreditActivity extends BaseActivity{
	private TextView back,myCredit;
	private SearchAdapter adapter;
	private List<JSONObject> jsonList;
	private BaseListView listView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_credit_activity);
		back = (TextView)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		myCredit = (TextView)findViewById(R.id.myCredit);
		myCredit.setText("50");
		listView = (BaseListView)findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});
		initData();
	}
	
	private void initData(){
		jsonList = new ArrayList<JSONObject>();
		try {
			JSONObject tmp1 = new JSONObject();
			tmp1.put("name", "王玮");
			tmp1.put("credit", "60");
			jsonList.add(tmp1);
			JSONObject tmp2 = new JSONObject();
			tmp2.put("name", "高江健");
			tmp2.put("credit", "80");
			jsonList.add(tmp2);
			JSONObject tmp3 = new JSONObject();
			tmp3.put("name", "陈路");
			tmp3.put("credit", "70");
			jsonList.add(tmp3);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		adapter = new SearchAdapter(MyCreditActivity.this, jsonList);
		listView.setAdapter(adapter);
	}
}
