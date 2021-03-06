package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.ShowMainTabActivity;
import com.yale.qcxxandroid.base.ViewPagerAdapter;
import com.yale.qcxxandroid.util.GlobalUtil;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ImgLoadThread;
import com.yale.qcxxandroid.util.ThreadUtil;

public class ShowPageListActivity extends BaseActivity {
	private Intent intent = new Intent();
	private ThreadUtil thread;
	private ViewPager viewPager;
	private List<View> views; // 滑动的view片集合
	private int[] viewResId; // viewID
	Bitmap bitmap, bitmap2;
	ViewPagerAdapter adapter;
	boolean bol = false;
	boolean bol2 = false;
	int yal, fag;

	private void praise(String pmrid, String youid) {
		thread = new ThreadUtil(handler);
		String methodStr = "[{'" + Globals.COMM_SESSION
				+ ".CommonDataSessionBean':'saveCommonAction'}]";
		String jsonParamStr = "[{'me_id':" + sp.getString("userId", "")
				+ ",'primary_id':" + "'" + pmrid + "'" + ",'action_type':" + 2
				+ ",'your_id':" + 1 + "}]";
		thread.doStartWebServicerequestWebService(getParent(), jsonParamStr,
				methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	// [{"returnStr":"true"}]
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					JSONArray joA = new JSONArray(returnJson);
					JSONObject jo = joA.getJSONObject(0);
					if (jo.getString("returnStr").equals("true")) {

					} else {
						Toast.makeText(getApplicationContext(), "点赞失败", 3000);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "网络连接失败", 3000);
				}

				break;
			case 2:
				Toast.makeText(getApplicationContext(), "网络连接失败", 3000);
				break;

			}
		}
	};

	@SuppressLint("HandlerLeak")
	public void ini() {
		thread = new ThreadUtil(mhandler);// com.yale.qcxx.sessionbean.member.impl
		String methodStr = "[{'" + Globals.SHOW_SESSION
				+ ".ShowsSessionBean':'showshowMain'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "") + "}]";
		thread.doStartWebServicerequestWebService(getParent(), jsonParamStr,
				methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	private Handler mhandler = new Handler() {
		@SuppressWarnings("deprecation")
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					JSONArray joA = new JSONArray(returnJson);
					JSONObject jo = joA.getJSONObject(0);
					final JSONArray manjsarr = jo.getJSONArray("listShows");
					final JSONArray wojsar = jo.getJSONArray("listShows1");
					final List<String> slideimg = new ArrayList<String>();
					final List<String> slideimg2 = new ArrayList<String>();

					viewPager = (ViewPager) findViewById(R.id.vp);
					views = new ArrayList<View>();
					editor.putBoolean("fag", false);
					editor.putBoolean("bol", false);
					editor.commit();
					if (manjsarr.length() > wojsar.length()) {
						viewResId = new int[manjsarr.length()];
					} else {
						viewResId = new int[wojsar.length()];
					}
					// 初始化图片资源
					for (int i = 0; i < viewResId.length; i++) {
						final int tag = i;
						View view = getLayoutInflater().inflate(
								R.layout.show_page_list_layout, null);
						final TextView time2 = (TextView) view
								.findViewById(R.id.time2);
						final TextView time = (TextView) view
								.findViewById(R.id.time);
						final TextView title = (TextView) view
								.findViewById(R.id.title);
						final TextView title2 = (TextView) view
								.findViewById(R.id.title2);
						final TextView topcount = (TextView) view
								.findViewById(R.id.topcount);
						final TextView topcount2 = (TextView) view
								.findViewById(R.id.topcount2);
						final TextView viewcount = (TextView) view
								.findViewById(R.id.viewcount);
						final TextView viewcount2 = (TextView) view
								.findViewById(R.id.viewcount2);
						final TextView nickname = (TextView) view
								.findViewById(R.id.nickname);
						final TextView nickname2 = (TextView) view
								.findViewById(R.id.nickname2);
						final ImageView img_yale1 = (ImageView) view
								.findViewById(R.id.img_yale1);
						final ImageView img_yale2 = (ImageView) view
								.findViewById(R.id.img_yale2);
						final ImageView top = (ImageView) view
								.findViewById(R.id.top);
						final ImageView top2 = (ImageView) view
								.findViewById(R.id.top2);
						try {
							if (wojsar.getJSONObject(i)
									.getJSONObject("userInfo").getString("sex")
									.equals("0")) {
								img_yale1.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.man));
							} else {
								img_yale1.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.women));
							}
							nickname.setText(wojsar.getJSONObject(i)
									.getJSONObject("userInfo")
									.getString("nickName"));
							topcount.setText(wojsar.getJSONObject(i).getInt(
									"topCount")
									+ "");
							viewcount.setText(wojsar.getJSONObject(i).getInt(
									"viewCount")
									+ "");
							title.setText(wojsar.getJSONObject(i).getString(
									"pubContent"));
							time.setText(wojsar.getJSONObject(i).getString(
									"pubTime"));
							fag = Integer.parseInt(topcount.getText()
									.toString());
							top2.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (sp.getBoolean("fag", true) == false) {
										editor.putBoolean("fag", true);
										editor.commit();
										fag++;
										topcount.setText(String.valueOf(fag));
									} else {
										editor.putBoolean("fag", false);
										editor.commit();
										fag--;
										topcount.setText(String.valueOf(fag));
									}
									try {
										praise(wojsar.getJSONObject(tag)
												.getString("showsId"), wojsar
												.getJSONObject(tag)
												.getJSONObject("userInfo")
												.getString("userId"));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									toast("点赞", getApplicationContext());
								}
							});
							// public static final String WSDL_URL =
							// "http://202.103.1.30/qcxxweservice/";
							String bitm1 = Globals.PHT_URL
									+ "/upload/images/"
									+ wojsar.getJSONObject(i).getString(
											"showsId")
									+ "/"
									+ wojsar.getJSONObject(i)
											.getString("pubImgs").split(",")[0]
									+ ".jpg";
							slideimg2.add(bitm1);
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							if (manjsarr.getJSONObject(i)
									.getJSONObject("userInfo").getString("sex")
									.equals("0")) {
								img_yale2.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.man));
							} else {
								img_yale2.setBackgroundDrawable(getResources()
										.getDrawable(R.drawable.women));
							}
							nickname2.setText(manjsarr.getJSONObject(i)
									.getJSONObject("userInfo")
									.getString("nickName"));
							topcount2.setText(manjsarr.getJSONObject(i).getInt(
									"topCount")
									+ "");
							viewcount2.setText(manjsarr.getJSONObject(i)
									.getInt("viewCount") + "");
							title2.setText(manjsarr.getJSONObject(i).getString(
									"pubContent"));
							time2.setText(manjsarr.getJSONObject(i).getString(
									"pubTime"));
							yal = Integer.parseInt(topcount2.getText()
									.toString());
							String bitm2 = Globals.PHT_URL
									+ "/upload/images/"
									+ manjsarr.getJSONObject(i).getString(
											"showsId")
									+ "/"
									+ manjsarr.getJSONObject(i)
											.getString("pubImgs").split(",")[0]
									+ ".jpg";
							slideimg.add(bitm2);
							top.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									if (sp.getBoolean("bol", true) == false) {
										editor.putBoolean("bol", true);
										editor.commit();
										yal++;
										topcount2.setText(String.valueOf(yal));
									} else {
										editor.putBoolean("bol", false);
										editor.commit();
										yal--;
										topcount2.setText(String.valueOf(yal));
									}
									try {
										praise(wojsar.getJSONObject(tag)
												.getString("showsId"), wojsar
												.getJSONObject(tag)
												.getJSONObject("userInfo")
												.getString("userId"));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									toast("点赞2", getApplicationContext());
								}
							});
							// slideimg.add();
						} catch (Exception e) {
							// TODO: handle exception
						}
						view.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								intent.setClass(ShowPageListActivity.this,
										ShowMainTabActivity.class);
								startActivity(intent);
							}
						});
						views.add(view);
					}
					adapter = new ViewPagerAdapter(viewResId, views);
					viewPager.setAdapter(adapter);
					Handler handlerSlideImg = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							Bundle b = msg.getData();
							int cl = b.getInt("cl");
							if (cl == 0) {
								for (int i = 0; i < viewResId.length; i++) {
									try {
										ImageView show1 = (ImageView) views
												.get(i).findViewById(
														R.id.showPic1);
										show1.setScaleType(ScaleType.CENTER_CROP);
										// show1.setImageResource(R.drawable.logo);
										if (GlobalUtil
												.getBitmapFromMemCache(slideimg
														.get(i)) == null) {
											show1.setImageResource(R.drawable.logo);
											// LOG.i("++++++++++++++++++++++++","失败");
										} else {
											show1.setImageBitmap(GlobalUtil
													.getBitmapFromMemCache(slideimg
															.get(i)));
										}

									} catch (Exception e) {
										// TODO: handle exception
									}

								}
								adapter.notifyDataSetChanged();
							}
						}
					};
					ImgLoadThread imgLoadThread = new ImgLoadThread(slideimg,
							handlerSlideImg);
					imgLoadThread.start();
					Handler handlerSlideImg2 = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							Bundle b = msg.getData();
							int cl = b.getInt("cl");
							if (cl == 0) {
								for (int i = 0; i < viewResId.length; i++) {
									try {
										ImageView show2 = (ImageView) views
												.get(i).findViewById(
														R.id.showPic);
										show2.setScaleType(ScaleType.CENTER_CROP);
										// show2.setImageResource(R.drawable.logo);
										show2.setImageBitmap(GlobalUtil
												.getBitmapFromMemCache(slideimg2
														.get(i)));
									} catch (Exception e) {
										// TODO: handle exception
									}

								}
								adapter.notifyDataSetChanged();
							}
						}
					};
					ImgLoadThread imgLoadThread2 = new ImgLoadThread(slideimg2,
							handlerSlideImg2);
					imgLoadThread2.start();
					viewPager
							.setOnPageChangeListener(new MyPageChangeListener());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			case 2:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_page_list_activity);
		ini();
	}

	private class MyPageChangeListener implements OnPageChangeListener {
		public void onPageSelected(int position) {
			// memo.setText((currentItem + 1) + "/" + viewResId.length);
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	public void userDetail(View view) {
		intent.setClass(ShowPageListActivity.this, MyDetailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, 0);
	}

	public void showList(View view) {
		intent.setClass(ShowPageListActivity.this, ShowMainTabActivity.class);
		startActivity(intent);
	}

}
