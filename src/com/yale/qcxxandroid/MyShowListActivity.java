package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.PicPagerAdapter;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
import com.yale.qcxxandroid.util.GlobalUtil;
import com.yale.qcxxandroid.util.ImgLoadThread;
import com.yale.qcxxandroid.util.ThreadUtil;
import com.yale.qcxxandroid.base.MyBaseListView;

public class MyShowListActivity extends BaseActivity {
	private MyBaseListView listView;
	private ThreadUtil threadUtil;
	Intent intent = new Intent();
	private Adapter adapter;
	private ArrayList<String> list;
	private TextView txt_id;
	private Button back;
	private JSONArray joA;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_show_list_activity);
		listView = (MyBaseListView) findViewById(R.id.listView);
		txt_id = (TextView) findViewById(R.id.txt_id);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		back.setText(getIntent().getStringExtra("name").toString());
		txt_id.setText(getIntent().getStringExtra("name").toString());
		list = new ArrayList<String>();
		list.add("我的滑板鞋");
		list.add("自由歌舞剧");
		list.add("酒吧畅饮");
		adapter = new Adapter(MyShowListActivity.this, list);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "刷新",
						Toast.LENGTH_SHORT).show();
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						listView.onRefreshComplete();
					}
				}.execute();
			}
		});
	}

	public Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					joA = new JSONArray(returnJson);
					// JSONObject jo = joA.getJSONObject(0);
					// JSONArray jsarr = jo.getJSONArray("imgurl");
					// JSONObject jsoo = jsarr.toJSONObject(jsarr);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 2:

				break;

			}
		}
	};

	public void init() {
		threadUtil = new ThreadUtil(mhandler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.member.impl.UserInfoSessionBean':'getUserInfo'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "") + "}]";
		threadUtil.doStartWebServicerequestWebService(MyShowListActivity.this,
				jsonParamStr, methodStr, true);
	}

	private class Adapter extends BaseAdapter {
		private Context context;
		private Viewholder viewholder;
		private ArrayList<String> list;
		private List<ImageView> img;
		private int[] imger = new int[] { R.drawable.example1,
				R.drawable.example2, R.drawable.xiuxiuban7 };

		// private int[] imger;

		private Adapter(Context context, ArrayList<String> list) {
			this.context = context;
			this.list = list;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.myshow_layout, null);
			viewholder.txt_active = (TextView) convertView
					.findViewById(R.id.txt_active);
			viewholder.viewpager = (ViewPager) convertView
					.findViewById(R.id.viewpager);
			img = new ArrayList<ImageView>();
			for (int i = 0; i < imger.length; i++) {
				ImageView im = new ImageView(context);
				im.setImageResource(imger[i]);
				im.setScaleType(ScaleType.CENTER_CROP);
				im.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), "222222", 2000)
								.show();
					}
				});
				img.add(im);
			}
			viewholder.viewpager.setAdapter(new PicPagerAdapter(imger, img));

			viewholder.viewpager
					.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {
							// TODO Auto-generated method stub

						}
					});
			viewholder.txt_active.setText(list.get(position));
			return convertView;

		}

		class Viewholder {
			public TextView txt_active;
			public android.support.v4.view.ViewPager viewpager;
		}
	}
}
