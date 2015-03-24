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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.BaseListView;
import com.yale.qcxxandroid.base.CircularImage;
import com.yale.qcxxandroid.base.OnDeleteListioner;
import com.yale.qcxxandroid.bean.ZhiTiao;

public class MyCollectionActivity extends BaseActivity {
	private BaseListView listView;
	private Adapter adapter;
	private List<ZhiTiao> zts = new ArrayList<ZhiTiao>();
	private Intent intent = new Intent();
	MyHandler myHandler;
	int tabHostValue = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycollect);
		tabHostValue = getIntent().getExtras().getInt("tabHostValue");
		listView = (BaseListView) findViewById(R.id.listView);
		listView.setDeleteListioner(this);
		listView.setSingleTapUpListenner(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
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
			MyCollectionActivity.this.myHandler.sendMessage(msg); // 向Handler发送消息,更新UI
		}
	}

	class MyHandler extends Handler {
		private MyCollectionActivity mContext;

		public MyHandler(Context conn) {
			mContext = (MyCollectionActivity) conn;
		}

		public MyHandler(Looper L) {
			super(L);
		}

		// 子类必须重写此方法,接受数据
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			List<ZhiTiao> zts = ((ArrayList<ZhiTiao>) b.getSerializable("zts"));
			adapter = new Adapter(mContext, zts, tabHostValue);
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

	public void userDetail(View view) {

	}

	public void replyClick(View view) {
	}

	public void dztClick(View view) {
	}

	public class Adapter extends BaseAdapter {
		private Context context;
		private List<ZhiTiao> list;
		private ViewHolder viewHolder;
		private OnDeleteListioner mOnDeleteListioner;
		private boolean delete = false;
		private int tabHostValue;
		private int[] init = { R.drawable.zhitian1, R.drawable.zhitian2 };

		public Adapter(Context context, List<ZhiTiao> list, int tabHostValue) {
			this.context = context;
			this.list = list;
			this.tabHostValue = tabHostValue;
		}

		public void setDelete(boolean delete) {
			this.delete = delete;
		}

		public boolean isDelete() {
			return delete;
		}

		public void setOnDeleteListioner(OnDeleteListioner mOnDeleteListioner) {
			this.mOnDeleteListioner = mOnDeleteListioner;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.my_zhi_tiao_adapter, null);
			viewHolder.headImg = (ImageView) convertView
					.findViewById(R.id.headimg);
			viewHolder.headImg.setImageResource(init[position]);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.content);
			viewHolder.school = (TextView) convertView
					.findViewById(R.id.school);
			viewHolder.deleteAction = (TextView) convertView
					.findViewById(R.id.delete_action);
			viewHolder.reply = (TextView) convertView.findViewById(R.id.reply);
			viewHolder.dzt = (TextView) convertView.findViewById(R.id.dzt);
			if (tabHostValue == 1) {
				viewHolder.reply.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.dzt.setVisibility(View.INVISIBLE);
			}
			viewHolder.name.setText(list.get(position).getName());
			viewHolder.time.setText(list.get(position).getTime());
			viewHolder.content.setText(list.get(position).getContent());
			viewHolder.school.setText(list.get(position).getSchool());
			OnClickListener mOnClickListener = new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (mOnDeleteListioner != null)
						mOnDeleteListioner.onDelete(position);
				}
			};
			viewHolder.deleteAction.setOnClickListener(mOnClickListener);
			return convertView;
		}

		private class ViewHolder {
			private ImageView headImg;
			private TextView name;
			private TextView time;
			private TextView content;
			private TextView school;
			private TextView deleteAction;
			private TextView reply;
			private TextView dzt;
		}
	}
}
