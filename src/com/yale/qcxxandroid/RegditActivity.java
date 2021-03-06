package com.yale.qcxxandroid;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.model.CityModel;
import kankan.wheel.widget.model.DistrictModel;
import kankan.wheel.widget.model.ProvinceModel;
import kankan.wheel.widget.model.XmlParserHandler;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

public class RegditActivity extends BaseActivity implements
		OnWheelChangedListener {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private ThreadUtil thread;
	private EditText phoneNum, Num;
	private TextView schoolAndCollege, time;
	private RelativeLayout ssq, year;
	private String pN, tie, sAndC, num;
	private Button next;
	/** 所有省 */
	protected String[] mProvinceDatas;
	/** 所有年 */
	protected String[] mYearDatas;
	/** key - 省 value - 市 */
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
	/**
	 * 当前年份
	 */
	private String currYear;

	private WheelView mViewProvince, mViewYear;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView mBtnConfirm, mBtnCancle, mBtnConfirm1, mBtnCancle1,
			areaNum;
	private int TIME = 1000;
	private int mobile_code;

	private void init() {
		thread = new ThreadUtil(handler);
		mobile_code = (int) (Math.random() * 9000 + 1000);// 4228
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'sendMsg'}]";
		String jsonParamStr = "[{'tel':" + pN + ",'param':" + mobile_code
				+ "}]";
		thread.doStartWebServicerequestWebService(RegditActivity.this,
				jsonParamStr, methodStr, true);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				String returnJson = (String) msg.getData().getString(
						"returnJson");
				if (returnJson.equals(Globals.RETURN_STR_TRUE)) {
					toast("短信发送成功，请填写正确的验证码", getApplicationContext());
				} else {
					toast("发送失败", getApplicationContext());

				}

			case 2:
				toast("网络连接失败", getApplicationContext());
				break;

			}
		}
	};
	private int fag = 120;
	private Runnable runnable;

	private void timer() {
		final Handler handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				// handler自带方法实现定时器
				try {
					handler.postDelayed(this, TIME);
					areaNum.setText(Integer.toString(fag--) + "秒");
					if (fag == 0) {
						phoneNum.setEnabled(true);
						areaNum.setEnabled(true);
						areaNum.refreshDrawableState();
						areaNum.setText("发送");
						fag = 120;
						mobile_code = 0;
						handler.removeCallbacks(runnable);
					}
					System.out.println("do...");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("exception...");
				}
			}
		};
		handler.postDelayed(runnable, TIME);

	}

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regdit_activity);
		phoneNum = (EditText) this.findViewById(R.id.phoneNum);
		Num = (EditText) this.findViewById(R.id.Num);
		next = (Button) this.findViewById(R.id.next);
		phoneNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				commValdate();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		time = (TextView) this.findViewById(R.id.time);

		areaNum = (TextView) findViewById(R.id.areaNum);
		areaNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				phoneNum.setEnabled(false);
				areaNum.setEnabled(false);
				init();
				timer();
			}
		});
		ssq = (RelativeLayout) findViewById(R.id.ssq);
		schoolAndCollege = (TextView) findViewById(R.id.schoolAndCollege);
		schoolAndCollege.setText(sp.getString("sf_name", "")
				+ sp.getString("sc_name", "") + sp.getString("xy_name", ""));
		Num.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				commValdate();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件 
		View view = inflater.inflate(R.layout.popup_select3, null);
		// 创建PopupWindow对象 
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 需要设置一下此参数，点击外边可消失 
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失 
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		pop.setFocusable(true);
		ssq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.setClass(getApplicationContext(),
						DeteShooleActivity.class);
				startActivity(intent);
			}
		});
		mViewProvince = (WheelView) view.findViewById(R.id.id_province);
		mViewCity = (WheelView) view.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
		mBtnCancle = (TextView) view.findViewById(R.id.btn_cancle);
		mBtnConfirm = (TextView) view.findViewById(R.id.btn_confirm);

		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				schoolAndCollege.setText(mCurrentProviceName + mCurrentCityName
						+ mCurrentSchoolName);
				pop.dismiss();
			}
		});
		mBtnCancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});

		LayoutInflater inflater1 = LayoutInflater.from(this);
		// 引入窗口配置文件 
		View viewYear = inflater1.inflate(R.layout.popup_select1, null);
		// 创建PopupWindow对象 
		final PopupWindow popYear = new PopupWindow(viewYear,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 需要设置一下此参数，点击外边可消失 
		popYear.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失 
		popYear.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		popYear.setFocusable(true);
		year = (RelativeLayout) findViewById(R.id.year);
		year.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (popYear.isShowing()) {
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					popYear.dismiss();
				} else {
					// 显示窗口 
					popYear.showAtLocation(v, Gravity.BOTTOM, 0, 0);
				}
			}
		});
		mViewYear = (WheelView) viewYear.findViewById(R.id.id_province);
		mBtnConfirm1 = (TextView) viewYear.findViewById(R.id.btn_confirm);
		mBtnCancle1 = (TextView) viewYear.findViewById(R.id.btn_cancle);
		mBtnConfirm1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				time.setText(currYear);
				popYear.dismiss();
			}
		});
		mBtnCancle1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popYear.dismiss();
			}
		});
		setUpListener();
		setUpData();
		setYearData();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		schoolAndCollege.setText(sp.getString("sf_name", "")
				+ sp.getString("sc_name", "") + sp.getString("xy_name", ""));
		super.onResume();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		schoolAndCollege.setText(sp.getString("sf_name", "")
				+ sp.getString("sc_name", "") + sp.getString("xy_name", ""));
		super.onRestart();
	}

	public void regBackClick(View view) {
		this.finish();
	}

	public void regNext(View view) {
		bundle.putString("phoneNum", phoneNum.getText().toString());
		intent.setClass(RegditActivity.this, RegditActivity1.class).putExtras(
				bundle);
		startActivity(intent);
	}

	public void commValdate() {
		pN = phoneNum.getText().toString();
		tie = time.getText().toString();
		sAndC = schoolAndCollege.getText().toString();
		num = Num.getText().toString();
		if (!StringUtils.isEmpty(pN) && !StringUtils.isEmpty(tie)
				&& !StringUtils.isEmpty(sAndC) && pN.length() == 11) {
			areaNum.setEnabled(true);
			areaNum.refreshDrawableState();
			if (num.length() == 4 && num.equals(String.valueOf(mobile_code))) {
				// if (num.length() == 4 &&
				// num.equals(String.valueOf(mobile_code))) {
				next.setEnabled(true);
				next.refreshDrawableState();
				next.setTextColor(getResources().getColor(R.color.green));
			}

		} else {
			next.setEnabled(false);
			next.refreshDrawableState();
			areaNum.setEnabled(false);
			areaNum.refreshDrawableState();
			next.setTextColor(getResources().getColor(R.color.gray_font));

		}
	}

	/**
	 * 解析省市区的XML数据
	 * 
	 * @throws JSONException
	 */
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
		mViewYear.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
	}

	private void setYearData() {
		mYearDatas = new String[100];
		for (int i = 0; i < 100; i++) {
			mYearDatas[i] = 1970 + i + "";
		}
		currYear = 1970 + "";
		mViewYear.setViewAdapter(new ArrayWheelAdapter<String>(
				RegditActivity.this, mYearDatas));
		// 设置可见条目数量
		mViewYear.setVisibleItems(5);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				RegditActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(5);
		mViewCity.setVisibleItems(5);
		mViewDistrict.setVisibleItems(5);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
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

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
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

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
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
}
