package com.yale.qcxxandroid;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MyBaseListView;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
import com.yale.qcxxandroid.chat.AnimatedGifDrawable;
import com.yale.qcxxandroid.chat.AnimatedImageSpan;
import com.yale.qcxxandroid.chat.ChatInfo;
import com.yale.qcxxandroid.chat.FaceGVAdapter;
import com.yale.qcxxandroid.chat.FaceVPAdapter;
import com.yale.qcxxandroid.chat.MyEditText;
import com.yale.qcxxandroid.util.ThreadUtil;

public class CommentsActivity extends BaseActivity {
	private MyBaseListView list;
	private com.yale.qcxxandroid.chat.MyEditText input_sms;
	private ImageView send_sms;
	private ImageView image_face, img_adder;
	private boolean tag = false;
	private ViewPager mViewPager;
	private LinearLayout mDotsLayout;
	private List<String> staticFacesList;
	private int columns = 7;
	private int rows = 3;
	private List<View> views = new ArrayList<View>();
	private ChatLVAdapter mLvAdapter;
	private LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();
	private ThreadUtil thread;
	private TextView txt_back;
	// private void init(){
	// thread=new ThreadUtil(mhandler);
	// }
	// Handler mhandler=new Handler(){
	// public void handlerMessage(Message msg){
	// super.handleMessage(msg);
	// switch (msg.what) {
	// case 1:
	//
	// break;
	// case 2:
	//
	// break;
	//
	//
	// }
	// }
	// };
	class PageChange implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
				mDotsLayout.getChildAt(i).setSelected(false);
			}
			mDotsLayout.getChildAt(arg0).setSelected(true);
		}

	}

	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}

	private SpannableStringBuilder getFace(String png) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		try {
			/**
			 * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
			 * 所以这里对这个tempText值做特殊处理
			 * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
			 * */
			String tempText = "#[" + png + "]#";
			sb.append(tempText);
			sb.setSpan(
					new ImageSpan(CommentsActivity.this, BitmapFactory
							.decodeStream(getAssets().open(png))), sb.length()
							- tempText.length(), sb.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	private void insert(CharSequence text) {
		int iCursorStart = Selection.getSelectionStart((input_sms.getText()));
		int iCursorEnd = Selection.getSelectionEnd((input_sms.getText()));
		if (iCursorStart != iCursorEnd) {
			((Editable) input_sms.getText()).replace(iCursorStart, iCursorEnd,
					"");
		}
		int iCursor = Selection.getSelectionEnd((input_sms.getText()));
		((Editable) input_sms.getText()).insert(iCursor, text);
	}

	private boolean isDeletePng(int cursor) {
		String st = "#[face/png/f_static_000.png]#";
		String content = input_sms.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length(),
					content.length());
			String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			return m.matches();
		}
		return false;
	}

	private void delete() {
		if (input_sms.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(input_sms.getText());
			int iCursorStart = Selection.getSelectionStart(input_sms.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(iCursorEnd)) {
						String st = "#[face/png/f_static_000.png]#";
						((Editable) input_sms.getText()).delete(
								iCursorEnd - st.length(), iCursorEnd);
					} else {
						((Editable) input_sms.getText()).delete(iCursorEnd - 1,
								iCursorEnd);
					}
				} else {
					((Editable) input_sms.getText()).delete(iCursorStart,
							iCursorEnd);
				}
			}
		}
	}

	private ChatInfo getChatInfoTo(String pullname, String message, String fag,
			boolean falg, String time) {
		// SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		// String date = format.format(new Date());
		ChatInfo info = new ChatInfo();
		info.content = message;
		info.pullname = pullname;
		info.falg = falg;
		// time=data;
		info.time = time;
		// info.time = date;
		if (fag.equals("")) {
			info.fromOrTo = 0;
		} else {
			info.fromOrTo = Integer.parseInt(fag);
		}
		// if ((System.currentTimeMillis() - upTime) > 60000) {
		// upTime = System.currentTimeMillis();
		// info.time = DateFormatUtil.getCurrDate(Constant.DATE_PATTERN_1);
		// } else {
		// info.time = "";
		// }
		// info.time = DateFormatUtil.getCurrDate(Constant.DATE_PATTERN_1);
		return info;
	}

	private String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;// 三元运算符 t>10时取 ""+t
	}

	public String time() {
		Calendar c = Calendar.getInstance();

		String time = c.get(Calendar.YEAR) + "-" + // 得到年
				formatTime(c.get(Calendar.MONTH) + 1) + "-" + // month加一 //月
				formatTime(c.get(Calendar.DAY_OF_MONTH)) + " " + // 日
				formatTime(c.get(Calendar.HOUR_OF_DAY));
		// + ":" + // 时
		// formatTime(c.get(Calendar.MINUTE)) + ":" + // 分
		// formatTime(c.get(Calendar.SECOND)); // 秒
		System.out.println(time); // 输出
		return time;
	}

	private View viewPagerItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.face_gridview, null);
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
		 * */
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (columns
								* rows - 1)
								* (position + 1)));
		// 0-20 20-40 40-60 60-80
		/**
		 * 末尾添加删除图标
		 * */
		subList.add("emotion_del_normal.png");
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		// 单击表情执行的操作
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				try {
					String png = ((TextView) ((LinearLayout) view)
							.getChildAt(1)).getText().toString();
					if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
						// input_sms.setText(sb);
						insert(getFace(png));
					} else {
						delete();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		input_sms.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (input_sms.getText().toString().equals("")) {
					img_adder.setVisibility(View.VISIBLE);
					send_sms.setVisibility(View.INVISIBLE);
				} else {
					img_adder.setVisibility(View.INVISIBLE);
					send_sms.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			// img_adder.setv
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// 发送
		// editText.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if(expanded){
		//
		// setFaceLayoutExpandState(false);
		// expanded=false;
		// }
		// return false;
		// }
		// });
		input_sms.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				input_sms.setFocusable(true);

				input_sms.setFocusableInTouchMode(true);

				input_sms.requestFocus();

				InputMethodManager input_smsManager =
				// INPUT_METHOD_SERVICE

				(InputMethodManager) input_sms.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);

				input_smsManager.showSoftInput(input_sms, 0);
				return false;
			}
		});

		send_sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(input_sms.getText().toString())) {
					infos.add(getChatInfoTo("", input_sms.getText().toString(),
							"1", false, time()));
					mLvAdapter.setList(infos);
					mLvAdapter.notifyDataSetChanged();
					list.setSelection(infos.size() - 1);
					input_sms.setText("");
				}
			}
		});

		/***
		 * listview下拉刷新
		 * */
		// mListView
		// .setOnRefreshListenerHead(new
		// DropdownListView.OnRefreshListenerHeader() {
		//
		// @Override
		// public void onRefresh() {
		// // TODO Auto-generated method stub
		// new Thread() {
		// @Override
		// public void run() {
		// try {
		// sleep(1000);
		// Message msg = mHandler.obtainMessage(0);
		// mHandler.sendMessage(msg);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		//
		// }.start();
		// }
		// });

		return gridview;
	}

	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	private void InitViewPager() {
		// 获取页数
		for (int i = 0; i < getPagerCount(); i++) {
			views.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			// LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);
	}

	private void initStaticFaces() {
		try {
			staticFacesList = new ArrayList<String>();
			String[] faces = getAssets().list("face/png");
			for (int i = 0; i < faces.length; i++) {
				staticFacesList.add(faces[i]);
			}
			staticFacesList.remove("emotion_del_normal.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitycomment);
		input_sms = (MyEditText) findViewById(R.id.input_sms);
		txt_back=(TextView) findViewById(R.id.txt_back);
		send_sms = (ImageView) findViewById(R.id.send_sms);
		image_face = (ImageView) findViewById(R.id.image_face);
		img_adder = (ImageView) findViewById(R.id.img_adder);
		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		txt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		image_face.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(CommentsActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				if (tag == false) {
					mViewPager.setVisibility(View.VISIBLE);
					mDotsLayout.setVisibility(View.VISIBLE);
					tag = true;
				} else {
					mViewPager.setVisibility(View.GONE);
					mDotsLayout.setVisibility(View.GONE);
					tag = false;
				}
			}
		});
		mViewPager.setOnPageChangeListener(new PageChange());
		initStaticFaces();
		initViews();
	}

	 
	private void initViews() {
		list = (MyBaseListView) findViewById(R.id.list);
		// chat = new ChatInfo();
		// chat.fromOrTo=0;
		// infos.add(chat);getExtras();

		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "1987-10-28"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "1987-10-27"));
		infos.add(getChatInfoTo("", "青春秀秀", "1", false, "1987-10-26"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "1987-10-25"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "1987-10-24"));
		infos.add(getChatInfoTo("", "青春秀秀", "1", false, "1987-10-23"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "1987-10-22"));
		mLvAdapter = new ChatLVAdapter(CommentsActivity.this, infos);
		list.setAdapter(mLvAdapter);
		list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(CommentsActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				mViewPager.setVisibility(View.GONE);
				mDotsLayout.setVisibility(View.GONE);
				return false;
			}
		});
		list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "刷新",
						Toast.LENGTH_SHORT).show();
				// editor.putBoolean("fasle", true);
				// editor.commit();
				new AsyncTask<Void, Void, Void>() {
					@SuppressWarnings("unchecked")
					protected Void doInBackground(Void... params) {

						try {
							// if (sp.getBoolean("false", false) == true) {
							// infos.add(getChatInfoTo("", "佳园路1", "1", false,
							// "1987-04-23"));
							// infos.add(getChatInfoTo("", "佳园路2", "1", false,
							// "1987-08-26"));
							// infos.add(getChatInfoTo("", "佳园路3", "1", false,
							// "1987-09-25"));
							// infos.add(getChatInfoTo("", "佳园路4", "1", false,
							// "1987-07-24"));
							// infos.add(getChatInfoTo("", "佳园路5", "1", false,
							// "1987-06-21"));
							// infos.add(getChatInfoTo("", "佳园路6", "1", false,
							// "1987-05-22"));
							// sortClass sort = new sortClass();
							// Collections.sort(infos, sort);
							// for (int i = 0; i < infos.size(); i++) {
							// ChatInfo temp = (ChatInfo) infos.get(i);
							// System.out.println("姓名:" + temp.pullname
							// + ",生日:" + temp.content);
							// }
							// mLvAdapter.notifyDataSetChanged();
							// editor.putBoolean("false",true);
							// editor.commit()
							// }

							// /list.setSelection(infos.size());
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						list.onRefreshComplete();
					}
				}.execute();
			}
		});
		InitViewPager();
	}

	private String mathc(String str) {
		Pattern p = Pattern.compile("\\(qemu\\)");
		// 假设你接受到的字符串为"I have a problem (qemu) about this"
		Matcher m = p.matcher(str);
		// 将"(qemu)"用""代替，即过滤掉。
		return m.replaceAll("");
	}

	class sortClass implements Comparator {
		public int compare(Object arg0, Object arg1) {
			// LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();
			// ChatInfo info = new ChatInfo();

			ChatInfo user0 = (ChatInfo) arg0;
			ChatInfo user1 = (ChatInfo) arg1;

			int flag = mathc(user0.time).compareTo(mathc(user0.time));
			// int flag = user0.time.compareTo(user1.time);
			return flag;
		}
	}

	class ChatLVAdapter extends BaseAdapter {
		private Context mContext;
		private List<ChatInfo> list;
		protected long mAnimationTime = 150;

		public ChatLVAdapter(Context mContext, List<ChatInfo> list) {
			super();
			this.mContext = mContext;
			this.list = list;
		}

		public void setList(List<ChatInfo> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
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
			ViewHodler hodler;
			if (convertView == null) {
				hodler = new ViewHodler();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.commentlistact, null);

				hodler.mesg = (TextView) convertView.findViewById(R.id.mesg);
				hodler.img = (ImageView) convertView.findViewById(R.id.img);
				hodler.name = (TextView) convertView.findViewById(R.id.name);
				hodler.time = (TextView) convertView.findViewById(R.id.time);
				convertView.setTag(hodler);
			} else {
				hodler = (ViewHodler) convertView.getTag();
			}
			// =1接受消息=0发送消息 to 发送消息 from接受消息

			// if (falg == 1) {
			// hodler.fromContainer.setVisibility(View.GONE);
			// hodler.img_otherhint.setVisibility(View.VISIBLE);
			// hodler.img_otherhint.setImageResource(R.drawable.demo5);

			SpannableStringBuilder sb = handler(hodler.mesg,
					list.get(position).content);
			hodler.mesg.setText(sb);
			// }
			hodler.img.setBackgroundResource(R.drawable.logo);
			hodler.name.setText("邱金勇  武汉工程大学");
			hodler.time.setText(list.get(position).time);
			return convertView;
		}

		private SpannableStringBuilder handler(final TextView gifTextView,
				String content) {
			SpannableStringBuilder sb = new SpannableStringBuilder(content);
			String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(content);
			while (m.find()) {
				String tempText = m.group();
				try {
					String num = tempText.substring(
							"#[face/png/f_static_".length(), tempText.length()
									- ".png]#".length());
					String gif = "face/gif/f" + num + ".gif";
					/**
					 * */
					InputStream is = mContext.getAssets().open(gif);
					sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(
							is, new AnimatedGifDrawable.UpdateListener() {
								@Override
								public void update() {
									gifTextView.postInvalidate();
								}
							})), m.start(), m.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					is.close();
				} catch (Exception e) {
					String png = tempText.substring("#[".length(),
							tempText.length() - "]#".length());
					try {
						sb.setSpan(
								new ImageSpan(mContext, BitmapFactory
										.decodeStream(mContext.getAssets()
												.open(png))), m.start(), m
										.end(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
			return sb;
		}

		class ViewHodler {
			TextView mesg, name, time;
			ImageView img;
		}

		public boolean isEnabled(int position) {
			return false;
		}

	}
}
