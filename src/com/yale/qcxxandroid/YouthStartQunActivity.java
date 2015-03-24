package com.yale.qcxxandroid;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
public class YouthStartQunActivity extends BaseActivity {
	private ListView searchList;
	private SearchAdapter searchAdapter;
	private List<JSONObject> searchListObj = new ArrayList<JSONObject>();;
	private EditText search;
	private TextView cancel,ok;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youth_start_qun_activity);
		cancel = (TextView)findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ok = (TextView)findViewById(R.id.ok);
		search = (EditText)findViewById(R.id.search);
		searchList = (ListView)findViewById(R.id.searchList);
		search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		search.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event)  {                          
				if (actionId==EditorInfo.IME_ACTION_SEARCH) 
				{   
					if (StringUtils.isNotEmpty(search.getText().toString())) {
						JSONObject tmp = new JSONObject();
						try {
							tmp.put("name", "王玮");
							tmp.put("checkbox", "true");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						searchListObj.clear();
						searchListObj.add(tmp);
						searchAdapter.notifyDataSetChanged();
					}
					return true;             
				}               
				return false;           
			}       
		});
		initData();
	}

	private void initData(){
		try {
			JSONObject tmp = new JSONObject();
			tmp.put("name", "王玮");
			tmp.put("checkbox", "true");
			searchListObj.add(tmp);
			JSONObject tmp1 = new JSONObject();
			tmp1.put("name", "高江健");
			tmp1.put("checkbox", "true");
			searchListObj.add(tmp1);
			JSONObject tmp2 = new JSONObject();
			tmp2.put("name", "吴文戈 ");
			tmp2.put("checkbox", "true");
			searchListObj.add(tmp2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		searchAdapter = new SearchAdapter(YouthStartQunActivity.this,searchListObj,ok);
		searchList.setAdapter(searchAdapter);
	}
}
