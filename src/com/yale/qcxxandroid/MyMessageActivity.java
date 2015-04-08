package com.yale.qcxxandroid;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.model.CityModel;
import kankan.wheel.widget.model.DistrictModel;
import kankan.wheel.widget.model.ProvinceModel;
import kankan.wheel.widget.model.XmlParserHandler;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.dick.Dialoger;
import com.yale.qcxxandroid.util.ThreadUtil;

public class MyMessageActivity extends BaseActivity implements
		kankan.wheel.widget.OnWheelChangedListener {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private ThreadUtil thread;
	private RelativeLayout rel_time, rel_photo, rel_sex, rel_lov, rel_nick,
			rel_qianming, rel_renick, rel_phone, rel_disan, luck;
	private ImageView img_tuxiang;
	private static String picFileFullName;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	AlertDialog alert;
	static Bitmap bm;
	private TextView marriage, birthday, back, txt_hot, txt_qianming, sign,
			verity_name, txt_renick, txt_phone, txt_disan, constellation;
	private int year, month, day;
	private kankan.wheel.widget.WheelView mViewProvince, mViewCity,
			mViewDistrict;
	private Dialoger dag = null;
	kankan.wheel.widget.WheelView mViewYear;
	private String currYear;
	protected String[] mProvinceDatas, mYearDatas;
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/** key - 市 values - 学校 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前学校的名称
	 */
	protected String mCurrentSchoolName = "";

	private TextView nick_name;

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
				ImageTools.readBitmapAutoSize(bm, picFileFullName, img_tuxiang);

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
					String realPath = getRealPathFromURI(uri);
					// Log.e(tag, "获取图片成功，path="+realPath);
					// toast("获取图片成功，path=" + realPath);
					//
					ImageTools.readBitmapAutoSize(bm, realPath, img_tuxiang);
				} else {
					// Log.e(tag, "从相册获取图片失败");
				}
			}
		}
		alert.dismiss();
	}

	/**
	 * This method is used to get real path of file from from uri<br/>
	 * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-
	 * captured-from-camera
	 * 
	 * @param contentUri
	 * @return String
	 */
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

	@SuppressLint("InlinedApi")
	@SuppressWarnings("deprecation")
	public void alert(int falg) {
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
				}
			});
			txt2.setText("相册");
			txt2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 1);
					openAlbum();
				}
			});
			break;
		case 1:
			txt_exit.setText("男");
			txt2.setText("保密");
			txt1.setText("女");
			txt_exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 2);
				}
			});

			txt1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 0);
				}
			});

			txt2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 1);
				}
			});
			break;
		case 2:
			txt_exit.setText("单身中");
			txt1.setText("恋爱中");
			txt2.setText("保密");
			txt_exit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 2);
					marriage.setText(txt_exit.getText().toString());
				}
			});

			txt1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					txt_arr(txt, 0);
					marriage.setText(txt1.getText().toString());
				}
			});

			txt2.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					txt_arr(txt, 1);
					marriage.setText(txt2.getText().toString());
				}
			});
			break;

		}
	}

	private void txt_arr(TextView[] view, int falg) {
		for (int i = 0; i < view.length; i++) {
			view[i].setTextColor(getResources().getColor(R.color.textcol));
		}
		view[falg].setTextColor(Color.RED);
	}

	public void childactivit(TextView v) {
		bundle.putString("chile", v.getText().toString());
		intent.setClass(MyMessageActivity.this, MychildeActivity.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentSchoolName = districtList.get(0).getName();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				MyMessageActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(5);
		mViewCity.setVisibleItems(5);
		mViewDistrict.setVisibleItems(5);
		updateCities();
		updateAreas();
	}

	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		} else {
			mCurrentCityName = cities[0];
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		} else {
			mCurrentSchoolName = areas[0];
		}
		mViewDistrict
				.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	private void poper() {
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		@SuppressWarnings("deprecation")
		int width = display.getWidth();
		alert = new AlertDialog.Builder(this).create();
		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.getWindow().setGravity(Gravity.BOTTOM);
		alert.show();
		alert.getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
		alert.getWindow().setContentView(R.layout.popup_select3);
		mViewProvince = (kankan.wheel.widget.WheelView) alert
				.findViewById(R.id.id_province);
		mViewCity = (kankan.wheel.widget.WheelView) alert
				.findViewById(R.id.id_city);
		mViewDistrict = (kankan.wheel.widget.WheelView) alert
				.findViewById(R.id.id_district);
		TextView mBtnCancle = (TextView) alert.findViewById(R.id.btn_cancle);
		TextView mBtnConfirm = (TextView) alert.findViewById(R.id.btn_confirm);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txt_disan.setText(mCurrentProviceName + mCurrentCityName
						+ mCurrentSchoolName);
				alert.dismiss();
			}
		});
		mBtnCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
			}
		});
		setUpListener();
		setUpData();
	}

	private void setYearData() {
		// 添加change事件
		mViewYear.addChangingListener(this);
		mYearDatas = new String[] { "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座",
				"天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座" };
		currYear = "";
		mViewYear.setViewAdapter(new ArrayWheelAdapter<String>(
				MyMessageActivity.this, mYearDatas));
		// 设置可见条目数量
		mViewYear.setVisibleItems(5);
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
					JSONArray joA = new JSONArray(returnJson);
					JSONObject jo = joA.getJSONObject(0);
					nick_name.setText(jo.getString("nickName"));
					verity_name.setText(jo.getString("verityName"));
					sign.setText(jo.getString("sign"));
					birthday.setText(jo.getString("birthday"));
				} catch (Exception e) {
					// TODO: handle exception
				}

				break;
			case 2:
				Log.i("@@@@@@@@@@@", "网络连接失败");
				break;

			}
		}
	};

	public void init() {

		thread = new ThreadUtil(mhandler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.member.impl.UserInfoSessionBean':'getUserInfo'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "") + "}]";
		thread.doStartWebServicerequestWebService(MyMessageActivity.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_activity);
		MyActivityManager.getInstance().addActivity(MyMessageActivity.this);
		rel_time = (RelativeLayout) findViewById(R.id.rel_time);
		rel_photo = (RelativeLayout) findViewById(R.id.rel_photo);
		rel_sex = (RelativeLayout) findViewById(R.id.rel_sex);
		rel_lov = (RelativeLayout) findViewById(R.id.rel_lov);
		rel_nick = (RelativeLayout) findViewById(R.id.rel_nick);
		constellation = (TextView) findViewById(R.id.constellation);
		verity_name = (TextView) findViewById(R.id.verity_name);
		sign = (TextView) findViewById(R.id.sign);

		luck = (RelativeLayout) findViewById(R.id.luck);
		WindowManager manager = getWindowManager();
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		LayoutInflater inflater1 = LayoutInflater.from(this);
		// 引入窗口配置文件 
		View viewYear = inflater1.inflate(R.layout.popup_select1, null);
		// 创建PopupWindow对象 
		final PopupWindow popYear = new PopupWindow(viewYear, width,
				LayoutParams.WRAP_CONTENT, true);
		// 需要设置一下此参数，点击外边可消失 
		popYear.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失 
		popYear.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		popYear.setFocusable(true);

		mViewYear = (WheelView) viewYear.findViewById(R.id.id_province);
		TextView btn_confirm = (TextView) viewYear
				.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				constellation.setText(currYear);
				popYear.dismiss();
			}
		});
		luck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popYear.isShowing()) {
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					popYear.dismiss();
				} else {
					// 显示窗口 
					popYear.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				}
			}
		});
		setYearData();
		rel_qianming = (RelativeLayout) findViewById(R.id.rel_qianming);
		rel_renick = (RelativeLayout) findViewById(R.id.rel_renick);
		rel_phone = (RelativeLayout) findViewById(R.id.rel_phone);
		rel_disan = (RelativeLayout) findViewById(R.id.rel_disan);
		txt_disan = (TextView) findViewById(R.id.txt_inschool);
		nick_name = (TextView) findViewById(R.id.nick_name);
		rel_disan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				poper();
			}
		});
		txt_qianming = (TextView) findViewById(R.id.txt_qianming);
		txt_renick = (TextView) findViewById(R.id.txt_renick);
		txt_phone = (TextView) findViewById(R.id.txt_phone);
		rel_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				childactivit(txt_phone);
			}
		});
		rel_renick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				childactivit(txt_renick);
			}
		});
		rel_qianming.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				childactivit(txt_qianming);
			}
		});
		txt_hot = (TextView) findViewById(R.id.txt_hot);
		rel_nick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				childactivit(txt_hot);
			}
		});
		img_tuxiang = (ImageView) findViewById(R.id.img_tuxiang);
		marriage = (TextView) findViewById(R.id.marriage);
		birthday = (TextView) findViewById(R.id.birthday);
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		birthday.setText(String.valueOf(year) + String.valueOf(month)
				+ String.valueOf(day));
		rel_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WindowManager manager = getWindowManager();
				Display display = manager.getDefaultDisplay();
				int width = display.getWidth();
				dag = new Dialoger(MyMessageActivity.this);
				dag.getWindow().setGravity(Gravity.BOTTOM);
				dag.getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
				dag.show();
				// dialg();
				// intent.setClass(MyActivity.this, Dialoger.class);
				// startActivity(intent);
			}
		});
		rel_lov.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert(2);
			}
		});
		rel_sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert(1);
			}
		});
		rel_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert(0);
			}
		});
		init();
	}

	@Override
	public void onChanged(kankan.wheel.widget.WheelView wheel, int oldValue,
			int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentSchoolName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
		} else if (wheel == mViewYear) {
			currYear = mYearDatas[newValue];
		}
	}

}
