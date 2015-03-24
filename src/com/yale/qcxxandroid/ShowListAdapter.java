package com.yale.qcxxandroid;

import java.util.List;

import com.yale.qcxxandroid.base.ViewPagerAdapter;
import com.yale.qcxxandroid.bean.Shows;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowListAdapter extends BaseAdapter {
	private List<Shows> mList;
	private Context mContext;
	Intent intent = new Intent();

	public ShowListAdapter(Context context, List<Shows> mList) {
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
					R.layout.show_list_adapter, null);
			thisItem.content = (TextView) v.findViewById(R.id.content);
			thisItem.cmtCont = (TextView) v.findViewById(R.id.cmtCont);
			thisItem.viewPager = (ViewPager) v.findViewById(R.id.vp);
			final LinearLayout click_lin = (LinearLayout) v
					.findViewById(R.id.click_lin);
			final LinearLayout lin_content = (LinearLayout) v
					.findViewById(R.id.lin_content);
			final EditText edt = (EditText) v.findViewById(R.id.edt);
			TextView txt = (TextView) v.findViewById(R.id.txt);
			txt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(mContext, edt.getText().toString().trim(),
							3000).show();
					lin_content.setVisibility(View.GONE);
					click_lin.setVisibility(View.VISIBLE);
				}
			});
			click_lin.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					lin_content.setVisibility(View.VISIBLE);
					click_lin.setVisibility(View.GONE);
					edt.setFocusable(true);

					edt.setFocusableInTouchMode(true);

					edt.requestFocus();

					InputMethodManager inputManager =

					(InputMethodManager) edt.getContext().getSystemService(
							Context.INPUT_METHOD_SERVICE);

					inputManager.showSoftInput(edt, 0);
				}
			});
			thisItem.viewPager
					.setOnPageChangeListener(new MyPageChangeListener());
			v.setTag(thisItem);
			convertView = v;
		} else {
			thisItem = (ThisItem) convertView.getTag();
		}
		thisItem.viewPager.setAdapter(new ViewPagerAdapter(show.getViewResId(),
				show.getViews()));
		 
		return convertView;
	}

	class ThisItem {
		int currentItem = 0; // 当前view的索引号
		ViewPager viewPager;
		TextView content,cmtCont;
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int flag = 0;
		private int currPosition = 0;

		public MyPageChangeListener() {
		}

		public void onPageSelected(int position) {
			currPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {
			if (currPosition == 0 && arg0 == 0 && flag > 3) {
				Intent intent = new Intent(mContext, MyDetailActivity.class);
				mContext.startActivity(intent);
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (arg0 == 0 && arg1 == 0 && arg2 == 0) {
				flag++;
			} else {
				flag = 0;
			}
		}
	}
}
