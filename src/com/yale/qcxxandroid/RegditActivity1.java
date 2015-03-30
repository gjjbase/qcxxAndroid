package com.yale.qcxxandroid;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.media.ExifInterface;
import java.io.IOException;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.MainActivity;

public class RegditActivity1 extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private TextView sexValue, loveValue;
	private EditText nc, passWord, valPassWord;
	private String ncheng, pw, valpw, sex, love;
	private Button regdit;
	Timer timer = new Timer();
	AlertDialog alert;
	private ImageView tx;
	private static String picFileFullName;
	private static final String tag = "MainActivity";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	static Bitmap bm;

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
				toast("获取图片成功，path=" + picFileFullName);
				readBitmapAutoSize(picFileFullName, 100, 100, tx);
				Log.e(tag, bitmap64(picFileFullName));

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
					toast("获取图片成功，path=" + realPath);
					readBitmapAutoSize(realPath, 100, 100, tx);
					Log.e(tag, bitmap64(realPath));

				} else {
					Log.e(tag, "从相册获取图片失败");
				}
			}
		}
	}

	public static void readBitmapAutoSize(String filePath, int outWidth,
			int outHeight, ImageView jpgView) {
		// outWidth和outHeight是目标图片的最大宽度和高度，用作限制
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
					outHeight);
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

	/**
	 * This method is used to get real path of file from from uri<br/>
	 * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-
	 * captured-from-camera
	 * 
	 * @param contentUri
	 * @return String
	 */
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

	/**
	 * 读取照片exif信息中的旋转角度<br/>
	 * http://www.eoeandroid.com/thread-196978-1-1.html
	 * 
	 * @param path
	 *            照片路径
	 * @return角度
	 */
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
		ncheng = nc.getText().toString();
		pw = passWord.getText().toString();
		valpw = valPassWord.getText().toString();
		sex = sexValue.getText().toString();
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
		// love = loveValue.getText().toString();
		regdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(ncheng) && !StringUtils.isEmpty(pw)
						&& !StringUtils.isEmpty(valpw)
						&& !StringUtils.isEmpty(sex)
						&& !StringUtils.isEmpty(love)) {
					// regdit.setEnabled(true);
					// regdit.refreshDrawableState();
					if (StringUtils.equals(valpw, pw)) {
						Toast.makeText(getApplicationContext(), "两次输入的密码不一致",
								3000).show();
					} else {

					}
				} else {
					// regdit.setEnabled(false);
					// regdit.refreshDrawableState();
					Toast.makeText(getApplicationContext(), "请填写完整的信息", 3000)
							.show();
				}

			}
		});
		nc = (EditText) this.findViewById(R.id.nc);
		nc.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});

		passWord = (EditText) this.findViewById(R.id.passWord);
		passWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});

		valPassWord = (EditText) this.findViewById(R.id.valPassWord);
		valPassWord.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});

		sexValue = (TextView) this.findViewById(R.id.sexValue);
		sexValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// commValid();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// commValid();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// commValid();
			}
		});
	}

	public void regPreClick(View view) {
		this.finish();
	}

	public void regditSub(View view) {
		intent = new Intent();
		bundle = new Bundle();
		intent.setClass(RegditActivity1.this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	public void loveChose(View view) {

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

		txt_exit.setText("单身中");
		txt1.setText("恋爱中");
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

	// public void commValid() {
	// cd = code.getText().toString();
	// ncheng = nc.getText().toString();
	// pw = passWord.getText().toString();
	// valpw = valPassWord.getText().toString();
	// sex = sexValue.getText().toString();
	// love = loveValue.getText().toString();
	// if (!StringUtils.isEmpty(cd) && !StringUtils.isEmpty(ncheng)
	// && !StringUtils.isEmpty(pw) && !StringUtils.isEmpty(valpw)
	// && !StringUtils.isEmpty(sex) && !StringUtils.isEmpty(love)) {
	// regdit.setEnabled(true);
	// regdit.refreshDrawableState();
	// } else {
	// regdit.setEnabled(false);
	// regdit.refreshDrawableState();
	// }
	// }
}
