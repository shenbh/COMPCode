package com.newland.comp.view;

import android.R.bool;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SlidingView extends ViewGroup {

	private FrameLayout mContainer;
	private Scroller mScroller;//中间视图
	private Scroller menuScroller;//左边视图滚动器
	private Scroller detailScroller;//右边视图滚动器
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private float mLastMoveX;
	private float menuBeginScrollX;
	private float detailBeginScrollX;
	private boolean isLeftShow;//标示左边视图是否显示
	private boolean isRightShow;//标示右边视图是否显示
	private boolean allowShowRightView;//标示是否可以显示右边视图
	private static final int SNAP_VELOCITY = 1000;
	private View mMenuView;//左边视图
	private View mDetailView;//右边视图

	public boolean isAllowShowRightView() {
		return allowShowRightView;
	}

	public void setAllowShowRightView(boolean allowShowRightView) {
		this.allowShowRightView = allowShowRightView;
	}

	public SlidingView(Context context) {
		super(context);
		init();
	}

	public SlidingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlidingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer.measure(widthMeasureSpec, heightMeasureSpec);
	    hideSideViews();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContainer.layout(0, 0, width, height);
	}

	private void init() {
		isLeftShow = false;
		isRightShow = false;
		menuBeginScrollX = 0;
		detailBeginScrollX = 0;
		mContainer = new FrameLayout(getContext());
		mContainer.setBackgroundColor(0xff000000);
		mScroller = new Scroller(getContext());
		mTouchSlop = 3;
		super.addView(mContainer);
	}

	public void setView(View v) {
        if (mContainer.getChildCount() > 0) {
			mContainer.removeAllViews();
		}
		mContainer.addView(v);
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}

	@Override
	public void computeScroll() {
		if (!menuScroller.isFinished()) {
			if (menuScroller.computeScrollOffset()) {
				int oldX = mMenuView.getScrollX();
				int oldY = mMenuView.getScrollY();
				int x = menuScroller.getCurrX();
				int y = menuScroller.getCurrY();
				if (oldX != x || oldY != y) {
					mMenuView.scrollTo(x, y);
				}
				// Keep on drawing until the animation has finished.
				mMenuView.invalidate();
			} else {
				clearChildrenCache();
			}
		} else {
			clearChildrenCache();
		}
		
		if (!detailScroller.isFinished()) {
			if (detailScroller.computeScrollOffset()) {
				int oldX = mDetailView.getScrollX();
				int oldY = mDetailView.getScrollY();
				int x = detailScroller.getCurrX();
				int y = detailScroller.getCurrY();
				if (oldX != x || oldY != y) {
					mDetailView.scrollTo(x, y);
				}
				// Keep on drawing until the animation has finished.
				mDetailView.invalidate();
			} else {
				clearChildrenCache();
			}
		} else {
			clearChildrenCache();
		}
	}

	private boolean mIsBeingDragged;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		final float x = ev.getX();
//		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			menuBeginScrollX = mMenuView.getScrollX();
			detailBeginScrollX = mDetailView.getScrollX();
			mLastMoveX = x;
			mIsBeingDragged = false;
			break;

		case MotionEvent.ACTION_MOVE:
			final float dx = x - mLastMoveX;
			System.out.print("dx  "+dx +" mTouchSlop  "+mTouchSlop);
			final float xDiff = Math.abs(dx);
			if (xDiff < mTouchSlop) {
//				mLastMoveX = x;
				return false;
			}
			mIsBeingDragged = true;
			break;
//		case MotionEvent.ACTION_CANCEL:
//		case MotionEvent.ACTION_UP:
//			mIsBeingDragged = false;
//			break;
		}
		return mIsBeingDragged;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) { 
		
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
//			if (!mScroller.isFinished()) {
//				mScroller.abortAnimation();
//			}
			menuBeginScrollX = mMenuView.getScrollX();
			detailBeginScrollX = mDetailView.getScrollX();
			mLastMotionX = x;
			mLastMotionY = y;
			
			break;
		case MotionEvent.ACTION_MOVE:
//			if (mIsBeingDragged) {
				enableChildrenCache();
				final float deltaX = mLastMotionX - x;
//				System.out.println(deltaX + "  "+ isLeftShow + "  "+ isRightShow + "  "+ menuBeginScrollX);;
				if (deltaX < 0) { // 右划
					if (isLeftShow == false) {
						if (isRightShow == false) {//划出左边
							scrollMenuView(deltaX);
						}else{//隐藏右边
							if (allowShowRightView) {
								scrollDetailView(deltaX);
							}
						}
					}else{
						if (isRightShow == false) {//划出左边
							scrollMenuView(deltaX);
						}else{//不可能
							
						}
					}
					
					
				} else { // 左划
					
					if (isLeftShow == false) {
						if (allowShowRightView) {
							if (isRightShow == false ) {//划出右边
								scrollDetailView(deltaX);
							}else{//划出右边
								scrollDetailView(deltaX);
							}
						}
					}else{
						if (isRightShow == false) {//隐藏左边
							scrollMenuView(deltaX);
						}else{//不可能
							
						}
					}
//				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mIsBeingDragged = false;
//			if (mIsBeingDragged) {
				if(mMenuView.getScrollX() != getMenuViewWidth()){
					isLeftShow = true;
//					isRightShow = false;
				}
				if(mDetailView.getScrollX()!= -getDetailViewWidth() && allowShowRightView == true){
//					isLeftShow = false;
					isRightShow = true;
				}
                if( true){
                	
					int oldScrollX = mMenuView.getScrollX();
					int dx = 0;
					if (oldScrollX >= 0) {
						if (oldScrollX <= getMenuViewWidth() / 2) {//小于一半，拉出来
							dx =  -oldScrollX;
//							menuScrollTo(dx);
							mMenuView.scrollTo(0, mMenuView.getScrollY());
						
						} else{//大于一半，缩回去
							dx = getMenuViewWidth()-oldScrollX;
							isLeftShow = false;
//							menuScrollTo(dx);
							mMenuView.scrollTo(getMeasuredWidth(), mMenuView.getScrollY());
						}
					}
					
					clearChildrenCache();
				}
				if(true){
					int detailOldScrollX = mDetailView.getScrollX();
					int dx = 0;
					if(detailOldScrollX <= 0){
						if (-detailOldScrollX >= getDetailViewWidth() / 2) {//右边隐藏
							dx = -(getDetailViewWidth() + detailOldScrollX);
							isRightShow = false;
//							detailScrollTo(dx);
							mDetailView.scrollTo(-(getDetailViewWidth()), mDetailView.getScrollY());
						   System.out.println("右边隐藏");
						} else {//显示右边视图
							
							dx = -detailOldScrollX;
//							detailScrollTo(dx);
							mDetailView.scrollTo(0, mDetailView.getScrollY());
							System.out.println("显示右边视图");
						}
						
					}
					invalidate();
					clearChildrenCache();
				}
//			}
			break;
		}

		return true;
	}
    
	/**
	 * 处理左边视图滚动
	 * @param deltaX
	 */
	private void scrollMenuView(float deltaX){
//		float oldScrollX = mMenuView.getScrollX();
		float scrollX = menuBeginScrollX + deltaX;
		final float leftBound = 0;
		final float rightBound = getMenuViewWidth();
		
		if (scrollX < leftBound) {
			scrollX = leftBound;
		} else if (scrollX > rightBound) {
			scrollX = rightBound;
		}
		mMenuView.scrollTo((int) scrollX, mMenuView.getScrollY());
		mMenuView.postInvalidate();
	}
	/**
	 * 处理右边视图滚动
	 * @param deltaX
	 */
	private void scrollDetailView(float deltaX){
		if (allowShowRightView == false) {
			return;
		}
		float scrollX = detailBeginScrollX + deltaX;
		final float rightBound = -getDetailViewWidth();
		final float leftBound = 0;
		if (scrollX > leftBound) {
			scrollX = leftBound;
		} else if (scrollX < rightBound) {
			scrollX = rightBound;
		}
		mDetailView.scrollTo((int) scrollX, mDetailView.getScrollY());
		mDetailView.postInvalidate();
	}
	private int getMenuViewWidth() {
		if (mMenuView == null) {
			return 0;
		}
		return mMenuView.getWidth();
	}

	private int getDetailViewWidth() {
		if (mDetailView == null) {
			return 0;
		}
		return mDetailView.getWidth();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public View getDetailView() {
		return mDetailView;
	}

	public void setDetailView(View mDetailView) {
		this.mDetailView = mDetailView;
		detailScroller = new Scroller(this.mDetailView.getContext());
	}

	public View getMenuView() {
		return mMenuView;
	}

	public void setMenuView(View mMenuView) {
		this.mMenuView = mMenuView;
		menuScroller = new Scroller(this.mMenuView.getContext());
	}
     public void hideSideViews(){
    	 hideLeftView();
    	 hideRightView();
    	 
     }
     /**
      * 隐藏左边菜单
      */
     public void hideLeftView(){
    	 isLeftShow = false;
    	 int menuWidth = mMenuView.getWidth();
 		 int oldScrollX = mMenuView.getScrollX();
 		 if (oldScrollX == 0) {
 			 menuScrollTo(menuWidth);
 		 }
     }
     /**
      * 隐藏右边菜单
      */
     public void hideRightView(){
    	 isRightShow = false;
    	 int menuWidth = mDetailView.getWidth();
    	 int oldScrollX = mDetailView.getScrollX();
 		 if (oldScrollX == 0) {
 			 detailScrollTo(-menuWidth);
 		 } 
     }
	/**
	 * 显示或隐藏左边菜单
	 */
	public void showLeftView() {
		//隐藏右边
		hideRightView();
		
		int menuWidth = getMenuViewWidth();
		
		int oldScrollX = mMenuView.getScrollX();
		if (oldScrollX == 0) {
			isLeftShow = false;
			mMenuView.scrollTo(menuWidth, mMenuView.getScrollY());
//			menuScrollTo(menuWidth);
		} else{
			isLeftShow = true;
			mMenuView.scrollTo(0, mMenuView.getScrollY());
			
//			menuScrollTo(-menuWidth);
		}
	}
    
	
	/**
	 * 显示或隐藏右边菜单
	 */
	public void showRightView() {
		//隐藏左边
		hideLeftView();
		int detailWidth = getDetailViewWidth();
		int oldScrollX = mDetailView.getScrollX();
		if (oldScrollX == 0) {
			isRightShow = true;
			mDetailView.scrollTo(-detailWidth, mDetailView.getScrollY());
			
			//detailScrollTo(-detailWidth);
		} else{
			isRightShow = false;
			//detailScrollTo(detailWidth);
			mDetailView.scrollTo(0, mDetailView.getScrollY());
		}
	}
/**
 * 左边视图滚动
 * @param dx
 */
	void menuScrollTo(int dx) {
		int duration = 100;
		int oldScrollX = mMenuView.getScrollX();
		menuScroller.startScroll(oldScrollX, mMenuView.getScrollY(), dx, mMenuView.getScrollY(),
				duration);
		invalidate();
	}
	/**
	 * 右边视图滚动
	 * @param dx
	 */
	void detailScrollTo(int dx) {
		int duration = 100;
		int oldScrollX = mDetailView.getScrollX();
		detailScroller.startScroll(oldScrollX, mDetailView.getScrollY(), dx, mDetailView.getScrollY(),
				duration);
		invalidate();
	}
	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

}
