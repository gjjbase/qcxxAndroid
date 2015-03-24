package com.yale.qcxxandroid.base;


/**
 * 基础activity
 */
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yale.qcxxandroid.base.ActionSheet.OnActionSheetSelected;
import com.yale.qcxxandroid.bean.MyLocation;
import com.yale.qcxxandroid.util.RedisThreadUtil;

public class BaseLocationActivity extends Activity implements OnDeleteListioner,
		ListViewonSingleTapUpListenner, OnActionSheetSelected, OnCancelListener {
	protected FlippingLoadingDialog mLoadingDialog;
	protected int delID = 0;
	protected SharedPreferences sp = null;
	protected Editor editor = null;
	protected LocationClient locationClient = null;
	protected MyLocation myLocation = new MyLocation();
	private static final int UPDATE_TIME = 60000;
	private static int LOCATION_COUTNS = 0;
	private RedisThreadUtil redisThreadUtil;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("qcxx", Context.MODE_PRIVATE);
		editor = sp.edit();
		locationClient = new LocationClient(this);
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setProdName("QcxxLocation"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
		locationClient.setLocOption(option);
		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				myLocation.setLatitude(location.getLatitude());//纬度
				myLocation.setLongitude(location.getLongitude());//经度
				redisThreadUtil = new RedisThreadUtil();
				redisThreadUtil.redisRequestService(location.getLatitude(),location.getLongitude(),sp,0,null);
				location.getTime();
				location.getLocType();
				location.getRadius();
				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					location.getSpeed();
					location.getSatelliteNumber();
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					location.getAddrStr();
				}
				LOCATION_COUTNS++;
				String.valueOf(LOCATION_COUTNS);//检查位置更新次数
			}
		});
		locationClient.start();
		/*
		 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
		 * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
		 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
		 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
		 */		
		locationClient.requestLocation();
	}
	@Override
	public void onCancel(DialogInterface dialog) {
	}

	@Override
	public void onSingleTapUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCandelete(int position) {
		return true;
	}

	@Override
	public void onDelete(int ID) {
		delID = ID;
		ActionSheet.showSheet(this, this, this);
	}

	@Override
	public void onBack() {
	}

	@Override
	public void onClick(int whichButton) {
	}

	protected void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
		}
		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			return imm.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
	}
}
