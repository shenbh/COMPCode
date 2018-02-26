package com.newland.comp.adapter.my;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.newland.comp.bean.indicator.IndicatorOperationItem;
import com.newland.comp.bean.indicator.Marking;
import com.newland.comp.bean.my.CheckIn;
import com.newland.comp.utils.StringUtil;
import com.newland.comp2.R;

/**
 * 我的考勤适配器
 * 
 * @author H81
 *
 */
public class MarkingWorkloadAdapter extends BaseAdapter {

	private Context context;
	private List<Marking> list = new ArrayList<Marking>();
	private LayoutInflater mInflater;
	private View mView; //fragemnet  view

	public MarkingWorkloadAdapter(Context context, List<Marking> list, View mView) {
		super();
		this.context = context;
		this.list = list;
		this.mView = mView;
		this.mInflater = LayoutInflater.from(context);
	}

	
	public int getCount() {
		return list.size();
	}

	
	public Object getItem(int position) {
		return position;
	}

	
	public long getItemId(int position) {
		return 0;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.kq_cell, null);
			holder = new ViewHolder();
			holder.left_tv = (TextView) convertView.findViewById(R.id.left_tv);
			holder.right_tv = (TextView) convertView.findViewById(R.id.right_tv);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Marking payrollWorkload = list.get(position);
		holder.left_tv.setText(StringUtil.noNull(payrollWorkload.getMarking_name()));
		holder.right_tv.setText(StringUtil.noNull(payrollWorkload.getMarking_value()));

		return convertView;
	}

	class ViewHolder {
		public TextView left_tv;
		public TextView right_tv;
		// public TextView time;//时间
	}

}
