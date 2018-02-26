package com.newland.comp.adapter.my;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class MyViewPageAdapter extends PagerAdapter {
	private List<View> listViews;

	public MyViewPageAdapter(List<View> listViews) {
		this.listViews = listViews;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(listViews.get(position), 0);
		return listViews.get(position);
	}

	@Override
	public int getCount() {

		return listViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0 == arg1;
	}

}
