package com.yale.qcxxandroid.chat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.yale.qcxxandroid.base.MyBaseListView;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;
import com.yale.qcxxandroid.bean.XmppMsgBean;
import com.yale.qcxxandroid.chat.xmpp.XmppGlobals;
import com.yale.qcxxandroid.chat.xmpp.XmppService;
import com.yale.qcxxandroid.util.DataHelper;
import com.yale.qcxxandroid.util.GlobalUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * */
public class ChatMainActivity extends BaseActivity {
	private ViewPager mViewPager;
	private LinearLayout mDotsLayout;
	private MyEditText input;
	private ImageView send;
	private MyBaseListView mListView;
	private ChatLVAdapter mLvAdapter;
	// 7列3行
	private int columns = 7;
	private int rows = 3;
	private List<View> views;
	private List<String> staticFacesList;
	private volatile LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();
	private ImageView image_face;
	private EditText num;
	private int tag = 0;
	private TextView txt_name, txt_back, txt1, txt2;
	private static String picFileFullName, realPath;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	AlertDialog alert;
	static Bitmap bm;
	private ImageView img_adder;
	RelativeLayout rel_hint;
	private ImageView left;
	private ButtonRecorder inputer;
	private int falg;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		img_adder = (ImageView) findViewById(R.id.img_adder);
		left = (ImageView) findViewById(R.id.left);
		inputer = (ButtonRecorder) findViewById(R.id.inputer);
		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				falg++;
				if (falg % 2 == 0) {
					left.setImageResource(R.drawable.img_chat);
					inputer.setVisibility(View.VISIBLE);
					input.setVisibility(View.GONE);
				} else {
					left.setImageResource(R.drawable.img_yuying);
					inputer.setVisibility(View.GONE);
					input.setVisibility(View.VISIBLE);
				}
			}
		});
		/*
		 * inputer.setOnLongClickListener(new OnLongClickListener() {
		 * 
		 * @Override public boolean onLongClick(View v) { // TODO Auto-generated
		 * method stub Toast.makeText(getApplicationContext(), "说话",
		 * 3000).show(); return false; } });
		 */
		img_adder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rel_hint.setVisibility(View.GONE);
				initStaticFaces(3);
				InitViewPager(3);
				mDotsLayout.setVisibility(View.VISIBLE);
				mViewPager.setVisibility(View.VISIBLE);
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(ChatMainActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);

			}
		});

		initViews();
		mListView.setSelection(infos.size());
		bindXMPPService();
		// 新消息监听
		IntentFilter filter = new IntentFilter(XmppGlobals.MESSAGE_ACTION);
		registerReceiver(receiver, filter);
		// mListView.setSelection(infos.size());
		// 消息发送是否成功的广播监听
		IntentFilter filter2 = new IntentFilter(XmppGlobals.SENDMSG_ISUCCESSED);
		registerReceiver(receiverCast, filter2);
		// 发送语音消息的广播监听
		IntentFilter filter3 = new IntentFilter(
				XmppGlobals.ACTION_RECORDER_SEND);
		registerReceiver(reciverSoundSend, filter3);

	}

	private BroadcastReceiver receiverCast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			boolean flag = intent.getBooleanExtra("IsSuccess", false);
			if (flag) {
				Toast.makeText(ChatMainActivity.this, "发送成功！", 2000).show();
			} else {
				Toast.makeText(ChatMainActivity.this, "发送失败！", 2000).show();
			}
		}
	};
	private BroadcastReceiver reciverSoundSend = new BroadcastReceiver() {

		@SuppressWarnings("deprecation")
		@Override
		public void onReceive(Context context, Intent intent) {
			String fileName = intent.getStringExtra("fileName");
			if (fileName == null || fileName.equals("")) {
				Toast.makeText(ChatMainActivity.this, "录制语音出错！", 2000).show();
			} else {
				infos.add(getChatInfoTo("", fileName, num.getText().toString(),
						XmppGlobals.MessageType.sound,
						new Date(System.currentTimeMillis()).toLocaleString()));
				mLvAdapter.setList(infos);
				mLvAdapter.notifyDataSetChanged();
				mListView.setSelection(infos.size() - 1);
				// 讲文件转换为base64字符串
				String absolutePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath().toString()
						+ fileName;
				try {
					String content = encodeBase64File(absolutePath);
					String fromUserId = getSharedPreferences("qcxx",
							Context.MODE_PRIVATE).getString("phoneNum", "");
					String type = XmppGlobals.MessageType.sound; // 语音消息
					byte[] myfie = Base64.decode(content, Base64.CRLF);
					String path = Environment.getExternalStorageDirectory()
							.getAbsolutePath().toString()
							+ "/qcxx/";
					byte2File(myfie, path, "text.qcxx");
					XmppMsgBean bean = new XmppMsgBean();
					bean.setChatTopic(chatName);
					bean.setContent(content);
					bean.setFileSize(fileName);
					bean.setFromUserId(fromUserId);
					bean.setMsgtype(0);
					bean.setReaded(false);
					bean.setTimeLen(fileName);
					bean.setTimeSend(GlobalUtil.getLocalDate());
					bean.setToUserId(chatName);
					bean.setType(type);
					mXmppService.sendMsg(bean);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		String paths = Environment.getExternalStorageDirectory()
				.getAbsolutePath().toString()
				+ "/qcxx/";
		byte2File(buffer, paths, "textAAAA.qcxx");
		return Base64.encodeToString(buffer, Base64.CRLF);
	}

	public static boolean byte2File(byte[] buf, String filePath, String fileName) {
		boolean flag = true;
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {
				boolean flags = dir.mkdirs();
				System.out.println(flags);
			}
			file = new File(filePath + fileName);
			file.createNewFile();
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	// 拍照
	public void takePicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File outDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
			picFileFullName = outFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} else {

		}
	}

	// 打开本地相册
	public void openAlbum() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// toast("获取图片成功，path=" + picFileFullName);
				// readBitmapAutoSize(picFileFullName, 100, 100, img_tuxiang);
				editor.putString("item", picFileFullName);
				editor.putInt("falger", 0);
				editor.commit();
			} else if (resultCode == RESULT_CANCELED) {
				// 用户取消了图像捕获
			} else {
				// 图像捕获失败，提示用户
				// Log.e(tag, "拍照失败");
			}
		} else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				if (uri != null) {
					realPath = getRealPathFromURI(uri);
					// Log.e(tag, "获取图片成功，path="+realPath);
					// toast("获取图片成功，path=" + realPath);
					// readBitmapAutoSize(realPath, 100, 100, img_tuxiang);
					editor.putString("item", realPath);
					editor.putInt("falger", 1);
					editor.commit();
				} else {
					// Log.e(tag, "从相册获取图片失败");
				}
			}
		}
		if (sp.getInt("falger", 3) == 1) {
			infos.add(getChatInfoTo(realPath, "", num.getText().toString(),
					XmppGlobals.MessageType.picture, ""));
		} else if (sp.getInt("falger", 3) == 0) {
			infos.add(getChatInfoTo(picFileFullName, "", num.getText()
					.toString(), XmppGlobals.MessageType.picture, ""));
		}

		mLvAdapter.setList(infos);
		mLvAdapter.notifyDataSetChanged();
		mListView.setSelection(infos.size() - 1);
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			// Do not call Cursor.close() on a cursor obtained using this
			// method,
			// because the activity will do that for you at the appropriate time

			Cursor cursor = this.managedQuery(contentUri, proj, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	private void initViews() {
		chatName = getIntent().getExtras().getString("name");
		mListView = (MyBaseListView) findViewById(R.id.message_chat_listview);
		try {
			Dao<XmppMsgBean, Integer> messageDao = DataHelper.getInstance(
					ChatMainActivity.this).getXmppMsgDAO();
			String sql = "select * from xmppMsgBean where chatTopic = '"
					+ chatName + "' order by timeSend desc limit 5";
			GenericRawResults<XmppMsgBean> rawResults = messageDao.queryRaw(
					sql, new RawRowMapper<XmppMsgBean>() {
						@Override
						public XmppMsgBean mapRow(String[] columnNames,
								String[] resultColumns) throws SQLException {
							XmppMsgBean bean = new XmppMsgBean();
							bean.setChatTopic(resultColumns[0]);
							bean.setContent(resultColumns[1]);
							bean.setFileSize(resultColumns[2]);
							bean.setFromUserId(resultColumns[3]);
							bean.setType(resultColumns[4]);
							bean.setToUserId(resultColumns[5]);
							bean.setTimeSend(resultColumns[6]);
							bean.setTimeLen(resultColumns[7]);
							bean.setMsgtype(Integer.parseInt(resultColumns[8]));
							bean.setReaded(Boolean
									.parseBoolean(resultColumns[9]));
							bean.setId(Integer.parseInt(resultColumns[10]));
							return bean;
						}
					});
			// there should be 1 result
			List<XmppMsgBean> results = rawResults.getResults();
			if (results != null && results.size() > 0) {
				for (int i = results.size(); i > 0; i--) {
					XmppMsgBean msg = results.get(i - 1);
					if (!msg.isReaded()) {
						msg.setReaded(true);
						int a = messageDao.update(msg);
						System.out.println(a + "");
					}
					infos.add(getChatInfoTo(msg.getFromUserId(),
							msg.getContent(), msg.getMsgtype() + "",
							msg.getType(), msg.getTimeSend()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mLvAdapter = new ChatLVAdapter(this, infos);
		mListView.setAdapter(mLvAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					int num = 0;
					if (infos != null && infos.size() > 0)
						num = infos.size() - 1;
					Dao<XmppMsgBean, Integer> messageDao = DataHelper
							.getInstance(ChatMainActivity.this).getXmppMsgDAO();
					String sql = "select * from xmppMsgBean where chatTopic = '"
							+ chatName
							+ "' order by timeSend desc limit "
							+ (num + 5);
					GenericRawResults<XmppMsgBean> rawResults = messageDao
							.queryRaw(sql, new RawRowMapper<XmppMsgBean>() {
								@Override
								public XmppMsgBean mapRow(String[] columnNames,
										String[] resultColumns)
										throws SQLException {
									XmppMsgBean bean = new XmppMsgBean();
									bean.setChatTopic(resultColumns[0]);
									bean.setContent(resultColumns[1]);
									bean.setFileSize(resultColumns[2]);
									bean.setFromUserId(resultColumns[3]);
									bean.setType(resultColumns[4]);
									bean.setToUserId(resultColumns[5]);
									bean.setTimeSend(resultColumns[6]);
									bean.setTimeLen(resultColumns[7]);
									bean.setMsgtype(Integer
											.parseInt(resultColumns[8]));
									bean.setReaded(Boolean
											.parseBoolean(resultColumns[9]));
									bean.setId(Integer
											.parseInt(resultColumns[10]));
									return bean;
								}
							});
					// there should be 1 result
					List<XmppMsgBean> results = rawResults.getResults();
					if (results != null && results.size() > (num + 1)) {
						infos.clear();
						for (int i = results.size(); i > 0; i--) {
							XmppMsgBean bean = results.get(i - 1);
							infos.add(getChatInfoTo(bean.getFromUserId(),
									bean.getContent(), bean.getMsgtype() + "",
									bean.getType(), bean.getTimeSend()));
							// System.out.println(bean.getId()+","+bean.getMsgContent()+","+bean.getMsgTime()+","+bean.getSender());
						}
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(0);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		image_face = (ImageView) findViewById(R.id.image_face);
		num = (EditText) findViewById(R.id.num);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_back = (TextView) findViewById(R.id.txt_back);
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		final List<Integer> list = new ArrayList<Integer>();
		list.add(R.drawable.photo);

		txt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt1.setTextColor(getResources().getColor(R.color.white));
				txt1.setBackgroundResource(R.drawable.mbtn);
				txt2.setTextColor(getResources().getColor(R.color.black));
				txt2.setBackgroundResource(R.drawable.copyofmbtnr);
				initStaticFaces(1);
				InitViewPager(1);
				mDotsLayout.setVisibility(View.VISIBLE);
				mViewPager.setVisibility(View.VISIBLE);
			}
		});
		txt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt2.setTextColor(getResources().getColor(R.color.white));
				txt2.setBackgroundResource(R.drawable.mbtnr);
				txt1.setTextColor(R.color.black);
				txt1.setBackgroundResource(R.drawable.copyofmbtn);

				initStaticFaces(2);
				InitViewPager(2);

				mDotsLayout.setVisibility(View.VISIBLE);
				mViewPager.setVisibility(View.VISIBLE);
			}
		});
		txt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		txt_name.setText(getIntent().getExtras().getString("name"));
		num.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mViewPager.setVisibility(View.GONE);
				mDotsLayout.setVisibility(View.GONE);
				rel_hint.setVisibility(View.GONE);
			}
		});
		image_face.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tag++;
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(ChatMainActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				if (tag % 2 == 0) {
					mViewPager.setVisibility(View.GONE);
					mDotsLayout.setVisibility(View.GONE);
					rel_hint.setVisibility(View.GONE);
				} else {
					initStaticFaces(1);
					InitViewPager(1);
					mViewPager.setVisibility(View.VISIBLE);
					mDotsLayout.setVisibility(View.VISIBLE);
					rel_hint.setVisibility(View.VISIBLE);
				}

			}
		});
		mViewPager.setOnPageChangeListener(new PageChange());
		mDotsLayout = (LinearLayout) findViewById(R.id.face_dots_container);
		rel_hint = (RelativeLayout) findViewById(R.id.rel_hint);
		input = (MyEditText) findViewById(R.id.input_sms);
		send = (ImageView) findViewById(R.id.send_sms);
		// 单击表情执行的操作
		input.addTextChangedListener(new TextWatcher() {

			@SuppressWarnings("deprecation")
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (input.getText().toString().equals("")) {
					img_adder.setAlpha(255);
					send.setVisibility(View.GONE);
				} else {
					img_adder.setAlpha(0);
					send.setVisibility(View.VISIBLE);
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

			}
		});

		input.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				rel_hint.setVisibility(View.GONE);
				mDotsLayout.setVisibility(View.GONE);
				mViewPager.setVisibility(View.GONE);
				input.setFocusable(true);

				input.setFocusableInTouchMode(true);

				input.requestFocus();

				InputMethodManager inputManager = (InputMethodManager) input
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(input, 0);
				return false;
			}
		});

		send.setOnClickListener(new Button.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(input.getText().toString())) {
					infos.add(getChatInfoTo("", input.getText().toString(), num
							.getText().toString(),
							XmppGlobals.MessageType.text,
							new Date(System.currentTimeMillis())
									.toLocaleString()));
					mLvAdapter.setList(infos);
					mLvAdapter.notifyDataSetChanged();
					mListView.setSelection(infos.size() - 1);
					// 构造消息格式
					String content = input.getText().toString();
					String fromUserId = getSharedPreferences("qcxx",
							Context.MODE_PRIVATE).getString("phoneNum", "");
					// String msg = getMessgeBody(content, chatName,
					// fromUserId);
					String type = XmppGlobals.MessageType.text;
					XmppMsgBean bean = new XmppMsgBean();
					bean.setChatTopic(chatName);
					bean.setContent(content);
					bean.setFileSize("0");
					bean.setFromUserId(fromUserId);
					bean.setMsgtype(0);
					bean.setReaded(false);
					bean.setTimeLen("0");
					bean.setTimeSend(GlobalUtil.getLocalDate());
					bean.setToUserId(chatName);
					bean.setType(type);
					mXmppService.sendMsg(bean);
					input.setText("");
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				mLvAdapter.notifyDataSetChanged();
			} else {
				Toast.makeText(ChatMainActivity.this, "没有更多了！", 2000).show();
			}
			new AsyncTask<Void, Void, Void>() {
				protected Void doInBackground(Void... params) {
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					mListView.onRefreshComplete();
				}
			}.execute();
		};
	};

	/*
	 * 初始表情 *
	 */
	private void InitViewPager(int fag) {
		mDotsLayout.removeAllViews();
		views = new ArrayList<View>();
		// 获取页数
		if (fag == 1) {
			for (int i = 0; i < getPagerCount(1); i++) {
				views.add(viewPagerItem(i, fag));
				LayoutParams params = new LayoutParams(16, 16);
				// LayoutParams params = new
				// LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);

				mDotsLayout.addView(dotsItem(i), params);
			}
		} else if (fag == 2) {
			for (int i = 0; i < getPagerCount(2); i++) {
				views.add(viewPagerItem(i, fag));
				LayoutParams params = new LayoutParams(16, 16);
				// LayoutParams params = new
				// LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);

				mDotsLayout.addView(dotsItem(i), params);
			}
		} else if (fag == 3) {
			for (int i = 0; i < getPagerCount(3); i++) {
				views.add(viewPagerItem(i, fag));
				LayoutParams params = new LayoutParams(16, 16);
				// LayoutParams params = new
				// LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);

				mDotsLayout.addView(dotsItem(i), params);
			}
		}

		FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);

	}

	private View viewPagerItem(int position, int fag) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.face_gridview, null);
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		/**
		 * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows　－　1; 空出最后一个位置给删除图标
		 * */

		// 0-20 20-40 40-60 60-80
		/**
		 * 末尾添加删除图标
		 * */
		if (fag == 1) {
			List<String> subList = new ArrayList<String>();
			subList.addAll(staticFacesList.subList(position
					* (columns * rows - 1),
					(columns * rows - 1) * (position + 1) > staticFacesList
							.size() ? staticFacesList.size()
							: (columns * rows - 1) * (position + 1)));
			subList.add("emotion_del_normal.png");
			FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this, fag);
			gridview.setAdapter(mGvAdapter);
			gridview.setNumColumns(columns);
			// gridview.setColumnWidth(30);
			gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					try {
						String png = ((TextView) ((LinearLayout) view)
								.getChildAt(1)).getText().toString();
						if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
							// input.setText(sb);
							insert(getFace(png));
						} else {
							delete();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else if (fag == 2) {
			List<String> subList = new ArrayList<String>();
			subList.addAll(staticFacesList);
			FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this, fag);
			gridview.setAdapter(mGvAdapter);
			WindowManager manager = getWindowManager();
			Display display = manager.getDefaultDisplay();
			@SuppressWarnings("deprecation")
			int width = display.getWidth();
			gridview.setColumnWidth(width % 3);
			gridview.setNumColumns(3);
			// gridview.setColumnWidth(60);
			gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					try {
						String type = XmppGlobals.MessageType.Big_face;
						String png = ((TextView) ((LinearLayout) view)
								.getChildAt(1)).getText().toString();
						String content = getFace(png).toString();
						infos.add(getChatInfoTo("", content, num
								.getText().toString(),type,
								new Date(System.currentTimeMillis())
										.toLocaleString()));
						mLvAdapter.setList(infos);
						mLvAdapter.notifyDataSetChanged();
						mListView.setSelection(infos.size() - 1);
						// 构造消息格式
						String fromUserId = getSharedPreferences("qcxx",
								Context.MODE_PRIVATE).getString("phoneNum", "");
						// String msg = getMessgeBody(content, chatName,
						// fromUserId);
						
						XmppMsgBean bean = new XmppMsgBean();
						bean.setChatTopic(chatName);
						bean.setContent(content);
						bean.setFileSize("0");
						bean.setFromUserId(fromUserId);
						bean.setMsgtype(0);
						bean.setReaded(false);
						bean.setTimeLen("0");
						bean.setTimeSend(GlobalUtil.getLocalDate());
						bean.setToUserId(chatName);
						bean.setType(type);
						mXmppService.sendMsg(bean);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} else if (fag == 3) {
			List<String> subList = new ArrayList<String>();
			subList.addAll(staticFacesList);
			FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, this, fag);
			gridview.setAdapter(mGvAdapter);
			WindowManager manager = getWindowManager();
			Display display = manager.getDefaultDisplay();
			@SuppressWarnings("deprecation")
			int width = display.getWidth();
			gridview.setColumnWidth(width % 3);
			gridview.setNumColumns(3);
			// gridview.setColumnWidth(60);
			gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					switch (position) {
					case 0:
						new AlertDialog.Builder(ChatMainActivity.this)
						.setTitle("添加图片")
						.setPositiveButton("相册",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										openAlbum();
									}
								})
						.setNegativeButton("拍照",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										openAlbum();
									}
								}).create().show();
						break;
					case 1:

						break;
					case 2:

						break;

					}
				}
			});
		}
		return gridview;
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
					new ImageSpan(ChatMainActivity.this, BitmapFactory
							.decodeStream(getAssets().open(png))), sb.length()
							- tempText.length(), sb.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	/**
	 * 向输入框里添加表情
	 * */
	private void insert(CharSequence text) {
		int iCursorStart = Selection.getSelectionStart((input.getText()));
		int iCursorEnd = Selection.getSelectionEnd((input.getText()));
		if (iCursorStart != iCursorEnd) {
			((Editable) input.getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((input.getText()));
		((Editable) input.getText()).insert(iCursor, text);
	}

	/**
	 * 删除图标执行事件
	 * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
	 * */
	private void delete() {
		if (input.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(input.getText());
			int iCursorStart = Selection.getSelectionStart(input.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(iCursorEnd)) {
						String st = "#[face/png/f_static_000.png]#";
						((Editable) input.getText()).delete(
								iCursorEnd - st.length(), iCursorEnd);
					} else {
						((Editable) input.getText()).delete(iCursorEnd - 1,
								iCursorEnd);
					}
				} else {
					((Editable) input.getText()).delete(iCursorStart,
							iCursorEnd);
				}
			}
		}
	}

	/**
	 * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
	 * **/
	private boolean isDeletePng(int cursor) {
		String st = "#[face/png/f_static_000.png]#";
		String content = input.getText().toString().substring(0, cursor);
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

	private ImageView dotsItem(int position) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dot_image, null);
		ImageView mb = (ImageView) layout.findViewById(R.id.face_dot);
		mb.setId(position);
		return mb;
	}

	private int getPagerCount(int fag) {
		int count = staticFacesList.size();
		if (fag == 1) {

			return count % (columns * rows - 1) == 0 ? count
					/ (columns * rows - 1) : count / (columns * rows - 1) + 1;
		} else if (fag == 2) {
			return 2;
		} else if (fag == 3) {
			return 1;
		}
		return 0;

	}

	private void initStaticFaces(int fag) {
		try {
			if (fag == 1) {
				staticFacesList = new ArrayList<String>();
				String[] faces = getAssets().list("face/png");
				for (int i = 0; i < faces.length; i++) {
					staticFacesList.add(faces[i]);
				}
				staticFacesList.remove("emotion_del_normal.png");
			} else if (fag == 2) {
				staticFacesList = new ArrayList<String>();
				String[] faces = getAssets().list("faceer/png");
				for (int i = 0; i < faces.length; i++) {
					staticFacesList.add(faces[i]);
				}
			} else if (fag == 3) {
				staticFacesList = new ArrayList<String>();
				String[] faces = getAssets().list("myface/png");
				for (int i = 0; i < faces.length; i++) {
					staticFacesList.add(faces[i]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 表情页改变时，dots效果也要跟着改变
	 * */
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

	private ChatInfo getChatInfoTo(String pullname, String message, String fag,
			String falg, String time) {
		ChatInfo info = new ChatInfo();
		info.content = message;
		info.pullname = pullname;
		info.falg = falg;
		// time=data;
		info.time = time;
		if (fag.equals("")) {
			info.fromOrTo = 0;
		} else {
			info.fromOrTo = Integer.parseInt(fag);
		}
		return info;
	}

	@SuppressWarnings("rawtypes")
	public class sortClass implements Comparator {
		public int compare(Object arg0, Object arg1) {
			ChatInfo user0 = (ChatInfo) arg0;
			ChatInfo user1 = (ChatInfo) arg1;
			int flag = user0.time.compareTo(user1.time);
			return flag;
		}
	}

	private String chatName = "";
	boolean hasflag = false;
	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			XmppMsgBean bean = (XmppMsgBean) intent
					.getSerializableExtra("body");
			String name = bean.getFromUserId();
			String content = bean.getContent();
			String time = bean.getTimeSend();
			bean.setReaded(true);
			try {
				Dao<XmppMsgBean, Integer> messageDao = DataHelper.getInstance(
						ChatMainActivity.this).getXmppMsgDAO();
				messageDao.update(bean); // 修改该好友的收到的的消息为已读
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (name.equals(chatName)) {
				infos.add(getChatInfoTo("", content, 1 + "", bean.getType(),
						time));
				mLvAdapter.notifyDataSetChanged();
				mListView.setSelection(infos.size() - 1);
			}
		}
	};

	/**
	 * 绑定XMPP服务 实现开始
	 */
	public XmppService mXmppService;
	ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mXmppService = null;
		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			mXmppService = ((XmppService.XmppBinder) service).getService();
		}
	};

	/**
	 * 解绑服务
	 */
	private void unbindXMPPService() {
		try {
			unbindService(mServiceConnection);
		} catch (IllegalArgumentException e) {
			Log.e("unbindXMPPService", "Service wasn't bound!");
		}
	}

	/**
	 * 绑定XMPP服务 实现结束
	 */

	/**
	 * 绑定服务
	 */
	private void bindXMPPService() {
		Intent mServiceIntent = new Intent(this, XmppService.class);
		bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver); // 取消receiver
		unregisterReceiver(receiverCast);
		unregisterReceiver(reciverSoundSend);
		unbindXMPPService();
		super.onDestroy();
	}

	public String getMessgeBody(String type, String content, String toUserId,
			String fromUserId) {
		String timeSend = GlobalUtil.getLocalDate();
		String fileSize = "0";
		String timeLen = "0";
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", type);
			obj.put("timeSend", timeSend);
			obj.put("content", content);
			obj.put("fileSize", fileSize);
			obj.put("toUserId", toUserId);
			obj.put("fromUserId", fromUserId);
			obj.put("timeLen", timeLen);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

}
