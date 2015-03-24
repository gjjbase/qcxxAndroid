package com.yale.qcxxandroid;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.bean.PicUpload;
import com.yale.qcxxandroid.camera.ImgFileListActivity;
import com.yale.qcxxandroid.util.DataHelper;
import com.yale.qcxxandroid.util.ThreadUtil;

public class ShowPubActivity extends BaseActivity {
	Intent intent = new Intent();
	Bundle bundle = new Bundle();
	DataHelper dataHelper;
	EditText address;
	TextView txt_a, txt_b;
	LinearLayout mLayout;
	Dialog daig;
	AlertDialog alert;
	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static String picFileFullName;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final String tag = "MainActivity";
	ArrayList<String> listfile = new ArrayList<String>();
	List<PicUpload> picList;
	PicUpload picupload;
	Adapter adpter;
	static Bitmap bm;
	GridView grd;
	private TextView txt_address;
	Dao<PicUpload, Integer> picUploadDAO;
	TextView pub;
	ThreadUtil threadutil;
	SharedPreferences share;
	EditText content;
	Spinner spi;

	public static void readBitmapAutoSize(String filePath, ImageView jpgView) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath);
			bm = BitmapFactory.decodeStream(bs, null, options);
			jpgView.setImageBitmap(bm);
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
			options = setBitmapOption(filepath);
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
			encode = new String(Base64.encode(buffer, Base64.DEFAULT), "UTF-8");
			// // encode = Base64.encodeToString(bytes, Base64.DEFAULT);
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
			options = setBitmapOption(filePath);
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

	private static BitmapFactory.Options setBitmapOption(String file) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		BitmapFactory.decodeFile(file, opt);

		// int outWidth = opt.outWidth; // 获得图片的实际高和宽
		// int outHeight = opt.outHeight;
		opt.inDither = false;
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
		opt.inSampleSize = 5;
		// 设置缩放比,1表示原比例，2表示原来的四分之一....
		// 计算缩放比
		// if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
		// int sampleSize = (outWidth / width + outHeight / height) / 2;
		// opt.inSampleSize = sampleSize;
		// }

		opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
	}

	public void init() {
		threadutil = new ThreadUtil(mhandler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.show.impl.ShowsSessionBean':'saveShows'}]";
		share = getSharedPreferences("qcxx", Context.MODE_PRIVATE);
		JSONArray jso = new JSONArray();
		JSONObject jsoo = new JSONObject();
		if (picList.isEmpty() == false) {
			try {
				picList = picUploadDAO.queryForAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuffer str = new StringBuffer();
			for (PicUpload pic : picList) {
				// Log.i("+++++", pic.getPicUrl());
				System.out.println(pic.getPicToken() + "///111"
						+ pic.getPicUrl());
				// toast(pic.getPicToken() + "///111" + pic.getPicUrl());
				String base = bitmap64(pic.getPicUrl());
				str.append(base + "++yale");
				// ++DEDADARR
				// ++JOSKLASLD

				Log.i("#############", str.toString());
			}
			try {
				if (content.getText().toString().equals("")) {
					toast("说点什么吧");
				} else {
					View layou = getLayoutInflater().inflate(R.layout.img_item,
							null);
					TextView txt = (TextView) layou.findViewById(R.id.img_item);
					jsoo.put("pub_imgs", str.toString());
					Log.i("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^",
							str.toString());
					jsoo.put("user_id", share.getString("userId", ""));
					if (txt.getText().toString().equals("才艺")) {
						jsoo.put("sub_type", "0");
					} else if (txt.getText().toString().equals("特长")) {
						jsoo.put("sub_type", "1");
					} else {
						jsoo.put("sub_type", "2");
					}

					jsoo.put("pub_type", "0");// 类型
					jsoo.put("pub_content", content.getText().toString());// 文本内容
					jsoo.put("latitude",
							String.valueOf(myLocation.getLatitude()));// 经度
					jsoo.put("longitude",
							String.valueOf(myLocation.getLongitude()));// 纬度
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jso.put(jsoo);
		}
		threadutil.doStartWebServicerequestWebService(ShowPubActivity.this,
				jso.toString(), methodStr, true);

	}

	@SuppressLint("HandlerLeak")
	public Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				try {
					JSONArray jso = new JSONArray(returnJson);
					JSONObject jsoo = jso.getJSONObject(0);
					if (jsoo.getString("returnStr").equals("true")) {
						String url = getIntent().getExtras().getInt("tager")
								+ "";
						if (getIntent().getExtras().getInt("tager") == 2) {

							bundle.putString("name", "我的秀");
							intent.setClass(ShowPubActivity.this,
									MyShowListActivity.class).putExtras(bundle);
							startActivity(intent);
						} else if (getIntent().getExtras().getInt("tager") == 1) {
							bundle.putInt("data", 2);
							intent.setClass(ShowPubActivity.this,
									MyClassActivityCont.class)
									.putExtras(bundle);
							startActivity(intent);
						}
						toast("发布成功");
					} else {
						toast("发布失败");
					}
				} catch (Exception e) {
					// TODO: handle exception
					toast("发布失败");
				}

				break;
			case 2:
				toast("网络连接失败");
				break;

			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_pub_activity);
		System.out.println(myLocation.getLatitude());
		picList = new ArrayList<PicUpload>();
		grd = (GridView) findViewById(R.id.grd);
		txt_address = (TextView) findViewById(R.id.txt_address);
		spi = (Spinner) findViewById(R.id.spi);
		List<String> mItems = new ArrayList<String>();
		mItems.add("才艺");
		mItems.add("特长");
		mItems.add("爱好");
		ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,
				R.layout.img_item, mItems);
		// 绑定 Adapter到控件
		spi.setAdapter(Adapter);
		content = (EditText) findViewById(R.id.content);
		txt_address.setText("地点:" + "武汉市光谷国际佳园路");
		// txt_address.setText("地点:" + String.valueOf(myLocation.getLatitude())
		// + " " + String.valueOf(myLocation.getLongitude()));
		pub = (TextView) findViewById(R.id.pub);
		pub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				init();
			}
		});
		dataHelper = DataHelper.getInstance(this);
		picUploadDAO = dataHelper.gePicUploadDataDAO();
		adpter = new Adapter(ShowPubActivity.this, picList);
		grd.setAdapter(adpter);
		try {
			picList = picUploadDAO.queryForAll();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (picList.isEmpty() == false) {
			grd.setVisibility(View.VISIBLE);
			adpter = new Adapter(ShowPubActivity.this, picList);
			grd.setAdapter(adpter);
			adpter.notifyDataSetChanged();
		}
		grd.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				bundle = new Bundle();
				bundle.putString("file", picList.get(position).getPicUrl());
				intent.setClass(getApplicationContext(),
						ImageViewActivity.class).putExtras(bundle);
				startActivity(intent);
			}
		});
		grd.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(final AdapterView<?> parent,
					final View view, final int position, long id) {
				// TODO Auto-generated method stub
				Dialog dalg = new AlertDialog.Builder(ShowPubActivity.this)
						.setTitle("删除")
						.setMessage("确定删除此照片")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										try {
											picUploadDAO.delete(picList
													.get(position));
											picList.remove(position);

											runOnUiThread(new Runnable() {
												public void run() {
													adpter.notifyDataSetChanged();
												}
											});
											dialog.dismiss();
										} catch (SQLException e) {
											// TODO Auto-generated catch
											e.printStackTrace();
										}
									}
								})
						.setPositiveButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).create();
				dalg.show();

				return true;
			}
		});
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (bm != null && bm.isRecycled()) {
			bm.recycle();
			bm = null;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		try {
			picUploadDAO.delete(picList);
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 删除
		finish();
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

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
			Log.e("000", "请确认已经插入SD卡");
		}
	}

	private int i = 10;

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				i++;
				Log.e(tag, "获取图片成功，path=" + picFileFullName);
				// toast("获取图片成功，path=" + picFileFullName);
				picupload = new PicUpload();
				picupload.setId(i);
				picupload.setPicUrl(picFileFullName);
				picupload.setPicToken(String.valueOf(i));
				picList.add(picupload);
				try {
					picUploadDAO.create(picupload);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				adpter = new Adapter(ShowPubActivity.this, picList);
				grd.setAdapter(adpter);
				adpter.notifyDataSetChanged();
				// adpter.notifyDataSetChanged();
			} else if (resultCode == RESULT_CANCELED) {
				// 用户取消了图像捕获
			} else {
				// 图像捕获失败，提示用户
				Log.e(tag, "拍照失败");
			}
		}
	}

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void txt_arr(TextView[] view, int falg) {
		for (int i = 0; i < view.length; i++) {
			view[i].setTextColor(getResources().getColor(R.color.textcol));
		}
		view[falg].setTextColor(Color.RED);
	}

	public void alert(int falg) {
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		alert = new AlertDialog.Builder(this).create();
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.getWindow().setGravity(Gravity.BOTTOM);
		alert.show();
		alert.getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
		alert.getWindow().setContentView(R.layout.popwind);
		final TextView txt_exit = (TextView) alert.findViewById(R.id.txt_exit);
		final TextView txt1 = (TextView) alert.findViewById(R.id.txt1);
		final TextView txt2 = (TextView) alert.findViewById(R.id.txt2);
		final TextView[] txt = { txt1, txt2, txt_exit };
		switch (falg) {
		case 0:
			txt_exit.setText("取消");
			txt_exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 2);
					alert.dismiss();
				}
			});
			txt1.setText("拍照");
			txt1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 0);
					takePicture();
					alert.dismiss();
				}
			});
			txt2.setText("相册");
			txt2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 1);
					Intent intent = new Intent();
					intent.setClass(ShowPubActivity.this,
							ImgFileListActivity.class);
					startActivity(intent);
					alert.dismiss();
				}
			});
			break;
		}
	}

	public void showPicturePicker(Context context, boolean isCrop) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {
					// 类型码

					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							takePicture();
							break;
						case CHOOSE_PICTURE:
							Intent intent = new Intent();
							intent.setClass(ShowPubActivity.this,
									ImgFileListActivity.class);
							startActivity(intent);
							break;
						}
					}
				});
		builder.create().show();
	}

	public void backClick(View view) {
		this.finish();
		try {
			picUploadDAO.delete(picList);
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 删除
		finish();
	}

	class Adapter extends BaseAdapter {
		private Context context;
		private List<PicUpload> picList;

		public Adapter(Context context, List<PicUpload> picList) {
			this.context = context;
			this.picList = picList;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return picList.size() + 1;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("NewApi")
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(context).inflate(R.layout.item,
					null);
			final ImageView imger = (ImageView) convertView
					.findViewById(R.id.imger);
			if (position == picList.size()) {
				imger.setImageResource(R.drawable.photo);
				imger.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert(0);
						// showPicturePicker(ShowPubActivity.this, false);
					}
				});

			} else {
				readBitmapAutoSize(picList.get(position).getPicUrl(), imger);
			}

			return convertView;
		}
	}

	public void doShower(String url, List<PicUpload> picList, boolean fag) {
		System.out.println(myLocation.getLatitude());
		try {

			dataHelper = DataHelper.getInstance(this);
			// sqliteDemo操作
			Dao<PicUpload, Integer> picUploadDAO = dataHelper
					.gePicUploadDataDAO();
			for (int i = 1; i < 3; i++) {
				PicUpload demo = new PicUpload();
				demo.setPicToken("pic" + i);
				demo.setPicUrl(url);
				picUploadDAO.create(demo);
			}
			// 查询
			if (picList.isEmpty() == false) {
				if (fag == false) {
					picUploadDAO.delete(picList); // 删除
				} else {

				}
				picList = picUploadDAO.queryForAll();
				for (PicUpload pic : picList) {
					// Log.i("+++++", pic.getPicUrl());
					System.out.println(pic.getPicToken() + "///111"
							+ pic.getPicUrl());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}