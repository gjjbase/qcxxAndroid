package com.yale.qcxxandroid;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yale.qcxxandroid.base.BaseActivity;
import com.yale.qcxxandroid.base.BaseListView;
import com.yale.qcxxandroid.chat.ChatMainActivity;
import com.yale.qcxxandroid.util.ThreadUtil;

public class YouthActivity extends BaseActivity {
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private BaseListView listView;
	private ListView searchList;
	private MsgAdapter msgAdapter;
	private SearchAdapter searchAdapter;
	private LinearLayout xxhd, qc_qun, qc_ad, bottom, lin_top;
	RelativeLayout shade;
	private ImageView add;
	private List<JSONObject> jsonList = new ArrayList<JSONObject>();
	private List<JSONObject> searchListObj = new ArrayList<JSONObject>();;
	private EditText search;
	private LinearLayout title;
	private TextView cancel;
	private LinearLayout lin_bg;
	private ThreadUtil thread;
	private LayoutTransition mTransitioner;

	private void resetTransition() {
		mTransitioner = new LayoutTransition();
		title.setLayoutTransition(mTransitioner);
	}

	private void initAnim() {
		mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
		mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
		setupCustomAnimations();
		long duration;
		duration = 3000;
		mTransitioner.setDuration(duration);
	}

	// 设置自定义动画的方法
	private void setupCustomAnimations() {
		// Changing while Adding
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0,
				1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",
				0, 1);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
				1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
				1f, 0f, 1f);
		final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
				this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX,
				pvhScaleY).setDuration(
				mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
		changeIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// Changing while Removing
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
				"rotation", kf0, kf1, kf2);
		final ObjectAnimator changeOut = ObjectAnimator
				.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight,
						pvhBottom, pvhRotation)
				.setDuration(
						mTransitioner
								.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
				changeOut);
		changeOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotation(0f);
			}
		});

		// Adding
		ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f,
				0f).setDuration(
				mTransitioner.getDuration(LayoutTransition.APPEARING));
		mTransitioner.setAnimator(LayoutTransition.APPEARING, animIn);
		animIn.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		// Removing
		ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f,
				90f).setDuration(
				mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
		mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
		animOut.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});

	}

	Handler handlerSlideImg = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			int cl = b.getInt("cl");
			if (cl == 0) {
				msgAdapter.notifyDataSetChanged();
			}
		}
	};

	// private void initData() {
	// jsonList.clear();
	// searchListObj.clear();
	// try {
	// JSONObject tmp = new JSONObject();
	// tmp.put("nc", "王玮");
	// tmp.put("content", "在？");
	// tmp.put("pubtime", "5分钟前");
	// jsonList.add(tmp);
	// JSONObject tmp1 = new JSONObject();
	// tmp1.put("nc", "陈路");
	// tmp1.put("content", "收到回复");
	// tmp1.put("pubtime", "6分钟前");
	// jsonList.add(tmp1);
	// JSONObject tmp2 = new JSONObject();
	// tmp2.put("nc", "邱老师");
	// tmp2.put("content", "一起出来吃个饭吧");
	// tmp2.put("pubtime", "7分钟前");
	// jsonList.add(tmp2);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// msgAdapter = new MsgAdapter(YouthActivity.this, jsonList);
	// msgAdapter.setOnDeleteListioner(YouthActivity.this);
	// listView.setAdapter(msgAdapter);
	// try {
	// JSONObject tmp = new JSONObject();
	// tmp.put("name", "华中科技大学计算机学院");
	// searchListObj.add(tmp);
	// JSONObject tmp1 = new JSONObject();
	// tmp1.put("name", "武汉大学传媒系");
	// searchListObj.add(tmp1);
	// JSONObject tmp2 = new JSONObject();
	// tmp2.put("name", "KJ2010班");
	// searchListObj.add(tmp2);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// searchAdapter = new SearchAdapter(YouthActivity.this, searchListObj);
	// }
	public void init() {
		Handler handler = new Handler() {
			public void handerMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					// jsonList =
					// (List<JSONObject>)msg.getData().getSerializable("jsonObject");
					// JSONArray jsonArr =
					// JSONArray.parseArray(jsonList.get("ad").toString());
					// String[] slideImg = new String[jsonArr.size()];
					// ImgLoadThread imgLoadThread = new ImgLoadThread(slideImg,
					// handlerSlideImg);
					// imgLoadThread.start();
					break;
				case 1:

					break;

				}
			}
		};
		thread = new ThreadUtil(handler);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.youth_activity);
		search = (EditText) findViewById(R.id.search);
		cancel = (TextView) findViewById(R.id.cancel);
		title = (LinearLayout) findViewById(R.id.title);
		listView = (BaseListView) findViewById(R.id.qcList);
		searchList = (ListView) findViewById(R.id.searchList);
		lin_bg = (LinearLayout) findViewById(R.id.lin_bg);
		xxhd = (LinearLayout) findViewById(R.id.xxhd);
		bottom = (LinearLayout) findViewById(R.id.bottom);
		lin_top = (LinearLayout) findViewById(R.id.lin_top);
		shade = (RelativeLayout) findViewById(R.id.shade);
		listView.setDeleteListioner(this);
		listView.setSingleTapUpListenner(this);
		// 声明动画
		resetTransition();

		// 初始化动画
		initAnim();
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// try {
				TextView nc = (TextView) arg1.findViewById(R.id.nc);
				bundle.putString("name", nc.getText().toString());
				// bundle.putString("name",
				// jsonList.get(position).getString("nc"));
				intent.setClass(YouthActivity.this, ChatMainActivity.class)
						.putExtras(bundle);
				startActivity(intent);
				// } catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}
		});
		searchList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(YouthActivity.this,
						MyClasslActivityDepar.class);
				startActivity(intent);
			}
		});
		search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!arg0.isFocusable()) {
					showSearch();
				}
			}
		});
		search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@SuppressLint("NewApi")
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					initData();
					shade.setBackgroundColor(getResources().getColor(
							R.color.white));
					// shade.setAlpha(0.5f);
					searchList.setVisibility(View.VISIBLE);
					bottom.setVisibility(View.GONE);
					lin_top.setVisibility(View.GONE);
					searchList.setAdapter(searchAdapter);
					return true;
				}
				return false;
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hideSearch();
			}
		});

		xxhd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(YouthActivity.this,
						YouthXxhdActivity.class);
				startActivity(intent);
			}
		});
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件 
		View view = inflater.inflate(R.layout.popupwindow, null);
		// 创建PopupWindow对象 
		final PopupWindow pop = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		add = (ImageView) findViewById(R.id.add);
		// 需要设置一下此参数，点击外边可消失 
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失 
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		pop.setFocusable(true);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pop.isShowing()) {
					// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
					pop.dismiss();
				} else {
					// 显示窗口 
					pop.showAsDropDown(v, 0, 20);
				}
			}
		});
		qc_qun = (LinearLayout) view.findViewById(R.id.qc_qun);
		qc_qun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pop.dismiss();
				Intent intent = new Intent(YouthActivity.this,
						YouthStartQunActivity.class);
				startActivity(intent);
			}
		});
		// qc_add = (LinearLayout) view.findViewById(R.id.qc_add);
		// qc_add.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// pop.dismiss();
		// Intent intent = new Intent(YouthActivity.this,
		// YouthAddFriendActivity.class);
		// startActivity(intent);
		// overridePendingTransition(R.anim.push_up_in, 0);
		// }
		// });
		initData();
	}

	@SuppressLint("NewApi")
	private void hideSearch() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(search, InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
		// lin_bg.setBackgroundColor(R.color.white);
		// search.setBackground(getResources().getDrawable(R.drawable.qcediters));
		title.setVisibility(View.VISIBLE);
		bottom.setVisibility(View.GONE);
		lin_top.setVisibility(View.GONE);
		cancel.setVisibility(View.GONE);
		shade.setVisibility(View.GONE);
		search.setGravity(Gravity.CENTER);
		bottom.setVisibility(View.GONE);
		lin_top.setVisibility(View.GONE);
		search.setText("");
		search.setFocusable(false);
		searchListObj.clear();
		// shade.setAlpha(0.5f);
	}

	private void showSearch() {
		search.setFocusable(true);
		search.setFocusableInTouchMode(true);
		search.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) search
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(search, 0);
		shade.setVisibility(View.VISIBLE);
		shade.setBackgroundColor(getResources().getColor(R.color.white));
		bottom.setVisibility(View.VISIBLE);
		lin_top.setVisibility(View.VISIBLE);
		// lin_bg.setBackgroundColor(R.color.bg_color);
		// search.setBackground(getResources().getDrawable(R.drawable.qcediters));
		/*
		 * TranslateAnimation translateAnimation = new
		 * TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
		 * Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
		 * Animation.RELATIVE_TO_SELF, -1.0f);
		 * translateAnimation.setFillAfter(true);
		 * translateAnimation.setDuration(500);
		 * title.startAnimation(translateAnimation);
		 */
		title.setVisibility(View.GONE);
		cancel.setVisibility(View.VISIBLE);
		search.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		// shade.setAlpha(0.5f);
	}

	private void initData() {
		jsonList.clear();
		searchListObj.clear();
		try {
			JSONObject tmp = new JSONObject();
			tmp.put("nc", "王玮");
			tmp.put("content", "在？");
			tmp.put("pubtime", "5分钟前");
			jsonList.add(tmp);
			JSONObject tmp1 = new JSONObject();
			tmp1.put("nc", "陈路");
			tmp1.put("content", "收到回复");
			tmp1.put("pubtime", "6分钟前");
			jsonList.add(tmp1);
			JSONObject tmp2 = new JSONObject();
			tmp2.put("nc", "邱老师");
			tmp2.put("content", "一起出来吃个饭吧");
			tmp2.put("pubtime", "7分钟前");
			jsonList.add(tmp2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		msgAdapter = new MsgAdapter(YouthActivity.this, jsonList);
		msgAdapter.setOnDeleteListioner(YouthActivity.this);
		listView.setAdapter(msgAdapter);
		try {
			JSONObject tmp = new JSONObject();
			tmp.put("name", "华中科技大学计算机学院");
			searchListObj.add(tmp);
			JSONObject tmp1 = new JSONObject();
			tmp1.put("name", "武汉大学传媒系");
			searchListObj.add(tmp1);
			JSONObject tmp2 = new JSONObject();
			tmp2.put("name", "KJ2010班");
			searchListObj.add(tmp2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		searchAdapter = new SearchAdapter(YouthActivity.this, searchListObj);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					YouthActivity.this);
			builder.setMessage("你确定退出吗？")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									MyActivityManager.getInstance().exit();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

		return super.onKeyDown(keyCode, event);
	}

	public void yougebi(View v) {
		bundle.putInt("data", 1);
		intent.setClass(YouthActivity.this, MyClassActivityitem.class)
				.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onClick(int whichButton) {
		switch (whichButton) {
		case 0:
			jsonList.remove(delID);
			listView.deleteItem();
			msgAdapter.notifyDataSetChanged();
			break;
		case 1:
			break;
		default:
			break;
		}
	}
}
