package com.newland.comp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

public class SlidingMenu extends RelativeLayout {

	private SlidingView mSlidingView;
	private View mMenuView;
	private View mDetailView;
    public void setAllowShowRightView (boolean isAllow) {
		mSlidingView.setAllowShowRightView(isAllow);
	}
	public SlidingMenu(Context context) {
		super(context);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void addViews(View left, View center, View right) {
		setCenterView(center);
		setLeftView(left);
		setRightView(right);
		
	}
    public void hideSideViews(){
    	mSlidingView.hideSideViews();
    }
	/**
	 * 设置左边侧滑
	 * 
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void setLeftView( View view) {
	     LayoutParams behindParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		behindParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//
	    behindParams.setMargins(0, 0, 0, 0);
		addView(view, behindParams);
		mMenuView = view;
		mSlidingView.setMenuView(mMenuView);
	    
		
	}

	/**
	 * 设置右边侧滑
	 * 
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void setRightView( View view) {
		//rightView的大小全屏
	   LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		behindParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// �ڸ��ؼ����ұ�
	    behindParams.setMargins(0, 0, 0, 0);
		addView(view, behindParams);
		mDetailView = view;
		mSlidingView.setDetailView(mDetailView);
	}

	/**
	 * 设置中间view
	 * 
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mSlidingView = new SlidingView(getContext());
		mSlidingView.setView(view);
		addView(mSlidingView, aboveParams);
		mSlidingView.invalidate();
	}

	public void showLeftView() {
		mSlidingView.showLeftView();
		System.out.println("left 2");
	}

	public void showRightView() {
		mSlidingView.showRightView();
	}
}
