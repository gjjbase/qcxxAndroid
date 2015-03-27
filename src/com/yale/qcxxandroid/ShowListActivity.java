package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.yale.qcxxandroid.base.MyBaseListView;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.PicPagerAdapter;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
import com.yale.qcxxandroid.util.GlobalUtil;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ImageThread;
import com.yale.qcxxandroid.util.ThreadUtil;

public class ShowListActivity extends BaseActivity {
	private MyBaseListView listView;
	private ThreadUtil threadUtil, thread;
	Intent intent = new Intent();
	Bundle bundle = new Bundle();

	// String jsonParamStr = "[{'number':" + uName + ",'password':" + pWord
	// + "}]";
	String PID = "G14C3080A6639B3FE30FFDC4";
	private int pras, par;
	private ShowListAdapter Adapter;
	private int tag, myfag;

	private void praise(String pmrid, String youid) {
		threadUtil = new ThreadUtil(handler);
		// "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'saveCommonAction'}]"
		String methodStr = "[{'com.yale.qcxx.sessionbean.comm.impl.CommonDataSessionBean':'saveCommonAction'}]";
		String jsonParamStr = "[{'me_id':" + sp.getString("userId", "")
				+ ",'primary_id':" + "'" + pmrid + "'" + ",'action_type':" + 2
				+ ",'your_id':" + 1 + "}]";
		threadUtil.doStartWebServicerequestWebService(ShowListActivity.this,
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

	private void init() {
		thread = new ThreadUtil(mhandler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.show.impl.ShowsSessionBean':'listOfShows'}]";
		String jsonParamStr = "[{'aa':" + 1 + "}]";
		thread.doStartWebServicerequestWebService(ShowListActivity.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	private Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				Log.i("#################", returnJson);
				// String[] sr = returnJson.split("{");
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
								myfag = 0;
							} else {

							}
							Adapter.notifyDataSetChanged();
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
					Adapter = new ShowListAdapter(ShowListActivity.this, jsoo,
							sliString);
					listView.setAdapter(Adapter);

				} catch (Exception e) {
					// TODO: handle exception
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
		setContentView(R.layout.show_list_activity);
		listView = (MyBaseListView) findViewById(R.id.listView);
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

	@Override
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

	public class ShowListAdapter extends BaseAdapter {
		private Context mContext;
		Intent intent = new Intent();

		private JSONArray jsoo;
		private boolean fag = false;

		private String[][] slideimg;

		public ShowListAdapter(Context context, JSONArray jsoo,
				String[][] slideimg) {
			this.mContext = context;
			this.jsoo = jsoo;
			this.slideimg = slideimg;
		}

		public int getCount() {
			return jsoo.length();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("HandlerLeak")
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ThisItem thisItem = new ThisItem();
			if (convertView == null) {
				View v = LayoutInflater.from(mContext).inflate(
						R.layout.show_list_adapter, null);
				thisItem.content = (TextView) v.findViewById(R.id.content);
				thisItem.cmtCont = (TextView) v.findViewById(R.id.cmtCont);
				thisItem.right = (TextView) v.findViewById(R.id.right);
				thisItem.name = (TextView) v.findViewById(R.id.name);
				thisItem.txt2_toper = (TextView) v
						.findViewById(R.id.txt2_toper);
				thisItem.txt_top = (TextView) v.findViewById(R.id.txt_top);
				thisItem.age = (TextView) v.findViewById(R.id.age);
				final TextView txt2_top = (TextView) v
						.findViewById(R.id.txt2_top);
				final LinearLayout click_lin = (LinearLayout) v
						.findViewById(R.id.click_lin);
				thisItem.img_lefttop = (ImageView) v
						.findViewById(R.id.img_lefttop);
				final LinearLayout lin_content = (LinearLayout) v
						.findViewById(R.id.lin_content);
				final EditText edt = (EditText) v.findViewById(R.id.edt);
				final ViewPager viewPager = (ViewPager) v.findViewById(R.id.vp);
				TextView txt = (TextView) v.findViewById(R.id.txt);

				try {
					thisItem.right.setText(jsoo.getJSONObject(position)
							.getString("pubTime"));
					thisItem.cmtCont.setText(jsoo.getJSONObject(position)
							.getString("pubTitle"));
					txt2_top.setText(jsoo.getJSONObject(position).getString(
							"topCount"));
					thisItem.txt2_toper.setText(jsoo.getJSONObject(position)
							.getString("viewCount"));
					thisItem.txt_top.setText(jsoo.getJSONObject(position)
							.getString("voteCount"));
					thisItem.name.setText(jsoo.getJSONObject(position)
							.getJSONObject("userInfo").getString("nickName"));
					thisItem.age.setText(jsoo.getJSONObject(position)
							.getJSONObject("userInfo").getString("age"));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pras = Integer.parseInt(txt2_top.getText().toString());
				par = Integer.parseInt(txt2_top.getText().toString());
				txt.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext,
								edt.getText().toString().trim(), 3000).show();
						lin_content.setVisibility(View.GONE);
						click_lin.setVisibility(View.VISIBLE);
					}
				});
				List<ImageView> img = new ArrayList<ImageView>();
				for (int i = 0; i < slideimg[position].length; i++) {
					ImageView im = new ImageView(mContext);
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
				viewPager.setAdapter(adapter);
				tag = position;
				viewPager.setOnPageChangeListener(new MyPageChangeListener());
				thisItem.img_lefttop.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

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
							txt2_top.setText(String.valueOf(pras));
						} else {
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
								txt2_top.setText(String.valueOf(pras));
								editor.putString("addm", "add");
								editor.commit();
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
								txt2_top.setText(String.valueOf(pras));
								editor.putString("addm", "minu");
								editor.commit();
							}

							editor.putString("tag", "tag");
							editor.commit();
						}

					}
				});
				click_lin.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						intent.setClass(mContext, CommentsActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
				});
				// click_lin.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// lin_content.setVisibility(View.VISIBLE);
				// click_lin.setVisibility(View.GONE);
				// edt.setFocusable(true);
				//
				// edt.setFocusableInTouchMode(true);
				//
				// edt.requestFocus();
				//
				// InputMethodManager inputManager =
				//
				// (InputMethodManager) edt.getContext().getSystemService(
				// Context.INPUT_METHOD_SERVICE);
				//
				// inputManager.showSoftInput(edt, 0);
				// }
				// });
				v.setTag(thisItem);
				convertView = v;

			} else {
				thisItem = (ThisItem) convertView.getTag();
			}

			return convertView;
		}

		class ThisItem {
			int currentItem = 0; // 当前view的索引号
			ImageView img_lefttop;
			TextView content, cmtCont, right, txt2_toper, txt_top, name, age;
			ImageView img_toper;
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
					bundle.putInt("tag", tag);
					try {
						bundle.putString("id", jsoo.getJSONObject(tag)
								.getJSONObject("userInfo").getString("userId"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent intent = new Intent(mContext, MyDetailActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					intent.putExtras(bundle);
					startActivity(intent);
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

	public void userDetail(View view) {
		intent = new Intent(ShowListActivity.this, MyDetailActivity.class);
		startActivity(intent);
	}

	public void commentDetail(View view) {
		intent = new Intent(ShowListActivity.this, CommentDetailActivity.class);
		startActivity(intent);
	}
}
