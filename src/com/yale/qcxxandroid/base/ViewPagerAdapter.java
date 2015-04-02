package com.yale.qcxxandroid.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 填充ViewPager页面的适配器
 */
public class ViewPagerAdapter extends PagerAdapter {
	private int[] viewResId;
	private List<View> views;
	@SuppressWarnings("unused")
	private ArrayList<HashMap<String, Object>> list;

	public ViewPagerAdapter(int[] viewResId, List<View> views) {
		this.viewResId = viewResId;
		this.views = views;
	}

	@Override
	public int getCount() {
		return viewResId.length;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(views.get(arg1));
		return views.get(arg1);
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public void finishUpdate(View arg0) {
	}
}
