package com.yale.qcxxandroid.chat;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yale.qcxxandroid.base.MyBaseListView;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MyBaseListView.OnRefreshListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.InputMethodService.Insets;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	private List<View> views = new ArrayList<View>();
	private List<String> staticFacesList;
	private LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();
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
	private ImageView left, inputer;
	private int falg, fag;
	private android.support.v4.view.ViewPager grd;
	private GridView mygrd;

	public static void readBitmapAutoSize(String filePath, int outWidth,
			int outHeight, TextView jpgView) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
					outHeight);
			bm = BitmapFactory.decodeStream(bs, null, options);

			// jpgView.setImageBitmap(bm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// readBitmapAutoSize(picList.get(position).getPicUrl(), 100, 100,
	// imger);
	public String Bitmap2Base64(Bitmap bitmap) {
		try {
			// 先将bitmap转换为普通的字节数组
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			byte[] buffer = out.toByteArray();
			// 将普通字节数组转换为base64数组
			String encode = Base64.encodeToString(buffer, Base64.DEFAULT);
			// // string = Base64.encodeToString(bytes, Base64.DEFAULT);
			return encode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 将文件路径转化为Bitmap string 类型
	public String bitmap64(String filepath) {
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		Bitmap bt = null;
		String encode = null;
		BitmapFactory.Options options = null;
		try {
			fs = new FileInputStream(filepath);
			bs = new BufferedInputStream(fs);
			options = setBitmapOption(filepath, 100, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bt = BitmapFactory.decodeStream(bs, null, options);
		try {
			// 先将bitmap转换为普通的字节数组
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			bt.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			byte[] buffer = out.toByteArray();
			// 将普通字节数组转换为base64数组
			encode = Base64.encodeToString(buffer, Base64.DEFAULT);
			// // string = Base64.encodeToString(bytes, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			bs.close();
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encode;

	}

	public static Bitmap BitmapAutoSize(String filePath) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		BitmapFactory.Options options = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			options = setBitmapOption(filePath, 100, 100);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return BitmapFactory.decodeStream(bs, null, options);
	}

	private static BitmapFactory.Options setBitmapOption(String file,
			int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		BitmapFactory.decodeFile(file, opt);

		int outWidth = opt.outWidth; // 获得图片的实际高和宽
		int outHeight = opt.outHeight;
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 1;
		// 设置缩放比,1表示原比例，2表示原来的四分之一....
		// 计算缩放比
		if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
			int sampleSize = (outWidth / width + outHeight / height) / 2;
			opt.inSampleSize = sampleSize;
		}

		opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
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
					//
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
					true, ""));
		} else if (sp.getInt("falger", 3) == 0) {
			infos.add(getChatInfoTo(picFileFullName, "", num.getText()
					.toString(), true, ""));
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

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	private void initViews() {
		mListView = (MyBaseListView) findViewById(R.id.message_chat_listview);
		// chat = new ChatInfo();
		// chat.fromOrTo=0;
		// infos.add(chat);getExtras();

		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "19871028"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "0", false, "19871027"));
		infos.add(getChatInfoTo("", "青春秀秀", "1", false, "19871026"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "19871025"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "0", false, "19871024"));
		infos.add(getChatInfoTo("", "青春秀秀", "1", false, "19871023"));
		infos.add(getChatInfoTo("", "武汉亦鸟科技", "1", false, "19871022"));
		mLvAdapter = new ChatLVAdapter(this, infos);
		mListView.setAdapter(mLvAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener() {

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
							infos.add(getChatInfoTo("", "佳园路1", "1", false,
									"19870423"));
							infos.add(getChatInfoTo("", "佳园路2", "0", false,
									"19870826"));
							infos.add(getChatInfoTo("", "佳园路3", "1", false,
									"19870925"));
							infos.add(getChatInfoTo("", "佳园路4", "1", false,
									"19870724"));
							infos.add(getChatInfoTo("", "佳园路5", "0", false,
									"19870621"));
							infos.add(getChatInfoTo("", "佳园路6", "1", false,
									"19870522"));
							sortClass sort = new sortClass();
							Collections.sort(infos, sort);
							for (int i = 0; i < infos.size(); i++) {
								ChatInfo temp = (ChatInfo) infos.get(i);
								System.out.println("姓名:" + temp.pullname
										+ ",生日:" + temp.content);
							}
							mLvAdapter.notifyDataSetChanged();
							// editor.putBoolean("false",true);
							// editor.commit()
							// }

							mListView.setSelection(infos.size());
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mListView.onRefreshComplete();
					}
				}.execute();
			}
		});
		mViewPager = (ViewPager) findViewById(R.id.face_viewpager);
		image_face = (ImageView) findViewById(R.id.image_face);
		num = (EditText) findViewById(R.id.num);
		txt_name = (TextView) findViewById(R.id.txt_name);
		txt_back = (TextView) findViewById(R.id.txt_back);
		txt1 = (TextView) findViewById(R.id.txt1);
		txt2 = (TextView) findViewById(R.id.txt2);
		grd = (ViewPager) findViewById(R.id.grd);
		mygrd = (GridView) findViewById(R.id.mygrd);
		final List<Integer> list = new ArrayList<Integer>();
		list.add(R.drawable.photo);
		BaseAdapter adapter = new BaseAdapter() {

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				// TODO Auto-generated method stub
				arg1 = LayoutInflater.from(getApplicationContext()).inflate(
						R.layout.item, null);
				ImageView img = (ImageView) arg1.findViewById(R.id.imger);
				img.setBackgroundResource(list.get(arg0));
				return arg1;
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}
		};
		mygrd.setAdapter(adapter);
		mygrd.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					Dialog dialog = new AlertDialog.Builder(
							ChatMainActivity.this)
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
									}).create();
					dialog.show();
					break;

				}
			}
		});
		txt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt1.setTextColor(getResources().getColor(R.color.white));
				txt1.setBackgroundResource(R.drawable.mbtn);
				txt2.setTextColor(getResources().getColor(R.color.black));
				txt2.setBackgroundResource(R.drawable.copyofmbtnr);
				mDotsLayout.setVisibility(View.VISIBLE);
				mViewPager.setVisibility(View.VISIBLE);
				mygrd.setVisibility(View.GONE);
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
				grd.setVisibility(View.GONE);
				mDotsLayout.setVisibility(View.GONE);
				mViewPager.setVisibility(View.GONE);
				mygrd.setVisibility(View.VISIBLE);
			}
		});
		txt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
				mygrd.setVisibility(View.GONE);
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(ChatMainActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				if (tag % 2 == 0) {
					mViewPager.setVisibility(View.GONE);
					mDotsLayout.setVisibility(View.GONE);
					rel_hint.setVisibility(View.GONE);
				} else {
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
		InitViewPager();
	}

	/*
	 * 初始表情 *
	 */
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
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (input.getText().toString().equals("")) {
					img_adder.setAlpha(255);
					send.setVisibility(View.GONE);
				} else {
					img_adder.setAlpha(0);
					send.setVisibility(View.VISIBLE);
				}
				// try {
				// if (count == "#[face/png/f_static_000.png]#".length()) {
				// String tempText = s.subSequence(start, start +
				// count).toString();
				// String regex =
				// "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
				// Pattern p = Pattern.compile(regex);
				// Matcher m = p.matcher(tempText);
				// if (m.matches()) {
				// SpannableStringBuilder sb = new SpannableStringBuilder(
				// input.getText());
				// String png = tempText.substring("#[".length(),
				// tempText.length() - "]#".length());
				// sb.setSpan(
				// new ImageSpan(MainActivity.this,
				// BitmapFactory
				// .decodeStream(getAssets()
				// .open(png))),
				// start, start + count,
				// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				// }
				// }
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
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
		input.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				input.setFocusable(true);

				input.setFocusableInTouchMode(true);

				input.requestFocus();

				InputMethodManager inputManager =

				(InputMethodManager) input.getContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);

				inputManager.showSoftInput(input, 0);
				return false;
			}
		});

		send.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(input.getText().toString())) {
					infos.add(getChatInfoTo("", input.getText().toString(), num
							.getText().toString(), false, ""));
					mLvAdapter.setList(infos);
					mLvAdapter.notifyDataSetChanged();
					mListView.setSelection(infos.size() - 1);
					input.setText("");
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
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
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

	private String formatTime(int t) {
		return t >= 10 ? "" + t : "0" + t;// 三元运算符 t>10时取 ""+t
	}

	public void time() {
		Calendar c = Calendar.getInstance();

		String time = c.get(Calendar.YEAR) + "-" + // 得到年
				formatTime(c.get(Calendar.MONTH) + 1) + "-" + // month加一 //月
				formatTime(c.get(Calendar.DAY_OF_MONTH)) + " " + // 日
				formatTime(c.get(Calendar.HOUR_OF_DAY)) + ":" + // 时
				formatTime(c.get(Calendar.MINUTE)) + ":" + // 分
				formatTime(c.get(Calendar.SECOND)); // 秒
		System.out.println(time); // 输出

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

	@SuppressLint("HandlerLeak")
	// private Handler mHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	// mLvAdapter.setList(infos);
	// mLvAdapter.notifyDataSetChanged();
	// mListView.onRefreshCompleteHeader();
	// break;
	// }
	// }
	// };
	// ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
	// (ChatActivity.this.getCurrentFocus().getWindowToken(),
	// InputMethodManager.HIDE_NOT_ALWAYS);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);
		img_adder = (ImageView) findViewById(R.id.img_adder);
		left = (ImageView) findViewById(R.id.left);
		inputer = (ImageView) findViewById(R.id.inputer);
		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				falg++;
				if (falg % 2 == 0) {
					left.setImageResource(R.drawable.img_chat);
					inputer.setVisibility(View.VISIBLE);
					input.setVisibility(View.GONE);
					mygrd.setVisibility(View.GONE);
				} else {
					left.setImageResource(R.drawable.img_yuying);
					inputer.setVisibility(View.GONE);
					mygrd.setVisibility(View.GONE);
					input.setVisibility(View.VISIBLE);
				}

			}
		});
		inputer.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "说话", 3000).show();
				return false;
			}
		});
		img_adder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rel_hint.setVisibility(View.GONE);
				mDotsLayout.setVisibility(View.GONE);
				mViewPager.setVisibility(View.GONE);
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(ChatMainActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				fag++;
				if (fag % 2 == 0) {
					mygrd.setVisibility(View.GONE);
				} else {
					mygrd.setVisibility(View.VISIBLE);
				}

				// TODO Auto-generated method stub

			}
		});
		initStaticFaces();
		initViews();
		mListView.setSelection(infos.size());
		// final TextView gifTextView = (TextView) findViewById(R.id.text);
		// SpannableStringBuilder sb = new SpannableStringBuilder();
		// sb.append("Text followed by animated gif: ");
		// String dummyText = "dummy";
		// sb.append(dummyText);
		// try {
		//
		// sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(
		// getAssets().open("face/gif/f031.gif"),
		// new AnimatedGifDrawable.UpdateListener() {
		// @Override
		// public void update() {
		// gifTextView.postInvalidate();
		// }
		// })), sb.length() - dummyText.length(), sb.length(),
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// sb.append(dummyText);
		// try {
		// sb.setSpan(
		// new ImageSpan(this, BitmapFactory.decodeStream(getAssets()
		// .open("face/f007.png"))), sb.length()
		// - dummyText.length(), sb.length(),
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// gifTextView.setText(sb);
		//
		// LinearLayout container = (LinearLayout) findViewById(R.id.container);
		// LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// ImageView ig = new ImageView(this);
		// ig.setLayoutParams(params);
		// try {
		// Bitmap mBitmap = BitmapFactory.decodeStream(getAssets().open(
		// "face/f007.png"));
		// ig.setImageBitmap(mBitmap);
		// container.addView(ig);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// Face face = new Face();
		// String[] staticFaces = face.getStaticFaces(this);
		// String[] dynamicFaces = face.getDynamicFaces(this);
		// System.out.println(staticFaces.length);
		// System.out.println(Arrays.toString(staticFaces));
		// System.out.println(dynamicFaces.length);
		// System.out.println(Arrays.toString(dynamicFaces));
	}

	@SuppressWarnings("rawtypes")
	class sortClass implements Comparator {
		public int compare(Object arg0, Object arg1) {
			// LinkedList<ChatInfo> infos = new LinkedList<ChatInfo>();
			// ChatInfo info = new ChatInfo();
			ChatInfo user0 = (ChatInfo) arg0;
			ChatInfo user1 = (ChatInfo) arg1;
			int flag = user0.time.compareTo(user1.time);
			return flag;
		}
	}
}
