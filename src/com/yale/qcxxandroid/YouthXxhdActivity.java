package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class YouthXxhdActivity extends FragmentActivity {
	private ViewPager mPageVp;
	private LinearLayout back;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private YouthFragmentAdapter mFragmentAdapter;

	/**
	 * Tab显示内容TextView
	 */
	private TextView mTabGift, mTabXm;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	private YouthGiftFragment giftFragment;
	private YouthXmFragment xmFragment;
	private YouthBmFragment bmFragment;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	private LinearLayout lin1, lin2, lin3;
	private LinearLayout[] lin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youth_xxhd_activity);
		lin1 = (LinearLayout) findViewById(R.id.lin1);
		lin2 = (LinearLayout) findViewById(R.id.lin2);
		lin3 = (LinearLayout) findViewById(R.id.lin3);
		lin = new LinearLayout[] { lin1, lin2, lin3 };
		findById();
		init();
		initTabLineWidth();

	}

	// case 0:
	// button[0].setTextColor(getResources().getColor(R.color.white));
	// button[1]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[2]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[3]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[0].setBackground(getResources().getDrawable(
	// R.color.greener));
	//
	// button[1].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[2].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[3].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// break;
	// case 1:
	// button[0].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	//
	// button[1].setBackground(getResources().getDrawable(
	// R.color.greener));
	// button[2].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[3].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[0]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[1].setTextColor(getResources().getColor(R.color.white));
	// button[2]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[3]
	// .setTextColor(getResources().getColor(R.color.greener));
	// break;
	// case 2:
	// button[0].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	//
	// button[1].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[2].setBackground(getResources().getDrawable(
	// R.color.greener));
	// button[3].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[0]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[1]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[2].setTextColor(getResources().getColor(R.color.white));
	// button[3]
	// .setTextColor(getResources().getColor(R.color.greener));
	// break;
	// case 3:
	// button[0].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	//
	// button[1].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[2].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// button[3].setBackground(getResources().getDrawable(
	// R.color.greener));
	// button[0]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[1]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[2]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[3].setTextColor(getResources().getColor(R.color.white));
	// break;
	private void findById() {
		back = (LinearLayout) findViewById(R.id.back);
		mTabGift = (TextView) this.findViewById(R.id.xxhd_gift);
		mTabXm = (TextView) this.findViewById(R.id.xxhd_xm);
		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);

	}

	private void init() {
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		giftFragment = new YouthGiftFragment();
		xmFragment = new YouthXmFragment();
		bmFragment = new YouthBmFragment();
		mFragmentList.add(giftFragment);
		mFragmentList.add(xmFragment);
		mFragmentList.add(bmFragment);

		mFragmentAdapter = new YouthFragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(0);

		mPageVp.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				Log.e("offset:", offset + "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景： 记3个页面, 从左到右分别为0,1,2 0->1; 1->2; 2->1;
				 * 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));


				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
					// select(2);
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 3) + currentIndex
							* (screenWidth / 3));
				}
				mTabLineIv.setLayoutParams(lp);
			}

			// lin = new LinearLayout[] { lin1, lin2, lin3 };
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					lin1.setBackgroundColor(getResources().getColor(
							R.color.greener));
					lin2.setBackground(getResources().getDrawable(
							R.drawable.btn_bg_xml));
					lin3.setBackground(getResources().getDrawable(
							R.drawable.btn_bg_xml));
					break;
				case 1:
					lin2.setBackgroundColor(getResources().getColor(
							R.color.greener));
					lin1.setBackground(getResources().getDrawable(
							R.drawable.btn_bg_xml));
					lin3.setBackground(getResources().getDrawable(
							R.drawable.btn_bg_xml));
					break;
				case 2:
					lin3.setBackgroundColor(getResources().getColor(
							R.color.greener));
					lin1.setBackground(getResources().getDrawable(
							R.drawable.btn_bg_xml));
					lin2.setBackground(getResources().getDrawable(
							R.drawable.btn_bg_xml));
					break;

				}
				// resetTextView();
				/*
				 * switch (position) { case 0:
				 * mTabGift.setTextColor(Color.GREEN); break; case 1:
				 * mTabXm.setTextColor(Color.GREEN); break; }
				 */
				// currentIndex = position;
				// select(position);
			}
		});

	}

	/**
	 * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 3;
		mTabLineIv.setLayoutParams(lp);
	}

	// private void select(int fag) {
	// for (int i = 0; i < lin.length; i++) {
	// lin[1].setBackground(getResources().getDrawable(
	// R.drawable.btn_bg_xml));
	// }
	// lin[fag].setBackgroundColor(getResources().getColor(
	// R.drawable.btn_bg_xml));
	// }

	// button[0].setTextColor(getResources().getColor(R.color.white));
	// button[1]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[2]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[3]
	// .setTextColor(getResources().getColor(R.color.greener));
	// button[0].setBackground(getResources().getDrawable(
	// R.color.greener));
	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		mTabGift.setTextColor(Color.BLACK);
		mTabXm.setTextColor(Color.BLACK);
	}

}
