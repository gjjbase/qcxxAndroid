package com.yale.qcxxandroid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.BaseListView;
import com.yale.qcxxandroid.bean.ZhiTiao;
public class MyZhiTiaoActivity extends BaseActivity {
    private BaseListView listView;  
    private MyZhiTiaoAdapter adapter;  
    private List<ZhiTiao> zts = new ArrayList<ZhiTiao>();  
    private Intent intent = new Intent();
    MyHandler myHandler;
    int tabHostValue = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.my_zhitiao_activity);  
        tabHostValue = getIntent().getExtras().getInt("tabHostValue");
        listView = (BaseListView) findViewById(R.id.listView); 
        listView.setDeleteListioner(this);
        listView.setSingleTapUpListenner(this);
        listView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,  
            	     int position, long id) {
            }
        });
        myHandler = new MyHandler(this);
        // 起一个线程初始数据
        MyThread m = new MyThread();
        new Thread(m).start();
    }  
  
	class MyThread implements Runnable {
		public void run() {
	        ZhiTiao p1 = new ZhiTiao();  
	        p1.setName("鸡棚");
	        p1.setContent("屌丝是一场注定孤独的旅行");
	        p1.setPicHead("");
	        p1.setSchool("武汉大学");
	        p1.setTime("15/12/11 04:00");
	        zts.add(p1); 
	        ZhiTiao p2 = new ZhiTiao();  
	        p2.setName("鸡棚");
	        p2.setContent("屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行屌丝是一场注定孤独的旅行");
	        p2.setPicHead("");
	        p2.setSchool("武汉大学");
	        p2.setTime("15/12/11 04:00");
	        zts.add(p2); 	        
			Message msg = new Message();
			Bundle b = new Bundle();// 存放数据
			b.putSerializable("zts", (Serializable) zts);
			msg.setData(b);
			MyZhiTiaoActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息,更新UI
		}
	}
	class MyHandler extends Handler {
		private MyZhiTiaoActivity mContext;

		public MyHandler(Context conn) {
			mContext = (MyZhiTiaoActivity) conn;
		}
		public MyHandler(Looper L) {
			super(L);
		}
		// 子类必须重写此方法,接受数据
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			List<ZhiTiao> zts = ((ArrayList<ZhiTiao>) b
					.getSerializable("zts"));
	        adapter = new MyZhiTiaoAdapter(mContext, zts,tabHostValue);  
	        adapter.setOnDeleteListioner(mContext);
	        listView.setAdapter(adapter);  
		}
	}
	
	@Override
	public void onClick(int whichButton) {
		switch (whichButton) {
		case 0:
			zts.remove(delID);
			listView.deleteItem();
			adapter.notifyDataSetChanged();
			break;
		case 1:
			break;
		default:
			break;
		}
	}
	
	public void userDetail(View view){
		intent = new Intent();
    	intent.setClass(MyZhiTiaoActivity.this,MyDetailActivity.class);
    	startActivity(intent);
	}
	public void replyClick(View view){
	}	
	public void dztClick(View view){
	}		
}
