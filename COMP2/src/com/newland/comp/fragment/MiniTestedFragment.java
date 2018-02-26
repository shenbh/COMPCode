package com.newland.comp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.comp.view.PullRefreshListView.IXListViewListener;

/**已考试
 * @author Administrator
 *
 */
public class MiniTestedFragment extends BaseFragment implements IXListViewListener{

	Context context;
	String hasCheck;
	public MiniTestedFragment(Context context,String hasCheck) {
		this.context = context;
		this.hasCheck=hasCheck;
	}

	public static MiniTestedFragment newInstance(Context context,String hasCheck) {
		return new MiniTestedFragment(context,hasCheck);
	}

	
	public void onClick(View v) {
		
	}

	public void onListViewRefresh() {
		
	}

	public void onListViewLoadMore() {
		
	}

	@Override
	protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@Override
	public void refresh() {
		
	}

}
