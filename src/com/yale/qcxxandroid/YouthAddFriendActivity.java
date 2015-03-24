package com.yale.qcxxandroid;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
public class YouthAddFriendActivity extends BaseActivity {
	private ListView searchList;
	private MsgAdapter searchAdapter;
	private List<JSONObject> searchListObj = new ArrayList<JSONObject>();;
	private EditText search;
	private TextView cancel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youth_addfriend_activity);
		cancel = (TextView)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		search = (EditText)findViewById(R.id.search);
		searchList = (ListView)findViewById(R.id.searchList);
		searchList.setOnItemClickListener(new ListView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				Intent intent = new Intent(YouthAddFriendActivity.this, MyDetailActivity.class);
				startActivity(intent);
			}
		});
		search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		search.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event)  {                          
				if (actionId==EditorInfo.IME_ACTION_SEARCH) 
				{   
					if (StringUtils.isNotEmpty(search.getText().toString())) {
						initData();
						searchList.setAdapter(searchAdapter);
					}
					return true;             
				}               
				return false;           
			}       
		});
		initData();
	}

	private void initData(){
		searchListObj.clear();
		try {
			JSONObject tmp = new JSONObject();
			tmp.put("nc", "王玮");
			tmp.put("pubtime", "");
			tmp.put("content", "头上有犄角，身后有尾巴");
			searchListObj.add(tmp);
			JSONObject tmp1 = new JSONObject();
			tmp1.put("nc", "陈路");
			tmp1.put("pubtime", "");
			tmp1.put("content", "我有许多的秘密");
			searchListObj.add(tmp1);
			JSONObject tmp2 = new JSONObject();
			tmp2.put("nc", "邱老师");
			tmp2.put("pubtime", "");
			tmp2.put("content", "就不告诉你，就不告诉你!");
			searchListObj.add(tmp2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		searchAdapter = new MsgAdapter(YouthAddFriendActivity.this,searchListObj);
	}
}
