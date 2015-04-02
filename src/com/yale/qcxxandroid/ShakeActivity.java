package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yale.qcxxandroid.ShakeListener.OnShakeListener;
import com.yale.qcxxandroid.base.AlertDialoger;
import com.yale.qcxxandroid.base.BaseLocationActivity;
import com.yale.qcxxandroid.util.RedisThreadUtil;

@SuppressLint("ShowToast")
public class ShakeActivity extends BaseLocationActivity {

	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	SoundPool sound;
	HashMap<String, Integer> spMap;
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;
	private RedisThreadUtil redisThreadUtil;
	private Intent intent;
	private Bundle bundle;
	private List<JSONObject> jsonPersons = new ArrayList<JSONObject>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_activity);
		InitSound();
		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);
		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);

		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				startAnim(); // 开始 摇一摇手掌动画
				mShakeListener.stop();
				redisThreadUtil = new RedisThreadUtil();
				redisThreadUtil.redisRequestService(0.0, 0.0, sp, 2, null);
				startVibrato(); // 开始 震动
				AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				float audioMaxVolumn = am
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				float volumnCurrent = am
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				float volumnRatio = volumnCurrent / audioMaxVolumn;
				sound.play(spMap.get("yao"), volumnRatio, volumnRatio, 1, 0, 1f);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
						float audioMaxVolumn = am
								.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
						float volumnCurrent = am
								.getStreamVolume(AudioManager.STREAM_MUSIC);
						float volumnRatio = volumnCurrent / audioMaxVolumn;
						sound.play(spMap.get("youren"), volumnRatio,
								volumnRatio, 1, 0, 1f);
						RedisThreadUtil redisThreadUtil1 = new RedisThreadUtil(
								handler);
						redisThreadUtil1.redisRequestService(0.0, 0.0, sp, 3,
								ShakeActivity.this);
					}
				}, 0);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				jsonPersons = (List<JSONObject>) msg.getData().getSerializable(
						"returnJsonPersons");
				if (jsonPersons != null && jsonPersons.size() > 0) {// 暂时先这样显示
					JSONObject jsonPerson = jsonPersons.get(0);
					String info;
					try {
						info = jsonPerson.getString("name") + "///"
								+ jsonPerson.getString("distance");
						Toast mtoast;
						mtoast = Toast.makeText(getApplicationContext(), info,
								10);
						mtoast.show();
						mVibrator.cancel();
						mShakeListener.start();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast mtoast;
					mtoast = Toast.makeText(getApplicationContext(),
							"抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", 10);
					// mtoast.setGravity(Gravity.CENTER, 0, 0);
					mtoast.show();
					mVibrator.cancel();
					mShakeListener.start();
				}
				break;
			case 2:
				Toast mtoast;
				mtoast = Toast.makeText(getApplicationContext(),
						"抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！", 10);
				// mtoast.setGravity(Gravity.CENTER, 0, 0);
				mtoast.show();
				mVibrator.cancel();
				mShakeListener.start();
				break;
			}
		}
	};

	public void commDialog() {
		intent = new Intent();
		bundle = new Bundle();
		bundle.putString("message", "数据异常");
		bundle.putString("falg", "1");
		intent.putExtras(bundle);
		intent.setClass(ShakeActivity.this, AlertDialoger.class);
		startActivity(intent);
	}

	public void InitSound() {
		sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		spMap = new HashMap<String, Integer>();
		spMap.put("yao", sound.load(this, R.raw.yao, 1));
		spMap.put("youren", sound.load(this, R.raw.youren, 1));
	}

	public void startAnim() { // 定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);
	}

	public void startVibrato() { // 定义震动
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // 第一个｛｝里面是节奏数组，
	}

	public void shake_activity_back(View v) { // 标题栏 返回按钮
		this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}
}