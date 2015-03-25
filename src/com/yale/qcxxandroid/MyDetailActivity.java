package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.CircularImage;
import com.yale.qcxxandroid.util.GlobalUtil;
import com.yale.qcxxandroid.util.ImgLoadThread;
import com.yale.qcxxandroid.util.ThreadUtil;

public class MyDetailActivity extends BaseActivity {
	Intent intent = new Intent();
	Bundle bundle;
	TextView nc;
	TextView nl;
	TextView xz;
	TextView sg;
	TextView dl;
	Button back;
	TextView qm;
	TextView showvideocount;
	TextView showpiccount;
	TextView rmhtcount;
	TextView twjdcount;
	TextView zh;
	TextView csny;
	TextView laqk;
	TextView szd;
	TextView dh;
	TextView ztcount;
	TextView verityName, jn, tc, ah, creditText, txt_role, nickname, sign,
			inschool;
	private RelativeLayout credit;
	private ThreadUtil thread;
	private ImageView img;
	private Bitmap bitmap;

	private void init() {
		Handler handler = new Handler() {
			@SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:

					String returnJson = (String) msg.getData().getString(
							"returnJson");
					try {
						JSONArray jso = new JSONArray(returnJson);
						JSONObject jsoo = jso.getJSONObject(0);
						creditText = (TextView) findViewById(R.id.creditText);
						creditText.setText("诚信分:"+jsoo.getInt("credit"));

						// qm.setText(bundle.getString("sign"));
						zh = (TextView) findViewById(R.id.zh);
						zh.setText(jsoo.getString("verityName"));
						csny = (TextView) findViewById(R.id.csny);
						csny.setText(jsoo.getString("birthday"));
						laqk = (TextView) findViewById(R.id.laqk);//
						laqk.setText("恋爱中");
						szd = (TextView) findViewById(R.id.szd);
						szd.setText("武汉");
						dh = (TextView) findViewById(R.id.dh);
						dh.setText(jsoo.getString("phoneNum"));
						verityName = (TextView) findViewById(R.id.verity_name);
						verityName.setText(jsoo.getString("xxNum"));
						txt_role = (TextView) findViewById(R.id.txt_role);
						txt_role.setText(jsoo.getInt("credit") + "");
						nickname = (TextView) findViewById(R.id.nickname);
						nickname.setText(jsoo.getString("nickName"));
						sign = (TextView) findViewById(R.id.sign);
						sign.setText(jsoo.getString("sign"));
						// inschool = (TextView) findViewById(R.id.inschool);
						// inschool.setText(sp.getString("inSchool", ""));
						// txt_role.setText(jsoo.getString("credit"));
						// img = (ImageView) findViewById(R.id.img);
						// img.setScaleType(ScaleType.CENTER);
						// bitmap = GlobalUtil
						// .getBitmapFromMemCache(jsoo
						// .getString("headImg"));
						// List<String> lisimg = new ArrayList<String>();
						// lisimg.add(jsoo.getString("headImg").toString());
						// if (bitmap != null) {
						//
						// } else {
						// bitmap = BitmapFactory.decodeResource(
						// getApplicationContext().getResources(),
						// R.drawable.logo);
						// }

						// Handler handlerSlideImg = new Handler() {
						// @SuppressWarnings("unused")
						// public void handlerMessage(Message msg) {
						// super.handleMessage(msg);
						// img.setImageBitmap(bitmap);
						// getLayoutInflater().inflate(
						// R.layout.mydetail_activity, null)
						// .invalidate();
						// }
						// };
						// ImgLoadThread imgLoadThread = new
						// ImgLoadThread(lisimg,
						// handlerSlideImg);
						// imgLoadThread.start();

					} catch (JSONException e) {
						e.printStackTrace();
					}

					break;
				case 2:

					break;

				}
			}
		};
		thread = new ThreadUtil(handler);
		String methodStr = "[{'com.yale.qcxx.sessionbean.member.impl.UserInfoSessionBean':'getUserInfo'}]";
		String jsonParamStr = "[{'userId':"
				+ getIntent().getExtras().getString("id") + "}]";
		// String jsonParamStr = "[{'userId':" + sp.getString("userId", "") +
		// "}]";
		thread.doStartWebServicerequestWebService(MyDetailActivity.this,
				jsonParamStr, methodStr, true);

	}

	public void chengxingonclick(View v) {

		intent.setClass(getApplicationContext(), MyCreditActivity.class);
		startActivity(intent);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydetail_activity);
		back = (Button) findViewById(R.id.back);
		int uid = getIntent().getExtras().getInt("tag");
		String st = getIntent().getExtras().getString("id");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		init();

	}

	public void viewSchoolOnclick(View ivew) {
		intent.setClass(MyDetailActivity.this, MySchoolActivity.class);
		startActivity(intent);
	}
}
