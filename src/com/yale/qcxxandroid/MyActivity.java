package com.yale.qcxxandroid;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.ZhiTiaoTabActivity;
import com.yale.qcxxandroid.util.GlobalUtil;
import com.yale.qcxxandroid.util.Globals;
import com.yale.qcxxandroid.util.ThreadUtil;

public class MyActivity extends BaseActivity {
	private RelativeLayout rel_mesg, rel_xiuxiu;
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private ThreadUtil thread;
	private TextView nickname, id;
	private ImageView head8;
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
					nickname.setText(jo.getString("nickName"));
					id.setText("秀秀号:" + jo.getString("xxNum"));
					Bitmap bitmap = GlobalUtil.getBitmapFromMemCache(jo.get(
							"headImg").toString());
					if (bitmap != null) {

					} else {
						bitmap = BitmapFactory.decodeResource(
								getApplicationContext().getResources(),
								R.drawable.im_chenxing);
					}
					head8.setScaleType(ScaleType.FIT_XY);
					head8.setImageBitmap(bitmap);
					// ImgLoadThread imgLoadThread = new
					// ImgLoadThread(slideImg,handlerSlideImg);
					// imgLoadThread.start();
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
		String methodStr = "[{'" + Globals.MEMBER_SESSIOM
				+ ".UserInfoSessionBean':'getUserInfo'}]";
		String jsonParamStr = "[{'userId':" + sp.getString("userId", "") + "}]";
		thread.doStartWebServicerequestWebService(MyActivity.this,
				jsonParamStr, methodStr, true);
	}

	public void myshezhi(View v) {
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.setClass(MyActivity.this, SettingActivity.class);
		startActivity(intent);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		PackageManager pm = getPackageManager();
		ResolveInfo homeInfo = pm.resolveActivity(
				new Intent(Intent.ACTION_MAIN)
						.addCategory(Intent.CATEGORY_HOME), 0);
		editor.putInt("intent", 3);
		editor.commit();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityInfo ai = homeInfo.activityInfo;
			Intent startIntent = new Intent(Intent.ACTION_MAIN);
			startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			startIntent
					.setComponent(new ComponentName(ai.packageName, ai.name));
			startActivitySafely(startIntent);
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	public void myzhitiao(View v) {
		intent.setClass(MyActivity.this, ZhiTiaoTabActivity.class);
		startActivity(intent);
	}

	public void myjifeng(View v) {
		// ScorePlazaActivity
		intent.setClass(MyActivity.this, ScorePlazaActivity.class);
		startActivity(intent);
	}

	public void myqianbao(View v) {
		intent.setClass(MyActivity.this, MoneyActivity.class);
		startActivity(intent);

	}

	public void myshoucan(View v) {
		intent.setClass(MyActivity.this, CollectionActivity.class);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(), "收藏", 3000).show();
	}

	public void onchenxing(View v) {
		intent.setClass(MyActivity.this, SinActivity.class);
		startActivity(intent);
	}

	public void Markonclik(View v) {
		intent.setClass(MyActivity.this, MarkActivity.class);
		startActivity(intent);
	}

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymessage);
		rel_mesg = (RelativeLayout) findViewById(R.id.rel_mesg);
		MyActivityManager.getInstance().addActivity(MyActivity.this);
		rel_xiuxiu = (RelativeLayout) findViewById(R.id.rel_xiuxiu);
		nickname = (TextView) findViewById(R.id.nickname);
		id = (TextView) findViewById(R.id.id);
		head8 = (ImageView) findViewById(R.id.head8);
		rel_xiuxiu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				bundle.putString("name", "我的秀秀");
				intent.setClass(MyActivity.this, MyShowListActivity.class)
						.putExtras(bundle);
				startActivity(intent);
			}
		});
		rel_mesg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(MyActivity.this, MyMessageActivity.class);
				startActivity(intent);
			}
		});
		init();
	}
}
