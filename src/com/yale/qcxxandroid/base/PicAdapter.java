package com.yale.qcxxandroid.base;

import java.util.List;

import com.yale.qcxxandroid.util.GlobalUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class PicAdapter extends PagerAdapter {
	private List<ImageView> imageViews;
	private List<String> list;
	private Context context;

	public PicAdapter(Context context, List<String> list,
			List<ImageView> imageViews) {
		this.list = list;
		this.imageViews = imageViews;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
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

				for (int i = 0; i < list.size(); i++) {
					ImageView im = new ImageView(context);
					im.setScaleType(ScaleType.CENTER_CROP);
					((ViewPager) arg0).addView(im, 0);
					imageViews.add(im);
					Bitmap bitm = GlobalUtil.getBitmapFromMemCache(list.get(i));
					if (bitm != null) {
						imageViews.get(i).setImageBitmap(bitm);
					}
				}

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
