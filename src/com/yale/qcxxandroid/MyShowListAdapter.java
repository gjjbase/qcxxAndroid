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
		this.mList  = mList;
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
			thisItem.content = (TextView)v.findViewById(R.id.content);
			thisItem.viewPager = (ViewPager) v.findViewById(R.id.vp);
//			thisItem.viewPager.setOnPageChangeListener(new MyPageChangeListener());
			v.setTag(thisItem);
			convertView = v;
		} else {
			thisItem = (ThisItem) convertView.getTag();
		}
		thisItem.viewPager.setAdapter(new ViewPagerAdapter(show.getViewResId(), show.getViews()));
		thisItem.content.setText(show.getContent());
		return convertView;
	}
	// 切换当前显示的图片
//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			thisItem.viewPager.setCurrentItem(currentItem);// 切换当前显示
//		};
//	};
	class ThisItem {
		int currentItem = 0; // 当前view的索引号
		ViewPager viewPager;
		TextView content;
	}


	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 */
//	private class MyPageChangeListener implements OnPageChangeListener {
//		public void onPageSelected(int position) {
//			currentItem = position;
////			memo.setText((currentItem + 1) + "/" + viewResId.length);
//		}
//		public void onPageScrollStateChanged(int arg0) {
//		}
//
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//		}
//	}  	
}
