package com.yale.qcxxandroid.base;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * 填充ViewPager页面的适配器
 */
public class PicPagerAdapter extends PagerAdapter {
	private int[] imageResId;
	private List<ImageView> imageViews;

	public PicPagerAdapter(int[] imageResId, List<ImageView> imageViews) {
		this.imageResId = imageResId;
		this.imageViews = imageViews;
	}

	@Override
	public int getCount() {
		return imageResId.length;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		// ViewGroup parent = (ViewGroup) arg0.getParent();
		// if (parent != null) {
		// parent.removeAllViews();
		// }
		// ((ViewPager) arg0).addView(imageViews.get(arg1));
		// return imageViews.get(arg1);
		try {
			if (imageViews.get(arg1).getParent() == null) {
				((ViewPager) arg0).addView(imageViews.get(arg1), 0);
			}
		} catch (Exception e) { // TODO Auto-generated catch block
								// e.printStackTrace();
		}
		return imageViews.get(arg1);
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
