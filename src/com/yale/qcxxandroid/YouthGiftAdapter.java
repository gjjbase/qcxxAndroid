package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONException;

import com.yale.qcxxandroid.MyShowListAdapter.ThisItem;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class YouthGiftAdapter extends BaseAdapter{

	private JSONArray giftArr;
	private Context context;
	
	public YouthGiftAdapter(Context context,JSONArray giftArr){
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ThisItem thisItem = new ThisItem();
		if (convertView == null) {
			View v = LayoutInflater.from(context).inflate(R.layout.youth_xxhd_gift_adapter, null);
			
			thisItem.image = (ImageView)v.findViewById(R.id.image);
			thisItem.name = (TextView)v.findViewById(R.id.name);
			thisItem.count = (TextView)v.findViewById(R.id.count);
			thisItem.content = (TextView)v.findViewById(R.id.content);	
			thisItem.price = (TextView)v.findViewById(R.id.price);
			
			thisItem.currentItem = position;
			thisItem.image.setImageResource(R.drawable.note);
			try {
				thisItem.name.setText(giftArr.getJSONObject(position).getString("name"));
				thisItem.count.setText(giftArr.getJSONObject(position).getString("count"));
				thisItem.content.setText(giftArr.getJSONObject(position).getString("content"));
				thisItem.price.setText(giftArr.getJSONObject(position).getString("price"));
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
		TextView count;
		TextView content;
		TextView price;
	}

}
