package com.yale.qcxxandroid.base;

import com.yale.qcxxandroid.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class MoveTab extends LinearLayout {

	private static final int DELAY = 10;
	private static final int SPEED = 16;
	private static final int MOVE = 1;
	private static final String TAG = "MoveTab";
	
	private Context mContext;
	private Drawable mDrawable;//移动的背景图
	private final Rect mNowRect = new Rect();//当前的区域
    private final Rect mEndRect = new Rect();//结束的区域
    private int i = 0;

    private final Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		if (msg.what == MOVE) {
    			//如果还没有到达目标区域，就延迟DELAY后，重新绘图
    			if (!move()) { 
    				this.sendEmptyMessageDelayed(MOVE, DELAY);
    			}
    		}
    	};
    };
    
	public MoveTab(Context context) {
		super(context);
		init(context, null);
	}
	
	public MoveTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mContext = context;
		TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.MoveTab);
		//通过XML中定义的属性"move_drawable"，生成mDrawable
		mDrawable = attr.getDrawable(R.styleable.MoveTab_move_drawable);
		if (mDrawable == null) {
			Log.e(TAG, "Errorr : mDrawable == null");
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(i<2){//系统初始化时
			//默认选择第一个table，把它的区域设置为mNowRect
			this.getChildAt(0).getHitRect(mNowRect);
			i++;
		}
		super.onLayout(changed, l, t, r, b);
	}
	
	/**
	 * 对外公开的接口，外部调用者应该在table被点击时调用它，将mDrawable移动到目标控件v
	 * @param 目标控件
	 */
	public void selectTab(View v) {
		//将目标控件v的区域设置为mEndRect
		v.getHitRect(mEndRect);
		if (mNowRect.right != mEndRect.right) {
			mHandler.sendEmptyMessage(MOVE); //向Handler发送消息，开始移动mDrawable
		}
	}
	
	/**
	 * 重新计算图片的位置
	 * @return 动画是否结束
	 */
	private boolean move() {
		int direction = 0;
		//已非常接近目标控件, 直接让mNowRect和mEndRect重合
		if (Math.abs(mNowRect.left - mEndRect.left) <= SPEED) {
			mNowRect.left = mEndRect.left;
			mNowRect.right = mEndRect.right;
			invalidate();
			return true;
		}
		
		if (mNowRect.left < mEndRect.left) {
			direction = 1; //向右
		} else {
			direction = -1; //向左
		}
		//更新mNowRect
		mNowRect.left += SPEED * direction;
		mNowRect.right += SPEED * direction;
		//请求onDraw()
		invalidate();
		return false;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (null != mDrawable) {
			//将mDrawable画到mNowRect上
			mDrawable.setBounds(mNowRect);
			mDrawable.draw(canvas);
			Log.i(TAG, "onDraw : " + mNowRect.left + ", " + mNowRect.right + ", " + mNowRect.top + ", " + mNowRect.bottom);
		} else {
			Log.e(TAG, "Errorr : mDrawable == null");
		}
	}
}
