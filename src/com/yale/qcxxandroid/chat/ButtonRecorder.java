package com.yale.qcxxandroid.chat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.boyaa.speech.SpeechController;
import com.boyaa.speech.SpeechListener;
import com.boyaa.speech.util.FileUtil;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.chat.xmpp.XmppGlobals;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class ButtonRecorder extends Button {
	// *录音相关 开始*/
	private SpeechController mSpeechController = null; // speech控制器
	@SuppressWarnings("unused")
	private int mSampleRate = 8000; // 质量
	private final static String fileDir = "/qcxx/";
	private String soundUrl = "";
	private String fileName; // 中专文件文件名
	// *录音相关 结束*/
	private Context context;
	float point_down_x, point_down_y; // 手指触碰的位置
	static final double move_stop = 100.0; // 取消录音移动的位置
	long startTime;
	private static int SumTime = 0;

	ImageView imgview; // 显示图片
	Dialog dialog; // 显示图片的窗口
	UiThread uit; // 根据时间来修改UI的线程
	private boolean flag = true; // 确保取消操作只触发一次！
	private volatile static int voiceValue = 0; // 录制的声音大小

	public ButtonRecorder(Context context) {
		super(context);
		this.context = context;
	}

	public ButtonRecorder(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	boolean sendflag = true;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			sendflag = true;
			System.out.println("按下录音按键");
			soundUrl = "";
			SumTime = 0;
			point_down_x = event.getX(); // 获取当前触碰到的点在屏幕坐标系中的绝对位置的X坐标
			point_down_y = event.getY(); // 获取当前触碰到的点在屏幕坐标系中的绝对位置的Y坐标
			// 开始录音
			startSpeech();
			break;
		case MotionEvent.ACTION_MOVE:
			float move_y = event.getY(); // 获取当前移动到的点在屏幕坐标系中的绝对位置的Y坐标
			if (point_down_y - move_y > move_stop && flag) { // 如果移动距离超过设定的移动距离，取消录音
				sendflag = false;
				// Toast.makeText(getContext(), "取消",
				// Toast.LENGTH_SHORT).show();
				// 取消录音
				// stopRecorder();
				// SumTime = 0;
				// voiceValue = 0;
			}
			break;
		case MotionEvent.ACTION_UP:
			// 发送录音
			stopRecorder();// 结束录制
			// 发送语音 发送一个广播到chatMainActivity
			if (sendflag) {
				if (SumTime <= 1) {
					Toast.makeText(context, "录音时间太短！", 1000).show();
					deleteFile();
				} else {
					Intent intent = new Intent(XmppGlobals.ACTION_RECORDER_SEND);
					intent.putExtra("fileName", soundUrl);
					context.sendBroadcast(intent);
				}
			} else {
				Toast.makeText(getContext(), "确定取消录音！", Toast.LENGTH_SHORT)
						.show();
				deleteFile();
			}
			SumTime = 0;
			voiceValue = 0;
			break;
		default:
			break;
		}
		return true;
	}

	// 结束录制
	private void stopRecorder() {
		if (dialog != null)
			dialog.dismiss();
		if (uit.isAlive())
			uit.stopFlag();
		dialog = null;
		flag = false;
		if (mSpeechController != null)
			mSpeechController.stopRecord();
	}

	// 开始录音的初始化动作
	private void startSpeech() {
		startTime = System.currentTimeMillis();
		/** 界面操作開始 **/

		dialog = new Dialog(context, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.my_dialog);
		imgview = (ImageView) dialog.findViewById(R.id.dialog_img);
		dialog.show();

		/*
		 * dialog = new Dialog(context, R.style.like_toast_dialog_style);
		 * imgview = new ImageView(context);
		 * imgview.setImageResource(R.drawable.mic_2);
		 * dialog.setContentView(imgview, new LayoutParams(
		 * ViewGroup.LayoutParams.MATCH_PARENT,
		 * ViewGroup.LayoutParams.MATCH_PARENT)); LayoutParams lp =
		 * dialog.getWindow().getAttributes(); lp.gravity = Gravity.CENTER;
		 */
		/** 界面操作結束 **/
		// dialog.show();
		uit = new UiThread(mHandler);
		uit.start();
		flag = true; // 设置开始标志为true 当取消的时候改变该标志，该标志由true到false完成一次录音全过程！
		initSpeech();
	}

	/**
	 * 操作UI的线程
	 */
	private class UiThread extends Thread {
		private Handler mHandler;
		private volatile boolean flag;

		public UiThread(Handler mHandler) {
			this.mHandler = mHandler;
			flag = true;
		}

		public void stopFlag() {
			flag = false;
		}

		int i = 0;

		@Override
		public void run() {
			while (flag) {
				try {
					mHandler.sendEmptyMessage(0);
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	int[] drawables = new int[] { R.drawable.mic_2, R.drawable.mic_3,
			R.drawable.mic_4, R.drawable.mic_5 };
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// int i = msg.what;
			// imgview.setImageResource(drawables[i]);
			setDialogImage();
		};
	};

	// 开始录音 实例操作
	@SuppressWarnings("static-access")
	private void initSpeech() {
		if (mSpeechController == null) {
			mSpeechController = SpeechController.getInstance();
			mSpeechController.setDebug(true);
			mSpeechController.setRecordingMaxTime(25);
			mSpeechController.setSpeechListener(new SpeechListener() {
				@Override
				public void timeConsuming(int type, int secondCount, Object tag) {
					Log.d("CDH", "SpeechListener secondCount:" + secondCount);
					SumTime = secondCount;
					if (type == SpeechListener.RECORDING) {
						// setMessage("正在录音（" + secondCount + "秒）....");
					} else if (type == SpeechListener.PLAYING) {
						// setMessage("正在播放（" + secondCount + "秒）....");
					}
				}

				@Override
				public void recordOver(int sampleRate) {
					Log.d("CDH", "SpeechListener recordOver(" + sampleRate
							+ ")");
					mSampleRate = sampleRate;
					// setMessage("录音结束");
				}

				@Override
				public void playOver(Object tag) {
					Log.d("CDH", "SpeechListener playOver(" + tag + ")");
					// setMessage("播放结束");
				}

				@Override
				public void recordingVolume(int volume) {
					voiceValue = volume;
					Log.i("CDH", "SpeechListener recordVolume(" + volume + ")");
				}

			});
		}
		// 开始录音
		fileName = FileUtil.getRandomFileName();
		SharedPreferences spf = context.getSharedPreferences("Speech",
				context.MODE_PRIVATE);
		String fn = spf.getString("file_name", null);
		if (TextUtils.isEmpty(fn)) {
			spf.edit().putString("file_name", fileName); // 临时存储filename 用于后面的播放
		}
		String str = fileName;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			String sdDir = Environment.getExternalStorageDirectory()
					.getAbsolutePath().toString()
					+ fileDir;// 获取跟目录
			str = System.currentTimeMillis() + ".qcxx";
			fileName = sdDir + str;
			soundUrl = fileDir + str;
			File filedir = new File(sdDir);
			if (!filedir.exists()) {
				filedir.mkdirs();
			}

		}
		// 建议语音文件存放在外存（SD卡），因为内置存储一般空间不大
		FileOutputStream fileOutputStream;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			fileOutputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new IllegalStateException("output file not found", e);
		}
		mSpeechController.startRecord(fileOutputStream);
	}

	// 删除录制的文件
	private void deleteFile() {
		// TODO Auto-generated method stub
		Toast.makeText(context, "删除文件！", 2000).show();
	}

	void setDialogImage() {
		if (voiceValue == 0) {
			imgview.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue == 1) {
			imgview.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue == 2) {
			imgview.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue == 3) {
			imgview.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue == 4) {
			imgview.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue == 5) {
			imgview.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue == 6) {
			imgview.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue == 7) {
			imgview.setImageResource(R.drawable.record_animate_14);
		}
		voiceValue = 0;
	}
}
