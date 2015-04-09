package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.yale.qcxxandroid.base.MyBaseListView;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.PicPagerAdapter;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
import com.yale.qcxxandroid.bean.Shows;
import com.yale.qcxxandroid.util.GlobalUtil;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

public class MyClassActivityCont extends BaseActivity {
	private int falg;
	private MyBaseListView conlist;
	private Adapter adapter;
	private ThreadUtil thread;
	private TextView txt_back;
	@SuppressWarnings("unused")
	private JSONArray jsoo;
	private List<String> list = new ArrayList<String>();
	private TextView txtliuyan;
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private String TIME, CMD;

	public String stringbuff(String[] array) {
		StringBuffer stringbu = new StringBuffer();
		for (String arrays : array) {
			stringbu.append(arrays + ",");
		}
		return stringbu.toString();
	}

	class Adapter extends BaseAdapter {
		private Context context;
		private int tag;
		private ViewHolder viewholder;
		List<Shows> allList = new ArrayList<Shows>();
		private int length;
		private JSONArray jsoo;
		private String[] str = { "光谷一日游", "青春秀秀合伙人", "户外徒步旅行", "爬山", "同学聚会" };
		private List<ImageView> img;
		private int[] imger = new int[] { R.drawable.example1,
				R.drawable.example2, R.drawable.xiuxiuban7 };

		public Adapter(Context context, int tag) {
			this.context = context;
			this.tag = tag;
		}

		public int getCount() {
			switch (tag) {
			case 1:

				length = 5;
				break;
			case 2:
				length = 4;
				break;

			case 4:
				length = 4;
				break;

			}
			try {
			} catch (Exception e) {
				// TODO: handle exception
			}
			// return jsoo.length();
			return length;
		}

		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "CutPasteId", "NewApi" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.member_childitem, null);
			viewholder.childertitle = (TextView) convertView
					.findViewById(R.id.child_text);
			viewholder.txt_join = (TextView) convertView
					.findViewById(R.id.txt_join);
			viewholder.txt_action = (TextView) convertView
					.findViewById(R.id.txt_action);
			viewholder.txt_address = (TextView) convertView
					.findViewById(R.id.txt_address);
			viewholder.child_image = (ImageView) convertView
					.findViewById(R.id.child_image);
			viewholder.hi_lin = (LinearLayout) convertView
					.findViewById(R.id.hi_lin);
			viewholder.list_lin = (LinearLayout) convertView
					.findViewById(R.id.list_lin);
			viewholder.lin = (LinearLayout) convertView.findViewById(R.id.lin);
			viewholder.bottom_lin = (LinearLayout) convertView
					.findViewById(R.id.bottom_lin);
			viewholder.other_lin = (LinearLayout) convertView
					.findViewById(R.id.other_lin);
			viewholder.child_text = (TextView) convertView
					.findViewById(R.id.child_text);
			viewholder.txt_enter = (TextView) convertView
					.findViewById(R.id.txt_enter);

			viewholder.txt_chose = (TextView) convertView
					.findViewById(R.id.txt_chose);

			viewholder.showTime = (TextView) convertView
					.findViewById(R.id.showTime);
			viewholder.nickName = (TextView) convertView
					.findViewById(R.id.nickName);
			viewholder.age = (TextView) convertView.findViewById(R.id.age);
			viewholder.viewstr = (TextView) convertView
					.findViewById(R.id.viewstr);
			viewholder.topstr = (TextView) convertView
					.findViewById(R.id.topstr);
			viewholder.sex = (TextView) convertView.findViewById(R.id.sex);
			viewholder.plstr = (TextView) convertView.findViewById(R.id.plstr);
			viewholder.txt_people = (TextView) convertView
					.findViewById(R.id.txt_people);

			viewholder.showPic = (android.support.v4.view.ViewPager) convertView
					.findViewById(R.id.showPic);
			viewholder.list = (ListView) convertView.findViewById(R.id.list);
			switch (tag) {
			case 1:
				viewholder.lin.setVisibility(View.VISIBLE);
				viewholder.other_lin.setVisibility(View.VISIBLE);
				viewholder.hi_lin.setVisibility(View.VISIBLE);
				viewholder.txt_join.setVisibility(View.VISIBLE);
				viewholder.txt_people.setVisibility(View.VISIBLE);
				viewholder.txt_people.setText("参加人数:陈璐,陈璐 ");
				// try {
				viewholder.txt_join.setText("100人参加");
				viewholder.txt_join.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "参加活动", 3000).show();
					}
				});
				viewholder.child_text.setText(str[position]);
				viewholder.child_image.setVisibility(View.GONE);
				viewholder.child_image.setImageResource(R.drawable.demo5);
				viewholder.txt_action.setVisibility(View.VISIBLE);
				viewholder.txt_action.setText("报名地点:" + "光谷国际佳园路");
				viewholder.txt_address.setVisibility(View.VISIBLE);
				viewholder.txt_address.setText("2013/13/14/");
				// viewholder.txt_join.setText("参加");
				// viewholder.child_text.setText("活动");
				// viewholder.child_image.setImageResource(R.drawable.demo5);
				// viewholder.txt_action.setVisibility(View.VISIBLE);
				// viewholder.txt_action.setText(jsoo.getJSONObject(0)
				// .getJSONArray("acList").getJSONObject(position)
				// .getString("pubContent"));
				// viewholder.txt_address.setVisibility(View.VISIBLE);
				// viewholder.txt_address.setText("报名时间:"
				// + jsoo.getJSONObject(0).getJSONArray("acList")
				// .getJSONObject(position)
				// .getString("pubTime"));
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				break;
			case 2:

				viewholder.list_lin.setVisibility(View.VISIBLE);
				img = new ArrayList<ImageView>();

				for (int i = 0; i < imger.length; i++) {
					ImageView im = new ImageView(context);
					im.setImageResource(imger[i]);
					im.setScaleType(ScaleType.CENTER_CROP);
					im.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(), "222222",
									2000).show();
						}
					});
					img.add(im);
				}
				viewholder.showPic.setAdapter(new PicPagerAdapter(imger, img));

				viewholder.showPic
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
				// JSONObject jso = jsoo.getJSONObject(0)
				// .getJSONArray("showList").getJSONObject(position);
				viewholder.nickName.setText("陈璐");
				viewholder.sex.setText("校草");
				viewholder.age.setText("19" + "岁");
				viewholder.showTime.setText("2012/3/5");
				viewholder.plstr.setText("123");
				viewholder.viewstr.setText("1234");
				viewholder.topstr.setText("334");

				// try {
				// JSONObject jso = jsoo.getJSONObject(0)
				// .getJSONArray("showList").getJSONObject(position);
				// viewholder.nickName.setText(jso.getJSONObject("userInfo")
				// .getString("nickName"));
				// viewholder.sex.setText(jso.getJSONObject("userInfo")
				// .getString("sex"));
				// viewholder.age.setText(jso.getJSONObject("userInfo")
				// .getString("age"));
				//
				// viewholder.showTime.setText(jso.getString("pubTime"));
				// viewholder.plstr.setText(jso.getString("topCount"));
				// viewholder.viewstr.setText(jso.getString("viewCount"));
				// viewholder.topstr.setText(jso.getString("voteCount"));
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// android:drawableLeft="@drawable/img_canjia"
				// setCompoundDrawablesRelativeWithIntrinsicBounds(null,
				// getResources().getDrawable(R.drawable.img_qingchun),
				// null, null);
				break;
			case 4:
				viewholder.other_lin.setVisibility(View.VISIBLE);
				viewholder.lin.setVisibility(View.VISIBLE);
				viewholder.hi_lin.setVisibility(View.VISIBLE);
				viewholder.txt_action.setVisibility(View.VISIBLE);
				viewholder.txt_join.setVisibility(View.VISIBLE);
				viewholder.child_text.setText("陈璐");
				viewholder.txt_join.setText("2013/12/15");
				viewholder.txt_action.setText(list.get(position));
				viewholder.txt_join
						.setCompoundDrawablesRelativeWithIntrinsicBounds(
								getResources().getDrawable(
										R.drawable.img_canjia), null, null,
								null);
				break;
			}
			return convertView;
		}

		class ViewHolder {
			public ImageView child_image;
			public TextView childertitle, child_text;
			public ImageView img;
			public android.support.v4.view.ViewPager showPic;
			public ListView list;
			public TextView title, txt_join, txt_address, txt_action,
					txt_chose, txt_enter, txt_people;
			public LinearLayout hi_lin, lin, bottom_lin, list_lin, other_lin;
			public TextView showTime, age, topstr, viewstr, nickName, sex,
					plstr;
		}
	}

	// AlertDialog.Builder builder = new AlertDialog.Builder(
	// MyClassActivityCont.this);
	// LinearLayout mLayout = new LinearLayout(getApplicationContext());
	// LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
	//
	// LinearLayout.LayoutParams.FILL_PARENT,
	//
	// LinearLayout.LayoutParams.WRAP_CONTENT
	//
	// );
	//
	// // 调用addView()方法增加一个TextView到线性布局中
	// EditText edt = new EditText(getApplicationContext());
	// mLayout.addView(edt, p);
	// builder.setMessage("你确定退出吗？")
	// .setCancelable(false)
	// .setPositiveButton("确定",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	//
	// }
	// })
	// .setNegativeButton("取消",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int id) {
	// dialog.cancel();
	// }
	// });
	//
	// AlertDialog alert = builder.create();
	// alert.show();
	public void childactivit(TextView v) {
		bundle.putString("chile", v.getText().toString());
		intent.setClass(MyClassActivityCont.this, MychildeActivity.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	// showshowMain
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cont);
		falg = getIntent().getExtras().getInt("data");
		conlist = (MyBaseListView) findViewById(R.id.conlist);
		conlist.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {

						try {
							// if (sp.getBoolean("false", false) == true) {
							Thread.sleep(500);
							Toast.makeText(getApplicationContext(), "刷新完成",
									Toast.LENGTH_SHORT).show();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						conlist.onRefreshComplete();
					}
				}.execute();
			}
		});
		txt_back = (TextView) findViewById(R.id.txt_back);
		txtliuyan = (TextView) findViewById(R.id.txtliuyan);
		list.add("还不错");
		list.add("值得一看");
		list.add("谢谢分享");
		list.add("还不错");
		list.add(sp.getString("back", ""));
		txtliuyan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				childactivit(txtliuyan);
			}
		});
		switch (falg) {
		case 1:
			txt_back.setText("班级活动");
			break;
		case 2:
			txt_back.setText("班级风采");
			break;
		case 4:
			txt_back.setText("班级留言");
			txtliuyan.setVisibility(View.VISIBLE);
			break;

		}
		txt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		adapter = new Adapter(MyClassActivityCont.this, falg);
		conlist.setAdapter(adapter);
		init(falg);
	}

	@SuppressLint("HandlerLeak")
	private void init(final int bundler) {

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					String returnJson = (String) msg.getData().getString(
							"returnJson");
					try {
						switch (bundler) {
						case 1:
							jsoo = new JSONArray(returnJson);
							break;
						case 2:

							break;
						case 4:

							break;
						}
						break;
					} catch (Exception e) {
						// TODO: handle exception
					}

				case 2:

					break;
				}
			}
		};
		thread = new ThreadUtil(handler);
		String methodStr = null, jsonParamStr = null;
		// 1班级活动 2 班级秀秀 4班级留言
		switch (bundler) {
		case 1:

			break;
		case 2:// listOfshows
			methodStr = "[{'" + Globals.COMM_SESSION
					+ ".ShowsSessionBean':'listOfshows'}]";
			jsonParamStr = "[{'primaryId':"
					
					+ getIntent().getExtras().getString("classlist")
					+ ",'orderBy':" + 4 + "}]";
			break;
		case 4:
			Log.i("**************", GlobalUtil.TIME());
			TIME = (GlobalUtil.TIME().substring(2));
			String comidd = TIME.substring(0, TIME.length() - 1) + "."
					+ TIME.substring(TIME.length() - 1, TIME.length());
			CMD = comidd.replace(" ", "");
			methodStr = "[{'" + Globals.COMM_SESSION
					+ ".CommonDataSessionBean':'commentList'}]";
			jsonParamStr = "[{'commentType':" + 1 + ",'primaryId':"
					+ getIntent().getExtras().getString("classlist")
					+ ",'commentId':" + CMD + "}]";
			break;

		}
		// primaryId 班级id //commentId 根据时间的150324150302.微妙毫毛
		// String methodStr =
		// "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'myClassDetail'}]";
		//
		// String jsonParamStr = "[{'signType':" + 1 + ",'commentType':" + 1
		// + ",'primaryId':"
		// + getIntent().getExtras().getString("classlist")
		// + ",'pubType':" + 0 + ",'orderBy':" + 4 + "}]";// primaryid 班级ID
		thread.doStartWebServicerequestWebService(MyClassActivityCont.this,
				jsonParamStr, methodStr, true);
	}
}
