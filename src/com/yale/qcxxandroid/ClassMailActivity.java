package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.ZhiTiaoTabActivity;
import com.yale.qcxxandroid.bean.ZhiTiao;
import com.yale.qcxxandroid.util.StringHelper;
public class ClassMailActivity extends BaseActivity {
    private ListView listView;  
//    private MsgAdapter adapter;  
    private List<ZhiTiao> friends = new ArrayList<ZhiTiao>();  
    private Intent intent = new Intent();
    private Bundle bundle = new Bundle();
    private EditText search;
    Button ignore;
    @Override
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.class_mail_activity);  
        listView = (ListView) findViewById(R.id.listView);  
        init("yaleviewmicro");
        //adapter = new MsgAdapter(this, friends);
//        listView.setAdapter(adapter);  
        listView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  
            	     int position, long id) {
            	switch (position) {
				case 0: 
					intent.setClass(ClassMailActivity.this, ZhiTiaoTabActivity.class);
					startActivity(intent);
					break;
				case 1:  
					intent.setClass(ClassMailActivity.this, null);
					startActivity(intent);
					break;
				case 2:  
					intent.setClass(ClassMailActivity.this, null);
					startActivity(intent);
					break;
				default:
					break;
				}
            }
        });
        ignore = (Button) findViewById(R.id.ignore); 
        ignore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
        search = (EditText) findViewById(R.id.search);  
        search.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                       int arg3) {
        }
        @Override
        public void afterTextChanged(Editable s) {
    		String searchStr = search.getText().toString();
    		init(searchStr);
            // 起一个线程更新数据
//        	adapter.notifyDataSetChanged();
        }});
    }  
    public void init(String searchStr){
    	friends.clear();
		List<ZhiTiao> tempList = new ArrayList<ZhiTiao>();
		for (ZhiTiao friend : tempList) {
			if(StringHelper.getPingYin(friend.getName()).contains(searchStr)||friend.getName().contains(searchStr)||searchStr.contains("yaleviewmicro")){
				friends.add(friend);
			}
		}
	}
}
