package com.yale.qcxxandroid.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yale.qcxxandroid.R;

public class  BaseListView extends ListView implements OnScrollListener,
		OnItemLongClickListener, GestureDetector.OnGestureListener,
		View.OnTouchListener {

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;
	@SuppressWarnings("unused")
	private int headContentWidth;
	private int headContentHeight;
	private int startY;
	private int firstItemIndex;
	private int state;
	private boolean isBack;
	private OnRefreshListener refreshListener;
	private boolean isRefreshable;

	private GestureDetector mDetector;
	private OnDeleteListioner mOnDeleteListioner;
	private int position;
	@SuppressWarnings("unused")
	private float velocityX, velocityY;
	private ListViewonSingleTapUpListenner thisonSingleTapUpListenner;

	public BaseListView(Context context) {
		super(context);
		init(context);
	}

	public BaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/****
	 * 初始化listview
	 * 
	 * @Title: init
	 * @Description: TODO(
	 * @param @param context
	 * @return void
	 * @throws
	 */
	private void init(Context context) {
		// 获得自定义布局文件对象，类似findViewById,不同在于LayoutInflater是
		// 用来找res/layout/下的xml布局文件，并且实例化；
		// 而findViewById()是找xml布局文件下的具体widget控件(如TextView等)。
		// 对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.base_listview_head,
				null);
		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);
		setOnScrollListener(this);
		// 旋转动画类
		// X轴顺时针转动到fromDegrees为旋转的起始点，
		// X轴顺时针转动到toDegrees为旋转的起始点。
		// fromDegrees=0，toDegrees=90；为左上角顶点为旋转点。0度为起始点，90度为终点。进行旋转，旋转了90度
		// pivotX为距离左侧的偏移量，pivotY为距离顶部的偏移量。即为相对于View左上角(0,0)的坐标点
		// pivotType旋转类型，RotateAnimation.RELATIVE_TO_SELF为自转
		// 下面的意思是：以中心点自转逆时针180度
		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// 动画特效为：平稳不变的（LinearInterpolator）
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250); // 动画持续时间，单位毫秒
		animation.setFillAfter(true); // 如果fillAfter的值为true，则动画执行后，控件将停留在执行结束的状态

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;

		mDetector = new GestureDetector(context, this);
		mDetector.setIsLongpressEnabled(false);
		standard_touch_target_size = (int) getResources().getDimension(
				R.dimen.delete_action_len);
		this.setOnTouchListener(this);
	}

	// 手指在触摸屏上滑动
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	// 分析捕捉到的触摸事件触发相应的回调函数
	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 触摸开始时
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP: // 触摸结束时

				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				isRecored = false;
				isBack = false;
				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {
					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						}
						// 上推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}

					// done状态下
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

					// 更新headView的size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}
		if (scroll || deleteView) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText("松开进行刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("向下拖动进行刷新");
			} else {
				tipsTextview.setText("向下拖动进行刷新");
			}
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			tipsTextview.setText("松开进行刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		state = DONE;
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		lastUpdatedTextView.setText("上次刷新 ：" + date);
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (null != refreshListener) {
			refreshListener.onRefresh();
		}
	}

	// “估计”headView的width以及height
	// ViewGroup是一个可以包含子View的容器，是布局文件和View容器的基类
	@SuppressWarnings("deprecation")
	private void measureView(View child) {
		// ViewGroup.LayoutParams类是布局参数的子类
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		// 封装了父布局传递给子布局的布局要求
		// 参数：spec 父窗口传递给子视图的大小和模式
		// padding 父窗口的边距，也就是xml中的android:padding
		// childDimension 子视图想要绘制的准确大小，但最终不一定绘制此值
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			// 封装了父布局传递给子布局的布局要求
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		lastUpdatedTextView.setText("上次刷新 ：" + date);
		super.setAdapter(adapter);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		return false;
	}

	// ////////////////////////以下是滑动删除方法////////////////////////////////

	public void setDeleteListioner(OnDeleteListioner mOnDeleteListioner) {
		this.mOnDeleteListioner = mOnDeleteListioner;
	}

	public void setSingleTapUpListenner(
			ListViewonSingleTapUpListenner thisonSingleTapUpListenner) {
		this.thisonSingleTapUpListenner = thisonSingleTapUpListenner;
	}

	private int standard_touch_target_size = 0;
	private float mLastMotionX;
	public boolean deleteView = false;
	private ScrollLinerLayout mScrollLinerLayout;
	private boolean scroll = false;
	private int pointToPosition;
	private boolean listViewMoving;
	private boolean delAll = false;
	public boolean isLongPress = false;

	public boolean isDelAll() {
		return delAll;
	}

	public void setDelAll(boolean delAll) {
		this.delAll = delAll;
	}

	public boolean onDown(MotionEvent e) {
		if (thisonSingleTapUpListenner != null) {
			thisonSingleTapUpListenner.onSingleTapUp();
		}
		mLastMotionX = e.getX();
		pointToPosition = this.pointToPosition((int) e.getX(), (int) e.getY());
		final int p = pointToPosition - this.getFirstVisiblePosition();
		if (mScrollLinerLayout != null) {
			mScrollLinerLayout.onDown();
			mScrollLinerLayout.setSingleTapUp(true);
		}
		if (deleteView && p != position) {
			deleteView = false;
			if (mScrollLinerLayout != null) {
				mScrollLinerLayout.snapToScreen(0);
				mScrollLinerLayout.setSingleTapUp(false);
			}
			position = p;
			scroll = false;
			return true;
		}
		isLongPress = false;
		position = p;
		scroll = false;
		listViewMoving = false;
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		return false;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (listViewMoving && !scroll) {
			if (mScrollLinerLayout != null)
				mScrollLinerLayout.snapToScreen(0);
			return false;
		} else if (scroll) {
			if (mScrollLinerLayout != null) {
				int deltaX = (int) (mLastMotionX - e2.getX());
				if (deleteView) {
					deltaX += standard_touch_target_size;
				}
				if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
					mScrollLinerLayout.scrollBy(
							deltaX - mScrollLinerLayout.getScrollX(), 0);
				}
			}
		} else {
			if (Math.abs(distanceX) > Math.abs(distanceY)) {
				final int pointToPosition1 = this.pointToPosition(
						(int) e2.getX(), (int) e2.getY());
				final int p1 = pointToPosition1
						- this.getFirstVisiblePosition();
				if (p1 == position && mOnDeleteListioner.isCandelete(p1)) {
					mScrollLinerLayout = (ScrollLinerLayout) this
							.getChildAt(p1);
					if (mScrollLinerLayout != null) {
						int deltaX = (int) (mLastMotionX - e2.getX());
						if (deleteView) {
							deltaX += standard_touch_target_size;
						}
						if (deltaX >= 0 && deltaX <= standard_touch_target_size
								&& Math.abs(distanceY) < 5) {
							isLongPress = true;
							scroll = true;
							listViewMoving = false;
							mScrollLinerLayout.setSingleTapUp(false);
							mScrollLinerLayout.scrollBy(
									(int) (e1.getX() - e2.getX()), 0);

						}
					}
				}
			}
		}
		if (scroll) {
			return true;
		}
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		if (deleteView) {
			position = -1;
			deleteView = false;
			mScrollLinerLayout.snapToScreen(0);
			scroll = false;
			return true;
		}
		return false;
	}

	public void setScroll(boolean b) {
		listViewMoving = b;

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isDelAll()) {
			return false;
		} else {
			if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				int deltaX2 = (int) (mLastMotionX - event.getX());
				if (scroll) {
					if (!deleteView
							&& deltaX2 >= standard_touch_target_size / 2) {
						mScrollLinerLayout
								.snapToScreen(standard_touch_target_size);
						position = pointToPosition
								- this.getFirstVisiblePosition();
						deleteView = true;
					} else {
						position = -1;
						deleteView = false;
						mScrollLinerLayout.snapToScreen(0);
					}
					scroll = false;
					return true;
				}/*
				 * else if (Math.abs(velocityX) > Math.abs(velocityY) && deltaX2
				 * < -80) { mOnDeleteListioner.onBack(); return false; }
				 */
			}
			return mDetector.onTouchEvent(event);
		}

	}

	public void deleteItem() {
		position = -1;
		deleteView = false;
		scroll = false;
		if (mScrollLinerLayout != null) {
			mScrollLinerLayout.snapToScreen(0);
		}
	}
	
	/**
	 * Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

}