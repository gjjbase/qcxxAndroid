package com.yale.qcxxandroid;

import java.io.File;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

public class RegditActivity1 extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private TextView sexValue;
	private EditText nc, passWord, valPassWord;
	private Button regdit;
	Timer timer = new Timer();
	AlertDialog alert;
	private ImageView tx;
	private static String picFileFullName;
	private static final String tag = "MainActivity";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	static Bitmap bm;
	private ThreadUtil thread;
	private String base;

	private void ini() {
		thread = new ThreadUtil(handler);
		int tag = 0;
		if (sexValue.getText().toString().equals("男")) {
			tag = 0;
		} else if (sexValue.getText().toString().equals("女")) {
			tag = 1;
		} else {
			tag = 2;
		}
		String jsonParamStr = "[{'phoneNum':"
				+ getIntent().getExtras().getString("phoneNum") + ",'headImg':"
				+ base + ",'passWord':" + valPassWord.getText().toString()
				+ ",'sex':" + tag + ",'nickName':" + "'"
				+ nc.getText().toString() + "'" + "}]";

		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'saveUserInfo'}]";
		thread.doStartWebServicerequestWebService(RegditActivity1.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	@SuppressWarnings("unused")
	Handler handler = new Handler() {
		public void handlerMessage(Message msg) throws JSONException {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				if (returnJson.equals(Globals.RETURN_STR_TRUE)) {
					bundle.putBoolean("true", true);
					bundle.putString("name", nc.getText().toString());
					bundle.putString("psw", passWord.getText().toString());
					intent.setClass(RegditActivity1.this, LoginActivity.class)
							.putExtras(bundle);
					startActivity(intent);
				}

				break;
			case 2:
				toast("注册失败", getApplicationContext());
				break;

			}
		}
	};

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
			Log.e(tag, "请确认已经插入SD卡");
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
				Log.e(tag, "获取图片成功，path=" + picFileFullName);
				toast("获取图片成功，path=" + picFileFullName, getApplicationContext());
				ImageTools.readBitmapAutoSize(bm, picFileFullName, tx);
				Log.e(tag, ImageTools.bitmap64(picFileFullName));
				base = ImageTools.bitmap64(picFileFullName);
			} else if (resultCode == RESULT_CANCELED) {
				// 用户取消了图像捕获
			} else {
				// 图像捕获失败，提示用户
				Log.e(tag, "拍照失败");
			}
		} else if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				if (uri != null) {
					String realPath = getRealPathFromURI(uri);
					Log.e(tag, "获取图片成功，path=" + realPath);
					toast("获取图片成功，path=" + realPath, getApplicationContext());
					ImageTools.readBitmapAutoSize(bm, realPath, tx);
					Log.e(tag, ImageTools.bitmap64(realPath));
					base = ImageTools.bitmap64(realPath);
				} else {
					Log.e(tag, "从相册获取图片失败");
				}
			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			// Do not call Cursor.close() on a cursor obtained using this
			// method,
			// because the activity will do that for you at the appropriate time
			@SuppressWarnings("deprecation")
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regdit1_activity);
		nc = (EditText) findViewById(R.id.nc);
		passWord = (EditText) findViewById(R.id.passWord);
		valPassWord = (EditText) findViewById(R.id.valPassWord);
		sexValue = (TextView) findViewById(R.id.sexValue);
		regdit = (Button) findViewById(R.id.regdit);
		tx = (ImageView) findViewById(R.id.tx);
		regdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("####################", nc.getText().toString()
						+ passWord.getText().toString());
				if (!StringUtils.isEmpty(nc.getText().toString())
						&& !StringUtils.isEmpty(passWord.getText().toString())
						&& !StringUtils.isEmpty(valPassWord.getText()
								.toString())
						&& valPassWord.getText().toString()
								.equals(passWord.getText().toString())
						&& !StringUtils.isEmpty(sexValue.getText().toString())) {
					ini();
				} else {
					if (!passWord.getText().toString()
							.equals(valPassWord.getText().toString())) {
						Toast.makeText(getApplicationContext(), "2次输入的密码不一致",
								3000).show();
					} else {
						Toast.makeText(getApplicationContext(), "请输入完整的信息",
								3000).show();

					}
				}

			}

		});
		tx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new AlertDialog.Builder(RegditActivity1.this)
						.setTitle("添加图片")
						.setPositiveButton("相册",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										openAlbum();
									}
								})
						.setNegativeButton("拍照",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										takePicture();

									}
								}).create();
				dialog.show();
			}
		});
	}

	public void regPreClick(View view) {
		this.finish();
	}

	private void txt_arr(TextView[] view, int falg) {
		for (int i = 0; i < view.length; i++) {
			view[i].setTextColor(getResources().getColor(R.color.textcol));
		}
		view[falg].setTextColor(Color.RED);
	}

	public void alert() {
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
		// LinearLayout view = (LinearLayout) getLayoutInflater().inflate(
		// R.layout.popwind, null);
		final TextView txt_exit = (TextView) alert.findViewById(R.id.txt_exit);
		final TextView txt1 = (TextView) alert.findViewById(R.id.txt1);
		final TextView txt2 = (TextView) alert.findViewById(R.id.txt2);
		final TextView[] txt = { txt1, txt2, txt_exit };

		txt_exit.setText("男");
		txt1.setText("女");
		txt2.setText("保密");
		txt_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_arr(txt, 2);
				sexValue.setText(txt_exit.getText().toString());
			}
		});

		txt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt_arr(txt, 0);
				sexValue.setText(txt1.getText().toString());
			}
		});

		txt2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				txt_arr(txt, 1);
				sexValue.setText(txt2.getText().toString());

			}
		});

	}

	public void sexChose(View view) {
		alert();
	}
}
