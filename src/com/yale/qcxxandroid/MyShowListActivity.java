package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
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

import com.yale.qcxxandroid.ShowListActivity.ShowListAdapter;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.PicPagerAdapter;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
import com.yale.qcxxandroid.util.GlobalUtil;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ImageThread;
import com.yale.qcxxandroid.util.ImgLoadThread;
import com.yale.qcxxandroid.util.ThreadUtil;
import com.yale.qcxxandroid.base.MyBaseListView;

public class MyShowListActivity extends BaseActivity {
	private MyBaseListView listView;
	private ThreadUtil threadUtil;
	Intent intent = new Intent();
	private Adapter adapter;
	private TextView txt_id, txt_name,txt_role;
	private Button back;
	private int pras, par;

	private void detail() {
		Handler handler = new Handler() {
			@SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:

					String returnJson = (String) msg.getData().getString(
							"returnJson");
					try {
						JSONArray jso = new JSONArray(returnJson);
						JSONObject jsoo = jso.getJSONObject(0);
						txt_name.setText(jsoo.getString("nickName")); 
						txt_role.setText("诚信分:"+jsoo.getInt("credit"));
						// inschool = (TextView) findViewById(R.id.inschool);
						// inschool.setText(sp.getString("inSchool", ""));
						// txt_role.setText(jsoo.getString("credit"));
						// img = (ImageView) findViewById(R.id.img);
						// img.setScaleType(ScaleType.CENTER);
						// bitmap = GlobalUtil
						// .getBitmapFromMemCache(jsoo
						// .getString("headImg"));
						// List<String> lisimg = new ArrayList<String>();
						// lisimg.add(jsoo.getString("headImg").toString());
						// if (bitmap != null) {
						//
						// } else {
						// bitmap = BitmapFactory.decodeResource(
						// getApplicationContext().getResources(),
						// R.drawable.logo);
						// }

						// Handler handlerSlideImg = new Handler() {
						// @SuppressWarnings("unused")
						// public void handlerMessage(Message msg) {
						// super.handleMessage(msg);
						// img.setImageBitmap(bitmap);
						// getLayoutInflater().inflate(
						// R.layout.mydetail_activity, null)
						// .invalidate();
						// }
						// };
						// ImgLoadThread imgLoadThread = new
						// ImgLoadThread(lisimg,
						// handlerSlideImg);
						// imgLoadThread.start();

					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;
				case 2:

					break;

				}
			}
		};
		threadUtil = new ThreadUtil(handler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.member.impl.UserInfoSessionBean':'getUserInfo'}]";
		String jsonParamStr = "[{'userId':"
				+ getIntent().getExtras().getString("id") + "}]";
		// String jsonParamStr = "[{'userId':" + sp.getString("userId", "") +
		// "}]";
		threadUtil.doStartWebServicerequestWebService(MyShowListActivity.this,
				jsonParamStr, methodStr, true);

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_show_list_activity);
		listView = (MyBaseListView) findViewById(R.id.listView);
		txt_id = (TextView) findViewById(R.id.txt_id);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_role = (TextView) findViewById(R.id.txt_role);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (par - pras == 0) {
					editor.putString("addm", "add");
					editor.commit();
				} else {
					editor.putString("addm", "minu");
					editor.commit();
				}
				finish();
			}
		});
		back.setText(getIntent().getStringExtra("name").toString());
		txt_id.setText(getIntent().getStringExtra("name").toString());
		// list = new ArrayList<String>();
		// list.add("我的滑板鞋");
		// list.add("自由歌舞剧");
		// list.add("酒吧畅饮");
		// adapter = new Adapter(MyShowListActivity.this, list);
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
		init();
	}

	private void praise(String pmrid, String youid) {
		threadUtil = new ThreadUtil(handler);
		// "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'saveCommonAction'}]"
		String methodStr = "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'saveCommonAction'}]";
		String jsonParamStr = "[{'me_id':" + sp.getString("userId", "")
				+ ",'primary_id':" + "'" + pmrid + "'" + ",'action_type':" + 2
				+ ",'your_id':" + 1 + "}]";
		threadUtil.doStartWebServicerequestWebService(MyShowListActivity.this,
				jsonParamStr, methodStr, true);
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
	public Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				System.out.println(returnJson);
				try {
					JSONArray jsoo = new JSONArray(returnJson);

					ArrayList<String> slideimg = new ArrayList<String>();
					String[][] sliString = new String[jsoo.length()][];
					Handler handlerSlideImg = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							Bundle b = msg.getData();
							int cl = b.getInt("cl");
							// 1成功 0失败
							if (cl == 0) {

							} else {

							}
							adapter.notifyDataSetChanged();
						}
					};
					try {
						for (int i = 0; i < jsoo.length(); i++) {

							String[] ster = jsoo.getJSONObject(i)
									.getString("pubImgs").split(",");
							String[] str = new String[ster.length];
							for (int j = 0; j < ster.length; j++) {
								// String bitm =
								// "http://202.103.1.2/qcxxweb/upload/images/G14C3080A6639B3FE30FFDC4/G14C3080A6749C3FD3C424F8.jpg";
								String bitm = Globals.PHT_URL
										+ "/qcxxweb/upload/images/"
										+ jsoo.getJSONObject(j).getString(
												"showsId") + "/" + ster[j]
										+ ".jpg";
								str[j] = bitm;
								slideimg.add(bitm);
							}
							sliString[i] = str;
							// Log.i("@@@@@@@@@@@@@@@@@@@@@@@@@@",
							// jsoo.getJSONObject(i).toString());
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					ImageThread imgLoadThread = new ImageThread(slideimg,
							handlerSlideImg);
					imgLoadThread.start();
					adapter = new Adapter(MyShowListActivity.this, jsoo,
							sliString);
					listView.setAdapter(adapter);
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
		String methodStr = "[{'com.yale.qcxx.sessionbean.show.impl.ShowsSessionBean':'listOfShows'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "") + "}]";
		threadUtil.doStartWebServicerequestWebService(MyShowListActivity.this,
				jsonParamStr, methodStr, true);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (par - pras == 0) {
			editor.putString("addm", "add");
			editor.commit();
		} else {
			editor.putString("addm", "minu");
			editor.commit();
		}
		return super.onKeyDown(keyCode, event);
	}

	private class Adapter extends BaseAdapter {
		private Context context;
		private Viewholder viewholder;
		private JSONArray jsoo;
		private String[][] slideimg;
		private boolean fag = false;

		private Adapter(Context context, JSONArray jsoo, String[][] slideimg) {
			this.context = context;
			this.jsoo = jsoo;
			this.slideimg = slideimg;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return jsoo.length();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.myshow_layout, null);
			viewholder.txt_active = (TextView) convertView
					.findViewById(R.id.txt_active);
			viewholder.msg = (TextView) convertView.findViewById(R.id.msg);
			viewholder.watch = (TextView) convertView.findViewById(R.id.watch);
			viewholder.zan = (TextView) convertView.findViewById(R.id.zan);
			try {
				viewholder.msg.setText(jsoo.getJSONObject(position).getString(
						"voteCount"));
				viewholder.zan.setText(jsoo.getJSONObject(position).getString(
						"topCount"));
				viewholder.watch.setText(jsoo.getJSONObject(position)
						.getString("viewCount"));
				viewholder.txt_active.setText(jsoo.getJSONObject(position)
						.getString("pubTitle"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			viewholder.viewpager = (ViewPager) convertView
					.findViewById(R.id.viewpager);
			pras = Integer.parseInt(viewholder.zan.getText().toString());
			par = Integer.parseInt(viewholder.zan.getText().toString());
			List<ImageView> img = new ArrayList<ImageView>();
			for (int i = 0; i < slideimg[position].length; i++) {
				ImageView im = new ImageView(context);
				im.setScaleType(ScaleType.CENTER_CROP);
				if (GlobalUtil.getBitmapFromMemCache(slideimg[position][i]) == null) {
					im.setImageResource(R.drawable.logo);

				} else {
					im.setImageBitmap(GlobalUtil
							.getBitmapFromMemCache(slideimg[position][i]));
					// Log.i("图片", slideimg[position][i] + "*******"
					// + String.valueOf(i));
					// Log.i("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", jsoo
					// .getJSONObject(i).getString("pubImgs"));
					// Log.i("%%%%%%%%%%%%%%%%%%%%%",
					// slideimg[position][i]);
				}
				img.add(im);
			}
			PicPagerAdapter adapter = new PicPagerAdapter(
					new int[slideimg[position].length], img);
			viewholder.viewpager.setAdapter(adapter);
			viewholder.zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (sp.getString("tag", "").equals("tag")) {
						if (sp.getString("addm", "").equals("add")) {
							if (fag == false) {
								pras--;
								try {
									praise(jsoo.getJSONObject(position)
											.getString("showsId"), jsoo
											.getJSONObject(position)
											.getJSONObject("userInfo")
											.getString("userId"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								fag = true;
							} else {
								pras++;
								try {
									praise(jsoo.getJSONObject(position)
											.getString("showsId"), jsoo
											.getJSONObject(position)
											.getJSONObject("userInfo")
											.getString("userId"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								fag = false;
							}
						}

						if (sp.getString("addm", "").equals("minu")) {
							if (fag == false) {
								pras++;
								try {
									praise(jsoo.getJSONObject(position)
											.getString("showsId"), jsoo
											.getJSONObject(position)
											.getJSONObject("userInfo")
											.getString("userId"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								fag = true;
							} else {
								pras--;
								try {
									praise(jsoo.getJSONObject(position)
											.getString("showsId"), jsoo
											.getJSONObject(position)
											.getJSONObject("userInfo")
											.getString("userId"));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								fag = false;
							}
						}
						viewholder.zan.setText(String.valueOf(pras));
					} else {
						if (fag == false) {
							pras++;
							try {
								praise(jsoo.getJSONObject(position).getString(
										"showsId"),
										jsoo.getJSONObject(position)
												.getJSONObject("userInfo")
												.getString("userId"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							fag = true;
							viewholder.zan.setText(String.valueOf(pras));
							editor.putString("addm", "add");
							editor.commit();
						} else {
							pras--;
							try {
								praise(jsoo.getJSONObject(position).getString(
										"showsId"),
										jsoo.getJSONObject(position)
												.getJSONObject("userInfo")
												.getString("userId"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							fag = false;
							viewholder.zan.setText(String.valueOf(pras));
							editor.putString("addm", "minu");
							editor.commit();
						}

						editor.putString("tag", "tag");
						editor.commit();
					}

				}
			});
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
			// viewholder.txt_active.setText(list.get(position));
			return convertView;

		}

		class Viewholder {
			public TextView txt_active, watch, zan, msg;
			public android.support.v4.view.ViewPager viewpager;
		}
	}
}
