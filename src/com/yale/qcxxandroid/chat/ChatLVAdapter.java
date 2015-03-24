package com.yale.qcxxandroid.chat;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nineoldandroids.view.ViewHelper;
import com.yale.qcxxandroid.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
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

	@Override
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
			// if (falg == 1) {
			// hodler.fromContainer.setVisibility(View.GONE);
			// hodler.img_otherhint.setVisibility(View.VISIBLE);
			// hodler.img_otherhint.setImageResource(R.drawable.demo5);
			if (list.get(position).falg == true) {
				hodler.fromContent.setVisibility(View.GONE);
				hodler.img_otherhint.setVisibility(View.VISIBLE);
				hodler.img_otherhint.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, "放大", 3000).show();
					}
				});
				readBitmapAutoSize(list.get(position).pullname, 100, 100,
						hodler.img_otherhint);
			} else {
				hodler.img_otherhint.setVisibility(View.GONE);
				hodler.fromContent.setVisibility(View.VISIBLE);
				SpannableStringBuilder sb = handler(hodler.fromContent,
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
			if (list.get(position).falg == true) {
				hodler.img_hint.setVisibility(View.VISIBLE);
				hodler.img_hint.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, "放大", 3000).show();
					}
				});
				hodler.toContent.setVisibility(View.GONE);
				readBitmapAutoSize(list.get(position).pullname, 100, 100,
						hodler.img_hint);
			} else {
				hodler.toContent.setVisibility(View.VISIBLE);
				hodler.img_hint.setVisibility(View.GONE);
				SpannableStringBuilder sb = handler(hodler.toContent,
						list.get(position).content);
				hodler.toContent.setText(sb);
				hodler.time.setText(list.get(position).time);
			}
			// // if(share.getInt("falger", 1)==true){
			// //
			// // }
			// // readBitmapAutoSize("", 100, 100, hodler.img_hint);
			// }
		}
		hodler.fromContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		hodler.toContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		hodler.fromContent.setOnLongClickListener(new popAction(convertView,
				position, list.get(position).fromOrTo));
		hodler.toContent.setOnLongClickListener(new popAction(convertView,
				position, list.get(position).fromOrTo));
		return convertView;
	}

	private SpannableStringBuilder handler(final TextView gifTextView,
			String content) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
			try {
				String num = tempText.substring(
						"#[face/png/f_static_".length(), tempText.length()
								- ".png]#".length());
				String gif = "face/gif/f" + num + ".gif";
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

}
