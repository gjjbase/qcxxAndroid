package com.yale.qcxxandroid.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.yale.qcxxandroid.R;

public class FlippingLoadingDialog extends Dialog {

	private FlippingImageView mFivIcon;
	private HandyTextView mHtvText;
	private String mText;

	public FlippingLoadingDialog(Context context, String text) {
		super(context, R.style.MyDialogStyle);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mText = text;
		this.setContentView(R.layout.common_flipping_loading_diloag);
		init();
	}

	private void init() {

		// setContentView(R.layout.common_flipping_loading_diloag);

		// this.setView(R.layout.common_flipping_loading_diloag, 0, 0, 0, 0);
		mFivIcon = (FlippingImageView) findViewById(R.id.loadingdialog_fiv_icon);
		mHtvText = (HandyTextView) findViewById(R.id.loadingdialog_htv_text);
		mFivIcon.startAnimation();
		mHtvText.setText(mText);
	}

	public void setText(String text) {
		mText = text;
		mHtvText.setText(mText);
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}
}
