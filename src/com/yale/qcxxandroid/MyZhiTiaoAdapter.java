package com.yale.qcxxandroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.yale.qcxxandroid.base.OnDeleteListioner;
import com.yale.qcxxandroid.bean.ZhiTiao;

public class MyZhiTiaoAdapter extends BaseAdapter {
	private Context context;
	private List<ZhiTiao> list;
	private ViewHolder viewHolder;
	private OnDeleteListioner mOnDeleteListioner;
	private boolean delete = false;
	private int tabHostValue;
	private int[] init = { R.drawable.zhitian2, R.drawable.zhitian1 };

	public MyZhiTiaoAdapter(Context context, List<ZhiTiao> list,
			int tabHostValue) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		viewHolder = new ViewHolder();
		convertView = LayoutInflater.from(context).inflate(
				R.layout.my_zhi_tiao_adapter, null);
		viewHolder.headImg = (ImageView) convertView.findViewById(R.id.headimg);
		viewHolder.name = (TextView) convertView.findViewById(R.id.name);
		viewHolder.time = (TextView) convertView.findViewById(R.id.time);
		viewHolder.content = (TextView) convertView.findViewById(R.id.content);
		viewHolder.school = (TextView) convertView.findViewById(R.id.school);
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
		viewHolder.headImg.setImageResource(init[position]);
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