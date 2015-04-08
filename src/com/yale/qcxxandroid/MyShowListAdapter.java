package com.yale.qcxxandroid;

import java.util.List;
import com.yale.qcxxandroid.base.ViewPagerAdapter;
import com.yale.qcxxandroid.bean.Shows;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyShowListAdapter extends BaseAdapter {
	private List<Shows> mList;
	private Context mContext;
	Intent intent = new Intent();

	public MyShowListAdapter(Context context, List<Shows> mList) {
		mContext = context;
		this.mList = mList;
	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Shows show = mList.get(position);
		ThisItem thisItem = new ThisItem();
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(
					R.layout.my_show_list_adapter, null);
			thisItem.content = (TextView) v.findViewById(R.id.content);
			thisItem.viewPager = (ViewPager) v.findViewById(R.id.vp);
			v.setTag(thisItem);
			convertView = v;
		} else {
			thisItem = (ThisItem) convertView.getTag();
		}
		thisItem.viewPager.setAdapter(new ViewPagerAdapter(show.getViewResId(),
				show.getViews()));
		thisItem.content.setText(show.getContent());
		return convertView;
	}

	class ThisItem {
		int currentItem = 0; // 当前view的索引号
		ViewPager viewPager;
		TextView content;
	}
}
