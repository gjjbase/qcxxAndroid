package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YouthXmAdapter extends BaseAdapter{

	private JSONArray giftArr;
	private Context context;
	
	public YouthXmAdapter(Context context,JSONArray giftArr){
		this.giftArr = giftArr;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return giftArr.length();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		try {
			return giftArr.getJSONObject(arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ThisItem thisItem = new ThisItem();
		if (convertView == null) {
			View v = LayoutInflater.from(context).inflate(R.layout.youth_xxhd_xm_adapter, null);
			
			thisItem.image = (ImageView)v.findViewById(R.id.image);
			thisItem.name = (TextView)v.findViewById(R.id.name);
			thisItem.order = (TextView)v.findViewById(R.id.order);
			thisItem.content = (TextView)v.findViewById(R.id.content);	
			thisItem.zan = (TextView)v.findViewById(R.id.zan);
			thisItem.vote = (TextView)v.findViewById(R.id.vote);
			thisItem.zhiTiao = (TextView)v.findViewById(R.id.zhiTiao);
			
			thisItem.currentItem = position;
			thisItem.image.setImageResource(R.drawable.welcome5);
			try {
				thisItem.name.setText(giftArr.getJSONObject(position).getString("name"));
				thisItem.order.setText(giftArr.getJSONObject(position).getString("order"));
				thisItem.content.setText(giftArr.getJSONObject(position).getString("content"));
				thisItem.zan.setText(giftArr.getJSONObject(position).getString("zan"));
				thisItem.vote.setText(giftArr.getJSONObject(position).getString("vote"));
				thisItem.zhiTiao.setText(giftArr.getJSONObject(position).getString("zhiTiao"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			v.setTag(thisItem);
			convertView = v;
		} else {
			thisItem = (ThisItem) convertView.getTag();
		}
		return convertView;
	}
	
	class ThisItem {
		int currentItem = 0; // 当前view的索引号
		ImageView image;
		TextView name;
		TextView order;
		TextView content;
		TextView zan;
		TextView vote;
		TextView zhiTiao;
	}

}
