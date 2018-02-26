package com.newland.comp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

/**ListView无滚动条
 * @author Administrator
 *
 */
public class NoScrollListView extends ListView{

	public NoScrollListView(Context context) {
		super(context);
	}
	public NoScrollListView(Context context , AttributeSet attrs) {
		super(context,attrs);
	}
	public NoScrollListView(Context context , AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}