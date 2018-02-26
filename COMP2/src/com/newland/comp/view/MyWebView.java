package com.newland.comp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 
 * @author H81
 *
 */
public class MyWebView extends WebView {

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 解决和listview发生点击冲突
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			return false;

		case MotionEvent.ACTION_MOVE:

			return false;
		case MotionEvent.ACTION_UP:
			return false;

		}
		return false;
	}
}
