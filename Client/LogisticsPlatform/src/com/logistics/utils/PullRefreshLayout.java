package com.logistics.utils;

import com.logistics.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * PullRefreshLayout is designed to activate an action by pull. 
 * It base on FrameLayout. This class defined two special child: actionView & 
 * toolView The actionView can be use on pull action (Ex. pull to refresh).
 * The toolView is a hidden view on first showing, it can be use as a hidden tool bar.
 * The two special child will be identify by its id.
 *
 */
public class PullRefreshLayout extends FrameLayout {
	private static final int ANIMATED_SCROLL_GAP = 250;
	private static final float TOOLVIEW_SPLIT_PREF = 0.6F;
	
	private static final int TOP_IN_TOOL = 1;
	private static final int TOP_IN_ACTION = 2;
	private static final int TOP_OUT_VIEWS = 4;
	
	private final int mActionViewId;
	private final int mToolViewId;
	private final int mTouchSlop;
	
	private View mActionView;
	private View mToolView;
	private int mActionViewHeight = 0;
	private int mToolViewHeight = 0;
	private final Scroller mScroller;
	private long mLastScroll;
	private final Rect mTempRect = new Rect();
	private View mTargetView;
	private AdapterView<?> mTargetAdapterView;
	private View mScrollbarStoreTarget;
	private boolean mTargetScrollbarEnable;
	private boolean mInTouch = false;
	private boolean mIsMotioned = false;
	private boolean mCancelSended = false;
	private float mLastMotionY;
	private int mMotionScrollY;
	private boolean mEnableStopInAction = true;
	private int mTopState;
	private OnPullStateListener mPullStateListener;
	private OnPullListener mActionViewPullListener;
	private OnPullListener mToolViewPullListener;

	public PullRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.PullActivateLayout, defStyle, 0);

		mActionViewId = a.getResourceId(0, R.id.action_view);
		mToolViewId = a.getResourceId(1, R.id.tool_view);
		
		mScroller = new Scroller(context);

		ViewConfiguration cfg = ViewConfiguration.get(context);
		mTouchSlop = cfg.getScaledTouchSlop();
	}

	public PullRefreshLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PullRefreshLayout(Context context) {
		super(context);
		mActionViewId = R.id.action_view;
		mToolViewId = R.id.tool_view;
		mScroller = new Scroller(context);

		ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	private void checkViewId(View view) {
		if (view == null) {
			return;
		}
		if ((mActionView == null) && (view.getId() == mActionViewId)) {
			mActionView = view;
		}

		if ((mToolView == null) && (view.getId() == mToolViewId))
			mToolView = view;
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		checkViewId(child);
		super.addView(child, index, params);
	}

	@Override
	protected boolean addViewInLayout(View child, int index,
			ViewGroup.LayoutParams params, boolean preventRequestLayout) {
		checkViewId(child);
		return super
				.addViewInLayout(child, index, params, preventRequestLayout);
	}

	@Override
	public void removeView(View child) {
		if (mActionView == child) {
			mActionView = null;
		}
		if (mToolView == child) {
			mToolView = null;
		}
		super.removeView(child);
	}

	@Override
	public void removeViewInLayout(View view) {
		if (mActionView == view) {
			mActionView = null;
		}
		if (mToolView == view) {
			mToolView = null;
		}
		super.removeViewInLayout(view);
	}

	@Override
	public void removeViewsInLayout(int start, int count) {
		for (int i = start; i < start + count; ++i) {
			View view = getChildAt(i);
			if (mActionView == view) {
				mActionView = null;
			}
			if (mToolView == view) {
				mToolView = null;
			}
		}
		super.removeViewsInLayout(start, count);
	}

	@Override
	public void removeViewAt(int index) {
		View view = getChildAt(index);
		if (mActionView == view) {
			mActionView = null;
		}
		if (mToolView == view) {
			mToolView = null;
		}
		super.removeViewAt(index);
	}

	@Override
	public void removeViews(int start, int count) {
		for (int i = start; i < start + count; ++i) {
			View view = getChildAt(i);
			if (mActionView == view) {
				mActionView = null;
			}
			if (mToolView == view) {
				mToolView = null;
			}
		}
		super.removeViews(start, count);
	}

	@Override
	public void removeAllViews() {
		mActionView = null;
		mToolView = null;
		super.removeAllViews();
	}

	@Override
	public void removeAllViewsInLayout() {
		mActionView = null;
		mToolView = null;
		super.removeAllViewsInLayout();
	}

	@Override
	protected void attachViewToParent(View child, int index,
			ViewGroup.LayoutParams params) {
		checkViewId(child);
		super.attachViewToParent(child, index, params);
	}

	@Override
	protected void detachViewFromParent(View child) {
		if (mActionView == child) {
			mActionView = null;
		}
		if (mToolView == child) {
			mToolView = null;
		}
		super.detachViewFromParent(child);
	}

	@Override
	protected void detachViewFromParent(int index) {
		View view = getChildAt(index);
		if (mActionView == view) {
			mActionView = null;
		}
		if (mToolView == view) {
			mToolView = null;
		}
		super.detachViewFromParent(index);
	}

	@Override
	protected void detachViewsFromParent(int start, int count) {
		for (int i = start; i < start + count; ++i) {
			View view = getChildAt(i);
			if (mActionView == view) {
				mActionView = null;
			}
			if (mToolView == view) {
				mToolView = null;
			}
		}
		super.detachViewsFromParent(start, count);
	}

	@Override
	protected void detachAllViewsFromParent() {
		mActionView = null;
		mToolView = null;
		super.detachAllViewsFromParent();
	}

	/**
	 * Replace the ActionView by code. The ActionView will also be add as a child. 
	 * @param view - The ActionView
	 */
	public void setActionView(View view) {
		if (view != null) {
			view.setId(mActionViewId);
			addView(view);
		} else {
			mActionView = null;
		}
	}

	/**
	 * Replace the ToolView by code. The ToolView will also be add as a child. 
	 * @param view - The ToolView
	 */
	public void setToolView(View view) {
		if (view != null) {
			view.setId(mToolViewId);
			addView(view);
		} else {
			mToolView = null;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int b = top;
		if (mToolView != null) {
			View v = mToolView;
			int t = b - (v.getBottom() - v.getTop());
			v.layout(v.getLeft(), t, v.getRight(), b);
			mToolViewHeight = ((v.getVisibility() == 0) ? b - t : 0);
			b = t;
		}
		if (mActionView != null) {
			View v = mActionView;
			int t = b - (v.getBottom() - v.getTop());
			v.layout(v.getLeft(), t, v.getRight(), b);
			mActionViewHeight = ((v.getVisibility() == 0) ? b - t : 0);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		float eventFloatY = ev.getY();
		boolean targetOnTop = isOnTargetTop();
		int scrollY = getScrollY();
		int action = ev.getAction();

		if (action == MotionEvent.ACTION_DOWN) {
			mCancelSended = false;
			mInTouch = true;
			mIsMotioned = false;
			mLastMotionY = eventFloatY;
			mTargetView = null;
			mTargetAdapterView = null;
			View v = findTargetView(this, ev);
			if (v instanceof AdapterView)
				mTargetAdapterView = ((AdapterView) v);
			else {
				mTargetView = v;
			}
		}

		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			if ((!mIsMotioned) && (scrollY <= 0) && (targetOnTop)) {
				mIsMotioned = true;
				mLastMotionY = eventFloatY;
				mMotionScrollY = scrollY;
			}

			if (!targetOnTop) {
				mIsMotioned = false;
			}
			if ((mIsMotioned) && (targetOnTop)) {
				float d = mMotionScrollY - eventFloatY + mLastMotionY;
				scrollTo(0, (int) d);
			}

			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			completeTouch();
		}

		if ((scrollY >= 0) || (!targetOnTop) || (action != 2)) {
			if ((mCancelSended) && (action != 1) && (action != 3)) {
				mCancelSended = false;
				ev.setAction(0);
			}
		} else if ((!mCancelSended) && (action == 2)
				&& (Math.abs(mMotionScrollY - scrollY) > mTouchSlop)) {
			mCancelSended = true;
			ev.setAction(3);
			super.dispatchTouchEvent(ev);
		}

		if (!mCancelSended) {
			super.dispatchTouchEvent(ev);
		}

		if (!mIsMotioned) {
			mLastMotionY = eventFloatY;
		}
		return true;
	}

	@Override
	public void scrollTo(int x, int y) {
		int destY = Math.min(y, 0);

		int newState = 0;
		if ((destY < 0) && (mToolViewHeight > 0)) {
			newState |= TOP_IN_TOOL;
		}
		if ((destY < mToolViewHeight) && (mActionViewHeight > 0)) {
			newState |= TOP_IN_ACTION;
		}
		if (destY < -mActionViewHeight - mToolViewHeight) {
			newState |= TOP_OUT_VIEWS;
		}

		if (newState != mTopState) {
			if (mPullStateListener != null) {
				if (((mTopState & TOP_OUT_VIEWS) == 0) && ((newState & TOP_OUT_VIEWS) != 0))
					mPullStateListener.onPullOut();
				else if (((mTopState & TOP_OUT_VIEWS) != 0)
						&& ((newState & TOP_OUT_VIEWS) == 0)) {
					mPullStateListener.onPullIn();
				}
			}

			if (mActionViewPullListener != null) {
				if (((mTopState & TOP_IN_ACTION) == 0) && ((newState & TOP_IN_ACTION) != 0))
					mActionViewPullListener.onShow();
				else if (((mTopState & TOP_IN_ACTION) != 0)
						&& ((newState & TOP_IN_ACTION) == 0)) {
					mActionViewPullListener.onHide();
				}
			}

			if (mToolViewPullListener != null) {
				if (((mTopState & TOP_IN_TOOL) == 0) && ((newState & TOP_IN_TOOL) != 0))
					mToolViewPullListener.onShow();
				else if (((mTopState & TOP_IN_TOOL) != 0)
						&& ((newState & TOP_IN_TOOL) == 0)) {
					mToolViewPullListener.onHide();
				}
			}

			mTopState = newState;
		}

		setVerticalScrollBarEnabled(destY < 0);

		if ((destY < 0) && (mScrollbarStoreTarget == null)) {
			if (mTargetView != null) {
				mScrollbarStoreTarget = mTargetView;
				mTargetScrollbarEnable = mTargetView
						.isVerticalScrollBarEnabled();
				mTargetView.setVerticalScrollBarEnabled(false);
			}
			if (mTargetAdapterView != null) {
				mScrollbarStoreTarget = mTargetAdapterView;
				mTargetScrollbarEnable = mTargetAdapterView
						.isVerticalScrollBarEnabled();
				mTargetAdapterView.setVerticalScrollBarEnabled(false);
			}
		}
		if ((destY >= 0) && (mScrollbarStoreTarget != null)) {
			mScrollbarStoreTarget
					.setVerticalScrollBarEnabled(mTargetScrollbarEnable);
			mScrollbarStoreTarget = null;
			setVerticalScrollBarEnabled(false);
		}

		super.scrollTo(x, destY);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldY = getScrollY();
			int scrollY = mScroller.getCurrY();

			scrollTo(0, scrollY);

			if (oldY != scrollY) {
				onScrollChanged(0, scrollY, 0, oldY);
			}
			if (scrollY > 0) {
				mScroller.abortAnimation();
			}

			postInvalidate();
		}
	}

	private View findTargetView(ViewGroup group, MotionEvent ev) {
		float xf = ev.getX();
		float yf = ev.getY();
		float scrolledXFloat = xf + group.getScrollX();
		float scrolledYFloat = yf + group.getScrollY();
		Rect frame = mTempRect;
		int orgAction = ev.getAction();

		ev.setAction(0);

		int scrolledXInt = (int) scrolledXFloat;
		int scrolledYInt = (int) scrolledYFloat;

		int count = group.getChildCount();
		for (int i = count - 1; i >= 0; --i) {
			View child = group.getChildAt(i);
			if ((child.getVisibility() == 0) || (child.getAnimation() != null)) {
				child.getHitRect(frame);
				if (!frame.contains(scrolledXInt, scrolledYInt))
					continue;
				float xc = scrolledXFloat - child.getLeft();
				float yc = scrolledYFloat - child.getTop();
				ev.setLocation(xc, yc);

				if (!child.dispatchTouchEvent(ev)) {
					continue;
				}

				ev.setAction(3);
				child.dispatchTouchEvent(ev);

				ev.setAction(orgAction);
				ev.setLocation(xf, yf);
				return child;
			}

		}

		ev.setAction(orgAction);
		ev.setLocation(xf, yf);
		return null;
	}

	/**
	 * Set the action view can partial show when scroll stop. 
	 * @param enable - True if the actionView can partial show, false otherwise.
	 */
	public void setEnableStopInActionView(boolean enable) {
		mEnableStopInAction = enable;
	}

	/**
	 * Set a listener to monitor the pull action on ActionView 
	 * @param listener - The monitor listener
	 */
	public void setOnActionPullListener(OnPullListener listener) {
		mActionViewPullListener = listener;
	}

	/**
	 * Set a listener to monitor the pull action on ToolView 
	 * @param listener - The monitor listener
	 */
	public void setOnToolPullListener(OnPullListener listener) {
		mToolViewPullListener = listener;
	}

	/**
	 * Set a listener to monitor the Pull out/in state. The listener will be call on pull state changed.
	 * @param listener - The OnPullStateListener
	 */
	public void setOnPullStateChangeListener(OnPullStateListener listener) {
		mPullStateListener = listener;
	}

	private boolean isOnTargetTop() {
		if (mTargetView != null) {
			return mTargetView.getScrollY() <= 0;
		}
		if (mTargetAdapterView != null) {
			int first = mTargetAdapterView.getFirstVisiblePosition();
			View firstView = null;
			if ((first == 0)
					&& ((firstView = mTargetAdapterView.getChildAt(0)) != null)) {
				return firstView.getTop() >= 0;
			}
			return false;
		}

		return true;
	}

	private void completeTouch() {
		mInTouch = false;
		int y = getScrollY();
		int toolTop = -mToolViewHeight;
		int top = toolTop - mActionViewHeight;
		if (y >= 0) {
			return;
		}

		if (y < top)
			snapToActionViewTop();
		else if ((!mEnableStopInAction) && (y < toolTop) && (y > top))
			hideActionView();
		else if ((y <= toolTop * TOOLVIEW_SPLIT_PREF) && (y > toolTop))
			snapToToolViewTop();
		else if (y > toolTop * TOOLVIEW_SPLIT_PREF)
			snapToToolViewBottom();
	}

	protected int computeVerticalScrollRange() {
		int scrollRange = getHeight();
		if (mTargetView != null) {
			scrollRange = Math.max(scrollRange, mTargetView.getHeight());
		}
		if (mTargetAdapterView != null) {
			int itemCount = mTargetAdapterView.getCount();
			int count = mTargetAdapterView.getChildCount();

			scrollRange = Math.max(scrollRange, Math.max(1, itemCount - count)
					* getHeight());
		}

		int scrollY = getScrollY();
		if (scrollY < 0) {
			scrollRange = (int) (scrollRange - scrollY * getHeight() * 0.1D);
		}

		return scrollRange;
	}

	@Override
	protected int computeVerticalScrollOffset() {
		return Math.max(0, super.computeVerticalScrollOffset());
	}

	/**
	 * Scroll to hide Action View if no touch event. 
	 */
	public void hideActionView() {
		int y = getScrollY();
		int bottom = -mToolViewHeight;
		if ((y >= bottom) || (mInTouch)) {
			return;
		}

		smoothScrollTo(bottom);
	}

	/**
	 * Snap to Top of Action View if no touch event. 
	 */
	public void snapToActionViewTop() {
		int y = getScrollY();
		int top = -mToolViewHeight - mActionViewHeight;
		if ((y >= top) || (mInTouch)) {
			return;
		}
		smoothScrollTo(top);
		if (mActionViewPullListener != null)
			mActionViewPullListener.onSnapToTop();
	}

	/**
	 * Snap to Top of Tool View if no touch event. 
	 */
	public void snapToToolViewTop() {
		int y = getScrollY();
		int top = -mToolViewHeight;
		if ((y >= 0) || (mInTouch)) {
			return;
		}
		smoothScrollTo(top);
		if (mToolViewPullListener != null)
			mToolViewPullListener.onSnapToTop();
	}

	/**
	 * Snap to hide all view if no touch event. 
	 */
	public void snapToToolViewBottom() {
		int y = getScrollY();
		if ((y >= 0) || (mInTouch)) {
			return;
		}
		smoothScrollTo(0);
	}

	/**
	 * Indicates whether all children view has been pull down under the top of layout.
	 * @return true if all children view is pull down, false otherwise
	 */
	public boolean isPullOut() {
		return getScrollY() < -mActionViewHeight - mToolViewHeight;
	}

	/**
	 * Like View#scrollBy, but scroll smoothly instead of immediately. 
	 * @param dy - the number of pixels to scroll by on the Y axis
	 */
	public final void smoothScrollBy(int dy) {
		long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
		if (duration > ANIMATED_SCROLL_GAP) {
			mScroller.startScroll(0, getScrollY(), 0, dy);
			invalidate();
		} else {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			scrollBy(0, dy);
		}
		mLastScroll = AnimationUtils.currentAnimationTimeMillis();
	}

	/**
	 * Like scrollTo(int, int), but scroll smoothly instead of immediately. 
	 * @param y - the position where to scroll on the Y axis
	 */
	public final void smoothScrollTo(int y) {
		smoothScrollBy(y - getScrollY());
	}

	public static abstract interface OnPullListener {
		public abstract void onSnapToTop();

		public abstract void onShow();

		public abstract void onHide();
	}

	public static abstract interface OnPullStateListener {
		public abstract void onPullOut();

		public abstract void onPullIn();
	}
}