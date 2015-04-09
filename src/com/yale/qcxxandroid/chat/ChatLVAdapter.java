package com.yale.qcxxandroid.chat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.boyaa.speech.SpeechController;
import com.boyaa.speech.SpeechListener;
import com.nineoldandroids.view.ViewHelper;
import com.yale.qcxxandroid.ImageTools;
import com.yale.qcxxandroid.R;
import com.yale.qcxxandroid.chat.xmpp.XmppGlobals;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ChatLVAdapter extends BaseAdapter {
	private Context mContext;
	private List<ChatInfo> list;
	private PopupWindow popupWindow;

	private TextView copy, delete;

	private LayoutInflater inflater;
	static Bitmap bm;
	/**
	 */
	protected long mAnimationTime = 150;
	
	

	public ChatLVAdapter(Context mContext, List<ChatInfo> list) {
		super();
		this.mContext = mContext;
		this.list = list;
		inflater = LayoutInflater.from(mContext);
		initPopWindow();
	}

	public void setList(List<ChatInfo> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler hodler;
		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.chat_lv_item, null);
			hodler.fromContainer = (ViewGroup) convertView
					.findViewById(R.id.chart_from_container);
			hodler.toContainer = (ViewGroup) convertView
					.findViewById(R.id.chart_to_container);

			hodler.fromContent = (TextView) convertView
					.findViewById(R.id.chatfrom_content);
			hodler.img_hint = (ImageView) convertView
					.findViewById(R.id.img_hint);
			hodler.img_otherhint = (ImageView) convertView
					.findViewById(R.id.img_otherhint);
			hodler.toContent = (TextView) convertView
					.findViewById(R.id.chatto_content);

			hodler.time = (TextView) convertView.findViewById(R.id.chat_time);
			convertView.setTag(hodler);
		} else {
			hodler = (ViewHodler) convertView.getTag();
		}
		// =1接受消息=0发送消息 to 发送消息 from接受消息
		if (list.get(position).fromOrTo == 1) {
			hodler.toContainer.setVisibility(View.GONE);
			hodler.fromContainer.setVisibility(View.VISIBLE);
			if (list.get(position).falg.equals(XmppGlobals.MessageType.picture)) {
				hodler.fromContent.setVisibility(View.GONE);
				hodler.img_otherhint.setVisibility(View.VISIBLE);
				hodler.img_otherhint.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, "放大", 3000).show();
					}
				});
				ImageTools.readBitmapAutoSize(bm, list.get(position).pullname,
						hodler.img_otherhint);
			} else if(list.get(position).falg.equals(XmppGlobals.MessageType.text)){
				hodler.img_otherhint.setVisibility(View.GONE);
				hodler.fromContent.setVisibility(View.VISIBLE);
				SpannableStringBuilder sb = handler(1,hodler.fromContent,
						list.get(position).content);
				hodler.fromContent.setText(sb);
				hodler.time.setText(list.get(position).time);
			}else if(list.get(position).falg.equals(XmppGlobals.MessageType.sound)){
				hodler.img_otherhint.setVisibility(View.GONE);
				hodler.fromContent.setVisibility(View.VISIBLE);
				/*SpannableStringBuilder sb = handler(hodler.fromContent,
						list.get(position).content);*/
				String path = list.get(position).content;
				hodler.fromContent.setText("语音");
				hodler.fromContent.setOnClickListener(new SoundOnclickListener(mContext,path));
				hodler.time.setText(list.get(position).time);
			}else if(list.get(position).falg.equals(XmppGlobals.MessageType.Big_face)){
				hodler.img_otherhint.setVisibility(View.GONE);
				hodler.fromContent.setVisibility(View.VISIBLE);
				SpannableStringBuilder sb = handler(2,hodler.fromContent,
						list.get(position).content);
				hodler.fromContent.setText(sb);
				hodler.time.setText(list.get(position).time);
			}
			// }
		} else {

			hodler.fromContainer.setVisibility(View.GONE);
			hodler.toContainer.setVisibility(View.VISIBLE);
			// if (falg == 1) {
			//
			// hodler.fromContainer.setVisibility(View.GONE);
			// hodler.img_hint.setVisibility(View.VISIBLE);
			if (list.get(position).falg.equals(XmppGlobals.MessageType.picture)) {
				hodler.img_hint.setVisibility(View.VISIBLE);
				hodler.img_hint.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, "放大", 3000).show();
					}
				});
				hodler.toContent.setVisibility(View.GONE);
				ImageTools.readBitmapAutoSize(bm, list.get(position).pullname,
						hodler.img_hint);
			} else if(list.get(position).falg.equals(XmppGlobals.MessageType.text)){
				hodler.toContent.setVisibility(View.VISIBLE);
				hodler.img_hint.setVisibility(View.GONE);
				SpannableStringBuilder sb = handler(1,hodler.toContent,
						list.get(position).content);
				hodler.toContent.setText(sb);
				hodler.time.setText(list.get(position).time);
			}else if(list.get(position).falg.equals(XmppGlobals.MessageType.sound)){
				hodler.toContent.setVisibility(View.VISIBLE);
				hodler.img_hint.setVisibility(View.GONE);
				String path = list.get(position).content;
				hodler.toContent.setText("语音");
				hodler.toContent.setOnClickListener(new SoundOnclickListener(mContext,path));
				hodler.time.setText(list.get(position).time);
			}else if(list.get(position).falg.equals(XmppGlobals.MessageType.Big_face)){	//大表情
				hodler.toContent.setVisibility(View.VISIBLE);
				hodler.img_hint.setVisibility(View.GONE);
				SpannableStringBuilder sb = handler(2,hodler.toContent,
						list.get(position).content);
				hodler.toContent.setText(sb);
				hodler.time.setText(list.get(position).time);
			}
			
		}
		/*hodler.fromContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});*/
		/*hodler.toContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});*/

		hodler.fromContent.setOnLongClickListener(new popAction(convertView,
				position, list.get(position).fromOrTo));
		hodler.toContent.setOnLongClickListener(new popAction(convertView,
				position, list.get(position).fromOrTo));
		return convertView;
	}
	
	private SpannableStringBuilder handler(int type,final TextView gifTextView,
			String content) {
		String regex;	//表情对应的正则
		String staticTxt;	//表情对应的静态字段
		String path ;	//表情对应的地址
		if(type == 1){	//小表情
			regex =  "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
			staticTxt = "#[face/png/f_static_";
			path = "face/gif/f";
		}else{	//大表情
			regex = "(\\#\\[faceer/png/f_static_)\\d{3}(.png\\]\\#)";
			staticTxt = "#[faceer/png/f_static_";
			path = "faceer/gif/f";
		}
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String num = tempText.substring(
						staticTxt.length(), tempText.length()
								- ".png]#".length());
				String gif =  path+ num + ".gif";
				/**
				 * */
				InputStream is = mContext.getAssets().open(gif);
				sb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is,
						new AnimatedGifDrawable.UpdateListener() {
							@Override
							public void update() {
								gifTextView.postInvalidate();
							}
						})), m.start(), m.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				is.close();
			} catch (Exception e) {
				String png = tempText.substring("#[".length(),
						tempText.length() - "]#".length());
				try {
					sb.setSpan(
							new ImageSpan(mContext, BitmapFactory
									.decodeStream(mContext.getAssets()
											.open(png))), m.start(), m.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return sb;
	}

	class ViewHodler {
		ImageView fromIcon, toIcon, img_hint, img_otherhint;
		TextView fromContent, toContent, time;
		ViewGroup fromContainer, toContainer;
	}

	/**
	 * */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	/**
	 * */
	private void initPopWindow() {
		View popView = inflater.inflate(R.layout.chat_item_copy_delete_menu,
				null);
		copy = (TextView) popView.findViewById(R.id.chat_copy_menu);
		delete = (TextView) popView.findViewById(R.id.chat_delete_menu);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		// popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
	}

	/**
	 * */
	public void showPop(View parent, int x, int y, final View view,
			final int position, final int fromOrTo) {
		popupWindow.showAtLocation(parent, 0, x, y);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		copy.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				ClipboardManager cm = (ClipboardManager) mContext
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(list.get(position).content);
			}
		});
		delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
				if (fromOrTo == 0) {
					// from
					leftRemoveAnimation(view, position);
				} else if (fromOrTo == 1) {
					// to
					rightRemoveAnimation(view, position);
				}

				// list.remove(position);
				// notifyDataSetChanged();
			}
		});
		popupWindow.update();
		if (popupWindow.isShowing()) {

		}
	}

	/**
	 * */
	public class popAction implements OnLongClickListener {
		int position;
		View view;
		int fromOrTo;

		public popAction(View view, int position, int fromOrTo) {
			this.position = position;
			this.view = view;
			this.fromOrTo = fromOrTo;
		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			int[] arrayOfInt = new int[2];
			v.getLocationOnScreen(arrayOfInt);
			int x = arrayOfInt[0];
			int y = arrayOfInt[1];
			// System.out.println("x: " + x + " y:" + y + " w: " +
			// v.getMeasuredWidth() + " h: " + v.getMeasuredHeight() );
			showPop(v, x, y, view, position, fromOrTo);
			return true;
		}
	}

	/**
	 * */
	private void rightRemoveAnimation(final View view, final int position) {
		final Animation animation = (Animation) AnimationUtils.loadAnimation(
				mContext, R.drawable.chatto_remove_anim);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				view.setAlpha(0);
				performDismiss(view, position);
				animation.cancel();
			}
		});

		view.startAnimation(animation);
	}

	/**
	 * */
	private void leftRemoveAnimation(final View view, final int position) {
		final Animation animation = (Animation) AnimationUtils.loadAnimation(
				mContext, R.drawable.chatfrom_remove_anim);
		animation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				view.setAlpha(0);
				performDismiss(view, position);
				animation.cancel();
			}
		});

		view.startAnimation(animation);
	}

	/**
	 * 
	 * @param dismissView
	 * @param dismissPosition
	 */
	private void performDismiss(final View dismissView,
			final int dismissPosition) {
		final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
		final int originalHeight = dismissView.getHeight();

		ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 0)
				.setDuration(mAnimationTime);
		animator.start();

		animator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				list.remove(dismissPosition);
				notifyDataSetChanged();
				ViewHelper.setAlpha(dismissView, 1f);
				ViewHelper.setTranslationX(dismissView, 0);
				ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
				lp.height = originalHeight;
				dismissView.setLayoutParams(lp);
			}
		});

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				lp.height = (Integer) valueAnimator.getAnimatedValue();
				dismissView.setLayoutParams(lp);
			}
		});

	}
	
	private class SoundOnclickListener implements OnClickListener{
		private String path;
		private Context context;
		private int mSampleRate = 8000;	//播放的速度
		SpeechController mSpeechController;
		public SoundOnclickListener(Context context,String path) {
			this.context = context;
			this.path = path;
		}
		
		@Override
		public void onClick(View view) {
			String tempFileName = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+path;
			mSpeechController = SpeechController.getInstance();
			mSpeechController.setDebug(true);
			mSpeechController.setRecordingMaxTime(25);
			if (TextUtils.isEmpty(tempFileName)) {
				return;
			}
			FileInputStream fileInputStream;
			try {
				File file = new File(tempFileName);
				if(file.exists()){
					fileInputStream = new FileInputStream(file);
					mSpeechController.play(fileInputStream, mSampleRate, tempFileName);
				}else{
					Toast.makeText(mContext, "文件已损坏", 2000).show();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
//				throw new IllegalStateException("input file not found", e);
			}
//			messageTextView.setText("正在播放....");
		}
	}
	

}
